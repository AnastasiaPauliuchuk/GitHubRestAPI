package model;

import json.JsonPullRequestDataParser;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import properties.PropertiesResourceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
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

    public static PullRequestsData getPullRequestsData() {
        return pullRequestsData;
    }

    public static void setPullRequestsData(PullRequestsData pullRequestsData) {
        PullRequestsDataManager.pullRequestsData = pullRequestsData;
    }

    public static void init() {
        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        accessToken = prop.getProperty(AUTH_TOKEN_PROP);
        postUrl = prop.getProperty(POST_URL_PROP);
        repositoryOwner = prop.getProperty(REPO_OWNER_PROP);
        repositoryName = prop.getProperty(REPO_NAME_PROP);
        jenkinsToken = prop.getProperty("jenkinsToken");

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
            List <ReportedUpdate> reportedUpdates =
            currentPullRequestsData.getReportedUpdates().stream().filter(item ->findReportedUpdate(pullRequestsData.getReportedUpdates(),item)).collect(Collectors.toList());
            List <PullRequest> pullRequests =
            currentPullRequestsData.getPullRequests().stream().filter(item -> findPullRequestByUpdates(currentPullRequestsData.getReportedUpdates(), item)).collect(Collectors.toList());
            data.setPullRequests((ArrayList<PullRequest>)pullRequests);
            data.setReportedUpdates((ArrayList<ReportedUpdate> )reportedUpdates);


            return data;


        }

    }

    private static boolean findPullRequestByUpdates(ArrayList<ReportedUpdate> reportedUpdates, PullRequest pullRequest) {
        return reportedUpdates.stream().anyMatch(item->item.getPullRequest().equals(pullRequest));
    }

    private static boolean findReportedUpdate(List<ReportedUpdate> reportedUpdates, ReportedUpdate element) {
        return reportedUpdates.stream().allMatch(item->item.getPullRequest().equals(element.getPullRequest())
                &&item.getCommitID().equals(element.getCommitID())
                &&!item.isReported());

    }

    public static boolean findPullRequestByNumber(List<PullRequest> prevList, PullRequest element) {
        return prevList.stream().anyMatch(item -> item.getNumber().equals(element.getNumber()));
    }

    public static boolean findByNumber(PullRequestsData pullRequestsData, PullRequest pullRequest, ReportedUpdate reportedUpdate) {
        return  (pullRequestsData.getPullRequests().stream().anyMatch(item -> item.getNumber().equals(pullRequest.getNumber()))||
                pullRequestsData.getReportedUpdates().stream().anyMatch(item->reportedUpdate.getCommitID().equals(item.getCommitID())));
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




    public static void run() {
        init();
        //System.out.println(pullRequests);
        System.out.println("run");
        PullRequestsData newPullRequestData = queryPullRequests();
        System.out.println("There are "+ newPullRequestData.getPullRequests().size() + " open pull request (s)");

       // ArrayList<PullRequest>resultPullRequests = (ArrayList<PullRequest>) filterUpdatedPullRequests(newPullRequestData.getPullRequests(),pullRequestsData.getPullRequests());
        PullRequestsData resultData = filterUpdatedPullRequests(newPullRequestData,pullRequestsData);
        System.out.println("There are  "+ resultData.getReportedUpdates().size() + " updated open pull request (s)");
        if(resultData.getReportedUpdates().size() > 0 ) {
            System.out.print(resultData.getReportedUpdates().toString());
          //  sendToJenkins(resultData.getReportedUpdates().toString());
            //update in DB
        }

        pullRequestsData= new PullRequestsData(newPullRequestData.getPullRequests(),newPullRequestData.getReportedUpdates());
      //  pullRequestsData.setPullRequests(newPullRequestData.getPullRequests());

    }
}
