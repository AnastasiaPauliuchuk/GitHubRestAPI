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

public class GitHubToJenkinsAgent {

    private static final String SETTINGS_FILE = "settings.properties";
    private static final String PERIOD_PROP = "jdbc.properties";
    private static final String JDBC_PROPERTIES = "jdbc.properties";
    private static final String JDBC_URL_PROP = "jdbc.url";
    private static final String JDBC_LOGIN_PROP = "jdbc.login";
    private static final String JDBC_PWD_PROP = "jdbc.password";

    private static final boolean isRewrite = true;

    //private static  PullRequestsData pullRequestsData;


    public static void main(String args[]) {

        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        Integer period = Integer.parseInt(prop.getProperty(PERIOD_PROP, "60000"));

        GitHubToJenkinsTask gitHubToJenkinsTask = new GitHubToJenkinsTask();
        PullRequestsDataManager pullRequestsDataManager = new PullRequestsDataManager(SETTINGS_FILE);
        gitHubToJenkinsTask.setPullRequestsDataManager(pullRequestsDataManager);


        prop = new PropertiesResourceManager(JDBC_PROPERTIES);
        DataSource dataSource = new DataSource(prop.getProperty(JDBC_URL_PROP), prop.getProperty(JDBC_LOGIN_PROP), prop.getProperty(JDBC_PWD_PROP));

        PullRequestsData initialPullRequestData = DbPullRequestDataManager.readPullRequestsDataFromDB(dataSource);
        int sizeNotReported = initialPullRequestData.getReportedUpdates().size();
        if(sizeNotReported > 0)
        {
            System.out.println("There are " + sizeNotReported + " not reported updates in DB");
            if(isRewrite) {
                initialPullRequestData.getReportedUpdates().stream().forEach(item->item.setReported(true));
                DbPullRequestDataManager.writeReportUpdatesClosed(dataSource);
            }
        }
      //  Timer time = new Timer();
        gitHubToJenkinsTask.setInitialData(initialPullRequestData);

        //time.schedule(gitHubToJenkinsTask, 0, period);
        gitHubToJenkinsTask.run();
        DbPullRequestDataManager.writePullRequestsDataToDB(gitHubToJenkinsTask.getData(), dataSource);
    }


}
