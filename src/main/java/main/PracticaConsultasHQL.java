package main;
import entidades.Departamento;
import entidades.Empleado;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
public class PracticaConsultasHQL {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // String cfg = "hibernate.cfg.xml";               // MySQL
        String cfg = "hibernate-postgres.cfg.xml";         // PostgreSQL

        // 2) Abrimos Hibernate
        SessionFactory sf = HibernateUtil.getSessionFactory(cfg);
        Session session = sf.openSession();
        int op;

        do {
            menu();
            System.out.print("Opción: ");
            op = leerInt(sc);

            System.out.println("\n--- CONSULTA " + op + " ---");
            System.out.println("Va a hacer: " + descripcion(op));
            System.out.println("Hace (resultado):");

            switch (op) {

                case 1 -> { // Listar todos los empleados
                    List<Empleado> lista = session.createQuery("from Empleado", Empleado.class).list();
                    for (Empleado e : lista) {
                        System.out.println(e.getEmpNo() + " - " + e.getNombre() + " " + e.getApellido());
                    }
                }

                case 2 -> { // Nombres y apellidos
                    List<Object[]> res = session.createQuery(
                            "select e.nombre, e.apellido from Empleado e", Object[].class
                    ).list();
                    for (Object[] fila : res) {
                        System.out.println(fila[0] + " " + fila[1]);
                    }
                }

                case 3 -> { // Departamentos y ubicación
                    List<Object[]> res = session.createQuery(
                            "select d.dnombre, d.loc from Departamento d", Object[].class
                    ).list();
                    for (Object[] fila : res) {
                        System.out.println(fila[0] + " - " + fila[1]);
                    }
                }

                case 4 -> { // Salario > 2000
                    Query<Empleado> q = session.createQuery(
                            "from Empleado e where e.salario > :x", Empleado.class
                    );
                    q.setParameter("x", new BigDecimal("2000.00"));
                    List<Empleado> lista = q.list();
                    for (Empleado e : lista) {
                        System.out.println(e.getNombre() + " " + e.getApellido() + " -> " + e.getSalario());
                    }
                }

                case 5 -> { // Ordenados por fecha desc
                    List<Empleado> lista = session.createQuery(
                            "from Empleado e order by e.fechaAlt desc", Empleado.class
                    ).list();
                    for (Empleado e : lista) {
                        System.out.println(e.getNombre() + " " + e.getApellido() + " - " + e.getFechaAlt());
                    }
                }

                case 6 -> { // Salario promedio
                    Double avg = session.createQuery(
                            "select avg(e.salario) from Empleado e", Double.class
                    ).uniqueResult();
                    System.out.println("Salario promedio = " + avg);
                }

                case 7 -> { // Total empleados
                    Long total = session.createQuery(
                            "select count(e) from Empleado e", Long.class
                    ).uniqueResult();
                    System.out.println("Total empleados = " + total);
                }

                case 8 -> { // Max y Min salario
                    Object[] fila = session.createQuery(
                            "select max(e.salario), min(e.salario) from Empleado e", Object[].class
                    ).uniqueResult();
                    System.out.println("Máximo = " + fila[0]);
                    System.out.println("Mínimo = " + fila[1]);
                }

                case 9 -> { // Total empleados por departamento
                    List<Object[]> res = session.createQuery(
                            "select e.departamento.dnombre, count(e) from Empleado e group by e.departamento.dnombre",
                            Object[].class
                    ).list();
                    for (Object[] fila : res) {
                        System.out.println(fila[0] + " -> " + fila[1]);
                    }
                }

                case 10 -> { // Suma salarios por departamento
                    List<Object[]> res = session.createQuery(
                            "select e.departamento.dnombre, sum(e.salario) from Empleado e group by e.departamento.dnombre",
                            Object[].class
                    ).list();
                    for (Object[] fila : res) {
                        System.out.println(fila[0] + " -> " + fila[1]);
                    }
                }

                case 11 -> { // Apellido empieza por A
                    List<Empleado> lista = session.createQuery(
                            "from Empleado e where e.apellido like 'A%'", Empleado.class
                    ).list();
                    for (Empleado e : lista) {
                        System.out.println(e.getNombre() + " " + e.getApellido());
                    }
                }

                case 12 -> { // Departamentos sin empleados
                    List<Departamento> lista = session.createQuery(
                            "select d from Departamento d where not exists " +
                                    "(select 1 from Empleado e where e.departamento = d)",
                            Departamento.class
                    ).list();
                    for (Departamento d : lista) {
                        System.out.println(d.getDnombre() + " - " + d.getLoc());
                    }
                }

                case 13 -> { // Salario entre 1500 y 3000
                    Query<Empleado> q = session.createQuery(
                            "from Empleado e where e.salario between :a and :b", Empleado.class
                    );
                    q.setParameter("a", new BigDecimal("1500.00"));
                    q.setParameter("b", new BigDecimal("3000.00"));
                    for (Empleado e : q.list()) {
                        System.out.println(e.getNombre() + " " + e.getApellido() + " -> " + e.getSalario());
                    }
                }

                case 14 -> { // Oficio diferente de Programador
                    Query<Empleado> q = session.createQuery(
                            "from Empleado e where e.oficio <> :of", Empleado.class
                    );
                    q.setParameter("of", "Programador");
                    for (Empleado e : q.list()) {
                        System.out.println(e.getNombre() + " " + e.getApellido() + " - " + e.getOficio());
                    }
                }

                case 15 -> { // Contratados después de 01/01/2020
                    LocalDate fecha = LocalDate.of(2020, 1, 1);
                    Query<Empleado> q = session.createQuery(
                            "from Empleado e where e.fechaAlt > :f", Empleado.class
                    );
                    q.setParameter("f", fecha);
                    for (Empleado e : q.list()) {
                        System.out.println(e.getNombre() + " " + e.getApellido() + " - " + e.getFechaAlt());
                    }
                }

                case 16 -> { // Empleado + nombre departamento
                    List<Object[]> res = session.createQuery(
                            "select e.nombre, e.apellido, e.departamento.dnombre from Empleado e",
                            Object[].class
                    ).list();
                    for (Object[] fila : res) {
                        System.out.println(fila[0] + " " + fila[1] + " -> " + fila[2]);
                    }
                }

                case 17 -> { // Empleados de Ventas
                    Query<Empleado> q = session.createQuery(
                            "from Empleado e where e.departamento.dnombre = :dep", Empleado.class
                    );
                    q.setParameter("dep", "Ventas");
                    for (Empleado e : q.list()) {
                        System.out.println(e.getNombre() + " " + e.getApellido());
                    }
                }

                case 18 -> { // Salario más alto de cada departamento
                    List<Object[]> res = session.createQuery(
                            "select e.departamento.dnombre, e.nombre, e.apellido, e.salario " +
                                    "from Empleado e " +
                                    "where e.salario = (select max(e2.salario) from Empleado e2 where e2.departamento = e.departamento)",
                            Object[].class
                    ).list();
                    for (Object[] fila : res) {
                        System.out.println(fila[0] + " -> " + fila[1] + " " + fila[2] + " (" + fila[3] + ")");
                    }
                }

                case 19 -> { // Departamentos con más de 5 empleados
                    List<Object[]> res = session.createQuery(
                            "select e.departamento.dnombre, count(e) from Empleado e " +
                                    "group by e.departamento.dnombre having count(e) > 5",
                            Object[].class
                    ).list();
                    for (Object[] fila : res) {
                        System.out.println(fila[0] + " -> " + fila[1]);
                    }
                }

                case 20 -> { // Departamentos + cantidad empleados (solo con empleados)
                    List<Object[]> res = session.createQuery(
                            "select e.departamento.dnombre, count(e) from Empleado e group by e.departamento.dnombre",
                            Object[].class
                    ).list();
                    for (Object[] fila : res) {
                        System.out.println(fila[0] + " -> " + fila[1]);
                    }
                }

                case 0 -> System.out.println("Saliendo...");

                default -> System.out.println("Opción inválida.");
            }

            System.out.println();
        } while (op != 0);

