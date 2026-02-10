package entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "importe", precision = 10, scale = 2)
    private BigDecimal importe;

    // MUCHOS pedidos -> UN cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public Pedido() {}

    public Pedido(Integer id, LocalDate fecha, BigDecimal importe, Cliente cliente) {
        this.id = id;
        this.fecha = fecha;
        this.importe = importe;
        this.cliente = cliente;
    }

    public Integer getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public BigDecimal getImporte() { return importe; }
    public Cliente getCliente() { return cliente; }

    @Override
    public String toString() {
        return "Pedido{id=" + id + ", fecha=" + fecha + ", importe=" + importe +
                ", cliente=" + (cliente != null ? cliente.getId() : null) + "}";
    }
}