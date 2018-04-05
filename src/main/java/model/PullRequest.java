package model;


public class PullRequest {




    private final static String GRAPHQL_STRING = "{\"query\":\"query{\\n  repository(owner:\\\"%1$s\\\", name:\\\"%2$s\\\") {\\n\\n    pullRequests (last:100) {\\n      edges {\\n        node {\\n          number\\n          baseRefName\\n          headRefName\\n          repository {\\n            url\\n          }\\n          author {\\n            login\\n          }\\n        }\\n      }\\n      \\n      \\n    }\\n  }\\n}\\n\\n  \",\"variables\":\"{}\"}";

    private String repoURL;
    private Integer number;
    private String authorLogin;
    private String headRefName;
    private String baseRefName;

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

    @Override
    public String toString() {
        return "\nPullRequest{" +
                "\nrepoURL='" + repoURL + '\'' +
                ", \nnumber=" + number +
                ", \nauthorLogin='" + authorLogin + '\'' +
                ", \nheadRefName='" + headRefName + '\'' +
                ", \nbaseRefName='" + baseRefName + '\'' +
                "\n}";
    }
}



