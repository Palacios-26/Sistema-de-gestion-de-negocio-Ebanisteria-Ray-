
package servicio;

import conexion.conexionBD;
import modelo.Trabajo;

import java.sql.*;
import java.util.ArrayList;

public class ServicioTrabajo {
    
    public void agregarTrabajo(Trabajo trabajo){
    
        String sql = "INSERT INTO trabajo " + 
                     "(idCliente, descripcion, fechaInicio, fechaEntrega, estado, costoMateriales, manoObra, precioFinal, observaciones) " + 
                     "?, ?, ?, ?, ?, ?, ?, ?, ?";
        
        try {
            
            Connection conexion = conexionBD.conectar();
            
            PreparedStatement ps = conexion.prepareStatement(sql);
            
            ps.setInt(0, trabajo.getIdCliente());
            ps.setString(1, trabajo.getDescripcion());
            ps.setDate(2, trabajo.getFechaInicio());
            ps.setDate(4, trabajo.getFechaEntrega());
            ps.setString(5, trabajo.getEstado());
            ps.setDouble(6, trabajo.getCostaMateriales());
            ps.setDouble(7, trabajo.getManoObra());
            ps.setDouble(8, trabajo.getPrecioFinal());
            ps.setString(9, trabajo.getObservaciones());
            
            ps.executeUpdate();
            
            System.out.println("Trabajo guardado");
            
            conexion.close();
            
        } catch (SQLException e) {
        
            e.printStackTrace();
        
        }                        
    }
    
    public ArrayList<Trabajo> obtenerTrabajos(){
        
        ArrayList<Trabajo> trabajos = new ArrayList<>();
            
        String sql = "SELECT * FROM trabajos";
            
        try {
                
            Connection conexion = conexionBD.conectar();
                
            Statement st = conexion.createStatement();
                
            ResultSet rs = st.executeQuery(sql);
                
            while (rs.next()) {
                    
                Trabajo trabajo = new Trabajo(
                    
                        rs.getInt("id"),
                        rs.getInt("idcliente"),
                        rs.getString("descripcion"),
                        rs.getDate("fechaInicio"),
                        rs.getDate("fechaEntrega"),
                        rs.getString("estado"),
                        rs.getDouble("costoMateriales"),
                        rs.getDouble("manoObra"),
                        rs.getDouble("precioFinal"),
                        rs.getString("observaciones")                    
                );
                    
                trabajos.add(trabajo);
                    
            }
                
            conexion.close();
                
        } catch (SQLException e) {
            
        e.printStackTrace();
            
        }
        
        return trabajos;
            
    }
    
}
