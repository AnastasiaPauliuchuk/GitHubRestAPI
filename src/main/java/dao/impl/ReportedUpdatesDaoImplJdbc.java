package dao.impl;

import dao.PullRequestDao;
import dao.ReportedUpdateDao;
import db.DataSource;
import model.ReportedUpdate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportedUpdatesDaoImplJdbc implements ReportedUpdateDao {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ReportedUpdatesDaoImplJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<ReportedUpdate> findAll() {
        List<ReportedUpdate> result = new ArrayList<ReportedUpdate>();
        try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL)) {
                    try(ResultSet rs = statement.executeQuery()) {
                        while (rs.next()) {
                            ReportedUpdate item = new ReportedUpdate();

                            PullRequestDao pullRequestDao = new PullRequestDaoImplJdbc(this.getDataSource());
                            item.setPullRequest(pullRequestDao.findById(rs.getString(ReportedUpdate.PULLREQUEST_COLUMN)));

                            item.setReported(rs.getBoolean(ReportedUpdate.ISREPORTED_COLUMN));
                            item.setCommitID(rs.getString(ReportedUpdate.COMMIT_COLUMN));

                            result.add(item);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<ReportedUpdate> findAllNotReported() {
        List<ReportedUpdate> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();){

            try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_NOT_REPORTED)) {
                try(ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        ReportedUpdate item = new ReportedUpdate();

                        PullRequestDao pullRequestDao = new PullRequestDaoImplJdbc(this.getDataSource());
                        item.setPullRequest(pullRequestDao.findById(rs.getString(ReportedUpdate.PULLREQUEST_COLUMN)));

                        item.setReported(rs.getBoolean(ReportedUpdate.ISREPORTED_COLUMN));
                        item.setCommitID(rs.getString(ReportedUpdate.COMMIT_COLUMN));

                        result.add(item);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void updateAllClose() {

        try (Connection connection = dataSource.getConnection();) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ALL_REPORTED)) {
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ReportedUpdate findByPullRequestId(Long id) {
        ReportedUpdate item = null;
        try (Connection connection = dataSource.getConnection();) {

            try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_PULLREQUEST_ID)) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery() ){
                    while (rs.next()) {
                        item = new ReportedUpdate();

                        PullRequestDao pullRequestDao = new PullRequestDaoImplJdbc(this.getDataSource());
                        item.setPullRequest(pullRequestDao.findById(rs.getString(ReportedUpdate.PULLREQUEST_COLUMN)));

                        item.setReported(rs.getBoolean(ReportedUpdate.ISREPORTED_COLUMN));
                        item.setCommitID(rs.getString(ReportedUpdate.COMMIT_COLUMN));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public ReportedUpdate findByCompositeKey(String pullRequestId, String commitId) {
        ReportedUpdate item = null;
        try (Connection  connection = dataSource.getConnection()){

            try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_PULLREQUEST_AND_COMMIT)) {
                statement.setString(1, pullRequestId);
                statement.setString(2, commitId);
                try(ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        item = new ReportedUpdate();

                        PullRequestDao pullRequestDao = new PullRequestDaoImplJdbc(this.getDataSource());
                        item.setPullRequest(pullRequestDao.findById(rs.getString(ReportedUpdate.PULLREQUEST_COLUMN)));

                        item.setReported(rs.getBoolean(ReportedUpdate.ISREPORTED_COLUMN));
                        item.setCommitID(rs.getString(ReportedUpdate.COMMIT_COLUMN));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }


    public void insert(ReportedUpdate item) {
        try (Connection  connection = dataSource.getConnection();) {

            try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {


                statement.setString(1, item.getPullRequest().getId());
                statement.setString(2, item.getCommitID());
                statement.setInt(3, (item.isReported()) ? 1 : 0);

                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ReportedUpdate item) {

        try (Connection connection = dataSource.getConnection()){

            try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_STATE)) {

                statement.setString(2, item.getPullRequest().getId());
                statement.setString(3, item.getCommitID());
                statement.setInt(1, item.isReported() ? 1 : 0);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(ReportedUpdate item) {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_PULLREQUEST_ID)) {
                statement.setString(1, item.getPullRequest().getId());
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
