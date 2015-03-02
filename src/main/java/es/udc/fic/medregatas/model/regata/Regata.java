/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.regata;

import es.udc.fic.medregatas.model.manga.Manga;
import es.udc.fic.medregatas.util.TimeUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author iago
 */
@Entity
public class Regata implements Serializable {

    private Long idRegata;
    private String nombre;
    private String descripcion;
    private List<Manga> mangas = new ArrayList<Manga>();

    public Regata() {
    }

    public Regata(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Regata(String nombre, String descripcion, List<Manga> mangas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.mangas = mangas;
    }

    @Id
    @GeneratedValue
    public Long getIdRegata() {
        return idRegata;
    }

    public void setIdRegata(Long idRegata) {
        this.idRegata = idRegata;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Transient
    public Calendar getDiaIni() {
        if (mangas == null || mangas.isEmpty()) {
            return null;
        }

        Calendar diaIni = null;
        for (Manga m : mangas) {
            if (diaIni == null || diaIni.compareTo(m.getFecha()) > 0) {
                diaIni = (Calendar) m.getFecha().clone();
            }
        }
        return diaIni;
    }

    @Transient
    public Calendar getDiaFin() {
        if (mangas == null || mangas.isEmpty()) {
            return null;
        }

        Calendar diaFin = null;
        for (Manga m : mangas) {
            if (diaFin == null || diaFin.compareTo(m.getFecha()) < 0) {
                diaFin = (Calendar) m.getFecha().clone();
            }
        }
        return diaFin;
    }

    @Transient
    public List<Calendar> getDiasManga() {
        List<Calendar> diasManga = new ArrayList<Calendar>();
        for (Manga manga : mangas) {
            boolean repetido = false;
            int i = 0;
            while (!repetido && i < diasManga.size()) {
                repetido = TimeUtil.compareByDay(diasManga.get(i), manga.getFecha());
                i++;
            }
            if (!repetido) {
                diasManga.add((Calendar) manga.getFecha().clone());
            }
        }
        Collections.sort(diasManga);
        return diasManga;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="regata")
    public List<Manga> getMangas() {
        return mangas;
    }

    public void setMangas(List<Manga> mangas) {
        this.mangas = mangas;
    }

    public void addManga(Manga manga) {
        this.mangas.add(manga);
    }

    @Override
    public String toString() {
        return nombre;
    }

}
