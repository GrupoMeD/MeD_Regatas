/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.inscripcion;

import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.util.dao.GenericDaoHibernate;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jesús
 */
@Repository("inscripcionDao")
public class InscripcionDaoHibernate extends GenericDaoHibernate<Inscripcion, Long> implements
        InscripcionDao {

    @Override
    public List<Inscripcion> findByRegata(Long idRegata) {
        return getSession()
                .createQuery(
                        "SELECT i "
                        + "FROM  Inscripcion i "
                        + "WHERE i.regata.idRegata = :idRegata ")
                .setParameter("idRegata", idRegata).list();
    }

    @Override
    public List<Inscripcion> fndByRegataAndTipo(Long idRegata, Long idTipo) {
        return getSession()
                .createQuery(
                        "SELECT i "
                        + "FROM  Inscripcion i "
                        + "WHERE i.regata.idRegata = :idRegata "
                                + "AND i.barco.tipo.idTipo = :idTipo")
                .setParameter("idRegata", idRegata)
                .setParameter("idTipo", idTipo).list();    }

}
