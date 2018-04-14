package githubapi;

import json.JsonPullRequestDataParser;
import model.PullRequestsData;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import properties.PropertiesResourceManager;

import java.io.IOException;

public class GitHubApiManager {

    private final static String PULL_REQUESTS_GRAPHQL_STRING =
            "{\"query\":\"{\\n  repository(owner: \\\"AnastasiaPauliuchuk\\\", name: \\\"GitTestAPI\\\") {\\n    pullRequests(last: 100, states: [OPEN]) {\\n      edges {\\n        node {\\n          id\\n          number\\n          baseRefName\\n          headRef {\\n            name\\n            target {\\n              ... on Commit {\\n                id\\n              }\\n            }\\n          }\\n          repository {\\n            url\\n          }\\n          author {\\n            login\\n          }\\n        }\\n      }\\n    }\\n  }\\n}\\n\",\"variables\":\"{}\",\"operationName\":null}";

    private static final String AUTH_TOKEN_PROP = "authToken";
    private static final String REPO_OWNER_PROP = "repositoryOwner";
    private static final String REPO_NAME_PROP = "repositoryName";
    private static final String POST_URL_PROP = "postUrl";

    private static final String GRAPHQL_RESOURCE_URL = "https://api.github.com/graphql";


    private static String accessToken;
    private static String repositoryOwner;
    private static String repositoryName;

    public GitHubApiManager(String settingsFilename) {
        PropertiesResourceManager prop = new PropertiesResourceManager(settingsFilename);
        accessToken = prop.getProperty(AUTH_TOKEN_PROP);
        repositoryOwner = prop.getProperty(REPO_OWNER_PROP);
        repositoryName = prop.getProperty(REPO_NAME_PROP);
    }


    public static PullRequestsData queryPullRequests() {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        String requestJson = String.format(PULL_REQUESTS_GRAPHQL_STRING, repositoryOwner, repositoryName);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.exchange(GRAPHQL_RESOURCE_URL, HttpMethod.POST, entity, String.class);
        try {
            return JsonPullRequestDataParser.parsePullRequestList(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
            return new PullRequestsData();
        }
    }

}
