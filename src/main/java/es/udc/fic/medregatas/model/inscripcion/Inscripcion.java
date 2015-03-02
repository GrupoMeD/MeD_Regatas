/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.inscripcion;

import es.udc.fic.medregatas.model.barco.Barco;
import es.udc.fic.medregatas.model.regata.Regata;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Jesús
 */
@Entity
public class Inscripcion implements Serializable {

    private Long idInscripcion;
    private Regata regata;
    private Barco barco;
    private String patron;

    public Inscripcion() {
    }

    public Inscripcion(Regata regata, Barco barco, String patron) {
        this.regata = regata;
        this.barco = barco;
        this.patron = patron;
    }

    @Id
    @GeneratedValue
    public Long getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Long idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRegata")
    public Regata getRegata() {
        return regata;
    }

    public void setRegata(Regata regata) {
        this.regata = regata;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idBarco")
    public Barco getBarco() {
        return barco;
    }

    public void setBarco(Barco barco) {
        this.barco = barco;
    }

    public String getPatron() {
        return patron;
    }

    public void setPatron(String patron) {
        this.patron = patron;
    }
    
    

}
