package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static SessionFactory sessionFactory;
    private static Session session;

    static {
        sessionFactory = HibernateUtil.getSessionFactory();
    }
    public UserDaoHibernateImpl() {}


    @Override
    public void createUsersTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS users (\n" +
                "    id SERIAL PRIMARY KEY,\n" +
                "    name VARCHAR(255),\n" +
                "    lastName VARCHAR(255),\n" +
                "    age SMALLINT\n" +
                ");";
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createSQLQuery(createTable).executeUpdate();
            session.getTransaction().commit();
        } catch(Exception e) {
            System.out.println("Fail to create table");
            session.getTransaction().rollback();
        }
        finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        String dropTable = "DROP Table IF EXISTS users";
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createSQLQuery(dropTable).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Fail to drop table");
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            User user = new User(name,lastName,age);
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Fail to save User");
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            User user = session.find(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Fail to remove User");
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            list = session.createQuery("SELECT u from User u", User.class).getResultList();
        } catch (Exception e) {
            System.out.println("Fail to remove User");
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createSQLQuery("DELETE from users").executeUpdate();
            session.getTransaction().commit();
            System.out.println("Table is cleaned");
        } catch (Exception e) {
            System.out.println("Fail to clean table");
        }
    }
}
