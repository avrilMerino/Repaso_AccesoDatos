package entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "direccion", length = 100)
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "correo", length = 80)
    private String correo;

    @Column(name = "ciudad", length = 50)
    private String ciudad;

    public Cliente() {}

    public Cliente(Integer id, String nombre, String direccion, String telefono, String correo, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.ciudad = ciudad;
    }

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getCiudad() { return ciudad; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nombre='" + nombre + "', ciudad='" + ciudad + "'}";
    }
}