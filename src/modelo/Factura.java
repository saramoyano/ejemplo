package modelo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Factura implements Comparable<Factura> {

    /* Atributos **************************************************************/
    private String id;                  // PK
    private String dni;
    private LocalDate fecha;
    private double total;

    private final int IVA = 21;
    private List<LineaFactura> tLineasF;

    private static int numFactura = 1;  // Contador de facturas (generación automática de ID)

    /* Constructores **********************************************************/
    public Factura() {
        id = "";
        dni = "";
        fecha = null;
        total = 0.0;
        tLineasF = new ArrayList<>();
    }

    /* Métodos getters & setters **********************************************/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFecha() {
        if (fecha != null) {
            return fecha.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
        } else {
            return "";
        }
    }

    public void setFecha(LocalDate fecha) {

        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIVA() {
        return IVA;
    }

    public List<LineaFactura> gettLineasF() {
        return tLineasF;
    }

    /* Métodos ****************************************************************/
    public Cliente getCliente(List<Cliente> tClientes) {
        for (Cliente c : tClientes) {
            if (c.getDni().equals(dni)) {
                return c;
            }
        }
        return null;
    }

    public Integer buscarMaximo() throws Exception {
        String sql = "SELECT id from Facturas";
        List<Integer> listIds = new ArrayList<>();
        try (PreparedStatement pst = ConexionBD.getConn().prepareStatement(sql)) {
            ResultSet rs1 = pst.executeQuery();

            while (rs1.next()) {
                Integer num = Integer.valueOf(rs1.getString("id"));
                listIds.add(num);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al identificar factura");
        } catch (Exception e) {
            throw new Exception("Otro error  al identificar  factura");
        }
        Collections.reverse(listIds);
        for (Integer idtmp : listIds) {
            return idtmp + 1;
        }
        return 1;
    }

    public void altaFactura(List<Cliente> tClientes, List<Producto> tProductos) throws Exception {
        // Comprobamos si el cliente existe
        if (this.getCliente(tClientes) == null) {
            throw new Exception("El cliente no existe");
        }
        // Comprobamos si los productos de las líneas existen
        for (LineaFactura lineaF : tLineasF) {
            if (lineaF.getProducto(tProductos) == null) {
                throw new Exception("El producto no existe");
            }
        }
        // Completamos la factura
        id = String.format("FA%04d", buscarMaximo());

        fecha = LocalDate.now();
        // Completamos las líneas
        double totalF = 0;
        String sql1 = "INSERT INTO Facturas VALUES(?,?,?,?)";
        String sql2 = "INSERT INTO LineasFactura VALUES(?,?,?,?)";
        try (PreparedStatement pst1 = ConexionBD.getConn().prepareStatement(sql1);
                PreparedStatement pst2 = ConexionBD.getConn().prepareStatement(sql2)) {

            ConexionBD.getConn().setAutoCommit(false);
            
            for (LineaFactura lineaF : tLineasF) {
                lineaF.setIdFactura(id);
                lineaF.setSubtotal(lineaF.getCantidad() * lineaF.getProducto(tProductos).getPrecioUnidad());
                totalF = totalF + lineaF.getSubtotal();
                
                pst2.setString(1, id);
                pst2.setInt(2, lineaF.getIdProducto());
                pst2.setInt(3, lineaF.getCantidad());
                pst2.setDouble(4, lineaF.getSubtotal());
                pst2.executeUpdate();

            }
        
        // Terminamos de completar la factura
        total = totalF + (totalF * IVA / 100);
        // Insertamos la nueva Factura

            pst1.setString(1, id);
            pst1.setString(2, dni);
            pst1.setDate(3, (fecha == null) ? null : Date.valueOf(fecha));
            pst1.setDouble(4, total);
            pst1.executeUpdate();

         }
    }

    public void bajaFactura() throws Exception {
        String sql1 = "DELETE FROM Facturas WHERE id=?";

        try (PreparedStatement pst1 = ConexionBD.getConn().prepareStatement(sql1)) {
            pst1.setString(1, id);
            pst1.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error en metodo baja factura");
        } catch (Exception e) {
            throw new Exception("Otro error en metodo baja factura");
        }
    }

    public void listadoFacturas(List<Factura> tFac) throws Exception {

        String sql = "SELECT * FROM Facturas ORDER BY id";
        String sql2 = "SELECT * FROM LineasFactura ORDER BY idFactura, idProducto";
        try (PreparedStatement pst = ConexionBD.getConn().prepareStatement(sql); PreparedStatement pst2 = ConexionBD.getConn().prepareStatement(sql2)) {
            ResultSet rs1 = pst.executeQuery();
            Factura f = null;
            while (rs1.next()) {
                f = new Factura();
                f.setId(rs1.getString("id"));
                f.setDni(rs1.getString("dni"));
                f.setFecha(rs1.getDate("fecha").toLocalDate());
                f.setTotal(rs1.getDouble("total"));
                ResultSet rs2 = pst2.executeQuery();
                while (rs2.next()) {
                    LineaFactura ln = new LineaFactura();
                    ln.setIdFactura(rs2.getString("id"));
                    ln.setIdProducto(rs2.getInt("idProducto"));
                    ln.setCantidad(rs2.getInt("cantidad"));
                    ln.setSubtotal(rs1.getDouble("subtotal"));
                    f.gettLineasF().add(ln);
                }
                tFac.add(f);
            }
        } catch (SQLException e) {
            throw new SQLException("Error en listado factura");
        } catch (Exception e) {
            throw new Exception("Otro error en listado factura");
        }

    }

    @Override
    public int compareTo(Factura o) {
        return id.compareToIgnoreCase(o.id);
    }

}
