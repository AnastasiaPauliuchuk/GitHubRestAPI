package agent;

import model.PullRequest;
import model.PullRequestsData;
import model.ReportedUpdate;

import java.util.List;
import java.util.stream.Collectors;


public class PullRequestsDataManager  {




    public PullRequestsData filterUpdatedPullRequests(PullRequestsData currentPullRequestsData,PullRequestsData prevPullRequestsData) {

        if(prevPullRequestsData==null || prevPullRequestsData.getPullRequests().isEmpty()) {
            return currentPullRequestsData;
        }
        else {
            PullRequestsData data = new PullRequestsData();
            List <ReportedUpdate> reportedUpdates = currentPullRequestsData.getReportedUpdates().stream().filter(item ->!findReportedUpdate(prevPullRequestsData.getReportedUpdates(),item)).collect(Collectors.toList());
            final List <PullRequest> pullRequests = currentPullRequestsData.getPullRequests();
            //archive
            List <PullRequest> pullRequestsToArchive = prevPullRequestsData.getPullRequests().stream().filter(item->!(pullRequests.contains(item))).collect(Collectors.toList());
            pullRequestsToArchive.stream().forEach(item->item.setIsOpen(false));

            List<PullRequest> filteredPullRequests = pullRequests.stream().filter(item -> findPullRequestByUpdates(reportedUpdates, item)).collect(Collectors.toList());


            data.setPullRequests(filteredPullRequests);
            data.setPullRequestsToArchive(pullRequestsToArchive);
            data.setReportedUpdates(reportedUpdates);


            return data;


        }

    }

    private  boolean findPullRequestByUpdates(List<ReportedUpdate> reportedUpdates, PullRequest pullRequest) {
        return reportedUpdates.stream().anyMatch(item->item.getPullRequest().equals(pullRequest));
    }

    private  boolean findReportedUpdate(List<ReportedUpdate> reportedUpdates, ReportedUpdate element) {
        return reportedUpdates.stream().anyMatch(item->item.getPullRequest().equals(element.getPullRequest())
                && item.getCommitID().equals(element.getCommitID()));
    }



    public void markReported(List<ReportedUpdate> reportedUpdates) {
        reportedUpdates.stream().forEach(item->item.setReported(true));
    }
}
