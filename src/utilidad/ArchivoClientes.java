
package utilidad;

import modelo.Cliente;
import java.io.*;
import java.util.ArrayList;

public class ArchivoClientes {

    private static final String ARCHIVO = "clientes.txt";
    
    //Guardar Clientes
    public static void guardarClientes(ArrayList<Cliente> clientes){
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            
            for (Cliente cliente : clientes) {
                bw.write(
                cliente.getId() + ", " +
                cliente.getNombre() + ", " + 
                cliente.getApellido() + ", " +
                cliente.getTelefono() + ", " + 
                cliente.getCorreo() + ", " + 
                cliente.getDireccion()
                );
                
                bw.newLine();
            }
            
        } catch (IOException e) {
            
            System.out.println("Error al guardar clientes");
            
        }
    }
    
    //Cargar Clientes
    public static ArrayList<Cliente> cargarClientes(){
    
    ArrayList<Cliente> clientes = new ArrayList<>();
    
    File archivo = new File(ARCHIVO);
    
        if (!archivo.exists()) {
            
            return clientes;
            
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            
            String linea;
                    
            while ((linea = br.readLine()) != null) {
                
                String[] datos = linea.split(",");
                
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                String apellido = datos[2];
                String telefono = datos[3];
                String correo = datos[4];
                String direccion = datos[5];
                
                Cliente cliente = new Cliente(id, nombre, apellido, telefono, correo, direccion);
                
                clientes.add(cliente);
                
            }
                    
        } catch (IOException e) {
        
            System.out.println("Error al cargar clientes");
            
        }
        
        return clientes;
        
    }
}
