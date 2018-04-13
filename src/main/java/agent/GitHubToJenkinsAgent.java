package agent;

import dao.PullRequestDao;
import dao.ReportedUpdateDao;
import dao.impl.PullRequestDaoImpl;
import dao.impl.ReportedUpdatesDaoImpl;
import db.DataSource;
import db.DbPullRequestDataManager;
import model.PullRequest;
import model.PullRequestsData;
import model.ReportedUpdate;
import properties.PropertiesResourceManager;

import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class GitHubToJenkinsAgent {

    private static final String SETTINGS_FILE = "settings.properties";
    private static final String PERIOD_PROP = "periodMilis";
    private static final String JDBC_PROPERTIES = "jdbc.properties";
    private static final String JDBC_URL_PROP = "jdbc.url";
    private static final String JDBC_LOGIN_PROP = "jdbc.login";
    private static final String JDBC_PWD_PROP = "jdbc.password";

    private static final boolean IS_RESEND = true;

    public static void main(String args[]) {

        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        Integer period = Integer.parseInt(prop.getProperty(PERIOD_PROP, "60000"));

        GitHubToJenkinsTask gitHubToJenkinsTask = new GitHubToJenkinsTask();
        PullRequestsDataManager pullRequestsDataManager = new PullRequestsDataManager(SETTINGS_FILE);
        gitHubToJenkinsTask.setPullRequestsDataManager(pullRequestsDataManager);

        prop = new PropertiesResourceManager(JDBC_PROPERTIES);
        DataSource dataSource = new DataSource(prop.getProperty(JDBC_URL_PROP), prop.getProperty(JDBC_LOGIN_PROP), prop.getProperty(JDBC_PWD_PROP));
        DbPullRequestDataManager dbDataManager = new DbPullRequestDataManager(dataSource);
        gitHubToJenkinsTask.setDbManager(dbDataManager);


        JenkinsManager jenkinsManager = new JenkinsManager(SETTINGS_FILE);
        gitHubToJenkinsTask.setJenkinsManager(jenkinsManager);

        PullRequestsData initialPullRequestData = dbDataManager.readPullRequestsDataFromDB();
        List <ReportedUpdate> notReportedUpdates = initialPullRequestData.getReportedUpdates().stream().filter(item->!item.isReported()).collect(Collectors.toList());
        int sizeNotReported = notReportedUpdates.size();
        if(sizeNotReported > 0)
        {
            System.out.println("There are " + sizeNotReported + " not reported updates in DB");
            if(IS_RESEND) {
                notReportedUpdates.stream().forEach(item ->jenkinsManager.postData(item));
            }
            notReportedUpdates.stream().forEach(item->item.setReported(true));
            dbDataManager.writeReportUpdatesClosed();
        }
        Timer time = new Timer();
        gitHubToJenkinsTask.setInitialData(initialPullRequestData);

        time.schedule(gitHubToJenkinsTask, 0, period);

    }


}
