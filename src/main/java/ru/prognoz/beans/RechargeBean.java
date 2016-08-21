package ru.prognoz.beans;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import ru.prognoz.DAO.AccountsDAO;
import ru.prognoz.DAO.TransactionsDAO;
import ru.prognoz.entities.AccountEntity;
import ru.prognoz.entities.TransactionsEntity;
import ru.prognoz.hibertane.utils.HibernateSessionFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:  Туров Данил
 * Дата создания: 17.08.2016
 * Реализует методы для управления формой пополнения счета.
 * The Prognoz Test Project
 */
@ManagedBean(name = "rechargeView")
@ViewScoped
public class RechargeBean implements Serializable {
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();  //при загрузке бина, создается сессия с бд через Hibernate
    private TransactionsDAO transactionsDAO = new TransactionsDAO(session);  // создается объект Data Access object для доступа к сущностям транзакций из базы
    private AccountsDAO accountsDAO = new AccountsDAO(session); // создается объект Data Access object для доступа к сущностям счетов из базы
    private List<AccountEntity> accountsList;   //Список счетов
    private List<SelectItem> accountIdSelfList; //Список счетов клиента для доступа из selectOne формы
    private int id; //id клиента
    private int depositingAccountId;    //id пополняемого счета
    private double sum; //Сумма пополнения

    /**
     * При инициализации формы берем id из параметров сессии
     * читаем список всех счетов у выбранного клиента
     * Кладем их в accountIdSelfList.
     */
    @PostConstruct
    public void init() {
        this.id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");

        this.accountsList = accountsDAO.readByAccountID(id);
        accountIdSelfList = new ArrayList<>();
        for (AccountEntity accountEntity : accountsList) {
            //TODO: Можно добавить сумму
            accountIdSelfList.add(new SelectItem(accountEntity.getId(), accountEntity.getId() + ""));
        }
    }
    
    /**
     * Вызывается при нажатии на кнопку "Пополнить" в форме.
     * Изменяет сумму на счету клиента
     * И сохраняет транзакцию в БД.
     */
    public void recharge() {

        if (sum > 0) {
            try {
                Transaction transaction = session.beginTransaction();   //Начало транзакции

                AccountEntity accountEntity = accountsDAO.read(depositingAccountId);    //читаем из бд объект счета

                accountEntity.setSum(accountEntity.getSum() + sum); //Изменяем сумму на счету

                accountsDAO.save(accountEntity); //сохранение счета

                TransactionsEntity transactionsEntity = new TransactionsEntity();   //Создаем обхект новой транзакции

                transactionsEntity.setRefillAccountId(depositingAccountId); //добавляем счет пополнения
                transactionsEntity.setSum(sum); //сумму пополнения
                transactionsEntity.setTransactionTime(new Timestamp(new Date().getTime())); //текщую дату
                transactionsEntity.setDecription("Пополнение счета");   //и описание операции

                transactionsDAO.save(transactionsEntity);   //сохраняем

                transaction.commit(); // коммит

            RequestContext.getCurrentInstance().closeDialog(null); //закрываем диалоговое окно
            } catch (Exception e) {
                transaction.rollback(); //на случай исключения
            }

        } 

    }

    public int getWriteOffAccountId() {
        return depositingAccountId;
    }

    public void setWriteOffAccountId(int writeOffAccountId) {
        this.depositingAccountId = writeOffAccountId;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public List<SelectItem> getAccountIdSelfList() {
        return accountIdSelfList;
    }

    public void setAccountIdSelfList(List<SelectItem> accountIdSelfList) {
        this.accountIdSelfList = accountIdSelfList;
    }
}
