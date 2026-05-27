package vista;

import modelo.Cliente;
import servicio.ServicioCliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaClientes extends JFrame {

    private JTextField campoId;
    private JTextField campoNombre;
    private JTextField campoApellido;
    private JTextField campoTelefono;
    private JTextField campoCorreo;
    private JTextField campoDireccion;    

    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    private JButton botonAgregar;
    private JButton botonListar;
    private JButton botonBuscar;
    private JButton botonEliminar;
    private JButton botonEditar;

    private ServicioCliente servicio;

    public VentanaClientes() {

        servicio = new ServicioCliente();

        setTitle("Sistema de Clientes");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        iniciarComponentes();

        setVisible(true);
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new GridLayout(9, 2, 5, 5));

        panelSuperior.add(new JLabel("ID:"));
        campoId = new JTextField();
        panelSuperior.add(campoId);

        panelSuperior.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        panelSuperior.add(campoNombre);
        
        panelSuperior.add(new JLabel("Apellido:"));
        campoApellido = new JTextField();
        panelSuperior.add(campoApellido);

        panelSuperior.add(new JLabel("Telefono:"));
        campoTelefono = new JTextField();
        panelSuperior.add(campoTelefono);
        
        panelSuperior.add(new JLabel("Correo:"));
        campoCorreo = new JTextField();
        panelSuperior.add(campoCorreo);
        
        panelSuperior.add(new JLabel("Dirección:"));
        campoDireccion = new JTextField();
        panelSuperior.add(campoDireccion);

        botonAgregar = new JButton("Agregar Cliente");
        panelSuperior.add(botonAgregar);

        botonListar = new JButton("Listar Clientes");
        panelSuperior.add(botonListar);
        
        botonBuscar = new JButton("Buscar Cliente");
        panelSuperior.add(botonBuscar);
        
        botonEliminar = new JButton("Eliminar Clientes");
        panelSuperior.add(botonEliminar);
        
        botonEditar = new JButton("Editar Cliente");
        panelSuperior.add(botonEditar);

        add(panelSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();
        
        /*modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Teléfono");*/
        
        tablaClientes = new JTable(modeloTabla);
        
        JScrollPane scroll = new JScrollPane(tablaClientes);
        
        scroll.setColumnHeaderView(null);
        
        add(scroll, BorderLayout.CENTER);

        eventos();
    }

    private void eventos() {

        botonAgregar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                int id = Integer.parseInt(campoId.getText());
                String nombre = campoNombre.getText();
                String apellido = campoApellido.getText();
                String telefono = campoTelefono.getText();
                String correo = campoApellido.getText();
                String direccion = campoDireccion.getText();
                

                Cliente cliente = new Cliente(id, nombre, apellido, telefono, correo, direccion);

                servicio.agregarCliente(cliente);

                JOptionPane.showMessageDialog(null, "Cliente agregado");

                limpiarCampos();
            }
        });

        botonListar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                modeloTabla.setRowCount(0);
                modeloTabla.setColumnCount(0);
                
                modeloTabla.addColumn("ID");
                modeloTabla.addColumn("Nombre");
                modeloTabla.addColumn("Apellido");
                modeloTabla.addColumn("Teléfono");
                modeloTabla.addColumn("Correo");
                modeloTabla.addColumn("Dirección");
                
                for (Cliente c : servicio.obtenerClientes()) {
                    
                    modeloTabla.addRow(new Object[]{
                        
                        c.getId(),
                        c.getNombre(),
                        c.getApellido(),
                        c.getTelefono(),
                        c.getCorreo(),
                        c.getDireccion()
                        
                    });                    
                }
                
                tablaClientes.getTableHeader().setVisible(true);
                
            }
        });
        
        botonBuscar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    String textoId = campoId.getText().trim();

                    System.out.println("ID escrito: " + textoId);

                    int id = Integer.parseInt(textoId);

                    Cliente encontrado = servicio.buscarClientePorId(id);

                    if (encontrado != null) {

                        campoNombre.setText(encontrado.getNombre());
                        campoTelefono.setText(encontrado.getTelefono());

                    } else {

                        JOptionPane.showMessageDialog(null,"Cliente no encontrado");
                    }

                } catch (NumberFormatException ex) {

                    JOptionPane.showMessageDialog(null, "Ingrese un ID válido");

                }
            }
        });
        
        botonEliminar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    int id = Integer.parseInt(campoId.getText());

                    boolean eliminado = servicio.eliminarCliente(id);

                    if (eliminado) {

                        JOptionPane.showMessageDialog(null, "Cliente eliminado");

                        limpiarCampos();

                    } else {

                        JOptionPane.showMessageDialog(null, "Cliente no encontrado");
                    }

                } catch (NumberFormatException ex) {

                    JOptionPane.showMessageDialog(null, "Ingrese un ID válido");
                }
            }
        });        

        botonEditar.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    
                    int id = Integer.parseInt(campoId.getText());
                    
                    String nuevoNombre = campoNombre.getText();
                    String nuevoApellido = campoApellido.getText();
                    String nuevoTelefono = campoTelefono.getText();
                    String nuevoCorreo = campoCorreo.getText();
                    String nuevoDireccion = campoDireccion.getText();
                    
                    
                    
                    boolean editado = servicio.editarCliente(id, nuevoNombre, nuevoApellido, nuevoTelefono, nuevoCorreo, nuevoDireccion);
                    
                    if (editado) {
                        
                        JOptionPane.showMessageDialog(null, "Cliente editado correctamente");
                        
                        limpiarCampos();
                        
                    } else {
                    
                        JOptionPane.showMessageDialog(null, "Cliente no encontrado");
                        
                    }
                            
                } catch (NumberFormatException ex) {
                    
                    JOptionPane.showMessageDialog(null, "Ingrese un ID valido");
                    
                }
            }
        
        });
        
        tablaClientes.addMouseListener(new MouseAdapter(){
        
            @Override
            public void mouseClicked(MouseEvent e){
                int fila = tablaClientes.getSelectedRow();
                
                if (fila != -1) {
                    
                    campoId.setText(tablaClientes.getValueAt(fila, 0).toString());
                    campoNombre.setText(tablaClientes.getValueAt(fila, 1).toString());
                    campoApellido.setText(tablaClientes.getValueAt(fila, 2).toString());
                    campoTelefono.setText(tablaClientes.getValueAt(fila, 3).toString());
                    campoCorreo.setText(tablaClientes.getValueAt(fila, 4).toString());
                    campoDireccion.setText(tablaClientes.getValueAt(fila, 5).toString());
                    
                }
                
                
            }
                    
                    
        
        });
        
    }

    private void limpiarCampos() {

        campoId.setText("");
        campoNombre.setText("");
        campoApellido.setText("");
        campoTelefono.setText("");
        campoCorreo.setText("");
        campoDireccion.setText("");
    }

    public static void main(String[] args) {

        new VentanaClientes();
    }
}
