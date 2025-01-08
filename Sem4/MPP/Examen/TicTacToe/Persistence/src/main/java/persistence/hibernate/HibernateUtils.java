package persistence.hibernate;

import model.Game;
import model.Move;
import model.Player;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if ((sessionFactory == null) || (sessionFactory.isClosed()))
            sessionFactory = createNewSessionFactory();
        return sessionFactory;
    }

    private static SessionFactory createNewSessionFactory() {
        sessionFactory = new Configuration().addAnnotatedClass(Player.class).addAnnotatedClass(Move.class)
                .addAnnotatedClass(Game.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null)
            sessionFactory.close();
    }
}
