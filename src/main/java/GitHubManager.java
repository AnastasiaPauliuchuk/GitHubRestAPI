import model.JsonPullRequestParser;
import model.PullRequest;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import utils.PropertiesResourceManager;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;


public class GitHubManager extends TimerTask {

    private static final String SETTINGS_FILE = "settings.properties";

    private static final String AUTH_TOKEN_PROP = "authToken";
    private static final String REPO_OWNER_PROP = "repositoryOwner";
    private static final String REPO_NAME_PROP = "repositoryName";
    private static final String POST_URL_PROP = "postUrl";

    private static final String GRAPHQL_RESOURCE_URL = "https://api.github.com/graphql";



    private static  String accessToken;
    private static  String repositoryOwner;
    private static  String repositoryName;
    private static  String postUrl;
    private static String jenkinsToken;

    private static ArrayList<PullRequest> pullRequests = null;

    private static void init() {
        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        accessToken = prop.getProperty(AUTH_TOKEN_PROP);
        postUrl = prop.getProperty(POST_URL_PROP);
        repositoryOwner = prop.getProperty(REPO_OWNER_PROP);
        repositoryName = prop.getProperty(REPO_NAME_PROP);
        jenkinsToken = prop.getProperty("jenkinsToken");

    }

    private static  ArrayList<PullRequest> queryPullRequests() {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+ accessToken);
        String requestJson = String.format(PullRequest.getGraphqlString(),repositoryOwner, repositoryName);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.exchange(GRAPHQL_RESOURCE_URL, HttpMethod.POST,entity,String.class);
        try {
            return JsonPullRequestParser.parsePullRequestList(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
            return new  ArrayList<PullRequest>();
        }
    }

    private static boolean findByNumber(List<PullRequest> prevList, PullRequest element) {
        return prevList.stream().anyMatch(item -> item.getNumber().equals(element.getNumber()) && element.getLastCommitId().equals(item.getLastCommitId()));
    }


    public static List<PullRequest> filterUpdatedPullRequests(ArrayList<PullRequest> currentPullrequests, ArrayList<PullRequest> prevPullRequests) {

        if(prevPullRequests==null || prevPullRequests.size()==0) {
            return currentPullrequests;
        }
        else {
            return currentPullrequests.stream().filter(item -> !findByNumber(prevPullRequests, item)).collect(Collectors.toList());
        }

    }


    public static void sendToJenkins(String data) {
        System.out.println(data);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("user",jenkinsToken);
        body.add("data",data);
        HttpEntity<MultiValueMap<String, String>> requestEntity=
                new HttpEntity<>(body, headers);
        restTemplate.postForLocation(postUrl,requestEntity);
    }



    @Override
    public void run() {
        init();
        //System.out.println(pullRequests);
        System.out.println("run");
        ArrayList<PullRequest> newPullRequests = queryPullRequests();
        System.out.println("There are "+ newPullRequests.size() + " open pull request (s)");

        ArrayList<PullRequest>resultPullRequests = (ArrayList<PullRequest>) filterUpdatedPullRequests(newPullRequests,pullRequests);
        System.out.println("There are  "+ resultPullRequests.size() + " updated open pull request (s)");
        if(resultPullRequests.size() > 0 ) {
            System.out.print(resultPullRequests.toString());
            sendToJenkins(resultPullRequests.toString());
        }

        pullRequests = newPullRequests;

    }
}
