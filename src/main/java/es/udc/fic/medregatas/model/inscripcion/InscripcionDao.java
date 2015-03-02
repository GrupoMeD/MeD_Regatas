/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.fic.medregatas.model.inscripcion;

import es.udc.fic.medregatas.util.dao.GenericDao;
import java.util.List;

/**
 *
 * @author Jesús
 */
public interface InscripcionDao extends GenericDao<Inscripcion, Long>{

    /**
     * Devuelve las inscripciones de una regata
     * 
     * @param idRegata
     * @return 
     */
    public List<Inscripcion> findByRegata(Long idRegata);

    public List<Inscripcion> fndByRegataAndTipo(Long idRegata, Long idTipo);
    
}
