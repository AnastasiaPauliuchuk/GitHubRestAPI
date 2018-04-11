package dao;

import model.PullRequest;

import java.util.List;

public interface PullRequestDao {

        public static final String SQL_FIND_ALL = "select * from " + PullRequest.TABLE_NAME;
        public static final String SQL_FIND_BY_ID = SQL_FIND_ALL + " where " + PullRequest.ID_COLUMN + " = ?";
        public static final String SQL_INSERT = "insert into " + PullRequest.TABLE_NAME + " ("
                + PullRequest.ID_COLUMN + ", "
                + PullRequest.NUMBER_COLUMN + ", "
                + PullRequest.REPO_COLUMN + ", "
                + PullRequest.AUTHOR_COLUMN+ ", "
                + PullRequest.BASE_COLUMN + ", "
                + PullRequest.HEAD_COLUMN + ", "
        + PullRequest.IS_OPEN_COLUMN + ") values (?, ?, ?, ?, ?, ?,?)";

        public static final String SQL_UPDATE = "update " + PullRequest.TABLE_NAME + " set "
                + PullRequest.NUMBER_COLUMN  + " = ?, "
                + PullRequest.REPO_COLUMN + "= ?, "
                + PullRequest.AUTHOR_COLUMN+ "= ?, "
                + PullRequest.BASE_COLUMN + "= ?, "
                + PullRequest.HEAD_COLUMN + "= ?"
                + PullRequest.IS_OPEN_COLUMN + "= ?" +
                " where " + PullRequest.ID_COLUMN + " = ?";

        public static final String SQL_DELETE = "delete from " + PullRequest.TABLE_NAME + " where " + PullRequest.ID_COLUMN + " = ?";

        public List<PullRequest> findAll();
        public PullRequest findById(String id);
        public void insert(PullRequest item);
        public void update(PullRequest item);
        public void delete(PullRequest item);


}
