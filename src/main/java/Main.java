import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import ru.prognoz.DAO.AccountsDAO;
import ru.prognoz.DAO.ClientsDAO;
import ru.prognoz.DAO.TransactionsDAO;
import ru.prognoz.entities.AccountEntity;
import ru.prognoz.entities.ClientsEntity;
import ru.prognoz.entities.TransactionsEntity;
import ru.prognoz.hibertane.utils.HibernateSessionFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by turov on 06.08.2016.
 */
public class Main {
    private static Map<Integer, String> clientList = new HashMap<>();
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();  //при загрузке бина, создается сессия с бд через Hibernate
    private TransactionsDAO transactionsDAO = new TransactionsDAO(session);  // создается объект Data Access object для доступа к сущностям из базы
    private AccountsDAO accountsDAO = new AccountsDAO(session);

    public static void main(String[] args) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();  //при загрузке бина, создается сессия с бд через Hibernate
        TransactionsDAO transactionsDAO = new TransactionsDAO(session);  // создается объект Data Access object для доступа к сущностям из базы


        List<TransactionsEntity> transactionsEntities = transactionsDAO.readByClientId(1);

        for(TransactionsEntity transactionsEntity: transactionsEntities){
            System.out.println(transactionsEntity.getTransactionId() + " " + transactionsEntity.getDecription());
        }


        System.out.println("Session Closed");
        HibernateSessionFactory.shutdown();


    }
}