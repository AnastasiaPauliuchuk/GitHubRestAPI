package model;

public class ReportedUpdate {

    PullRequest pullRequest;
    String commitID;
    boolean isReported;


    public PullRequest getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(PullRequest pullRequest) {
        this.pullRequest = pullRequest;
    }

    public String getCommitID() {
        return commitID;
    }

    public void setCommitID(String commitID) {
        this.commitID = commitID;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    @Override
    public String toString() {
        return "ReportedUpdate{" +
                "pullRequest=" + pullRequest +
                ", commitID='" + commitID + '\'' +
                ", isReported=" + isReported +
                '}';
    }
}
