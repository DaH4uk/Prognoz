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
 * @author:  Туров Данил
 * Дата создания: 16.08.2016
 * Реализует методы формы создания нового счета.
 * The Prognoz Test Project
 */
@ManagedBean(name = "addAccountView")
@ViewScoped
public class AddAccountBean implements Serializable {
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();
    private AccountsDAO accountsDAO = new AccountsDAO(session);
    private int id;
    private double sum;

    /**
     * Инициализация формы
     * Берет id из списка параметров в url, 
     * кладет его в переменную id.
     */
    @PostConstruct
    public void init() {
        this.id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
    }
    
    /**
     * Вызывается при нажатии на кнопку "Сохранить" в диалоговом окне
     * Сохраняет счет в бд
     */
    public void saveAccount() {
        
        try {
            Transaction transaction = session.beginTransaction();   //Начало транкзакции

            AccountEntity accountEntity = new AccountEntity();    //Создание объекта нового счета
            
            accountEntity.setClient(id);    //установка его id и начальной суммы
            accountEntity.setSum(sum);


            accountsDAO.save(accountEntity); //сохранение объекта счета в бд
            transaction.commit(); // коммит

            RequestContext.getCurrentInstance().closeDialog(null); //закрывает диалоговое окно
            //TODO: Реализовать обновление формы
        } catch (Exception e){
            transaction.rollback(); //rollback в случае возникновения исключения
            //TODO: Реализовать логгирование
        }

    }


    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
