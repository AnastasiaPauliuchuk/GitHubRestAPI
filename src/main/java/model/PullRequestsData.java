package model;

import java.util.List;

public class PullRequestsData {

    private List<PullRequest> pullRequests;
    private List<PullRequest> pullRequestsToArchive;
    private List<ReportedUpdate> reportedUpdates;

    public PullRequestsData(List<PullRequest> pullRequests, List<ReportedUpdate> reportedUpdates) {
        this.pullRequests = pullRequests;
        this.reportedUpdates = reportedUpdates;
    }
    public PullRequestsData(List<PullRequest> pullRequests, List<ReportedUpdate> reportedUpdates , List<PullRequest> pullRequestsToArchive ) {
        this.pullRequests = pullRequests;
        this.reportedUpdates = reportedUpdates;
        this.pullRequestsToArchive = pullRequestsToArchive;
    }
    public PullRequestsData() {

    }

    public List<PullRequest> getPullRequests() {
        return pullRequests;
    }

    public void setPullRequests(List<PullRequest> pullRequests) {
        this.pullRequests = pullRequests;
    }

    public List<ReportedUpdate> getReportedUpdates() {
        return reportedUpdates;
    }

    public void setReportedUpdates(List<ReportedUpdate> reportedUpdates) {
        this.reportedUpdates = reportedUpdates;
    }

    public List<PullRequest> getPullRequestsToArchive() {
        return pullRequestsToArchive;
    }

    public void setPullRequestsToArchive(List<PullRequest> pullRequestsToArchive) {
        this.pullRequestsToArchive = pullRequestsToArchive;
    }

    public PullRequest getPullRequestById(String id) {
        List<PullRequest> pullRequests = this.getPullRequests();
        for(PullRequest item:pullRequests) {
            if(item.getId().equals(id))
                return item;
        }
        pullRequests = this.getPullRequestsToArchive();
        for(PullRequest item:pullRequests) {
            if(item.getId().equals(id))
                return item;
        }
        return null;
    }


}


