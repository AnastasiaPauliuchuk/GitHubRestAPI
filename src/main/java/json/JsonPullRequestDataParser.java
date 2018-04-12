package json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.PullRequest;
import model.PullRequestsData;
import model.ReportedUpdate;

import java.io.IOException;
import java.util.ArrayList;

public class JsonPullRequestDataParser {

    private static PullRequest parsePullRequest(JsonNode node) {
        PullRequest pullRequest = new PullRequest() ;

        pullRequest.setNumber(node.get("number").asInt());
        pullRequest.setId(node.get("id").textValue());
        pullRequest.setHeadRefName(node.get("headRef").get("name").textValue());
        pullRequest.setBaseRefName(node.get("baseRefName").textValue());
        pullRequest.setAuthorLogin(node.get("author").get("login").textValue());
        pullRequest.setRepoURL(node.get("repository").get("url").textValue());
        pullRequest.setIsOpen(true);
        return pullRequest;
    }
    private static ReportedUpdate parsePullRequestUpdate(PullRequest pullRequest, JsonNode node) {
        ReportedUpdate reportedUpdate = new ReportedUpdate();
        reportedUpdate.setPullRequest(pullRequest);
        reportedUpdate.setCommitID(node.get("headRef").get("target").get("id").textValue());
        reportedUpdate.setReported(false);
        return reportedUpdate;
    }

    public static PullRequestsData parsePullRequestList(String jsonString) throws IOException {
        ArrayList<PullRequest> pullRequests = new ArrayList<>();
        ArrayList<ReportedUpdate> reportedUpdates = new ArrayList<>();
        JsonNode tree = new ObjectMapper().readTree(jsonString);
        JsonNode pullRequestNodes = tree.get("data")
                .get("repository")
                .get("pullRequests")
                .get("edges");
        for(JsonNode node:pullRequestNodes) {
            PullRequest pullRequest = parsePullRequest(node.get("node"));
            pullRequests.add(pullRequest);
            reportedUpdates.add(parsePullRequestUpdate(pullRequest,node.get("node")));
        }
        return new PullRequestsData(pullRequests,reportedUpdates);

    }



}
