import model.JsonPullRequestParser;
import model.PullRequest;
import model.WebHook;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import utils.PropertiesResourceManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


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
    private static String jenkinsToken;


    public static void init() {
        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        accessToken = prop.getProperty(AUTH_TOKEN_PROP);
        postUrl = prop.getProperty(POST_URL_PROP);
        repositoryOwner = prop.getProperty(REPO_OWNER_PROP);
        repositoryName = prop.getProperty(REPO_NAME_PROP);
        jenkinsToken = prop.getProperty("jenkinsToken");

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

    public static void sendToJenkins(String data) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      //  headers.set("Authorization", "Bearer "+ accessToken);

        //String requestURL = "http://localhost:8081/job/GitHubApiProject/buildWithParameters";
        //{"parameter": [{"name":"id", "value":"123"}, {"name":"verbosity", "value":"high"}]}
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        //Map<String,Object> body=new HashMap<String,Object>();
        body.add("user",jenkinsToken);
        body.add("data","something");
        HttpEntity<MultiValueMap<String, String>> requestEntity=
                new HttpEntity<MultiValueMap<String, String>>(body, headers);
      //  HttpEntity<Map> entity = new HttpEntity<Map>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(postUrl, HttpMethod.POST,requestEntity,String.class);
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
        //createWebHook();
        try {
             sendToJenkins(JsonPullRequestParser.parsePullRequestList(getPullRequests()).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
