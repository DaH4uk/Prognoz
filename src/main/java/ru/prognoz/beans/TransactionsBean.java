package ru.prognoz.beans;

import org.hibernate.Session;
import ru.prognoz.DAO.AccountsDAO;
import ru.prognoz.DAO.ClientsDAO;
import ru.prognoz.DAO.TransactionsDAO;
import ru.prognoz.entities.AccountEntity;
import ru.prognoz.entities.ClientsEntity;
import ru.prognoz.entities.TransactionsEntity;
import ru.prognoz.hibertane.utils.HibernateSessionFactory;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:  Туров Данил
 * Дата создания: 21.08.2016
 * Реализует методы для управления формой с транзакциями.
 * The Prognoz Test Project
 */
@ManagedBean(value = "transactionsView")
@ViewScoped
public class TransactionsBean implements Serializable {
    private List<TransactionsEntity> transactions;  //Список транзакций
    private List<ClientsEntity> clientsEntities;    //Список объектов с клиентами
    private List<SelectItem> clientList;    //список клиентов для формы selectOne
    private AccountEntity selectedTransaction;  //выбранная транзакция //TODO:WTF????
    private String clientName;  //Имя клиента
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();    //Получаем сессию
    private ClientsDAO clientsDAO = new ClientsDAO(session);    //Получаем DAO с текущей сессией для доступа к списку клиентов
    private TransactionsDAO transactionsDAO = new TransactionsDAO(session); //Получаем DAO с текущей сессией для доступа к списку транзакций
    private int id; //id выбранного клиента
    private Date dateFrom;  //дата начала периода для поиска
    private Date dateTo;    //дата конца периода для поиска

    /**
     * При инициализации формы из url запроса берем id клиента, по которому будем выводить список транзакций
     * Если поле пусто, выводим информацию по всем
     * Получаем список всех клиентов, для формы поиска
     * //TODO: Добавить чтение параметров из запроса dateFrom и dateTo.
     */
    @PostConstruct
    public void init() {

        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        if (params.get("id") != null){
            id = Integer.parseInt(params.get("id"));
            transactions = transactionsDAO.readByClientId(id);
        } else {
            transactions = new ArrayList<>();
        }
        if (params.get("dateFrom") != null && params.get("dateTo") != null){
            dateFrom = new Date(Long.parseLong(params.get("dateFrom")));
            dateTo = new Date(Long.parseLong(params.get("dateTo")));
            List<TransactionsEntity> transactionsByDate = transactionsDAO.readByDate(dateFrom,dateTo);
            for (TransactionsEntity entity : transactionsByDate){
                transactions.add(entity);
            }

        }

        clientsEntities = clientsDAO.readAll();
        clientList = new ArrayList<>();

        if (id == 0 && dateFrom ==null && dateTo == null){
            transactions = transactionsDAO.readAll();
        }

        for (ClientsEntity client: clientsEntities) {
            clientList.add(new SelectItem(client.getId(), client.getName()));
        }

    }

    //TODO: Реализовать метод поиска.
    public void search(){
        if (dateFrom != null && dateTo != null && id==0){
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("transactions?dateFrom=" +dateFrom.getTime() +"&dateTo=" + dateTo.getTime()); //сам редирект
            } catch (IOException e) {
                //TODO: реализовать вывод в лог
                e.printStackTrace();
            }
        } else if(id != 0 && dateFrom == null && dateTo == null){
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("transactions?id=" + id); //сам редирект
            } catch (IOException e) {
                //TODO: реализовать вывод в лог
                e.printStackTrace();
            }
        } else if (dateFrom != null && dateTo != null && id!=0){
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("transactions?id=" + id+"&dateFrom=" +dateFrom.getTime() +"&dateTo=" + dateTo.getTime()); //сам редирект
            } catch (IOException e) {
                //TODO: реализовать вывод в лог
                e.printStackTrace();
            }
        }

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
