package ru.prognoz.DAO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.prognoz.entities.AccountEntity;
import ru.prognoz.entities.TransactionsEntity;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.Date;
import java.util.List;

/**
 * Created by turov on 20.08.2016.
 */
@ManagedBean(name = "TransactionsDAO")
@ApplicationScoped
public class TransactionsDAO {
    private Session session;

    public TransactionsDAO(Session session) {
        this.session = session;
    }

    public void save(TransactionsEntity dataSet) {
        session.save(dataSet);
    }

    public TransactionsEntity read(int id) {
        return (TransactionsEntity) session.load(TransactionsEntity.class, id);
    }

    public List<TransactionsEntity> readByTransactionID(int id) {
        Criteria criteria = session.createCriteria(TransactionsEntity.class);

        return (List<TransactionsEntity>) criteria.add(Restrictions.eq("transaction_id", id)).list();
    }


    public List<TransactionsEntity> readAll() {
        Criteria criteria = session.createCriteria(TransactionsEntity.class);
        return (List<TransactionsEntity>) criteria.list();
    }

    public List<TransactionsEntity> readByDate(Date dateFrom, Date dateTo){
        Criteria criteria = session.createCriteria(TransactionsEntity.class);
        return (List<TransactionsEntity>) criteria.add(Restrictions.eq("TRANSACTION_TIME", dateFrom))
                .add(Restrictions.eq("TRANSACTION_TIME",dateTo)).list();
    }
}
