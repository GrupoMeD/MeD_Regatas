/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.tipo;

import es.udc.fic.medregatas.util.dao.GenericDaoHibernate;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import org.springframework.stereotype.Repository;

/**
 *
 * @author iago
 */
@Repository("tipoDao")
public class TipoDaoHibernate extends GenericDaoHibernate<Tipo, Long> implements
        TipoDao {

    @Override
    public Tipo findByNombre(String nombre) throws InstanceNotFoundException {

        Tipo tipo = (Tipo) getSession().createQuery("SELECT t FROM Tipo t "
                + "WHERE t.nombre = :nombre").setParameter("nombre", nombre).uniqueResult();
        if (tipo == null) {
            throw new InstanceNotFoundException(nombre, Tipo.class.getName());
        } else {
            return tipo;
        }

    }

}
