package agent;

import json.JsonPullRequestDataParser;
import model.PullRequest;
import model.PullRequestsData;
import model.ReportedUpdate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import properties.PropertiesResourceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PullRequestsDataManager  {

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

   // private static ArrayList<PullRequest> pullRequests = null;
    private static PullRequestsData pullRequestsData = null;

    public PullRequestsDataManager(String settingsFile) {
        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        accessToken = prop.getProperty(AUTH_TOKEN_PROP);
        postUrl = prop.getProperty(POST_URL_PROP);
        repositoryOwner = prop.getProperty(REPO_OWNER_PROP);
        repositoryName = prop.getProperty(REPO_NAME_PROP);
        jenkinsToken = prop.getProperty("jenkinsToken");
    }

    public static PullRequestsData getPullRequestsData() {
        return pullRequestsData;
    }

    public static void setPullRequestsData(PullRequestsData prData) {
        pullRequestsData = prData;
    }

    public static void init() {


    }

    public static PullRequestsData queryPullRequests() {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+ accessToken);
        String requestJson = String.format(PullRequest.getGraphqlString(),repositoryOwner, repositoryName);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.exchange(GRAPHQL_RESOURCE_URL, HttpMethod.POST,entity,String.class);
        try {
            return JsonPullRequestDataParser.parsePullRequestList(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
            return new PullRequestsData();
        }
    }




    /*public static List<PullRequest> filterUpdatedPullRequests(ArrayList<PullRequest> currentPullrequests, ArrayList<PullRequest> prevPullRequests) {

        if(prevPullRequests==null || prevPullRequests.size()==0) {
            return currentPullrequests;
        }
        else {
            return currentPullrequests.stream().filter(item -> !findByNumber(prevPullRequests, item)).collect(Collectors.toList());
        }

    }*/


    public static PullRequestsData filterUpdatedPullRequests(PullRequestsData currentPullRequestsData,PullRequestsData prevPullRequestsData) {

        if(prevPullRequestsData==null || prevPullRequestsData.getPullRequests().size()==0) {
            return currentPullRequestsData;
        }
        else {
            PullRequestsData data = new PullRequestsData();
            List <ReportedUpdate> reportedUpdates = currentPullRequestsData.getReportedUpdates().stream().filter(item ->!findReportedUpdate(prevPullRequestsData.getReportedUpdates(),item)).collect(Collectors.toList());
            final List <PullRequest> pullRequests = currentPullRequestsData.getPullRequests();
            //archive
            List <PullRequest> pullRequestsToArchive = prevPullRequestsData.getPullRequests().stream().filter(item->!(pullRequests.contains(item))).collect(Collectors.toList());
            pullRequestsToArchive.stream().forEach(item->item.setIsOpen(false));

            List<PullRequest> filteredPullRequests = pullRequests.stream().filter(item -> findPullRequestByUpdates(reportedUpdates, item)).collect(Collectors.toList());


            data.setPullRequests(filteredPullRequests);
            data.setPullRequestsToArchive(pullRequestsToArchive);
            data.setReportedUpdates(reportedUpdates);


            return data;


        }

    }

    private static boolean findPullRequestByUpdates(List<ReportedUpdate> reportedUpdates, PullRequest pullRequest) {
        return reportedUpdates.stream().anyMatch(item->item.getPullRequest().equals(pullRequest));
    }

    private static boolean findReportedUpdate(List<ReportedUpdate> reportedUpdates, ReportedUpdate element) {
        return reportedUpdates.stream().anyMatch(item->item.getPullRequest().equals(element.getPullRequest())
                && item.getCommitID().equals(element.getCommitID()));


      //return reportedUpdates.contains(element));
      //||(element.isReported());
    }


    public static void sendToJenkins(String data) {

    }


    public void markReported(List<ReportedUpdate> reportedUpdates) {
        reportedUpdates.stream().forEach(item->item.setReported(true));
    }
}
