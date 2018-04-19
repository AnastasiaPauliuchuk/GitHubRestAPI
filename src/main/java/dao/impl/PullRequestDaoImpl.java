package dao.impl;

import dao.PullRequestDao;
import model.PullRequest;
import org.hibernate.SessionFactory;
import org.hibernate.cache.ReadWriteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
public class PullRequestDaoImpl implements PullRequestDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Transactional(readOnly = true)
    @Override
    public List<PullRequest> findAll() throws SQLException,Exception {
        return (List<PullRequest>) sessionFactory.getCurrentSession().createQuery("from pull_request").list();
    }

    @Override
    public PullRequest findById(String id) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public void insert(PullRequest item) throws  SQLException, Exception {
        sessionFactory.getCurrentSession().save(item);
    }
    @Override
    public void update(PullRequest item) {

    }

    @Override
    public void delete(PullRequest item) {

    }

    @Override
    public List<PullRequest> findAllOpen() {
        return null;
    }
}
