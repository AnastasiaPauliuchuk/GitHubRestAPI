package agent;

import db.DataSource;
import db.DbPullRequestDataManager;
import githubapi.GitHubApiManager;
import json.JsonPullRequestDataParser;
import model.PullRequestsData;
import model.ReportedUpdate;
import org.apache.log4j.Logger;
import properties.PropertiesResourceManager;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class GitHubToJenkinsAgent {

    private static final String SETTINGS_FILE = "settings.properties";
    private static final String PERIOD_PROP = "periodMilis";

    private static final String JDBC_PROPERTIES = "jdbc.properties";
    private static final String JDBC_URL_PROP = "jdbc.url";
    private static final String JDBC_LOGIN_PROP = "jdbc.login";
    private static final String JDBC_PASS_PROP = "jdbc.password";

    private static boolean isResend = true;
    private static boolean isUseDb = true;

    private static final Logger logger = Logger.getLogger(GitHubToJenkinsAgent.class);

    public static void main(String args[]) {

        List<String> argsList = Arrays.asList(args);
        isResend = argsList.contains("-r");
        isUseDb = argsList.contains("-d");

        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        Integer period = Integer.parseInt(prop.getProperty(PERIOD_PROP, "60000"));

        GitHubToJenkinsTask gitHubToJenkinsTask = new GitHubToJenkinsTask();
        PullRequestsDataManager pullRequestsDataManager = new PullRequestsDataManager();
        gitHubToJenkinsTask.setPullRequestsDataManager(pullRequestsDataManager);

        GitHubApiManager gitHubApiManager = new GitHubApiManager(SETTINGS_FILE);
        JsonPullRequestDataParser jsonPullRequestDataParser = new JsonPullRequestDataParser();
        gitHubApiManager.setJsonParser(jsonPullRequestDataParser);
        gitHubToJenkinsTask.setGitHubApiManager(gitHubApiManager);

        prop = new PropertiesResourceManager(JDBC_PROPERTIES);
        DataSource dataSource = new DataSource(prop.getProperty(JDBC_URL_PROP), prop.getProperty(JDBC_LOGIN_PROP), prop.getProperty(JDBC_PASS_PROP));
        DbPullRequestDataManager dbDataManager = null;

        if (isUseDb) {
            dbDataManager = new DbPullRequestDataManager(dataSource);
            gitHubToJenkinsTask.setDbManager(dbDataManager);
        }

        JenkinsManager jenkinsManager = new JenkinsManager(SETTINGS_FILE);
        gitHubToJenkinsTask.setJenkinsManager(jenkinsManager);

        if (dbDataManager != null) {
            PullRequestsData initialPullRequestData = dbDataManager.readPullRequestsDataFromDB();
            List<ReportedUpdate> notReportedUpdates = initialPullRequestData.getReportedUpdates().stream().filter(item -> !item.isReported()).collect(Collectors.toList());
            int sizeNotReported = notReportedUpdates.size();
            if (sizeNotReported > 0) {
                logger.info("There are " + sizeNotReported + " not reported updates in DB");
                if (isResend) {
                    notReportedUpdates.stream().forEach(item -> jenkinsManager.postData(item));
                }
                notReportedUpdates.stream().forEach(item -> item.setReported(true));
                dbDataManager.writeReportUpdatesClosed();
            }
            gitHubToJenkinsTask.setInitialData(initialPullRequestData);
        }


        Timer time = new Timer();
        time.schedule(gitHubToJenkinsTask, 0, period);

    }


}
