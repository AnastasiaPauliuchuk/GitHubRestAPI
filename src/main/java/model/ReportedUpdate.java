package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "reported_updates")
public class ReportedUpdate {


    public static final String TABLE_NAME = "reported_updates";
    public static final String PULLREQUEST_COLUMN = "pull_request_id";
    public static final String COMMIT_COLUMN = "commit_id";
    public static final String ISREPORTED_COLUMN = "is_reported";


    @Column(name = "pull_request_id")
    private PullRequest pullRequest;
    @Column(name = "commit_id")
    private String commitID;
    @Column(name = "is_reported")
    private boolean isReported;

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
        return "\n{" +
                "pullRequest=" + pullRequest.getId() +
                ",commitID='" + commitID + '\'' +
                ",isReported=" + isReported +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportedUpdate that = (ReportedUpdate) o;
        return isReported == that.isReported &&
                Objects.equals(pullRequest, that.pullRequest) &&
                Objects.equals(commitID, that.commitID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pullRequest, commitID, isReported);
    }
}
