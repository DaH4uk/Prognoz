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

@ManagedBean(name = "transfersBean")  //имя бина, используется для ссылки из xhtml файла на этот бин
@ViewScoped
public class TransfersBean implements Serializable {
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();  //при загрузке бина, создается сессия с бд через Hibernate
    private TransactionsDAO transactionsDAO = new TransactionsDAO(session);  // создается объект Data Access object для доступа к сущностям из базы
    private AccountsDAO accountsDAO = new AccountsDAO(session);
    private List<AccountEntity> accountsList;
    private List<SelectItem> accountIdList;
    private List<SelectItem> accountIdSelfList;
    private int id;
    private int writeOffAccountId;
    private int depositingAccountId;
    private double sumTransfer;
    private Object service;


    @PostConstruct
    public void init() {
        this.id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");

        this.accountsList = accountsDAO.readAll();
        accountIdList = new ArrayList<>();
        for (AccountEntity accountEntity : accountsList) {
            accountIdList.add(new SelectItem(accountEntity.getId(), accountEntity.getId() + ""));
        }

        this.accountsList = accountsDAO.readByAccountID(id);
        accountIdSelfList = new ArrayList<>();
        for (AccountEntity accountEntity : accountsList) {

            accountIdSelfList.add(new SelectItem(accountEntity.getId(), accountEntity.getId() + ""));
        }
    }

    public void transfer() {
        AccountEntity accountEntityWriteOff = accountsDAO.read(writeOffAccountId);

        if (writeOffAccountId != depositingAccountId && accountEntityWriteOff.getSum() > sumTransfer && sumTransfer > 0) {
            Transaction transaction = session.beginTransaction();   //Начало транзакции


            accountEntityWriteOff.setSum(accountEntityWriteOff.getSum() - sumTransfer);

            accountsDAO.save(accountEntityWriteOff); //сохранение клиента

            AccountEntity accountEntityDepositing = accountsDAO.read(depositingAccountId);


            accountEntityDepositing.setSum(accountEntityDepositing.getSum() + sumTransfer);

            accountsDAO.save(accountEntityDepositing); //сохранение клиента

            TransactionsEntity transactionsEntity = new TransactionsEntity();

            transactionsEntity.setWriteoffAccountId(writeOffAccountId);
            transactionsEntity.setRefillAccountId(depositingAccountId);
            transactionsEntity.setSum(sumTransfer);
            transactionsEntity.setTransactionTime(new Timestamp(new Date().getTime()));
            transactionsEntity.setDecription("Перевод другому клиенту");

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

    public int getDepositingAccountId() {
        return depositingAccountId;
    }

    public void setDepositingAccountId(int depositingAccountId) {
        this.depositingAccountId = depositingAccountId;
    }

    public double getSumTransfer() {
        return sumTransfer;
    }

    public void setSumTransfer(double sumTransfer) {
        this.sumTransfer = sumTransfer;
    }

    public List<SelectItem> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<SelectItem> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public List<SelectItem> getAccountIdSelfList() {
        return accountIdSelfList;
    }

    public void setAccountIdSelfList(List<SelectItem> accountIdSelfList) {
        this.accountIdSelfList = accountIdSelfList;
    }

    public Object getService() {
        return service;
    }
}
