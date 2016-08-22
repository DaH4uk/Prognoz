package ru.prognoz.DAO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import ru.prognoz.entities.AccountEntity;
import ru.prognoz.entities.TransactionsEntity;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:  Туров Данил
 * Дата создания: 20.08.2016
 * Реализует data access object для TransactionsEntity,
 * скрывает реализацию методов для доступа к объектам транзакций.
 * The Prognoz Test Project
 */
@ManagedBean(name = "TransactionsDAO")
@ApplicationScoped
public class TransactionsDAO {
    /**
     * Текущая сессия
     */
    private Session session;

    /**
    * Конструктор DAO
    * @param session текущая сессия
    */
    public TransactionsDAO(Session session) {
        this.session = session;
    }

    /**
    * Реализует сохранение объекта в БД
    * @param dataSet для сохранения его в бд
     */
    public void save(TransactionsEntity dataSet) {
        session.save(dataSet);
    }

    /**
    * Реализует чтение сущности транзакции из БД
    * @param id транзакции
    * @return объект транзакции 
     */
    public TransactionsEntity read(int id) {
        return (TransactionsEntity) session.load(TransactionsEntity.class, id);
    }

    /**
     * Чтение списка транзакций по id клиента
     * @param id клиента
     * @return список транзакций
     */
     //TODO: переименовать корректно класс
    public List<TransactionsEntity> readByClientId(int id) {
        Criteria criteria = session.createCriteria(TransactionsEntity.class);

        AccountsDAO accountsDAO = new AccountsDAO(session);
        List<AccountEntity> accountEntities = accountsDAO.readByClientId(id);
        List<Integer> accountIdList = new ArrayList<>();

        for (AccountEntity i : accountEntities){
            accountIdList.add(i.getId());
        }

        return (List<TransactionsEntity>) criteria.add(Restrictions.or(Restrictions.in("writeoffAccountId", accountIdList.toArray()),
                Restrictions.in("refillAccountId", accountIdList.toArray()))).list();
    }

    /**
     * Чтение списка всех транзакций
     * @return список всех транзакций
     */
    public List<TransactionsEntity> readAll() {
        Criteria criteria = session.createCriteria(TransactionsEntity.class);
        return (List<TransactionsEntity>) criteria.list();
    }

    /**
     * Чтение транзакции по временному промежутку
     * @param dateFrom  дата начала промежутка, dateTo дата конца промежутка
     * @return список транзакций 
     */
    //TODO: реализовать корректно этот метод
    public List<TransactionsEntity> readByDate(Date dateFrom, Date dateTo){
        Criteria criteria = session.createCriteria(TransactionsEntity.class);

        Criterion range = Restrictions.between("transactionTime", dateFrom, dateTo);

        return (List<TransactionsEntity>) criteria.add(range).list();
    }
}
