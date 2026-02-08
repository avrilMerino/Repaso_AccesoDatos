package main;

import entidades.Empleado;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

public class PruebaReadMySQL {

    public static void main(String[] args) {

        String cfg = "hibernate.cfg.xml"; // MySQL
        SessionFactory sf = HibernateUtil.getSessionFactory(cfg);
        Session session = sf.openSession();

        try {
            int idBuscar = 1;

            Empleado e = session.get(Empleado.class, idBuscar);

            if (e != null) {
                System.out.println("ENCONTRADO (MySQL)");
                System.out.println("EmpNo: " + e.getEmpNo());
                System.out.println("Nombre: " + e.getNombre() + " " + e.getApellido());
                System.out.println("Oficio: " + e.getOficio());
                System.out.println("Salario: " + e.getSalario());
                // Si quieres ver el dept_no sin líos de lazy:
                System.out.println("DeptNo: " + (e.getDepartamento() != null ? e.getDepartamento().getDeptNo() : "null"));
            } else {
                System.out.println("NO ENCONTRADO (MySQL)");
            }

        } finally {
            session.close();
            HibernateUtil.closeSessionFactory();
        }
    }
}
