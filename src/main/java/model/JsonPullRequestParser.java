package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public class JsonPullRequestParser {

    private static PullRequest parsePullRequest(JsonNode node) {
        PullRequest pullRequest = new PullRequest() ;

        pullRequest.setNumber(node.get("number").asInt());
        pullRequest.setHeadRefName(node.get("headRefName").textValue());
        pullRequest.setBaseRefName(node.get("baseRefName").textValue());
        pullRequest.setAuthorLogin(node.get("author").get("login").textValue());
        pullRequest.setRepoURL(node.get("repository").get("url").textValue());

        return pullRequest;
    }

    public static ArrayList<PullRequest> parsePullRequestList(String jsonString) throws IOException {
        ArrayList<PullRequest> pullRequests = new ArrayList<>();
        JsonNode tree = new ObjectMapper().readTree(jsonString);
        JsonNode pullRequestNodes = tree.get("data")
                .get("repository")
                .get("pullRequests")
                .get("edges");
        for(JsonNode node:pullRequestNodes) {
            pullRequests.add(parsePullRequest(node.get("node")));
        }
        return pullRequests;
    }

}
