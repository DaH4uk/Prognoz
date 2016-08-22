package ru.prognoz.DAO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.prognoz.entities.AccountEntity;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Автор:  Туров Данил
 * Дата создания: 16.08.2016
 * Реализует data access object для AccountEntity,
 * скрывает реализацию методов для доступа к сущностям счетов клиентов.
 * The Prognoz Test Project
 */
@ManagedBean(name = "UsersDAO")
@ApplicationScoped
public class AccountsDAO {
    /**
     * Текущая сессия
     */
    private Session session;

    /**
    * Конструктор DAO
    * @param session текущая сессия
    */
    public AccountsDAO(Session session) {
        this.session = session;
    }
    
    /**
    * Реализует сохранение объекта в БД
    * @param dataSet для сохранения его в бд
    */
    public void save(AccountEntity dataSet) {
        session.save(dataSet);
    }

    /**
    * Реализует чтение сущности счета из БД
    * @param id счета
    * @return объект счета  
    */
    public AccountEntity read(int id) {
        return (AccountEntity) session.load(AccountEntity.class, id);
    }

    /**
    * Чтение списка счетов, принадлежащих клиенту
    * @param id клиента
    * @return List с обектами счетов
    */
    public List<AccountEntity> readByClientId(int id) {
        Criteria criteria = session.createCriteria(AccountEntity.class);

        return (List<AccountEntity>) criteria.add(Restrictions.eq("client", id)).list();
    }

    /**
    * Чтение списка всех счетов
    * @return список объектов счетов
    */
    public List<AccountEntity> readAll() {
        Criteria criteria = session.createCriteria(AccountEntity.class);
        return (List<AccountEntity>) criteria.list();
    }
}
