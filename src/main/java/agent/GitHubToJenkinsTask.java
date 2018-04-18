package agent;

import db.DbPullRequestDataManager;
import githubapi.GitHubApiManager;
import model.PullRequestsData;
import model.ReportedUpdate;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;


public class GitHubToJenkinsTask extends TimerTask {


    private PullRequestsData pullRequestsData = null;

    private PullRequestsDataManager pullRequestsDataManager;
    private DbPullRequestDataManager dbManager = null;
    private JenkinsManager jenkinsManager;
    private GitHubApiManager gitHubApiManager;


    public Logger logger = Logger.getLogger(GitHubToJenkinsTask.class);

    public void setPullRequestsDataManager(PullRequestsDataManager pullRequestsDataManager) {
        this.pullRequestsDataManager = pullRequestsDataManager;
    }

    public PullRequestsDataManager getPullRequestsDataManager() {
        return pullRequestsDataManager;
    }

    public DbPullRequestDataManager getDbManager() {
        return dbManager;
    }

    public void setInitialData(PullRequestsData pullRequestsData) {
        this.pullRequestsData = pullRequestsData;
    }


    @Override
    public void run() {

        logger.info("*************************************************************************************************************************");

        if (pullRequestsData != null) {
            logger.info("There are " + pullRequestsData.getPullRequests().size() + " open pull request (s) from db");
            logger.info("There are " + pullRequestsData.getReportedUpdates().size() + " open updates from db");
            logger.info("-----------------initial data-----------------");
            logger.info(pullRequestsData.getReportedUpdates().toString());
        }


        PullRequestsData newPullRequestData = gitHubApiManager.queryPullRequests();

        logger.info("\nThere are " + newPullRequestData.getPullRequests().size() + " open pull request (s) from github");
        logger.info("\nCreated " + newPullRequestData.getReportedUpdates().size() + "  updates from github");
        logger.info("-----------------github data-----------------");
        logger.info(newPullRequestData.getReportedUpdates().toString());

        PullRequestsData resultData = pullRequestsDataManager.filterUpdatedPullRequests(newPullRequestData, pullRequestsData);

        logger.info("-----------------data ro report -----------------");
        logger.info("There are  " + resultData.getPullRequests().size() + " updated open pull request (s)");
        if (resultData.getReportedUpdates().isEmpty()) {
            logger.info("There are " + resultData.getReportedUpdates().size() + " new open updates");
            logger.info(resultData.getReportedUpdates().toString());
            resultData.getReportedUpdates().stream().forEach(item -> jenkinsManager.postData(item));

        }
        pullRequestsDataManager.markReported(newPullRequestData.getReportedUpdates());
        if (resultData.getPullRequestsToArchive() != null) {
            logger.info("There are " + resultData.getPullRequestsToArchive().size() + " closed request (s)");
            logger.info(resultData.getPullRequestsToArchive().toString());
        }

        pullRequestsData = new PullRequestsData(newPullRequestData.getPullRequests(), newPullRequestData.getReportedUpdates(), resultData.getPullRequestsToArchive());

        if (dbManager != null) dbManager.writePullRequestsDataToDB(pullRequestsData);
    }


    public void setDbManager(DbPullRequestDataManager dbManager) {
        this.dbManager = dbManager;
    }

    public void setJenkinsManager(JenkinsManager jenkinsManager) {
        this.jenkinsManager = jenkinsManager;
    }

    public void setGitHubApiManager(GitHubApiManager gitHubApiManager) {
        this.gitHubApiManager = gitHubApiManager;
    }

    public JenkinsManager getJenkinsManager() {
        return jenkinsManager;
    }

    public void processInitialDataFromDb(boolean isResend) {

        if (dbManager != null) {
            PullRequestsData initialPullRequestData = dbManager.readPullRequestsDataFromDB();

            List<ReportedUpdate> notReportedUpdates = initialPullRequestData.getReportedUpdates().stream().filter(item -> !item.isReported()).collect(Collectors.toList());

            int sizeNotReported = notReportedUpdates.size();
            if (sizeNotReported > 0) {
                logger.info("There are " + sizeNotReported + " not reported updates in DB");
                if (isResend) {
                    notReportedUpdates.stream().forEach(item -> jenkinsManager.postData(item));
                }
                notReportedUpdates.stream().forEach(item -> item.setReported(true));
                dbManager.writeReportUpdatesClosed();
            }
            this.setInitialData(initialPullRequestData);
        }
    }
}
