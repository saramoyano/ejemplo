package modelo;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Cliente implements Serializable, Comparable<Cliente> {

    /* Atributos **************************************************************/
    private String dni;                 // PK
    private String nombre;

    /* Constructores **********************************************************/
    public Cliente() {
        dni = "";
        nombre = "";
    }

    /* Métodos getters & setters **********************************************/
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /* Métodos ****************************************************************/
    public boolean existeCliente(List<Cliente> tClientes) {
        for (Cliente c : tClientes) {
            if (c.getDni().equals(dni)) {
                return true;
            }
        }
        return false;
    }

    public boolean altaCliente(List<Cliente> tClientes) {
        if (existeCliente(tClientes)) {
            return false;
        }
        return tClientes.add(this);
    }

    public boolean bajaCliente(List<Cliente> tClientes) throws Exception {
        if (!existeCliente(tClientes)) {
            return false;
        }
        if (clienteEnFactura()) {
            throw new Exception("No se puede eliminar el Cliente, datos en factura");
        }

        for (Cliente c : tClientes) {
            if (c.getDni().equals(dni)) {
                return tClientes.remove(c);
            }
        }
        return false;
    }

    @Override
    public int compareTo(Cliente o) {
        return dni.compareToIgnoreCase(o.dni);
    }

    public boolean clienteEnFactura() throws Exception {

        String sql = "SELECT * FROM Facturas where dni=?";
        try (PreparedStatement pst = ConexionBD.getConn().prepareStatement(sql)) {
            pst.setString(1, dni);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new SQLException("Error al buscar cliente en facturas");
        }
    }
}
