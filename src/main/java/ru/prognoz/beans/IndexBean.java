package ru.prognoz.beans;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import ru.prognoz.DAO.ClientsDAO;
import ru.prognoz.entities.ClientsEntity;
import ru.prognoz.hibertane.utils.HibernateSessionFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by turov on 06.08.2016.
 * Бин, реализующий функционал страницы index.html
 */


@ManagedBean(name = "indexView")  //имя бина, используется для ссылки из xhtml файла на этот бин
@ViewScoped
public class IndexBean implements Serializable {
    private List<ClientsEntity> clients;  //список пользователей
    private ClientsEntity selectedUser;  //выбранный пользователь
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();  //при загрузке бина, создается сессия с бд через Hibernate
    private ClientsDAO clientsDAO = new ClientsDAO(session);  // создается объект Data Access object для доступа к сущностям из базы



    //при инициализации бина читаем список всех клиентов и кладем его в список пользователей
    @PostConstruct
    public void init() {
        clients = clientsDAO.readAll();
    }


    //метод вызывается при клике на строке в столбце, выполняет редирект на страницу /accounts и кладет в параметры id выбранного клиента
    public void onRowSelect(SelectEvent event) {
        int id = ((ClientsEntity) event.getObject()).getId();

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("accounts?id="+id); //сам редирект
        } catch (IOException e) {
            //TODO: реализовать вывод в лог
            e.printStackTrace();
        }
    }

    //метод вызывается при нажатии на кнопку "Добавить клиента"
    public void addClient() {
        //устанавливаются параметры диалогового окна
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("width", 500);
        options.put("height", 300);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");

        //открывается диалоговое окно, во внутрь которого вставляется "addClientDialog.xhtml"
        RequestContext.getCurrentInstance().openDialog("addClientDialog", options, null);
    }


    public List<ClientsEntity> getClients() {
        return clients;
    }

    public ClientsEntity getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(ClientsEntity selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void setClients(List<ClientsEntity> clients) {
        this.clients = clients;
    }
}
