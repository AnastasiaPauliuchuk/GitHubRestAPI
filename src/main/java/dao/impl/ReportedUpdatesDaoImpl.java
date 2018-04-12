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

public class ReportedUpdatesDaoImpl implements ReportedUpdateDao {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ReportedUpdatesDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<ReportedUpdate> findAll() {
        List<ReportedUpdate> result = new ArrayList<ReportedUpdate>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ReportedUpdate item = new ReportedUpdate();

                PullRequestDao pullRequestDao = new PullRequestDaoImpl(this.getDataSource());
                item.setPullRequest(pullRequestDao.findById(rs.getString(ReportedUpdate.PULLREQUEST_COLUMN)));

                item.setReported(rs.getBoolean(ReportedUpdate.ISREPORTED_COLUMN));
                item.setCommitID(rs.getString(ReportedUpdate.COMMIT_COLUMN));

                result.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return result;
    }
    @Override
    public List<ReportedUpdate> findAllNotReported() {
        List<ReportedUpdate> result = new ArrayList<ReportedUpdate>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_NOT_REPORTED);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ReportedUpdate item = new ReportedUpdate();

                PullRequestDao pullRequestDao = new PullRequestDaoImpl(this.getDataSource());
                item.setPullRequest(pullRequestDao.findById(rs.getString(ReportedUpdate.PULLREQUEST_COLUMN)));

                item.setReported(rs.getBoolean(ReportedUpdate.ISREPORTED_COLUMN));
                item.setCommitID(rs.getString(ReportedUpdate.COMMIT_COLUMN));

                result.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    @Override
    public void updateAllClose() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ALL_REPORTED);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ReportedUpdate findByPullRequestId(Long id) {
        ReportedUpdate item = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_PULLREQUEST_ID);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                item = new ReportedUpdate();

                PullRequestDao pullRequestDao = new PullRequestDaoImpl(this.getDataSource());
                item.setPullRequest(pullRequestDao.findById(rs.getString(ReportedUpdate.PULLREQUEST_COLUMN)));

                item.setReported(rs.getBoolean(ReportedUpdate.ISREPORTED_COLUMN));
                item.setCommitID(rs.getString(ReportedUpdate.COMMIT_COLUMN));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

    @Override
    public ReportedUpdate findByCompositeKey(String pullRequestId, String commitId) {
        ReportedUpdate item = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_PULLREQUEST_AND_COMMIT);
            statement.setString(1, pullRequestId);
            statement.setString(2, commitId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                item = new ReportedUpdate();

                PullRequestDao pullRequestDao = new PullRequestDaoImpl(this.getDataSource());
                item.setPullRequest(pullRequestDao.findById(rs.getString(ReportedUpdate.PULLREQUEST_COLUMN)));

                item.setReported(rs.getBoolean(ReportedUpdate.ISREPORTED_COLUMN));
                item.setCommitID(rs.getString(ReportedUpdate.COMMIT_COLUMN));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return item;
    }


    public void insert(ReportedUpdate item) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);


            statement.setString(1, item.getPullRequest().getId());
            statement.setString(2, item.getCommitID());
            statement.setInt(3, (item.isReported()) ? 1 : 0 );

            statement.execute();

            //ResultSet generatedkeys = statement.executeQuery();
          /*  if (generatedkeys.next()) {
                item.setId(generatedkeys.getString(1));
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(ReportedUpdate item) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_BY_PULLREQUEST_ID);

            statement.setString(3, item.getPullRequest().getId());
            statement.setString(1, item.getCommitID());
            statement.setInt(2,item.isReported()? 1 : 0);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(ReportedUpdate item) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_PULLREQUEST_ID);
            statement.setString(1, item.getPullRequest().getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
