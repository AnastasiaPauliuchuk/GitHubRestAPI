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

    private  PullRequestDao pullRequestDao;
    private  ReportedUpdateDao reportedUpdateDao;

    private static DataSource dataSource;

    public DbPullRequestDataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public  PullRequestsData readPullRequestsDataFromDB() throws NullPointerException {
        pullRequestDao = new PullRequestDaoImpl(dataSource);
        reportedUpdateDao = new ReportedUpdatesDaoImpl(dataSource);

        List<PullRequest> pullRequests = pullRequestDao.findAllOpen();
        List<ReportedUpdate> reportedUpdates = reportedUpdateDao.findAll();
        return new PullRequestsData(pullRequests, reportedUpdates);
    }

    public  void writePullRequestsDataToDB(PullRequestsData pullRequestsData) {
        pullRequestDao = new PullRequestDaoImpl(dataSource);
        reportedUpdateDao = new ReportedUpdatesDaoImpl(dataSource);

        List<PullRequest> pullRequests = pullRequestsData.getPullRequests();
        for (PullRequest item : pullRequests) {
            if (pullRequestDao.findById(item.getId()) != null) {
                pullRequestDao.update(item);
            } else {
                pullRequestDao.insert(item);
            }
        }
        List<PullRequest> pullRequestsToArchive = pullRequestsData.getPullRequestsToArchive();

        if(pullRequestsToArchive!=null) pullRequestsToArchive.stream().forEach(item -> pullRequestDao.update(item));

        List<ReportedUpdate> reportedUpdates = pullRequestsData.getReportedUpdates();
        for (ReportedUpdate item : reportedUpdates) {

            if (reportedUpdateDao.findByCompositeKey(item.getPullRequest().getId(), item.getCommitID()) != null) {
                reportedUpdateDao.update(item);
            } else {
                reportedUpdateDao.insert(item);
            }

        }
        // pullRequestsData.getPullRequests().stream().
        //pullRequestDao.insert();
        //List<PullRequest> pullRequests = pullRequestDao.findAllOpen();
        // List<ReportedUpdate> reportedUpdates = reportedUpdateDao.findAllNotReported();

    }

    public  void writeReportUpdatesClosed() {
        reportedUpdateDao = new ReportedUpdatesDaoImpl(dataSource);
        reportedUpdateDao.updateAllClose();
    }


}


