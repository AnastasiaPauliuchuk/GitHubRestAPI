package model;

import java.util.ArrayList;

public class PullRequestsData {

    private ArrayList<PullRequest> pullRequests;
    private ArrayList<ReportedUpdate> reportedUpdates;

    public PullRequestsData(ArrayList<PullRequest> pullRequests, ArrayList<ReportedUpdate> reportedUpdates) {
        this.pullRequests = pullRequests;
        this.reportedUpdates = reportedUpdates;
    }

    public PullRequestsData() {

    }

    public ArrayList<PullRequest> getPullRequests() {
        return pullRequests;
    }

    public void setPullRequests(ArrayList<PullRequest> pullRequests) {
        this.pullRequests = pullRequests;
    }

    public ArrayList<ReportedUpdate> getReportedUpdates() {
        return reportedUpdates;
    }

    public void setReportedUpdates(ArrayList<ReportedUpdate> reportedUpdates) {
        this.reportedUpdates = reportedUpdates;
    }
}
