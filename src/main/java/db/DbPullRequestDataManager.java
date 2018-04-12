package db;

import dao.PullRequestDao;
import dao.ReportedUpdateDao;
import dao.impl.PullRequestDaoImpl;
import dao.impl.ReportedUpdatesDaoImpl;
import model.PullRequest;
import model.PullRequestsData;
import model.ReportedUpdate;

import java.util.List;

public class DbPullRequestDataManager {

    private static PullRequestDao pullRequestDao;
    private static ReportedUpdateDao reportedUpdateDao;


    public static PullRequestsData readPullRequestsDataFromDB(DataSource dataSource) {
        pullRequestDao = new PullRequestDaoImpl(dataSource);
        reportedUpdateDao = new ReportedUpdatesDaoImpl(dataSource);
        List<PullRequest> pullRequests = pullRequestDao.findAllOpen();
        List<ReportedUpdate> reportedUpdates = reportedUpdateDao.findAllNotReported();
        return new PullRequestsData(pullRequests,reportedUpdates);
    }

    public static void writePullRequestsDataToDB(PullRequestsData pullRequestsData, DataSource dataSource) {
        pullRequestDao = new PullRequestDaoImpl(dataSource);
        reportedUpdateDao = new ReportedUpdatesDaoImpl(dataSource);
        pullRequestDao.updateAllClose();
        List<PullRequest> pullRequests = pullRequestsData.getPullRequests();
        for(PullRequest item:pullRequests) {
            //if item exists - > update
            if(pullRequestDao.findById(item.getId())!=null) {
                pullRequestDao.update(item);
            }
            else {
                pullRequestDao.insert(item);
            }
        }
        List<ReportedUpdate> reportedUpdates = pullRequestsData.getReportedUpdates();
        for(ReportedUpdate item:reportedUpdates) {

            if(reportedUpdateDao.findByCompositeKey(item.getPullRequest().getId(),item.getCommitID())!=null) {
                reportedUpdateDao.update(item);
            }
            else {
                reportedUpdateDao.insert(item);
            }

        }
        // pullRequestsData.getPullRequests().stream().
        //pullRequestDao.insert();
        //List<PullRequest> pullRequests = pullRequestDao.findAllOpen();
        // List<ReportedUpdate> reportedUpdates = reportedUpdateDao.findAllNotReported();

    }

    public static void writeReportUpdatesClosed(DataSource dataSource) {
        reportedUpdateDao = new ReportedUpdatesDaoImpl(dataSource);
        reportedUpdateDao.updateAllClose();
    }

}


