package main;

import entidades.Empleado;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.math.BigDecimal;

public class PruebaUpdateMySQL {

    public static void main(String[] args) {

        String cfg = "hibernate.cfg.xml"; // MySQL
        SessionFactory sf = HibernateUtil.getSessionFactory(cfg);
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {
            int idBuscar = 1;

            Empleado e = session.find(Empleado.class, idBuscar);

            if (e != null) {
                e.setSalario(new BigDecimal("2500.00"));
                e.setOficio("Senior Comercial");

                tx.commit();
                System.out.println("UPDATE OK (MySQL)");
            } else {
                System.out.println("Empleado no encontrado (MySQL)");
                tx.rollback();
            }

        } catch (Exception ex) {
            tx.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
            HibernateUtil.closeSessionFactory();
        }
    }
}
