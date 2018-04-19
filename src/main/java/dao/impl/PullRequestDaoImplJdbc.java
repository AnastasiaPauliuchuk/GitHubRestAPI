package dao.impl;

import dao.PullRequestDao;
import db.DataSource;
import model.PullRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PullRequestDaoImplJdbc implements PullRequestDao {

    private DataSource dataSource;

    public PullRequestDaoImplJdbc(DataSource dataSource) {
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
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL)) {
               try( ResultSet rs = statement.executeQuery()) {
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
               }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public PullRequest findById(String id) {
        PullRequest item = null;
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
                statement.setString(1, id);
                try(ResultSet rs = statement.executeQuery()) {
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
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }



    public void insert(PullRequest item) {
        try (Connection  connection = dataSource.getConnection()){

            try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
                statement.setString(1, item.getId());
                statement.setInt(2, item.getNumber());
                statement.setString(3, item.getRepoURL());
                statement.setString(4, item.getAuthorLogin());
                statement.setString(5, item.getBaseRefName());
                statement.setInt(7, (item.getIsOpen()) ? 1 : 0);
                statement.setString(6, item.getHeadRefName());
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(PullRequest item) {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {

                statement.setInt(1, item.getNumber());
                statement.setString(2, item.getRepoURL());
                statement.setString(3, item.getAuthorLogin());
                statement.setString(4, item.getBaseRefName());
                statement.setString(5, item.getHeadRefName());
                statement.setInt(6, (item.getIsOpen()) ? 1 : 0);
                statement.setString(7, item.getId());
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(PullRequest item) {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
                statement.setString(1, item.getId());
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PullRequest> findAllOpen() {
        List<PullRequest> result = new ArrayList<>();
        try (Connection  connection = dataSource.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_OPEN)) {
               try( ResultSet rs = statement.executeQuery()){
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
               }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
