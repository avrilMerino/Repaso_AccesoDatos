/*package entidades;
import jakarta.persistence.*;
@Entity
@Table(name="departamentos")
public class Departamento {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La BD genera el id (AUTO_INCREMENT / IDENTITY)
    @Column(name = "dept_no") // Columna PK en la tabla
    private Integer deptNo;

    @Column(name = "dnombre", nullable = false, length = 50) // Columna obligatoria
    private String dnombre;

    @Column(name = "loc", nullable = false, length = 50) // Columna obligatoria
    private String loc;

    // Constructor vacío obligatorio para Hibernate
    public Departamento() {}

    // Constructor útil para crear departamentos (sin id porque lo genera la BD)
    public Departamento(String dnombre, String loc) {
        this.dnombre = dnombre;
        this.loc = loc;
    }

    // Getters y setters
    public Integer getDeptNo() {
        return deptNo;
    }
    public void setDeptNo(Integer deptNo) {
        this.deptNo = deptNo;
    }

    public String getDnombre() { return dnombre;
    }
    public void setDnombre(String dnombre) { this.dnombre = dnombre;
    }

    public String getLoc() { return loc;
    }
    public void setLoc(String loc) { this.loc = loc;
    }
}*/
package entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "departamentos")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_no")
    private Short deptNo;

    @Column(name = "dnombre", nullable = false, length = 20)
    private String dnombre;

    @Column(name = "loc", length = 15) // en tu SQL no es NOT NULL
    private String loc;

    public Departamento() {}

    public Departamento(String dnombre, String loc) {
        this.dnombre = dnombre;
        this.loc = loc;
    }

    public Short getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(Short deptNo) {
        this.deptNo = deptNo;
    }

    public String getDnombre() {
        return dnombre;
    }

    public void setDnombre(String dnombre) {
        this.dnombre = dnombre;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
