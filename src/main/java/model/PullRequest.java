package model;


public class PullRequest {




    private final static String GRAPHQL_STRING =
"{\"query\":\"{\\n  repository(owner: \\\"AnastasiaPauliuchuk\\\", name: \\\"GitTestAPI\\\") {\\n    pullRequests(last: 100, states: [OPEN]) {\\n      edges {\\n        node {\\n          id\\n          number\\n          baseRefName\\n          headRef {\\n            name\\n            target {\\n              ... on Commit {\\n                id\\n              }\\n            }\\n          }\\n          repository {\\n            url\\n          }\\n          author {\\n            login\\n          }\\n        }\\n      }\\n    }\\n  }\\n}\\n\",\"variables\":\"{}\",\"operationName\":null}";
    private String repoURL;
    private Integer number;
    private String authorLogin;
    private String headRefName;
    private String baseRefName;
    private String lastCommitId;

    public static String getGraphqlString() {
        return GRAPHQL_STRING;
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

    public String getLastCommitId() {
        return lastCommitId;
    }

    public void setLastCommitId(String lastCommitId) {
        this.lastCommitId = lastCommitId;
    }

    @Override
    public String toString() {
        return "PullRequest{" +
                "repoURL='" + repoURL + '\'' +
                ", number=" + number +
                ", authorLogin='" + authorLogin + '\'' +
                ", headRefName='" + headRefName + '\'' +
                ", baseRefName='" + baseRefName + '\'' +/*
                ", lastCommitId='" + lastCommitId + '\'' +*/
                '}';
    }
}



