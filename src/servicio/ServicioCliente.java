package servicio;

import conexion.conexionBD;
import modelo.Cliente;

import java.sql.*;
import java.util.ArrayList;

public class ServicioCliente {

    public void agregarCliente(Cliente cliente) {

        String sql = "INSERT INTO clientes " + "(id, nombre, apellido, telefono, correo, direccion) " + "VALUES (?, ?, ?, ?, ?, ?)";

        try {

            Connection conexion = conexionBD.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, cliente.getId());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getApellido());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getCorreo());
            ps.setString(6, cliente.getDireccion());

            ps.executeUpdate();

            System.out.println("Cliente guardado");

            conexion.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public ArrayList<Cliente> obtenerClientes() {

        ArrayList<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT * FROM clientes";

        try {

            Connection conexion = conexionBD.conectar();

            Statement st = conexion.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                Cliente cliente = new Cliente(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("telefono"),
                                rs.getString("correo"),
                                rs.getString("direccion")
                        );

                clientes.add(cliente);
            }

            conexion.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return clientes;
    }

    public Cliente buscarClientePorId(int id) {

        String sql =
        "SELECT * FROM clientes WHERE id = ?";

        try {

            Connection conexion = conexionBD.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Cliente cliente = new Cliente(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("telefono"),
                                rs.getString("correo"),
                                rs.getString("direccion")
                        );

                return cliente;
            }

            conexion.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    public boolean eliminarCliente(int id) {

        String sql = "DELETE FROM clientes WHERE id = ?";

        try {

            Connection conexion = conexionBD.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            conexion.close();

            return filas > 0;

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }

    public boolean editarCliente(
            int id,
            String nombre,
            String apellido,
            String telefono,
            String correo,
            String direccion
    ) {

        String sql =
        "UPDATE clientes SET nombre=?, apellido=?, telefono=?, correo=?, direccion=? WHERE id=?";

        try {

            Connection conexion = conexionBD.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, telefono);
            ps.setString(4, correo);
            ps.setString(5, direccion);
            ps.setInt(6, id);

            int filas = ps.executeUpdate();

            conexion.close();

            return filas > 0;

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }
    
    public void listarClientes() {

        ArrayList<Cliente> clientes = obtenerClientes();

        if (clientes.isEmpty()) {

            System.out.println("No hay clientes");

            return;
        }

        for (Cliente c : clientes) {

            System.out.println(c);
        }
    }
}
