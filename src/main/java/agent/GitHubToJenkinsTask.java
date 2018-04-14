package agent;

import db.DbPullRequestDataManager;
import githubapi.GitHubApiManager;
import model.PullRequestsData;
import org.apache.log4j.Logger;

import java.util.TimerTask;


public class GitHubToJenkinsTask extends TimerTask {


    private static PullRequestsData pullRequestsData = null ;

    private static  PullRequestsDataManager pullRequestsDataManager;
    private static DbPullRequestDataManager dbManager = null;
    private static JenkinsManager jenkinsManager;
    private static GitHubApiManager gitHubApiManager;

    public static PullRequestsDataManager getPullRequestsDataManager() {
        return pullRequestsDataManager;
    }

    public static Logger logger = Logger.getLogger(GitHubToJenkinsTask.class);

    public static void setPullRequestsDataManager(PullRequestsDataManager pullRequestsDataManager) {
        GitHubToJenkinsTask.pullRequestsDataManager = pullRequestsDataManager;
    }

    public void setInitialData(PullRequestsData pullRequestsData) {
        this.pullRequestsData = pullRequestsData;
    }


    @Override
    public void run() {

        logger.info("run");

        if(pullRequestsData!=null) {
            logger.info("\nThere are "+ pullRequestsData.getPullRequests().size() + " open pull request (s) from db");
            logger.info("\nThere are "+ pullRequestsData.getReportedUpdates().size() + " open updates from db");
            System.out.print("\n-----------------initial data-----------------\n");
            System.out.print(pullRequestsData.getReportedUpdates().toString());
        }
        

        PullRequestsData newPullRequestData = gitHubApiManager.queryPullRequests();

        logger.info("\nThere are "+ newPullRequestData.getPullRequests().size() + " open pull request (s) from github");
        logger.info("\nCreated "+ newPullRequestData.getReportedUpdates().size() + "  updates from github");
        System.out.print("\n-----------------github data-----------------\n");
        System.out.print(newPullRequestData.getReportedUpdates().toString());

        PullRequestsData resultData = pullRequestsDataManager.filterUpdatedPullRequests(newPullRequestData,pullRequestsData);

        System.out.print("\n-----------------data ro report -----------------\n");
        logger.info("\nThere are  "+ resultData.getPullRequests().size() + " updated open pull request (s)");
        if(resultData.getReportedUpdates().size() > 0 ) {
            logger.info("\nThere are "+ resultData.getReportedUpdates().size() + " new open updates");
            System.out.print(resultData.getReportedUpdates().toString());
            resultData.getReportedUpdates().stream().forEach(item ->jenkinsManager.postData(item));

        }
        pullRequestsDataManager.markReported(newPullRequestData.getReportedUpdates());
        if(resultData.getPullRequestsToArchive()!=null) {
            logger.info("\nThere are "+ resultData.getPullRequestsToArchive().size() + " closed request (s)");
            System.out.print(resultData.getPullRequestsToArchive().toString());
        }

        pullRequestsData = new PullRequestsData(newPullRequestData.getPullRequests(),newPullRequestData.getReportedUpdates(),resultData.getPullRequestsToArchive());
        System.out.print("\n-----------------new initial data  -----------------\n");
        System.out.print(pullRequestsData.getReportedUpdates().toString());

        if(dbManager!=null) dbManager.writePullRequestsDataToDB(pullRequestsData);
    }


    public void setDbManager(DbPullRequestDataManager dbManager) {
        this.dbManager = dbManager;
    }

    public  void setJenkinsManager(JenkinsManager jenkinsManager) {
        this.jenkinsManager = jenkinsManager;
    }

    public void setGitHubApiManager(GitHubApiManager gitHubApiManager) {
        this.gitHubApiManager = gitHubApiManager;
    }
}
