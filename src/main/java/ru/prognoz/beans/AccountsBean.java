package ru.prognoz.beans;

import org.hibernate.Session;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import ru.prognoz.DAO.AccountsDAO;
import ru.prognoz.DAO.ClientsDAO;
import ru.prognoz.entities.AccountEntity;
import ru.prognoz.hibertane.utils.HibernateSessionFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by turov on 16.08.2016.
 */
@ManagedBean(name = "accountsView")
@ViewScoped
public class AccountsBean implements Serializable {
    private List<AccountEntity> accounts;
    private AccountEntity selectedAccount;
    private String clientName;
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();
    private AccountsDAO accountsDAO = new AccountsDAO(session);
    private ClientsDAO clientsDAO = new ClientsDAO(session);
    private int id;


    @PostConstruct
    public void init() {

        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        id = Integer.parseInt(params.get("id"));

        clientName = clientsDAO.read(id).getName();
        accounts = accountsDAO.readByAccountID(id);

    }

    public void onRowSelect(SelectEvent event) {
        int id = ((AccountEntity) event.getObject()).getId();

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("transactions"); //сам редирект
        } catch (IOException e) {
            //TODO: реализовать вывод в лог
            e.printStackTrace();
        }
    }

    //метод вызывается при нажатии на кнопку "Добавить клиента"
    public void addAccount() {
        //устанавливаются параметры диалогового окна
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("width", 500);
        options.put("height", 300);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("id",id);
        //открывается диалоговое окно, во внутрь которого вставляется "addAccount.xhtml"
        RequestContext.getCurrentInstance().openDialog("addAccount", options, null);
    }

    public void openTransfers() {
        //устанавливаются параметры диалогового окна
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 500);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("id",id);
        //открывается диалоговое окно, во внутрь которого вставляется "transfers.xhtml"

        RequestContext.getCurrentInstance().openDialog("transfers", options, null);

    }

    public void openRecharge(){
        //устанавливаются параметры диалогового окна
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 500);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("id",id);
        //открывается диалоговое окно, во внутрь которого вставляется "transfers.xhtml"

        RequestContext.getCurrentInstance().openDialog("recharge", options, null);
    }


    public void openPayments(){
        //устанавливаются параметры диалогового окна
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 500);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("id",id);
        //открывается диалоговое окно, во внутрь которого вставляется "transfers.xhtml"

        RequestContext.getCurrentInstance().openDialog("payments", options, null);
    }





    public List<AccountEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountEntity> accounts) {
        this.accounts = accounts;
    }

    public AccountEntity getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(AccountEntity selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

}
