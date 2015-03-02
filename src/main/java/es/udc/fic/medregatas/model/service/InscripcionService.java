/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.service;

import es.udc.fic.medregatas.model.barco.Barco;
import es.udc.fic.medregatas.model.inscripcion.Inscripcion;
import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import java.util.List;

/**
 *
 * @author iago
 */
public interface InscripcionService {

    /**
     * Inscribe al barco en la regata creando y persistiendo la inscripción
     * resultante
     *
     * @param regata
     * @param barco
     * @param patron
     * @return
     */
    public Inscripcion inscribir(Regata regata, Barco barco, String patron);

    public List<Inscripcion> getInscripciones(Regata regata);

    public List<Inscripcion> getInscripcionesByTipo(Regata regata, Tipo tipo);

    /**
     * Persiste un objeto barco
     *
     * @param barco
     * @return
     */
    public Barco guardarBarco(Barco barco);

    /**
     * Devuelbe todos los barcos de sistema
     * 
     * @return 
     */
    public List<Barco> findAllBarcos();

    public List<Barco> findBarcosByRegata(Regata regata);

    public void borrarInscripcion(Inscripcion inscripcion) throws InstanceNotFoundException;

}
