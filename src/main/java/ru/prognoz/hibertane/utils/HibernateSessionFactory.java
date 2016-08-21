package ru.prognoz.hibertane.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * @author:  Туров Данил
 * Дата создания: 06.08.2016
 * Реализует фабрику сессий Hibernate.
 * The Prognoz Test Project
 */
public class HibernateSessionFactory {
    private static SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Консутруктор Session Factory
     * @return фабрику сессий
     */
    protected static SessionFactory buildSessionFactory() {
        // Session Fatcory создается 1 раз на приложение!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // закгрузка конфигурации из hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // Необходимо запустить destroy в случае исключения при создании фабрики
            StandardServiceRegistryBuilder.destroy( registry );

            throw new ExceptionInInitializerError("Initial SessionFactory failed" + e);
        }
        return sessionFactory;
    }

    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    /**
     * Закрывает кэши и пулы соединений к БД
     */
    public static void shutdown() {
        getSessionFactory().close();
    }

}
