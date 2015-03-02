/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.tipo;

import es.udc.fic.medregatas.util.dao.GenericDao;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;

/**
 *
 * @author iago
 */
public interface TipoDao extends GenericDao<Tipo, Long> {

    public Tipo findByNombre(String nombre) throws InstanceNotFoundException;

}
