package main;

import entidades.Cliente;
import entidades.Pedido;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ExamenStyleApp {

    public static void main(String[] args) {

        // 1) Elige config (MySQL o Postgres)
        String cfg = "hibernate.cfg.xml"; // o "hibernate-postgres.cfg.xml"

        // 2) SessionFactory (1 vez para toda la app)
        SessionFactory sf = HibernateUtil.getSessionFactory(cfg);

        Scanner sc = new Scanner(System.in);
        int op;

        do {
            menu();
            System.out.print("Opción: ");
            op = leerInt(sc);

            switch (op) {
                case 1 -> listarClientes(sf);
                case 2 -> insertarCliente(sf, pedirCliente(sc));
                case 3 -> modificarCliente(sf, sc);
                case 4 -> listarPedidos(sf);
                case 5 -> insertarPedido(sf, sc);
                case 6 -> borrarPedidosDeCliente(sf, sc);
                case 7 -> listarPedidosDeCliente(sf, sc);
                case 8 -> mostrarImporteTotal(sf, sc);
                case 9 -> mostrarNumeroPedidos(sf, sc);
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }

            System.out.println();
        } while (op != 0);

        sc.close();
        HibernateUtil.closeSessionFactory();
    }

    // =========================
    // MENÚ
    // =========================
    private static void menu() {
        System.out.println("========== MENÚ EXAMEN Hibernate ==========");
        System.out.println("1) Listar clientes");
        System.out.println("2) Insertar cliente");
        System.out.println("3) Modificar cliente");
        System.out.println("4) Listar pedidos");
        System.out.println("5) Insertar pedido (cliente debe existir)");
        System.out.println("6) Borrar TODOS los pedidos de un cliente");
        System.out.println("7) Listar pedidos de un cliente");
        System.out.println("8) Mostrar importe TOTAL de pedidos de un cliente");
        System.out.println("9) Mostrar NÚMERO de pedidos de un cliente");
        System.out.println("0) Salir");
        System.out.println("===========================================");
    }

    // =========================
    // 1) LISTAR CLIENTES
    // =========================
    private static void listarClientes(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            List<Cliente> lista = session.createQuery("from Cliente", Cliente.class).list();
            if (lista.isEmpty()) System.out.println("(Sin clientes)");
            for (Cliente c : lista) System.out.println(c);
        }
    }

    // =========================
    // 2) INSERTAR CLIENTE
    // =========================
    private static void insertarCliente(SessionFactory sf, Cliente c) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            // Comprobar que no exista
            Cliente existe = session.find(Cliente.class, c.getId());
            if (existe != null) {
                System.out.println("Ya existe un cliente con ese ID.");
                tx.rollback();
                return;
            }

            session.persist(c);
            tx.commit();
            System.out.println("Cliente insertado OK.");
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            ex.printStackTrace();
        }
    }

    // =========================
    // 3) MODIFICAR CLIENTE (merge)
    // =========================
    private static void modificarCliente(SessionFactory sf, Scanner sc) {
        System.out.print("ID del cliente a modificar: ");
        int id = leerInt(sc);

        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            Cliente c = session.find(Cliente.class, id);
            if (c == null) {
                System.out.println("Cliente no existe.");
                tx.rollback();
                return;
            }

            System.out.print("Nuevo nombre: ");
            sc.nextLine();
            String nuevoNombre = sc.nextLine();

            c.setNombre(nuevoNombre);

            // merge no es estrictamente necesario si c ya está gestionado,
            // pero en examen lo suelen usar:
            session.merge(c);

            tx.commit();
            System.out.println("Cliente modificado OK.");
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            ex.printStackTrace();
        }
    }

    // =========================
    // 4) LISTAR PEDIDOS
    // =========================
    private static void listarPedidos(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            List<Pedido> lista = session.createQuery("from Pedido", Pedido.class).list();
            if (lista.isEmpty()) System.out.println("(Sin pedidos)");
            for (Pedido p : lista) System.out.println(p);
        }
    }

    // =========================
    // 5) INSERTAR PEDIDO (cliente debe existir)
    // =========================
    private static void insertarPedido(SessionFactory sf, Scanner sc) {
        System.out.print("ID pedido: ");
        int idPedido = leerInt(sc);

        System.out.print("ID cliente del pedido: ");
        int idCliente = leerInt(sc);

        System.out.print("Importe: ");
        BigDecimal importe = leerBigDecimal(sc);

        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            // Comprobar que el pedido no exista
            Pedido existePedido = session.find(Pedido.class, idPedido);
            if (existePedido != null) {
                System.out.println("Ya existe un pedido con ese ID.");
                tx.rollback();
                return;
            }

            // Comprobar que el cliente exista
            Cliente cliente = session.find(Cliente.class, idCliente);
            if (cliente == null) {
                System.out.println("El cliente NO existe. No se puede crear pedido.");
                tx.rollback();
                return;
            }

            Pedido p = new Pedido(idPedido, LocalDate.now(), importe, cliente);
            session.persist(p);

            tx.commit();
            System.out.println("Pedido insertado OK.");
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            ex.printStackTrace();
        }
    }

    // =========================
    // 6) BORRAR TODOS los pedidos de un cliente
    // =========================
    private static void borrarPedidosDeCliente(SessionFactory sf, Scanner sc) {
        System.out.print("ID cliente: ");
        int idCliente = leerInt(sc);

        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            Cliente c = session.find(Cliente.class, idCliente);
            if (c == null) {
                System.out.println("Cliente no existe.");
                tx.rollback();
                return;
            }

            List<Pedido> pedidos = session.createQuery(
                    "from Pedido p where p.cliente.id = :id", Pedido.class
            ).setParameter("id", idCliente).list();

            for (Pedido p : pedidos) session.remove(p);

            tx.commit();
            System.out.println("Pedidos borrados: " + pedidos.size());
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            ex.printStackTrace();
        }
    }

    // =========================
    // 7) LISTAR pedidos de un cliente
    // =========================
    private static void listarPedidosDeCliente(SessionFactory sf, Scanner sc) {
        System.out.print("ID cliente: ");
        int idCliente = leerInt(sc);

        try (Session session = sf.openSession()) {
            List<Pedido> pedidos = session.createQuery(
                    "from Pedido p where p.cliente.id = :id", Pedido.class
            ).setParameter("id", idCliente).list();

            if (pedidos.isEmpty()) System.out.println("(Sin pedidos para ese cliente)");
            for (Pedido p : pedidos) System.out.println(p);
        }
    }

    // =========================
    // 8) IMPORTE TOTAL pedidos de un cliente
    // =========================
    private static void mostrarImporteTotal(SessionFactory sf, Scanner sc) {
        System.out.print("ID cliente: ");
        int idCliente = leerInt(sc);

        try (Session session = sf.openSession()) {
            BigDecimal total = session.createQuery(
                    "select coalesce(sum(p.importe), 0) from Pedido p where p.cliente.id = :id",
                    BigDecimal.class
            ).setParameter("id", idCliente).uniqueResult();

            System.out.println("Importe total = " + total);
        }
    }

    // =========================
    // 9) NÚMERO de pedidos de un cliente
    // =========================
    private static void mostrarNumeroPedidos(SessionFactory sf, Scanner sc) {
        System.out.print("ID cliente: ");
        int idCliente = leerInt(sc);

        try (Session session = sf.openSession()) {
            Long num = session.createQuery(
                    "select count(p) from Pedido p where p.cliente.id = :id",
                    Long.class
            ).setParameter("id", idCliente).uniqueResult();

            System.out.println("Número de pedidos = " + num);
        }
    }

    // =========================
    // PEDIR CLIENTE (helper)
    // =========================
    private static Cliente pedirCliente(Scanner sc) {
        System.out.print("ID: ");
        int id = leerInt(sc);

        System.out.print("Nombre: ");
        sc.nextLine();
        String nombre = sc.nextLine();

        System.out.print("Dirección: ");
        String direccion = sc.nextLine();

        System.out.print("Teléfono: ");
        String telefono = sc.nextLine();

        System.out.print("Correo: ");
        String correo = sc.nextLine();

        System.out.print("Ciudad: ");
        String ciudad = sc.nextLine();

        return new Cliente(id, nombre, direccion, telefono, correo, ciudad);
    }

    // =========================
    // LECTURAS SEGURAS
    // =========================
    private static int leerInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Introduce un número: ");
        }
        return sc.nextInt();
    }

    private static BigDecimal leerBigDecimal(Scanner sc) {
        while (!sc.hasNextBigDecimal()) {
            sc.next();
            System.out.print("Introduce un número decimal: ");
        }
        return sc.nextBigDecimal();
    }
}