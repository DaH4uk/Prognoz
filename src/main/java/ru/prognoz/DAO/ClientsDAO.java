package ru.prognoz.DAO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.prognoz.entities.ClientsEntity;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * @author:  Туров Данил
 * Дата создания: 06.08.2016
 * Реализует data access object для ClientsDAO,
 * скрывает реализацию методов для доступа к объектам клиентов.
 * The Prognoz Test Project
 */

@ManagedBean(name = "UsersDAO")
@ApplicationScoped
public class ClientsDAO {
    /**
     * Текущая сессия
     */
    private Session session;

    /**
     * Конструктор DAO
     * @param текущая сессия
     */
    public ClientsDAO(Session session) {
        this.session = session;
    }

    /**
     * Сохраняет обект клиента
     * @param объект клиента
     */
    public void save(ClientsEntity dataSet) {
        session.save(dataSet);
    }

    /**
     * Читает объект клиента по id
     * @param id клиента
     * @return объект клиента
     */
    public ClientsEntity read(int id) {
        return (ClientsEntity) session.load(ClientsEntity.class, id);
    }

    /**
     * Читает объект клиента по ФИО
     * @param ФИО клиента
     * @return сущность клиента
     */
    public ClientsEntity readByName(String name) {
        Criteria criteria = session.createCriteria(ClientsEntity.class);
        return (ClientsEntity) criteria.add(Restrictions.eq("name", name)).uniqueResult();
    }

    /**
     * Читает список всех клиентов
     * @return список всех клиентов
     */
    public List<ClientsEntity> readAll() {
        Criteria criteria = session.createCriteria(ClientsEntity.class);
        return (List<ClientsEntity>) criteria.list();
    }

}
