package agent;


import model.PullRequest;
import model.ReportedUpdate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import properties.PropertiesResourceManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JenkinsManager {



    private   String jenkinsPostUrl;
    private  String jenkinsToken;


    public JenkinsManager(String jenkinsPostUrl, String jenkinsToken) {
        this.jenkinsPostUrl = jenkinsPostUrl;
        this.jenkinsToken = jenkinsToken;
    }

    public void postData(ReportedUpdate reportedUpdate) {

        PullRequest pullRequest = reportedUpdate.getPullRequest();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("user",jenkinsToken);
        body.add("header", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM d yyyy  hh:mm a"))+": Pull request was updated:");
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
