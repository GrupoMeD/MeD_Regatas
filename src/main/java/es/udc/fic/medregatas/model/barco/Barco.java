/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.barco;

import es.udc.fic.medregatas.model.tipo.Tipo;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author iago
 */
@Entity
public class Barco implements Serializable {

    private Long idBarco;
    private int vela;
    private String nombre;
    private Tipo tipo;
    private Float gph;
    private String modelo;

    public Barco() {
    }

    public Barco(int vela, String nombre, Tipo tipo, Float gph, String modelo) {
        this.vela = vela;
        this.nombre = nombre;
        this.tipo = tipo;
        this.gph = gph;
        this.modelo = modelo;
    }

    @Id
    @GeneratedValue
    public Long getIdBarco() {
        return idBarco;
    }

    public void setIdBarco(Long idBarco) {
        this.idBarco = idBarco;
    }

    public int getVela() {
        return vela;
    }

    public void setVela(int vela) {
        this.vela = vela;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTipo")
    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Float getGph() {
        return gph;
    }

    public void setGph(Float gph) {
        this.gph = gph;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.vela;
        hash = 67 * hash + Objects.hashCode(this.nombre);
        hash = 67 * hash + Objects.hashCode(this.tipo);
        hash = 67 * hash + Objects.hashCode(this.gph);
        hash = 67 * hash + Objects.hashCode(this.modelo);
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
        final Barco other = (Barco) obj;
        if (this.vela != other.vela) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.gph, other.gph)) {
            return false;
        }
        if (!Objects.equals(this.modelo, other.modelo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ESP" + String.valueOf(vela);
    }

}
