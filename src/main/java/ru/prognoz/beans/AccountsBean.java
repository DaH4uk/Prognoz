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
 * @author:  Туров Данил
 * Дата создания: 16.08.2016
 * Реализует методы для управления accounts.xhtml.
 * The Prognoz Test Project
 */
@ManagedBean(name = "accountsView")
@ViewScoped
public class AccountsBean implements Serializable {
    private List<AccountEntity> accounts;   //Список счетов клиента
    private AccountEntity selectedAccount;  //Выбранный счет
    private String clientName;  //Имя выбранного клиента
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();    //Сессия Hibernate создается при создании 
    private AccountsDAO accountsDAO = new AccountsDAO(session); //Объект DAO
    private ClientsDAO clientsDAO = new ClientsDAO(session);    //Объект DAO
    private int id; //id текущего клиента

    /**
     * Инициализация accounts.xhtml
     * Берет id из списка параметров в url, 
     * кладет его в переменную id.
     * Читает имя пользователя по id.
     * Получает список всех счетов выбранного пользователя.
     */
    @PostConstruct
    public void init() {

        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        id = Integer.parseInt(params.get("id"));

        clientName = clientsDAO.read(id).getName();
        accounts = accountsDAO.readByAccountID(id);

    }

    /**
     * По клику в строке таблицы, перенаправляет на url: /transactions
     */
    public void onRowSelect(SelectEvent event) {
        int id = ((AccountEntity) event.getObject()).getId();

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("transactions"); //сам редирект
        } catch (IOException e) {
            //TODO: реализовать вывод в лог
            e.printStackTrace();
        }
    }

    /**
     * Метод вызывается при нажатии на кнопку "Добавить счет"
     */
    public void addAccount() {
        //устанавливаются параметры диалогового окна
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("width", 500);
        options.put("height", 300);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");
        
        //Кладет в сессию параметр id
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("id",id);
        //открывается диалоговое окно, во внутрь которого вставляется "addAccount.xhtml"
        RequestContext.getCurrentInstance().openDialog("addAccount", options, null);
    }

    /**
     * Вызывается при нажатии на кнопку "Перевести"
     */
    public void openTransfers() {
        //устанавливаются параметры диалогового окна
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 500);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");

        //Кладет в сессию параметр id
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("id",id);
        
        //открывается диалоговое окно, во внутрь которого вставляется "transfers.xhtml"
        RequestContext.getCurrentInstance().openDialog("transfers", options, null);

    }

    /**
     * Вызывается при нажатии на кнопку "Пополнить"
     */
    public void openRecharge(){
        //устанавливаются параметры диалогового окна
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 500);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");

        //Кладет в сессию параметр id
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("id",id);
        
        //открывается диалоговое окно, во внутрь которого вставляется "transfers.xhtml"
        RequestContext.getCurrentInstance().openDialog("recharge", options, null);
    }

    /**
     * Вызывается при нажатии на кнопку "Платежи"
     */
    public void openPayments(){
        //устанавливаются параметры диалогового окна
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 500);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");

        //Кладет в сессию параметр id
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
