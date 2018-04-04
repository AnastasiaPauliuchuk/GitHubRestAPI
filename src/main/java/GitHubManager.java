import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import utils.PropertiesResourceManager;

public class GitHubManager {

    private static final String SETTINGS_FILE = "settings.properties";
    private static final String RESOURCE_URL = "https://api.github.com/graphql";
    private static final String[] HOOKS_EVENTS = {"push","pull request"};
    private static  String accessToken;
    public static void init() {
        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        accessToken = prop.getProperty("authToken");
    }
    public static String getPullRequests() {

        init();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+ accessToken);

        String requestJson = "{\"query\":\"query {   repository(owner: \\\"AnastasiaPauliuchuk\\\", name: \\\"GitTestAPI\\\") {     pullRequests(last: 10) {       edges {         node {           number           author {             login           }           merged           baseRefName         }       }     } } }\",\"variables\":\"{}\"}";
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange(RESOURCE_URL, HttpMethod.POST,entity,String.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.toString());
        System.out.println(response.getBody());

        GitHubResponse request = new GitHubResponse();
        HttpEntity<GitHubResponse> httpRequest = new HttpEntity<GitHubResponse>(request, headers);

        GitHubResponse result = restTemplate.postForObject(RESOURCE_URL, entity, GitHubResponse.class);
        System.out.println(result.data);

        return "";
    }

    public static void createWebHooks() {

    }

    public static void main(String[] args) {
        getPullRequests();
    }

}
