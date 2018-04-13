package agent;


import model.PullRequest;
import model.PullRequestsData;
import model.ReportedUpdate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import properties.PropertiesResourceManager;

import java.util.List;

public class JenkinsManager {


    private static final String JENKINS_TOKEN_PROP = "jenkinsToken";
    private static final String JENKINS_POST_URL_PROP = "jenkinsPostUrl";

    private static  String jenkinsPostUrl;
    private static String jenkinsToken;

    public JenkinsManager(String filename) {
        PropertiesResourceManager prop = new PropertiesResourceManager(filename);
        jenkinsPostUrl = prop.getProperty(JENKINS_POST_URL_PROP);
        jenkinsToken = prop.getProperty(JENKINS_TOKEN_PROP);
    }

    public void postData(ReportedUpdate reportedUpdate) {

        PullRequest pullRequest = reportedUpdate.getPullRequest();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("user",jenkinsToken);
        body.add("header","Pull request was updated:");
        body.add("number",pullRequest.getNumber().toString());
        body.add("repoUrl",pullRequest.getRepoURL());
        body.add("author",pullRequest.getAuthorLogin());
        body.add("baseBranch", pullRequest.getBaseRefName());
        body.add("headBranch",pullRequest.getHeadRefName());
        body.add("lastCommit",reportedUpdate.getCommitID());
        HttpEntity<MultiValueMap<String, String>> requestEntity=
                new HttpEntity<>(body, headers);
        restTemplate.postForLocation(jenkinsPostUrl,requestEntity);
    }


}
