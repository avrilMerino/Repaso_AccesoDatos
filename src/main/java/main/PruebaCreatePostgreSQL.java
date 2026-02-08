package main;

import entidades.Departamento;
import entidades.Empleado;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PruebaCreatePostgreSQL {
    public static void main(String[] args) {
        // 1 Elegimos el cfg de POSTGRES
        String cfg = "hibernate-postgres.cfg.xml";

        // 2 Obtener SessionFactory
        SessionFactory sf = HibernateUtil.getSessionFactory(cfg);

        // 3 Abrir sesión
        Session session = sf.openSession();

        // 4 Iniciar transacción
        Transaction tx = session.beginTransaction();

        try {
            // 5 Crear departamento
            Departamento d = new Departamento("Ventas", "Jaén");
            session.persist(d);
            // 6 Crear empleado asociado
            Empleado e = new Empleado(
                    "Avril",
                    "Ruiz",
                    "Comercial",
                    LocalDate.now(),
                    new BigDecimal("2100.00"),
                    d
            );

            session.persist(e);
            // 7 Commit
            tx.commit();
            System.out.println(
                    "INSERT OK (POSTGRES) DeptNo=" + d.getDeptNo() +
                            " | EmpNo=" + e.getEmpNo()
            );
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            System.out.println("ERROR  ROLLBACK");
            ex.printStackTrace();

        } finally {
            session.close();
            HibernateUtil.closeSessionFactory();
        }
    }
}
