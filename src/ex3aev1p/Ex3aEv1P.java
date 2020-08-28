package ex3aev1p;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.Cliente;
import modelo.Factura;
import modelo.LineaFactura;
import modelo.Producto;
import java.util.Scanner;
import modelo.ConexionBD;
import modelo.Ficheros;

public class Ex3aEv1P {

    private static int mostrarMenu() {
        Scanner sc = new Scanner(System.in);
        int op;
        System.out.println("MENÚ");
        System.out.println("----");
        System.out.println("");
        System.out.println("1.- Alta de Cliente.");
        System.out.println("2.- Baja de Cliente.");
        System.out.println("3.- Alta de Producto.");
        System.out.println("4.- Baja de Producto.");
        System.out.println("5.- Nueva Factura.");
        System.out.println("6.- Borrar Factura.");
        System.out.println("7.- Listado de Clientes.");
        System.out.println("8.- Listado de Productos.");
        System.out.println("9.- Listado de Facturas.");
        System.out.println("0.- Salir.");
        System.out.println("");
        System.out.print("Opción? ");
        op = sc.nextInt();
        System.out.println("");
        return op;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int op;

        List<Cliente> tClientes = new ArrayList<>();
        List<Producto> tProductos = new ArrayList<>();

        //CARGAR DATOS DESDE FICHERO
        System.out.println("Cargando Datos...");
        try {
            Ficheros.cargarClientes(tClientes, "clientes.dat");
            Ficheros.cargarProductos(tProductos, "productos.dat");
            System.out.println("Datos cargados correctamente");
        } catch (Exception e) {
            System.out.println("Error");
        }

        System.out.println("Abriendo conexion BD");
        try {
            ConexionBD.abrirConexion();
            System.out.println("Conexion abierta correctamente");
        } catch (Exception e) {
            System.out.println("Error");
        }

        //
        do {
            System.out.println("");
            op = mostrarMenu();
            switch (op) {
                case 1: // Alta de Cliente
                {
                    Cliente c = new Cliente();
                    System.out.print("Introduce el dni del cliente: ");
                    c.setDni(sc.next());
                    System.out.print("Introduce el nombre del cliente: ");
                    c.setNombre(sc.next());
                    System.out.println("");
                    if (c.altaCliente(tClientes)) {
                        System.out.println("Alta de cliente correcta. DNI: " + c.getDni());
                    } else {
                        System.out.println("Alta de cliente incorrecta!!");
                    }
                    break;
                }
                case 2: // Baja de Cliente
                {
                    Cliente c = new Cliente();
                    System.out.print("Introduce el dni del cliente: ");
                    c.setDni(sc.next());
                    System.out.println("");
                    try {
                        c.bajaCliente(tClientes);
                        System.out.println("Baja de cliente correcta. DNI: " + c.getDni());
                    } catch (Exception e) {
                        System.out.println("Baja de cliente incorrecta!!");
                    }

                    break;
                }
                case 3: // Alta de Producto
                {
                    Producto p = new Producto();
                    System.out.print("Introduce el id del producto: ");
                    p.setId(sc.nextInt());
                    System.out.print("Introduce el nombre del producto: ");
                    p.setNombre(sc.next());
                    System.out.print("Introduce la fecha del producto (dd/mm/yyyy), 0 = null: ");
                    p.setFecha(sc.next());
                    System.out.print("Introduce el precio por unidad del producto: ");
                    p.setPrecioUnidad(sc.nextDouble());
                    System.out.println("");
                    if (p.altaProducto(tProductos)) {
                        System.out.println("Alta de producto correcta. ID: " + p.getId());
                    } else {
                        System.out.println("Alta de producto incorrecta!!");
                    }
                    break;
                }
                case 4: // Baja de Producto
                {
                    Producto p = new Producto();
                    System.out.print("Introduce el id del producto: ");
                    p.setId(sc.nextInt());
                    System.out.println("");
                    if (p.bajaProducto(tProductos)) {
                        System.out.println("Baja de producto correcta. ID: " + p.getId());
                    } else {
                        System.out.println("Baja de producto incorrecta!!");
                    }
                    break;
                }
                case 5: // Nueva Factura
                {
                    Factura f = new Factura();
                    do {
                        System.out.print("Introduce el dni del cliente: ");
                        f.setDni(sc.next());
                    } while (f.getCliente(tClientes) == null);
                    boolean otraLinea = true;
                    LineaFactura lf;
                    do {
                        lf = new LineaFactura();
                        do {
                            System.out.print("Introduce el id del producto: ");
                            lf.setIdProducto(sc.nextInt());
                        } while (lf.getProducto(tProductos) == null);
                        System.out.print("Introduce la cantidad del producto: ");
                        lf.setCantidad(sc.nextInt());
                        f.gettLineasF().add(lf);
                        System.out.print("Quiere introducir otra línea de factura (s/n)? ");
                        if (!sc.next().equals("s")) {
                            otraLinea = false;
                        }
                    } while (otraLinea);
                    System.out.println("");
                    try {
                        f.altaFactura(tClientes, tProductos);
                        System.out.println("Alta de factura correcta. ID: " + f.getId() + " Fecha: " + f.getFecha());
                    } catch (Exception e) {
                        System.out.println("Alta de factura incorrecta!!" + e.getMessage());
                    }
                    break;
                }
                case 6: // Borrar Factura
                {
                    Factura f = new Factura();
                    System.out.print("Introduce el id de la factura: ");
                    f.setId(sc.next());
                    System.out.println("");
                    try {
                        f.bajaFactura();
                        System.out.println("Baja de factura correcta. ID: " + f.getId());
                    } catch (Exception e) {
                        System.out.println("Baja de factura incorrecta!!" + e.getMessage());
                    }
                    break;
                }
                case 7: // Listado de Clientes
                {
                    System.out.println("LISTADO DE CLIENTES");
                    System.out.println("-------------------");
                    Collections.sort(tClientes);
                    for (Cliente c : tClientes) {
                        System.out.printf("DNI: %-10s NOMBRE: %-20s\n",
                                c.getDni(),
                                c.getNombre());
                    }
                    break;
                }
                case 8: // Listado de Productos
                {
                    System.out.println("LISTADO DE PRODUCTOS");
                    System.out.println("--------------------");
                    Collections.sort(tProductos);
                    for (Producto p : tProductos) {
                        System.out.printf("ID: %-10s NOMBRE: %-20s FECHA: %10s PRECIOUNIDAD: %.2f\n",
                                p.getId(),
                                p.getNombre(),
                                p.getFecha(),
                                p.getPrecioUnidad());
                    }
                    break;
                }
                case 9: // Listado de Facturas
                {
                    System.out.println("LISTADO DE FACTURAS");
                    System.out.println("-------------------");
                    List<Factura> tFacturas = new ArrayList<>();
                    Collections.sort(tFacturas);
                    for (Factura f : tFacturas) {
                        System.out.printf("ID: %-10s DNI: %-10s CLIENTE: %-20s FECHA: %-10s IVA: %d TOTAL: %.2f\n",
                                f.getId(),
                                f.getDni(),
                                f.getCliente(tClientes).getNombre(),
                                f.getFecha(),
                                f.getIVA(),
                                f.getTotal());
                        Collections.sort(f.gettLineasF());
                        for (LineaFactura lf : f.gettLineasF()) {
                            System.out.printf("\tCÓDIGO: %-10d PRODUCTO: %-20s P.U.: %.2f CANTIDAD: %-5d SUBTOTAL: %.2f\n",
                                    lf.getIdProducto(),
                                    lf.getProducto(tProductos).getNombre(),
                                    lf.getProducto(tProductos).getPrecioUnidad(),
                                    lf.getCantidad(),
                                    lf.getSubtotal());
                        }
                    }
                    break;
                }

                case 0:
                    System.out.println("Guardando Datos...");
                    try {
                        Ficheros.guardarClientes(tClientes, "clientes.dat");
                        Ficheros.guardarProductos(tProductos, "productos.dat");
                        System.out.println("Datos guardados correctamente");
                    } catch (Exception e) {
                        System.out.println("Error");
                    }
                    break;

                default:
                    System.out.println("Opción incorrecta!!");
                    System.out.println("");
            }

        } while (op != 0);
        System.out.println("");

    }

}
