package main;

import entidades.Departamento;
import entidades.Empleado;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PruebaCreateMySQL {
    public static void main(String[] args) {
        //1º- Elegimos  el cfg MYSQ
        String cfg = "hibernate.cfg.xml";
        //2º obtenemos session factory
        SessionFactory sf = HibernateUtil.getSessionFactory(cfg);
        // 3) Abrir Session
        Session session = sf.openSession();
        // 4) Empezar transacción (OBLIGATORIA en INSERT)
        Transaction tx = session.beginTransaction();
        try {
            // 5) Crear objetos
            Departamento d = new Departamento("Ventas", "Jaén");
            // 6) Guardar departamento primero (para que tenga id)
            session.persist(d);
            // 7) Crear empleado asociado a ese departamento
            Empleado e = new Empleado("Avril", "Ruiz",
                    "Comercial", LocalDate.now(), new BigDecimal("2100.00"),
                    d);
            // 8) Guardar empleado
            session.persist(e);
            // 9) Confirmar transacción (aquí se ejecutan los INSERT reales)
            tx.commit();
            System.out.println("INSERT OK DeptNo=" + d.getDeptNo() + " | EmpNo=" + e.getEmpNo());
        }catch (Exception ex) {
            // Si falla, deshacer
            if (tx != null) tx.rollback();
            System.out.println("ERRORSe hace ROLLBACK");
            ex.printStackTrace();
        } finally {
            // 10) Cerrar session y SessionFactory
            session.close();
            HibernateUtil.closeSessionFactory();
        }

    }
}
