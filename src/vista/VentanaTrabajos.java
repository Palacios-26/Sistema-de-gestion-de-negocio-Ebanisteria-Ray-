
package vista;

//Importar modelo Trabajo
import modelo.Trabajo;
    
//Importar servicio que conecta con SQL Server
import servicio.ServicioTrabajo;
    
//Librerias de interfaz grafica
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
    
    

public class VentanaTrabajos extends JFrame{
    
    //Campos de texto del formulario
    private JTextField campoIdCliente;
    private JTextField campoDescripcion;
    private JTextField campoEstado;
    private JTextField campoCostoMateriales;
    private JTextField campoManoObra;
    private JTextField campoPrecioFinal;
    
    //Area de etxto para observaciones largas
    private JTextArea campoObservaciones;
    
    //Tabla para mostrar trabajos
    private JTable tablaTrabajos;
    
    //Modelo de la tabla
    private DefaultTableModel modeloTabla;
    
    //Botones
    private JButton botonAgregar;
    private JButton botonListar;
    
    //Servicio que maneja SQL Server
    private ServicioTrabajo servicio;
    
    //Constructor de la ventana
    public VentanaTrabajos(){
    
        //Crear objeto servicio
        servicio = new ServicioTrabajo();
        
        //Titulo de la ventana
        setTitle("Gestión de Trabajos");
        
        //Tamaño de la ventana
        setSize(900, 500);
        
        //Cerrar solo esta ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //Centrar ventana
        setLocationRelativeTo(null);
        
        //Iniciar componentes
        iniciarComponentes();
        
        //Mostrar ventana
        setVisible(true);
                    
    }
    
    //Metodo que crea todos los componentes
    private void iniciarComponentes(){
    
        //Layout principal
        setLayout(new BorderLayout());
        
        //Panel superior (Formulario)
        JPanel panelSuperior = new JPanel();
        
        //Organizar formulario en filas y columnas
        panelSuperior.setLayout(new GridLayout(7,2));
        
        //---------CAMPOS DEL FORMULARIOS--------------\\
        
        //CLientes
        panelSuperior.add(new JLabel("ID Cliente: "));
        campoIdCliente = new JTextField();
        panelSuperior.add(campoIdCliente);
        
        //Descripcion
        panelSuperior.add(new JLabel("Descripcion: "));
        campoDescripcion = new JTextField();
        panelSuperior.add(campoDescripcion);
        
        //Estado
        panelSuperior.add(new JLabel("Estado: "));
        campoEstado = new JTextField();
        panelSuperior.add(campoEstado);
        
        //Costo materiales
        panelSuperior.add(new JLabel("Costo de Materiales: "));
        campoCostoMateriales = new JTextField();
        panelSuperior.add(campoCostoMateriales);
        
        //Mano de obra
        panelSuperior.add(new JLabel("Mano de Obra: "));
        campoManoObra = new JTextField();
        panelSuperior.add(campoManoObra);
        
        //Precio final
        panelSuperior.add(new JLabel("Precio Final: "));
        campoPrecioFinal = new JTextField();
        panelSuperior.add(campoPrecioFinal);
        
        //Area de observaciones
        panelSuperior.add(new JLabel("Costo de Materiales: "));
        campoObservaciones = new JTextArea(3, 20);
        
        //JScrollPane agrega scroll al JTextArea
        panelSuperior.add(new JScrollPane(campoObservaciones));
        
        //Agrega un formulario arriba
        add(panelSuperior, BorderLayout.NORTH);
        
        
        //------------------TABLA-----------------\\
        modeloTabla = new DefaultTableModel();
        
        //Crear columnas
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Cliente");
        modeloTabla.addColumn("Descripcion");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Precio");
        
        //Crear tabla usando el modelo
        tablaTrabajos = new JTable(modeloTabla);
        
        //Agregar scroll a la tabla
        JScrollPane scroll = new JScrollPane(tablaTrabajos);
        
        //Agregar tabla al centro
        add(scroll, BorderLayout.CENTER);
        
        
        //---------BOTONES------------\\
        
        //Panel para los botones
        JPanel panelBotones = new JPanel();
        
        //Crear los botones
        botonAgregar = new JButton("Agregar Trabajo");
        botonListar = new JButton("Listar Trabajos");
        
        //Agregar botones al panel
        panelBotones.add(botonAgregar);
        panelBotones.add(botonListar);
        
        //Agregar panel abajo
        add(panelBotones, BorderLayout.SOUTH);
        eventos();
        
    }
    
    
    private void eventos(){
    
        //Evento del boton agregar
        botonAgregar.addActionListener(e -> {
        
            try {
                
                //Obtener datos del formulario
                int idCliente = Integer.parseInt(campoIdCliente.getText());
                String descripcion = campoDescripcion.getText();
                String estado = campoEstado.getText();
                double costoMateriales = Double.parseDouble(campoCostoMateriales.getText());
                double manoObra = Double.parseDouble(campoManoObra.getText());
                double precioFinal = Double.parseDouble(campoPrecioFinal.getText());
                String observaciones = campoObservaciones.getText();
                
                //Fechas actuales
                java.sql.Date fechaInicio = new java.sql.Date(System.currentTimeMillis());
                java.sql.Date fechaEntrega = new java.sql.Date(System.currentTimeMillis());
                
                //Crear objeto Trabajo
                Trabajo trabajo = new Trabajo(
                
                        0,
                        idCliente,
                        descripcion,
                        fechaInicio,
                        fechaEntrega,
                        estado,
                        costoMateriales,
                        manoObra,
                        precioFinal,
                        observaciones                                        
                );
                
                //Guardar en SQL Server
                servicio.agregarTrabajo(trabajo);
                
                JOptionPane.showMessageDialog(null, "Trabajo agregado correctamente");
                
                limpiarCampos();
                
                
                
            } catch (Exception ex) {
                
                JOptionPane.showMessageDialog(null, "Error al agregar trabajo");
                
                ex.printStackTrace();
                
            }
                
        });
        
        //Evento para listar trabajos
        botonListar.addActionListener(e -> {
        
            //Limpiar tablas antes de cargar datos
            modeloTabla.setRowCount(0);
            
            //Recorrer lista de trabajos
            for (Trabajo t : servicio.obtenerTrabajos()) {
                
                //Agregar fila a la tabla
                modeloTabla.addRow(new Object[]{
                
                    t.getIdTrabajo(),
                    t.getIdCliente(),
                    t.getDescripcion(),
                    t.getEstado(),
                    t.getPrecioFinal()                    
                });
                
            }
        
        });
        
        
    
    }
    
    private void limpiarCampos(){

        campoIdCliente.setText("");
        campoDescripcion.setText("");
        campoEstado.setText("");
        campoCostoMateriales.setText("");
        campoManoObra.setText("");
        campoPrecioFinal.setText("");
        campoObservaciones.setText("");
        
    }
    
    
    //Metodo principal
    public static void main(String[] args) {
        //Ejecutar ventana
        new VentanaTrabajos();
    }
}