        // Cerrar to
        session.close();
        HibernateUtil.closeSessionFactory();
        sc.close();
    }

    // -------- MENÚ SIMPLE --------
    private static void menu() {
        System.out.println("=========== MENÚ HQL ===========");
        System.out.println("0) Salir");
        for (int i = 1; i <= 20; i++) {
            System.out.println(i + ") " + descripcion(i));
        }
        System.out.println("================================");
    }

    // -------- TEXTO “QUÉ VA A HACER” --------
    private static String descripcion(int n) {
        return switch (n) {
            case 1 -> "Listar todos los empleados";
            case 2 -> "Obtener nombres y apellidos de empleados";
            case 3 -> "Listar departamentos con ubicación";
            case 4 -> "Empleados con salario > 2000";
            case 5 -> "Empleados ordenados por fecha alta DESC";
            case 6 -> "Salario promedio de empleados";
            case 7 -> "Número total de empleados";
            case 8 -> "Salario máximo y mínimo";
            case 9 -> "Total empleados por departamento";
            case 10 -> "Suma de salarios por departamento";
            case 11 -> "Empleados cuyo apellido empieza por A";
            case 12 -> "Departamentos sin empleados";
            case 13 -> "Empleados con salario entre 1500 y 3000";
            case 14 -> "Empleados con oficio distinto de Programador";
            case 15 -> "Empleados contratados después del 01/01/2020";
            case 16 -> "Empleados con nombre de su departamento";
            case 17 -> "Empleados del departamento Ventas";
            case 18 -> "Empleado(s) con salario más alto de cada departamento";
            case 19 -> "Departamentos con más de 5 empleados";
            case 20 -> "Departamentos con cantidad de empleados (solo con empleados)";
            default -> "Opción no válida";
        };
    }

    private static int leerInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Introduce un número: ");
        }
        return sc.nextInt();
    }
}
