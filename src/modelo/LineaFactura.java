package modelo;

import java.util.List;

public class LineaFactura implements Comparable<LineaFactura> {

    /* Atributos **************************************************************/
    private String idFactura;           // PK
    private int idProducto;             // PK
    private int cantidad;
    private double subtotal;

    /* Constructores **********************************************************/
    public LineaFactura() {
        idFactura = "";
        idProducto = 0;
        cantidad = 0;
        subtotal = 0.0;
    }

    /* Métodos getters & setters **********************************************/
    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    /* Métodos ****************************************************************/
    public Producto getProducto(List<Producto> tProductos) {
        for (Producto p : tProductos) {
            if (p.getId() == idProducto) {
                return p;
            }
        }
        return null;
    }

    @Override
    public int compareTo(LineaFactura o) {
        return Integer.compare(idProducto, o.idProducto);
    }

}
