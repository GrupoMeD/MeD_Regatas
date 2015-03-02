/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.posicion;

import es.udc.fic.medregatas.util.dao.GenericDaoHibernate;
import java.util.Calendar;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jesús
 */
@Repository("posicionDao")
public class PosicionDaoHibernate extends GenericDaoHibernate<Posicion, Long>
        implements PosicionDao {

    @Override
    public List<Posicion> findByTipoAndRegata(Long idRegata, Calendar dia, Long idTipo) {
        if (dia != null) {
            //Tenemos tipo y dia por los que buscar
            return getSession()
                    .createQuery(
                            "SELECT p "
                            + "FROM  Posicion p "
                            + "WHERE p.barco.tipo.idTipo = :idTipo "
                            + "AND p.manga.regata.idRegata = :idRegata "
                            + "AND DATE(p.manga.fecha) = :dia ")
                    .setParameter("idTipo", idTipo)
                    .setParameter("idRegata", idRegata)
                    .setParameter("dia", dia.getTime()).list();
        } else {
            return getSession()
                    .createQuery(
                            "SELECT p "
                            + "FROM  Posicion p "
                            + "WHERE p.manga.regata.idRegata = :idRegata "
                            + "AND p.barco.tipo.idTipo = :idTipo ")
                    .setParameter("idRegata", idRegata)
                    .setParameter("idTipo", idTipo).list();
        }
    }

    @Override
    public List<Posicion> findByTipoAndManga(Long idTipo, Long idManga) {
        return getSession()
                .createQuery(
                        "SELECT p "
                        + "FROM  Posicion p "
                        + "WHERE p.manga.idManga = :idRegata "
                        + "AND p.barco.tipo.idTipo = :idTipo ")
                .setParameter("idRegata", idManga)
                .setParameter("idTipo", idTipo).list();
    }

}
