package agent;

import db.DbPullRequestDataManager;
import model.PullRequestsData;

import java.util.TimerTask;

public class GitHubToJenkinsTask extends TimerTask {


    private static  PullRequestsDataManager pullRequestsDataManager;

    private static PullRequestsData pullRequestsData;
    private static DbPullRequestDataManager dbManager;
    private static JenkinsManager jenkinsManager;

    public static PullRequestsDataManager getPullRequestsDataManager() {
        return pullRequestsDataManager;
    }

    public static void setPullRequestsDataManager(PullRequestsDataManager pullRequestsDataManager) {
        GitHubToJenkinsTask.pullRequestsDataManager = pullRequestsDataManager;
    }

    public void setInitialData(PullRequestsData pullRequestsData) {
        this.pullRequestsData = pullRequestsData;
       // pullRequestsDataManager.setPullRequestsData(pullRequestsData);
    }

    public PullRequestsData getData() {
       return pullRequestsData;
        // return pullRequestsDataManager.getPullRequestsData();
    }
    @Override
    public void run() {

        //PullRequestsDataManager.init();

        System.out.println("run");

        System.out.println("\nThere are "+ pullRequestsData.getPullRequests().size() + " open pull request (s) from db");
        System.out.println("\nThere are "+ pullRequestsData.getReportedUpdates().size() + " open updates from db");
        System.out.print("\n-----------------initial data-----------------\n");
       System.out.print(pullRequestsData.getReportedUpdates().toString());

        PullRequestsData newPullRequestData = pullRequestsDataManager.queryPullRequests();

        System.out.println("\nThere are "+ newPullRequestData.getPullRequests().size() + " open pull request (s) from github");
        System.out.println("\nCreated "+ newPullRequestData.getReportedUpdates().size() + "  updates from github");
        System.out.print("\n-----------------github data-----------------\n");
        System.out.print(newPullRequestData.getReportedUpdates().toString());

        PullRequestsData resultData = pullRequestsDataManager.filterUpdatedPullRequests(newPullRequestData,pullRequestsData);

        System.out.print("\n-----------------data ro report -----------------\n");
        System.out.println("\nThere are  "+ resultData.getPullRequests().size() + " updated open pull request (s)");
        if(resultData.getReportedUpdates().size() > 0 ) {
            System.out.println("\nThere are "+ resultData.getReportedUpdates().size() + " new open updates");
            System.out.print(resultData.getReportedUpdates().toString());


            //  sendToJenkins(resultData.getReportedUpdates().toString());
            resultData.getReportedUpdates().stream().forEach(item ->jenkinsManager.postData(item));
        //    jenkinsManager.postData(resultData.getReportedUpdates());
            //update in DB

        }
        pullRequestsDataManager.markReported(newPullRequestData.getReportedUpdates());
        if(resultData.getPullRequestsToArchive()!=null) {
            System.out.println("\nThere are "+ resultData.getPullRequestsToArchive().size() + " closed request (s)");
            System.out.print(resultData.getPullRequestsToArchive().toString());
        }

        pullRequestsData = new PullRequestsData(newPullRequestData.getPullRequests(),newPullRequestData.getReportedUpdates(),resultData.getPullRequestsToArchive());
        System.out.print("\n-----------------new initial data  -----------------\n");
        System.out.print(pullRequestsData.getReportedUpdates().toString());

        dbManager.writePullRequestsDataToDB(pullRequestsData);
    }


    public void setDbManager(DbPullRequestDataManager dbManager) {
        this.dbManager = dbManager;
    }

    public  void setJenkinsManager(JenkinsManager jenkinsManager) {
        this.jenkinsManager = jenkinsManager;
    }
}
