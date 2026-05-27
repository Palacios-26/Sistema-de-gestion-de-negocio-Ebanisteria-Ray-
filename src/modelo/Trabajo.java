
package modelo;

import java.sql.Date;

public class Trabajo {

    private int idTrabajo;
    private int idCliente;
    
    private String descripcion;
    
    private Date fechaInicio;
    private Date fechaEntrega;
    
    private String estado;
    
    private double costaMateriales;
    private double manoObra;
    private double precioFinal;
    
    private String observaciones;

    public Trabajo() {
    }

    public Trabajo(int idTrabajo, int idCliente, String descripcion, Date fechaInicio, Date fechaEntrega, String estado, double costaMateriales, double manoObra, double precioFinal, String observaciones) {
        this.idTrabajo = idTrabajo;
        this.idCliente = idCliente;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.costaMateriales = costaMateriales;
        this.manoObra = manoObra;
        this.precioFinal = precioFinal;
        this.observaciones = observaciones;
    }

    public int getIdTrabajo() {
        return idTrabajo;
    }

    public void setIdTrabajo(int idTrabajo) {
        this.idTrabajo = idTrabajo;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getCostaMateriales() {
        return costaMateriales;
    }

    public void setCostaMateriales(double costaMateriales) {
        this.costaMateriales = costaMateriales;
    }

    public double getManoObra() {
        return manoObra;
    }

    public void setManoObra(double manoObra) {
        this.manoObra = manoObra;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }        
    
}
