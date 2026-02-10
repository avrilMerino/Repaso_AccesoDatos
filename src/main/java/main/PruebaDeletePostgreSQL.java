package main;


import entidades.Empleado;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class PruebaDeletePostgreSQL {

    public static void main(String[] args) {
        // 1º Elegimos el fichero de configuración de PostgreSQL
        String cfg = "hibernate-postgres.cfg.xml";

        // 2º Obtener la SessionFactory
        SessionFactory sf = HibernateUtil.getSessionFactory(cfg);

        // 3º Abrir una sesión contra la base de datos
        Session session = sf.openSession();

        // 4º Iniciar una transacción (obligatoria para DELETE)
        Transaction tx = session.beginTransaction();

        try {
            // 5º ID del empleado que queremos borrar
            int idBorrar = 1;

            // 6º Buscar el empleado por su clave primaria
            Empleado e = session.find(Empleado.class, idBorrar);

            if (e != null) {
                // 7º Eliminar el objeto persistente
                session.remove(e);

                // 8º Confirmar la transacción (se ejecuta el DELETE)
                tx.commit();
                System.out.println("DELETE OK (PostgreSQL)");
            } else {
                // 9º Si no existe el empleado, no se borra nada
                System.out.println("Empleado no encontrado (PostgreSQL)");
                tx.rollback();
            }

        } catch (Exception ex) {
            // 10º En caso de error, deshacer la transacción
            if (tx != null) tx.rollback();
            ex.printStackTrace();

        } finally {
            // 11º Cerrar la sesión
            session.close();

            // 12º Cerrar la SessionFactory
            HibernateUtil.closeSessionFactory();
        }
    }
}