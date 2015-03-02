/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.fic.medregatas.model.posicion;

import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.util.dao.GenericDao;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Jesús
 */
public interface PosicionDao extends GenericDao<Posicion,Long>{
    
    public List<Posicion> findByTipoAndRegata(Long idRegata, Calendar dia, Long idTipo);

    public List<Posicion> findByTipoAndManga(Long idTipo, Long idManga);
}
