package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    /* Atributos **************************************************************/
    private static Connection conn;

    private static final String DB = "ex3aev1p";  // El schema y usuario deben existir en la BD!!
    private static final String URL = "jdbc:mysql://localhost:3306/" + DB
            + "? serverTimezone=Europe/Madrid & useServerPrepStmts=true";//& useSSL=false 
    private static final String USER = "alumno";
    private static final String PASS = "alumno";

    /* Constructores **********************************************************/
    public ConexionBD() {
        conn = null;
    }

    /* Métodos getters & setters **********************************************/
    public static Connection getConn() {
        return conn;
    }

    /* Métodos ****************************************************************/
    private static void crearTablas() throws Exception {

        String sql;
        try (Statement st = conn.createStatement()) {
            sql = "CREATE TABLE IF NOT EXISTS Facturas ("
                    + "id VARCHAR(6) NOT NULL,"
                    + "dni VARCHAR(10) NULL,"
                    + "fecha DATE NOT NULL,"
                    + "total DECIMAL(8,2) NOT NULL,"
                    + "CONSTRAINT pk_Facturas PRIMARY KEY (id))";
            st.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS LineasFactura ("
                    + "idFactura VARCHAR(6) NOT NULL,"
                    + "idProducto INTEGER NOT NULL,"
                    + "cantidad INTEGER NOT NULL,"
                    + "subtotal DECIMAL(8,2) NOT NULL,"
                    + "CONSTRAINT pk_LineasFactura PRIMARY KEY (idFactura,idProducto),"
                    + "CONSTRAINT fk_LineasFactura_Factura FOREIGN KEY (idFactura)"
                    + " REFERENCES Facturas (id) ON DELETE CASCADE)";
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new Exception("Error al crear tablas");
        }catch (Exception e) {
            throw new Exception("Otro error al crear tablas");
        }
    }

    public static void abrirConexion() throws Exception {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            crearTablas();
        } catch (SQLException e) {
            throw new Exception("Error al abrir conexion", e);
        }catch (Exception e) {
            throw new Exception("Otro error al abrir conexion");
        }
    }

    public static void cerrarConexion() throws Exception {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new Exception("Error al cerrar conexion", e);
            }
        }
    }

}
