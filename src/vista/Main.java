
package vista;

import modelo.Cliente;
import servicio.ServicioCliente;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        
        Scanner entrada = new Scanner(System.in);
        ServicioCliente servicio = new ServicioCliente();
        
        int opcion;
        
        do {
            
            System.out.println("\n==== MENÚ====");
            System.out.println("1. Agregar Cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Buscar cliente");
            System.out.println("4. Eliminar Cliente");
            System.out.println("5. Editar cliente");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            
            opcion = entrada.nextInt();
            entrada.nextLine();
            
            switch (opcion) {
                case 1:
                    System.out.print("ID:");
                    int id = entrada.nextInt();
                    entrada.nextLine();
                    
                    System.out.println("Nombre: ");
                    String nombre = entrada.nextLine();
                    
                    System.out.println("Apellido: ");
                    String apellido = entrada.nextLine();
                    
                    System.out.println("Teléfono: ");
                    String telefono = entrada.nextLine();
                    
                    System.out.println("Corre: ");
                    String correo = entrada.nextLine();
                    
                    System.out.println("Dirección: ");
                    String direccion = entrada.nextLine();
                    
                    Cliente nuevoCliente = new Cliente(id, nombre, apellido, telefono, correo, direccion);
                    
                    servicio.agregarCliente(nuevoCliente);
                    
                    System.out.println("Cliente agregado correctamente");
                    break;
                case 2:
                    servicio.listarClientes();
                    break;
                case 3:
                    
                    System.out.println("Ingrese el ID a buscar: ");
                    int idBuscar = entrada.nextInt();
                    
                    Cliente encontrado = servicio.buscarClientePorId(idBuscar);
                    
                    if (encontrado != null) {
                        System.out.println("Cliente encontrado: ");
                        System.out.println(encontrado);
                    } else {
                        
                        System.out.println("Cliente no encontrado");
                        
                    }
                    break;
                case 4:
                    System.out.println("Ingrese el ID a eliminar: ");
                    int idEliminar = entrada.nextInt();
                    
                    boolean eliminado = servicio.eliminarCliente(idEliminar);
                    
                    if (eliminado) {
                        System.out.println("Cliente eliminado correctamente");
                    } else {
                        System.out.println("Cliente no encontrado");
                    }
                    break;
                case 5:
                    
                    System.out.println("Ingrese el Id del cliente");
                    int idEditar = entrada.nextInt();
                    entrada.nextLine();
                    
                    System.out.println("Nuevo nombre: ");
                    String nuevoNombre = entrada.nextLine();
                    
                    System.out.println("Nuevo apellido: ");
                    String nuevoApellido = entrada.nextLine();
                    
                    System.out.println("Nuevo teléfono: ");
                    String nuevoTelefono = entrada.nextLine();
                    
                    System.out.println("Nuevo Correo: ");
                    String nuevoCorreo = entrada.nextLine();
                    
                    System.out.println("Nueva Direccion: ");
                    String nuevoDireccion = entrada.nextLine();
                    
                    boolean editado = servicio.editarCliente(idEditar, nuevoNombre, nuevoApellido, nuevoTelefono, nuevoCorreo, nuevoDireccion);
                    
                    if (editado) {
                        
                        System.out.println("Cliente editado correctamente");
                        
                    } else {
                    
                        System.out.println("Cliente no encontrado");
                        
                    }
                    
                    break;
                case 6:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion inválida");                   
            }
        } while (opcion != 6);
        
        entrada.close();
    }
}
