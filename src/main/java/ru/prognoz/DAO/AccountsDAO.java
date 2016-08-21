package ru.prognoz.DAO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.prognoz.entities.AccountEntity;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by turov on 16.08.2016.
 */
@ManagedBean(name = "UsersDAO")
@ApplicationScoped
public class AccountsDAO {
    private Session session;

    public AccountsDAO(Session session) {
        this.session = session;
    }

    public void save(AccountEntity dataSet) {
        session.save(dataSet);
    }

    public AccountEntity read(int id) {
        return (AccountEntity) session.load(AccountEntity.class, id);
    }

    public List<AccountEntity> readByAccountID(int id) {
        Criteria criteria = session.createCriteria(AccountEntity.class);

        return (List<AccountEntity>) criteria.add(Restrictions.eq("client", id)).list();
    }


    public List<AccountEntity> readAll() {
        Criteria criteria = session.createCriteria(AccountEntity.class);
        return (List<AccountEntity>) criteria.list();
    }
}
