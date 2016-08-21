package ru.prognoz.DAO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.prognoz.entities.ClientsEntity;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by turov on 06.08.2016.
 */

@ManagedBean(name = "UsersDAO")
@ApplicationScoped
public class ClientsDAO {
    private Session session;

    public ClientsDAO(Session session) {
        this.session = session;
    }

    public void save(ClientsEntity dataSet) {
        session.save(dataSet);
    }

    public ClientsEntity read(int id) {
        return (ClientsEntity) session.load(ClientsEntity.class, id);
    }

    public ClientsEntity readByName(String name) {
        Criteria criteria = session.createCriteria(ClientsEntity.class);
        return (ClientsEntity) criteria.add(Restrictions.eq("name", name)).uniqueResult();
    }


    public List<ClientsEntity> readAll() {
        Criteria criteria = session.createCriteria(ClientsEntity.class);
        return (List<ClientsEntity>) criteria.list();
    }

}
