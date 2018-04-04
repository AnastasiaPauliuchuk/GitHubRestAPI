/**
 * @author Anastasia Pauliuchuk
 *         created:  4/3/2018.
 */
public class GitHubResponse {
    public GitHubData data;
}

class GitHubData {
    public GitHubRepository repository;
}

class GitHubRepository {
    public String pullRequests;
}

class GitHubPullsWdges {
    public String data;
}

class GitHubPullRequests {
    public String edges;
}


