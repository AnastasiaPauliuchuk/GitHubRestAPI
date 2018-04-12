package agent;

import model.PullRequestsData;

import java.util.TimerTask;

public class GitHubToJenkinsTask extends TimerTask {


    private static  PullRequestsDataManager pullRequestsDataManager;

    private static PullRequestsData pullRequestsData;

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

        System.out.println("\nThere are  "+ resultData.getReportedUpdates().size() + " updated open pull request (s)");
        if(resultData.getReportedUpdates().size() > 0 ) {
            System.out.println("\nThere are "+ resultData.getReportedUpdates().size() + " new open updates");
            System.out.print(resultData.getReportedUpdates().toString());

            //  sendToJenkins(resultData.getReportedUpdates().toString());

            //update in DB

        }

        pullRequestsData = new PullRequestsData(newPullRequestData.getPullRequests(),newPullRequestData.getReportedUpdates());
    }



}
