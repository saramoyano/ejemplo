

package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Producto implements Serializable, Comparable<Producto> {

    /* Atributos **************************************************************/
    private int id;                     // PK
    private String nombre;
    private LocalDate fecha;
    private double precioUnidad;

    /* Constructores **********************************************************/
    public Producto() {
        id = 0;
        nombre = "";
        fecha = null;
        precioUnidad = 0.0;
    }

    /* Métodos getters & setters **********************************************/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        if (fecha != null) {
            return fecha.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
        } else {
            return "0";
        }
    }

    public void setFecha(String fecha) {
        if (fecha.equals("0")) {
            this.fecha = null;
        } else {
            this.fecha= LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/uuuu"));
//            this.fecha = LocalDate.of(Integer.parseInt(fecha.substring(6, 10)),
//                    Integer.parseInt(fecha.substring(3, 5)),
//                    Integer.parseInt(fecha.substring(0, 2)));
        }
    }

    public double getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(double precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    /* Métodos ****************************************************************/
    public boolean existeProducto(List<Producto> tProductos) {
        for (Producto p : tProductos) {
            if (p.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean altaProducto(List<Producto> tProductos) {
        if (existeProducto(tProductos)) {
            return false;
        }
        return tProductos.add(this);
    }

    public boolean bajaProducto(List<Producto> tProductos) {
        if (!existeProducto(tProductos)) {
            return false;
        }
        for (Producto p : tProductos) {
            if (p.getId() == id) {
                return tProductos.remove(p);
            }
        }
        return false;
    }

    @Override
    public int compareTo(Producto o) {
        return Integer.compare(id, o.id);
    }

}
