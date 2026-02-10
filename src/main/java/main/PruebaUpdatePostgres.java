package main;
import entidades.Empleado;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.math.BigDecimal;

public class PruebaUpdatePostgres {

    public static void main(String[] args) {

        // 1) Elegimos el cfg de POSTGRES
        String cfg = "hibernate-postgres.cfg.xml";

        // 2) Obtener SessionFactory
        SessionFactory sf = HibernateUtil.getSessionFactory(cfg);

        // 3) Abrir sesión
        Session session = sf.openSession();

        // 4) Iniciar transacción
        Transaction tx = session.beginTransaction();

        try {
            // 5) Buscar empleado por ID
            int idBuscar = 1;
            Empleado e = session.find(Empleado.class, idBuscar);

            if (e != null) {
                // 6) Modificar campos
                e.setSalario(new BigDecimal("2500.00"));
                e.setOficio("Senior Comercial");

                // 7) Commit (se ejecuta el UPDATE)
                tx.commit();
                System.out.println("UPDATE OK (POSTGRES)");
            } else {
                System.out.println("Empleado NO encontrado (POSTGRES)");
                tx.rollback();
            }

        } catch (Exception ex) {
            // 8) Rollback si hay error
            if (tx != null) tx.rollback();
            ex.printStackTrace();

        } finally {
            // 9) Cerrar sesión
            session.close();

            // 10) Cerrar SessionFactory
            HibernateUtil.closeSessionFactory();
        }
    }
}