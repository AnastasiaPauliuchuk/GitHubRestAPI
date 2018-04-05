import model.JsonPullRequestParser;
import model.PullRequest;
import model.WebHook;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import utils.PropertiesResourceManager;

import java.io.IOException;


public class GitHubManager {

    private static final String SETTINGS_FILE = "settings.properties";

    private static final String AUTH_TOKEN_PROP = "authToken";
    private static final String REPO_OWNER_PROP = "repositoryOwner";
    private static final String REPO_NAME_PROP = "repositoryName";
    private static final String POST_URL_PROP = "postUrl";

    private static final String GRAPHQL_RESOURCE_URL = "https://api.github.com/graphql";

    private static final String API_GITHUB_HOOKS_URL_TEMPLATE ="https://api.github.com/repos/%1$s/%2$s/hooks";


    private static  String accessToken;
    private static  String repositoryOwner;
    private static  String repositoryName;
    private static  String postUrl;


    public static void init() {
        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        accessToken = prop.getProperty(AUTH_TOKEN_PROP);
        postUrl = prop.getProperty(POST_URL_PROP);
        repositoryOwner = prop.getProperty(REPO_OWNER_PROP);
        repositoryName = prop.getProperty(REPO_NAME_PROP);
    }

    public static String getPullRequests() {


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+ accessToken);
        String requestJson = String.format(PullRequest.getGraphqlString(),repositoryOwner, repositoryName);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.exchange(GRAPHQL_RESOURCE_URL, HttpMethod.POST,entity,String.class);
        return response.getBody();
    }

    public static void createWebHook() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+ accessToken);

        String requestURL = String.format(API_GITHUB_HOOKS_URL_TEMPLATE,repositoryOwner, repositoryName);
        String requestJson = String.format(WebHook.getCreateJsonTemplate(),"web","\"pull_request\",\"push\"",postUrl);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.exchange(requestURL, HttpMethod.POST,entity,String.class);
        System.out.println(response.getStatusCode());
    }

    public static void main(String[] args) {

        init();
        //get pull requests
        try {
            System.out.println(JsonPullRequestParser.parsePullRequestList(getPullRequests()).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //create webhook
        createWebHook();



    }

}
