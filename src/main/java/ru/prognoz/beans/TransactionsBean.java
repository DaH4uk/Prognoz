package ru.prognoz.beans;

import org.hibernate.Session;
import ru.prognoz.DAO.AccountsDAO;
import ru.prognoz.DAO.ClientsDAO;
import ru.prognoz.DAO.TransactionsDAO;
import ru.prognoz.entities.AccountEntity;
import ru.prognoz.entities.ClientsEntity;
import ru.prognoz.entities.TransactionsEntity;
import ru.prognoz.hibertane.utils.HibernateSessionFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by turov on 21.08.2016.
 */
@ManagedBean(name = "transactionsView")
@ViewScoped
public class TransactionsBean implements Serializable {
    private List<TransactionsEntity> transactions;
    private List<ClientsEntity> clientsEntities;
    private List<SelectItem> clientList;
    private AccountEntity selectedTransaction;
    private String clientName;
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();
    private ClientsDAO clientsDAO = new ClientsDAO(session);
    private TransactionsDAO transactionsDAO = new TransactionsDAO(session);
    private int id;
    private Date dateFrom;
    private Date dateTo;

    @PostConstruct
    public void init() {

        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();
        if (params.get(id)!=null){
            id = Integer.parseInt(params.get("id"));

            clientName = clientsDAO.read(id).getName();
            transactions = transactionsDAO.readByTransactionID(id);
        } else {
            transactions = transactionsDAO.readAll();
        }
        clientsEntities = clientsDAO.readAll();
        clientList = new ArrayList<>();

        for (ClientsEntity client: clientsEntities) {
            System.out.println(client.getId());
            clientList.add(new SelectItem(client.getId(), client.getName()));
        }

    }

    public void search(){

    }

    public List<TransactionsEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionsEntity> transactions) {
        this.transactions = transactions;
    }

    public AccountEntity getSelectedTransaction() {
        return selectedTransaction;
    }

    public void setSelectedTransaction(AccountEntity selectedTransaction) {
        this.selectedTransaction = selectedTransaction;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Date getDateTo() {
        return dateTo;
    }


    public List<SelectItem> getClientList() {
        return clientList;
    }

    public void setClientList(List<SelectItem> clientList) {
        this.clientList = clientList;
    }
}
