package model;


import java.util.Objects;

public class PullRequest {

    private final static String GRAPHQL_STRING =
"{\"query\":\"{\\n  repository(owner: \\\"AnastasiaPauliuchuk\\\", name: \\\"GitTestAPI\\\") {\\n    pullRequests(last: 100, states: [OPEN]) {\\n      edges {\\n        node {\\n          id\\n          number\\n          baseRefName\\n          headRef {\\n            name\\n            target {\\n              ... on Commit {\\n                id\\n              }\\n            }\\n          }\\n          repository {\\n            url\\n          }\\n          author {\\n            login\\n          }\\n        }\\n      }\\n    }\\n  }\\n}\\n\",\"variables\":\"{}\",\"operationName\":null}";


    public static final String TABLE_NAME = "pull_requests";
    public static final String ID_COLUMN = "id";
    public static final String NUMBER_COLUMN = "number";
    public static final String AUTHOR_COLUMN = "author";
    public static final String BASE_COLUMN = "base_branch";
    public static final String HEAD_COLUMN = "head_branch";
    public static final String REPO_COLUMN = "repo_url";
    public static final String IS_OPEN_COLUMN = "is_open";


    private String id;
    private Integer number;
    private String repoURL;
    private String authorLogin;
    private String headRefName;
    private String baseRefName;
    private boolean isOpen;

    public static String getGraphqlString() {
        return GRAPHQL_STRING;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepoURL() {
        return repoURL;
    }

    public void setRepoURL(String repoURL) {
        this.repoURL = repoURL;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    public void setAuthorLogin(String authorLogin) {
        this.authorLogin = authorLogin;
    }

    public String getHeadRefName() {
        return headRefName;
    }

    public void setHeadRefName(String headRefName) {
        this.headRefName = headRefName;
    }

    public String getBaseRefName() {
        return baseRefName;
    }

    public void setBaseRefName(String baseRefName) {
        this.baseRefName = baseRefName;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }


    @Override
    public String toString() {
        return "PullRequest{" +
                "id='" + id + '\'' +
                ", number=" + number +
                ", repoURL='" + repoURL + '\'' +
                ", authorLogin='" + authorLogin + '\'' +
                ", headRefName='" + headRefName + '\'' +
                ", baseRefName='" + baseRefName + '\'' +
                ", isOpen=" + isOpen +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PullRequest that = (PullRequest) o;
        return isOpen == that.isOpen &&
                Objects.equals(id, that.id) &&
                Objects.equals(number, that.number) &&
                Objects.equals(repoURL, that.repoURL) &&
                Objects.equals(authorLogin, that.authorLogin) &&
                Objects.equals(headRefName, that.headRefName) &&
                Objects.equals(baseRefName, that.baseRefName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, number, repoURL, authorLogin, headRefName, baseRefName, isOpen);
    }
}



