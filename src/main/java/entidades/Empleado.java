package entidades;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.math.BigDecimal;
import java.time.LocalDate;
/**
 * Entidad Empleado -> tabla empleados
 */
@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_no")
    private Integer empNo;

    @Column(name = "nombre", nullable = false, length = 15)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @Column(name = "oficio", length = 50)
    private String oficio;

    @Column(name = "fecha_alt")
    private LocalDate fechaAlt;

    @Column(name = "salario", precision = 10, scale = 2)
    private BigDecimal salario;

    /**
     * Relación muchos-a-uno:
     * Muchos empleados pertenecen a un departamento.
     * En la tabla empleados existe la FK: dept_no
     */
    @ManyToOne
    @JoinColumn(name = "dept_no") // columna FK en empleados
    private Departamento departamento;

    // Constructor vacío obligatorio para Hibernate
    public Empleado() {}

    // Constructor útil (sin empNo si la BD lo genera)
    public Empleado(String nombre, String apellido, String oficio,
                    LocalDate fechaAlt, BigDecimal salario, Departamento departamento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.oficio = oficio;
        this.fechaAlt = fechaAlt;
        this.salario = salario;
        this.departamento = departamento;
    }

    public Empleado(int empNo, String nombre, String apellido, String oficio, LocalDate now, BigDecimal salario, Departamento d) {
    }

    // Getters y setters
    public Integer getEmpNo() { return empNo; }
    public void setEmpNo(Integer empNo) { this.empNo = empNo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getOficio() { return oficio; }
    public void setOficio(String oficio) { this.oficio = oficio; }

    public LocalDate getFechaAlt() { return fechaAlt; }
    public void setFechaAlt(LocalDate fechaAlt) { this.fechaAlt = fechaAlt; }

    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }

    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }
}
