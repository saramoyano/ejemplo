package modelo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MiniScanner {
    
    public String leerCadena() throws Exception {
        String texto = "";
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (br.readLine() != "0") {
                texto = texto + "\n" + br.readLine();
            }
        } catch (Exception e) {
            throw new Exception("Error al leer Cadena");
        }
        return texto;
    }
    
    public LocalDate leerFecha() throws Exception {
        String fecha = "";        
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (br.readLine() != "0") {
                fecha = br.readLine();
                
            }
            
        } catch (Exception e) {
            throw new Exception("Error al leer fecha");
        }
        
        return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/uuuu"));        
    }
//        
//        

    public Double leerDecimal() throws Exception {
        String decimal = "";        
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (br.readLine() != "0") {
                decimal=br.readLine();
            }            
        } catch (Exception e) {
            throw new Exception("Error al leer decimal");
        }
        
        return Double.valueOf(decimal);        
    }

//        
//    
}
