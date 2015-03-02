/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.manga;

import es.udc.fic.medregatas.model.posicion.Posicion;
import es.udc.fic.medregatas.model.regata.Regata;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author iago
 */
@Entity
public class Manga implements Serializable {

    private Long idManga;
    private Calendar fecha;
    private Regata regata;
    private int millas;
    private List<Posicion> posiciones = new ArrayList<Posicion>();

    public Manga() {
    }

    public Manga(Calendar fecha, Regata regata, List<Posicion> posiciones, int millas) {
        this.fecha = fecha;
        this.regata = regata;
        this.posiciones = posiciones;
        this.millas = millas;
    }

    @Id
    @GeneratedValue
    public Long getIdManga() {
        return idManga;
    }

    public void setIdManga(Long idManga) {
        this.idManga = idManga;
    }

    @Temporal(TemporalType.DATE)
    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRegata")
    public Regata getRegata() {
        return regata;
    }

    public void setRegata(Regata regata) {
        this.regata = regata;
    }

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "manga")
    public List<Posicion> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(List<Posicion> posiciones) {
        this.posiciones = posiciones;
    }

    public int getMillas() {
        return millas;
    }

    public void setMillas(int millas) {
        this.millas = millas;
    }

    @Override
    public String toString() {
        return "Manga{" + "idManga=" + idManga + ", fecha=" + fecha + ", regata=" + regata + '}';
    }
    
    
}
