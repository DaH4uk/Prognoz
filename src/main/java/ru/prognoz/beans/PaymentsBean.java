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
 * Created by turov on 17.08.2016.
 */

@ManagedBean(name = "paymentsView")  //имя бина, используется для ссылки из xhtml файла на этот бин
@ViewScoped
public class PaymentsBean implements Serializable {
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();  //при загрузке бина, создается сессия с бд через Hibernate
    private TransactionsDAO transactionsDAO = new TransactionsDAO(session);  // создается объект Data Access object для доступа к сущностям из базы
    private AccountsDAO accountsDAO = new AccountsDAO(session);
    private List<AccountEntity> accountsList;
    private List<SelectItem> accountIdSelfList;
    private int id;
    private int writeOffAccountId;
    private double sum;
    private String service;
    private int accountNumber;


    @PostConstruct
    public void init() {
        this.id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");

        this.accountsList = accountsDAO.readByAccountID(id);
        accountIdSelfList = new ArrayList<>();
        for (AccountEntity accountEntity : accountsList) {

            accountIdSelfList.add(new SelectItem(accountEntity.getId(), accountEntity.getId() + ""));
        }
    }

    public void pay() {
        AccountEntity accountEntity = accountsDAO.read(writeOffAccountId);
        if (sum > accountEntity.getSum() && sum > 0) {
            Transaction transaction = session.beginTransaction();   //Начало транзакции


            accountEntity.setSum(accountEntity.getSum() - sum);

            accountsDAO.save(accountEntity); //сохранение клиента

            TransactionsEntity transactionsEntity = new TransactionsEntity();

            transactionsEntity.setWriteoffAccountId(writeOffAccountId);
            transactionsEntity.setSum(sum);
            transactionsEntity.setTransactionTime(new Timestamp(new Date().getTime()));
            transactionsEntity.setDecription("Оплата услуги " + service +". № счета: " + accountNumber);

            transactionsDAO.save(transactionsEntity);

            transaction.commit(); // коммит

            RequestContext.getCurrentInstance().closeDialog(null); //закрывает диалоговое окно

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