package githubapi;

import json.JsonPullRequestDataParser;
import model.PullRequestsData;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import properties.PropertiesResourceManager;

import java.io.IOException;

public class GitHubApiManager {

    private static final  String PULL_REQUESTS_GRAPHQL_STRING =
            "{\"query\":\"{\\n  repository(owner: \\\"AnastasiaPauliuchuk\\\", name: \\\"GitTestAPI\\\") {\\n    pullRequests(last: 100, states: [OPEN]) {\\n      edges {\\n        node {\\n          id\\n          number\\n          baseRefName\\n          headRef {\\n            name\\n            target {\\n              ... on Commit {\\n                id\\n              }\\n            }\\n          }\\n          repository {\\n            url\\n          }\\n          author {\\n            login\\n          }\\n        }\\n      }\\n    }\\n  }\\n}\\n\",\"variables\":\"{}\",\"operationName\":null}";

    private static final String GRAPHQL_RESOURCE_URL = "https://api.github.com/graphql";

    private  String accessToken;
    private  String repositoryOwner;
    private  String repositoryName;
    private JsonPullRequestDataParser jsonPullRequestDataParser;

    public GitHubApiManager(String accessToken, String repositoryOwner, String repositoryName) {
        this.accessToken = accessToken;
        this.repositoryOwner = repositoryOwner;
        this.repositoryName = repositoryName;
    }

    public  PullRequestsData queryPullRequests() {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        String requestJson = String.format(PULL_REQUESTS_GRAPHQL_STRING, repositoryOwner, repositoryName);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.exchange(GRAPHQL_RESOURCE_URL, HttpMethod.POST, entity, String.class);
        try {
            return jsonPullRequestDataParser.parsePullRequestList(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
            return new PullRequestsData();
        }
    }

    public void setJsonPullRequestDataParser(JsonPullRequestDataParser jsonParser) {
        this.jsonPullRequestDataParser = jsonParser;
    }
}
