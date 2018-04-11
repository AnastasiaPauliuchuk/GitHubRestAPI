package dao;


import model.ReportedUpdate;

import java.util.List;

public interface ReportedUpdateDao {

    public static final String SQL_FIND_ALL = "select * from " + ReportedUpdate.TABLE_NAME;

    public static final String SQL_FIND_BY_PULLREQUEST_ID = SQL_FIND_ALL + " where " + ReportedUpdate.PULLREQUEST_COLUMN + " = ?";

    public static final String SQL_INSERT = "insert into " + ReportedUpdate.TABLE_NAME + " ("

            + ReportedUpdate.PULLREQUEST_COLUMN + ", "
            + ReportedUpdate.COMMIT_COLUMN + ", "
            + ReportedUpdate.ISREPORTED_COLUMN + ", "
            + ") values (?, ?, ?)";

    public static final String SQL_UPDATE_BY_PULLREQUEST_ID= "update " + ReportedUpdate.TABLE_NAME + " set "
            + ReportedUpdate.COMMIT_COLUMN + "= ?, "
            + ReportedUpdate.ISREPORTED_COLUMN +  "= ?"
            + " where " + ReportedUpdate.PULLREQUEST_COLUMN + " = ?";

    public static final String SQL_DELETE_BY_PULLREQUEST_ID = "delete from " + ReportedUpdate.TABLE_NAME + " where " + ReportedUpdate.PULLREQUEST_COLUMN + " = ?";

    public List<ReportedUpdate> findAll();
    public ReportedUpdate findByPullRequestId(Long id);
    public void insert(ReportedUpdate item);
    public void update(ReportedUpdate item);
    public void delete(ReportedUpdate item);

}
