import model.PullRequest;
import model.PullRequestsData;
import model.PullRequestsDataManager;

import java.util.ArrayList;
import java.util.TimerTask;

public class GitHubToJenkinsTask extends TimerTask {

    //private static PullRequestsData pullRequestsData = null;

    public void setInitialData(PullRequestsData dataFormDB) {
        PullRequestsDataManager.setPullRequestsData(dataFormDB);
    }
    @Override
    public void run() {
       /* PullRequestsDataManager.init();
        //System.out.println(pullRequests);
        System.out.println("run");
        PullRequestsData newPullRequestData = PullRequestsDataManager.queryPullRequests();
        System.out.println("There are "+ newPullRequestData.getPullRequests().size() + " open pull request (s)");


*/
      /*  ArrayList<PullRequest> resultPullRequests = (ArrayList<PullRequest>) PullRequestsDataManager.filterUpdatedPullRequests(newPullRequestData.getPullRequests(),
                PullRequestsDataManager.getPullRequestsData().getPullRequests());*/
        /*System.out.println("There are  "+ resultPullRequests.size() + " updated open pull request (s)");
        if(resultPullRequests.size() > 0 ) {
            System.out.print(resultPullRequests.toString());
          //  sendToJenkins(resultPullRequests.toString());
            //update in DB
        }


        PullRequestsDataManager.getPullRequestsData().setPullRequests(newPullRequestData.getPullRequests());
*/
    PullRequestsDataManager.run();
    }


    public void writeDataToDB() {
    }
}
