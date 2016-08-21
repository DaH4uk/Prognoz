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
 * Дата создания: 16.08.2016
 * Реализует методы для управления accounts.xhtml.
 * The Prognoz Test Project
 */
@ManagedBean(name = "paymentsView")
@ViewScoped
public class PaymentsBean implements Serializable {
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();  //при загрузке бина, создается сессия с бд через Hibernate
    private TransactionsDAO transactionsDAO = new TransactionsDAO(session);  // создается объект Data Access object для доступа к сущностям транзакций из базы
    private AccountsDAO accountsDAO = new AccountsDAO(session); // создается объект Data Access object для доступа к сущностям счетов из базы
    private List<AccountEntity> accountsList;   //Список счетов
    private List<SelectItem> accountIdSelfList; //Список счетов выбранного клиента для формы selectone
    private int id; //id клиента
    private int writeOffAccountId;  //id счета, с которого будет произведено списание средств
    private double sum; //Сумма списания
    //TODO: WTF?
    private String service; //Название услуги 
    private int accountNumber;  //Номер счета

    /**
     * При инициализации формы из параметров сессии получаем id.
     * Читаем список счетов по id пользователя
     * и кладем его в список selectOne.
     */
    @PostConstruct
    public void init() {
        this.id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");

        this.accountsList = accountsDAO.readByAccountID(id);
        accountIdSelfList = new ArrayList<>();
        for (AccountEntity accountEntity : accountsList) {
            //TODO: Можно добавить сумму
            accountIdSelfList.add(new SelectItem(accountEntity.getId(), accountEntity.getId() + " "));
        }
    }

    /**
     * Вызывается при нажатии на кнопку оплатить
     * Изменяет сумму на счету клиента и записывает в бд новую транзакцию.
     */
    public void pay() {
        AccountEntity accountEntity = accountsDAO.read(writeOffAccountId);
        if (sum > accountEntity.getSum() && sum > 0) {
            try{
                Transaction transaction = session.beginTransaction();   //Начало транзакции


                accountEntity.setSum(accountEntity.getSum() - sum); //Получаем текущую сумму на счету клиента и вичитаем из него суппу платежа

                accountsDAO.save(accountEntity); //изменение счета

                TransactionsEntity transactionsEntity = new TransactionsEntity();   //Создаем объект новой транзакции

                transactionsEntity.setWriteoffAccountId(writeOffAccountId); //Пишем аккаунт, с которого будем списывать средства
                transactionsEntity.setSum(sum); //Сумму списания
                transactionsEntity.setTransactionTime(new Timestamp(new Date().getTime())); //Пишем текущую дату
                transactionsEntity.setDecription("Оплата услуги " + service +". № счета: " + accountNumber);    //Добавляем описание

                transactionsDAO.save(transactionsEntity);   //И сохраняем все это дело

                transaction.commit(); // коммит

                RequestContext.getCurrentInstance().closeDialog(null); //закрывает диалоговое окно
            } catch (Exception e){
                transaction.rollback;
                //TODO: realise.
            }


        }

    }

    public int getWriteOffAccountId() {
        return writeOffAccountId;
    }

    public void setWriteOffAccountId(int writeOffAccountId) {
        this.writeOffAccountId = writeOffAccountId;
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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
}
