package ru.prognoz.beans;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import ru.prognoz.DAO.ClientsDAO;
import ru.prognoz.entities.ClientsEntity;
import ru.prognoz.hibertane.utils.HibernateSessionFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created by turov on 17.08.2016.
 */
@ManagedBean(name = "addClientView")  //имя бина, используется для ссылки из xhtml файла на этот бин
@ViewScoped
public class AddClientBean implements Serializable {
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();  //при загрузке бина, создается сессия с бд через Hibernate
    private ClientsDAO clientsDAO = new ClientsDAO(session);  // создается объект Data Access object для доступа к сущностям из базы
    private String name;  //фио для формы создания нового клиента
    private String address;  //адрес для формы создания нового клиента
    private int age;    //возраст для формы создания нового клиента

    //при инициализации бина читаем список всех клиентов и кладем его в список пользователей
    @PostConstruct
    public void init() {

    }

    //метод вызывается при нажатии на кнопку "Сохранить" в диалоговом окне
    public void saveClient(Object o) {

        Transaction transaction = session.beginTransaction();   //Начало транзакции

        ClientsEntity clientsEntity = new ClientsEntity();    //Создание сущности пользователя
        clientsEntity.setName(name);
        clientsEntity.setAddress(address);

        if (age != 0)
            clientsEntity.setAge(age); // если не было изменено поле возраст

        clientsDAO.save(clientsEntity); //сохранение клиента
        transaction.commit(); // коммит

        RequestContext.getCurrentInstance().closeDialog(o); //закрывает диалоговое окно
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("form");
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
