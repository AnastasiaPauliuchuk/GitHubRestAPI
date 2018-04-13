package dao.impl;

import dao.PullRequestDao;
import db.DataSource;
import model.PullRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PullRequestDaoImpl implements PullRequestDao {

    private DataSource dataSource;

    public PullRequestDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<PullRequest> findAll() {
        List<PullRequest> result = new ArrayList<PullRequest>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                PullRequest item = new PullRequest();
                item.setId(rs.getString(PullRequest.ID_COLUMN));
                item.setNumber(rs.getInt(PullRequest.NUMBER_COLUMN));
                item.setRepoURL(rs.getString(PullRequest.REPO_COLUMN));
                item.setBaseRefName(rs.getString(PullRequest.BASE_COLUMN));
                item.setHeadRefName(rs.getString(PullRequest.HEAD_COLUMN));
                item.setAuthorLogin(rs.getString(PullRequest.AUTHOR_COLUMN));
                item.setIsOpen(rs.getBoolean(PullRequest.IS_OPEN_COLUMN));
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

    public PullRequest findById(String id) {
        PullRequest item = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                item = new PullRequest();
                item.setId(rs.getString(PullRequest.ID_COLUMN));
                item.setNumber(rs.getInt(PullRequest.NUMBER_COLUMN));
                item.setRepoURL(rs.getString(PullRequest.REPO_COLUMN));
                item.setBaseRefName(rs.getString(PullRequest.BASE_COLUMN));
                item.setHeadRefName(rs.getString(PullRequest.HEAD_COLUMN));
                item.setAuthorLogin(rs.getString(PullRequest.AUTHOR_COLUMN));
                item.setIsOpen(rs.getBoolean(PullRequest.IS_OPEN_COLUMN));
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



    public void insert(PullRequest item) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
            statement.setString(1, item.getId());
            statement.setInt(2, item.getNumber());
            statement.setString(3,item.getRepoURL());
            statement.setString(4,item.getAuthorLogin());
            statement.setString(5,item.getBaseRefName());
            statement.setInt(7, (item.getIsOpen()) ? 1 : 0);
            statement.setString(6,item.getHeadRefName());
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

    public void update(PullRequest item) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);

            statement.setInt(1, item.getNumber());
            statement.setString(2,item.getRepoURL());
            statement.setString(3,item.getAuthorLogin());
            statement.setString(4,item.getBaseRefName());
            statement.setString(5,item.getHeadRefName());
            statement.setInt(6, (item.getIsOpen()) ? 1 : 0);
            statement.setString(7, item.getId());
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

    public void delete(PullRequest item) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setString(1, item.getId());
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

    @Override
    public List<PullRequest> findAllOpen() {
        List<PullRequest> result = new ArrayList<PullRequest>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_OPEN);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                PullRequest item = new PullRequest();
                item.setId(rs.getString(PullRequest.ID_COLUMN));
                item.setNumber(rs.getInt(PullRequest.NUMBER_COLUMN));
                item.setRepoURL(rs.getString(PullRequest.REPO_COLUMN));
                item.setBaseRefName(rs.getString(PullRequest.BASE_COLUMN));
                item.setHeadRefName(rs.getString(PullRequest.HEAD_COLUMN));
                item.setAuthorLogin(rs.getString(PullRequest.AUTHOR_COLUMN));
                item.setIsOpen(rs.getBoolean(PullRequest.IS_OPEN_COLUMN));
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

}
