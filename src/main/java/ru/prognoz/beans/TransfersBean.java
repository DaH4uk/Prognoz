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
 * @author:  Туров Данил
 * Дата создания: 17.08.2016
 * Реализует методы для управления формой переводов.
 * The Prognoz Test Project
 */
@ManagedBean(name = "transfersBean")
@ViewScoped
public class TransfersBean implements Serializable {
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();  //при загрузке бина, создается сессия с бд через Hibernate
    private TransactionsDAO transactionsDAO = new TransactionsDAO(session);  // создается объект Data Access object для доступа к сущностям транзакций из базы
    private AccountsDAO accountsDAO = new AccountsDAO(session);// создается объект Data Access object для доступа к сущностям счетов из базы
    private List<AccountEntity> accountsList;   //Список счетов
    private List<SelectItem> accountIdList; //Список счетов TODO: WTF?
    private List<SelectItem> accountIdSelfList; //Список счетов для формы selectOne
    private int id; //id клиента
    private int writeOffAccountId;  //счет списания
    private int depositingAccountId;    //счет пополнения
    private double sumTransfer; //Сумма пополнения
    private Object service; //Услуга TODO:WTF????

    /**
     * При инициализации формы из параметров сессии берем id
     * Читаем список всех счетов
     * Кладем его для формы selectOne в список всех счетов
     * Аналогично для списка счетов выбранного клиента
     */
    @PostConstruct
    public void init() {
        this.id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");

        this.accountsList = accountsDAO.readAll();
        accountIdList = new ArrayList<>();
        for (AccountEntity accountEntity : accountsList) {
            accountIdList.add(new SelectItem(accountEntity.getId(), accountEntity.getId() + ""));
        }

        this.accountsList = accountsDAO.readByClientId(id);
        accountIdSelfList = new ArrayList<>();
        for (AccountEntity accountEntity : accountsList) {

            accountIdSelfList.add(new SelectItem(accountEntity.getId(), accountEntity.getId() + ""));
        }
    }

    /**
     * Вызывается при нажатии на кнопку "Перевести"
     * Изменяет суммы на счете списания и пополнения, 
     * так же создает запись о новой транзакции в бд.
     */
    public void transfer() {
        Transaction transaction = session.beginTransaction();   //Начало транзакции
        try {
            AccountEntity accountEntityWriteOff = accountsDAO.read(writeOffAccountId);  //Читаем информацию о счете списания

            /**Проверяем, чтобы был выбран не один и тот же аккаунт,
             * чтобы имеющаяся на счету сумма была больше суммы перевода,
             * чтобы сумма перевода была положительная.
             */
            if (writeOffAccountId != depositingAccountId && accountEntityWriteOff.getSum() > sumTransfer && sumTransfer > 0) {


                accountEntityWriteOff.setSum(accountEntityWriteOff.getSum() - sumTransfer); //из текущей суммы на счету вычитаем сумму перевода
                accountsDAO.save(accountEntityWriteOff); //сохранение состояния счета списания


                AccountEntity accountEntityDepositing = accountsDAO.read(depositingAccountId);  //читаем информацию о счете пополнения
                accountEntityDepositing.setSum(accountEntityDepositing.getSum() + sumTransfer); //к текущей сумме на счету прибавляем сумму перевода
                accountsDAO.save(accountEntityDepositing); //сохранение состояния счета пополнения

                TransactionsEntity transactionsEntity = new TransactionsEntity();   //Создание нового объекта транзакции

                transactionsEntity.setWriteoffAccountId(writeOffAccountId); //Пишем в поле счет списания
                transactionsEntity.setRefillAccountId(depositingAccountId); //счет пополнения
                transactionsEntity.setSum(sumTransfer); //сумма перевода
                transactionsEntity.setTransactionTime(new Timestamp(new Date().getTime())); //текущая дата/время
                transactionsEntity.setDecription("Перевод другому клиенту");    //добавляем описание

                transactionsDAO.save(transactionsEntity);   //сохраняем объект транзакции в бд

                transaction.commit(); // коммит

                RequestContext.getCurrentInstance().closeDialog(null); //закрывает диалоговое окно
            }
        } catch (Exception e){
            transaction.rollback(); //rollback в случае исключения
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
