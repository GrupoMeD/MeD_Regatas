/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.tipo;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author iago
 */
@Entity
public class Tipo implements Serializable {

    private Long idTipo;
    private String nombre;
    private String descripcion;
    private Boolean compiteTmpReal;

    public Tipo() {
    }

    public Tipo(String _nombre, String _descripcion, Boolean _compiteTmpReal) {
        nombre = _nombre;
        descripcion = _descripcion;
        compiteTmpReal = _compiteTmpReal;
    }

    @Id
    @GeneratedValue
    public Long getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Long idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getCompiteTmpReal() {
        return compiteTmpReal;
    }

    public void setCompiteTmpReal(Boolean compiteTmpReal) {
        this.compiteTmpReal = compiteTmpReal;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.nombre);
        hash = 73 * hash + Objects.hashCode(this.descripcion);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tipo other = (Tipo) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
