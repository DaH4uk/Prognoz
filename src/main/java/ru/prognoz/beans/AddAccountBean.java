package ru.prognoz.beans;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import ru.prognoz.DAO.AccountsDAO;
import ru.prognoz.DAO.ClientsDAO;
import ru.prognoz.entities.AccountEntity;
import ru.prognoz.entities.ClientsEntity;
import ru.prognoz.hibertane.utils.HibernateSessionFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;


/**
 * Created by turov on 16.08.2016.
 */
@ManagedBean(name = "addAccountView")
@ViewScoped
public class AddAccountBean implements Serializable {
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();
    private AccountsDAO accountsDAO = new AccountsDAO(session);
    private int id;
    private double sum;

    @PostConstruct
    public void init() {
        this.id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
    }

    //метод вызывается при нажатии на кнопку "Сохранить" в диалоговом окне
    public void saveAccount(Object o) {

        Transaction transaction = session.beginTransaction();   //Начало транкзакции

        AccountEntity accountEntity = new AccountEntity();    //Создание сущности пользователя
        accountEntity.setClient(id);
        accountEntity.setSum(sum);


        accountsDAO.save(accountEntity); //сохранение клиента
        transaction.commit(); // коммит

        RequestContext.getCurrentInstance().closeDialog(o); //закрывает диалоговое окно
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("form");
    }


    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
