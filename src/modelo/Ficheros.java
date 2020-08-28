package modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Ficheros {

    public static void guardarClientes(List<Cliente> tClientes, String strFichCli) throws Exception {
        //Object
        File f = new File(strFichCli);

        try (FileOutputStream fos = new FileOutputStream(f); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (Cliente c : tClientes) {
                oos.writeObject(c);
            }
        } catch (IOException e) {
            throw new IOException("Error al guardar en fichero clientes", e);
            //es Exception, no IO en linea 27
        }

    }

    public static void cargarClientes(List<Cliente> tClientes, String strFichCli) throws Exception {

        File f = new File(strFichCli);
        if (f.exists()) {
            try (FileInputStream fis = new FileInputStream(f); ObjectInputStream ois = new ObjectInputStream(fis)) {
                Cliente c;
                while (true) {
                    c = (Cliente) ois.readObject();
                    tClientes.add(c);
                }
            } catch (EOFException e) {
                ;
            } catch (IOException e) {
                throw new IOException("Error al cargar en fichero clientes", e);
            } catch (Exception e) {
                throw new IOException("Error en fichero clientes", e);
            }
        }
    }

    public static void guardarProductos(List<Producto> tProductos, String strFichPro) throws Exception {
        //Data

        File f = new File(strFichPro);
        
         try (FileOutputStream fos = new FileOutputStream(f); DataOutputStream dos = new DataOutputStream(fos)) {
            for (Producto p : tProductos) {
                dos.writeInt(p.getId());
                dos.writeUTF(p.getNombre());
                dos.writeUTF((p.getFecha()==null)? "0": p.getFecha());//en fichero guardo null no 0
                dos.writeDouble(p.getPrecioUnidad());
            }
         }
         //catch   
    }

    public static void cargarProductos(List<Producto> tProductos, String strFichPro) throws Exception {
        File f = new File(strFichPro);
        if (f.exists()) {
            try (FileInputStream fis = new FileInputStream(f); DataInputStream dis = new DataInputStream(fis)) {
                Producto p;
                while (true) {
                    p = new Producto();
                    p.setId(dis.readInt());
                    p.setNombre(dis.readUTF());
                    p.setFecha(dis.readUTF());
                    p.setPrecioUnidad(dis.readDouble());
                    tProductos.add(p);
                }
            } catch (EOFException e) {
                ;
            } catch (IOException e) {
                throw new IOException("Error al cargar en fichero productos", e);
            } catch (Exception e) {
                throw new IOException("Error en fichero productos", e);
            }
        }

    }

}
