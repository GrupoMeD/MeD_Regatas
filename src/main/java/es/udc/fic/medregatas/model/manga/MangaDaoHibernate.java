/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.fic.medregatas.model.manga;

import es.udc.fic.medregatas.util.dao.GenericDaoHibernate;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jesús
 */
@Repository("mangaDao")
public class MangaDaoHibernate extends GenericDaoHibernate<Manga, Long> implements
        MangaDao {
    
    @Override
    public List<Manga> findByRegata(Long idRegata) {
        
        return getSession()
        .createQuery(
                "SELECT i "
                + "FROM  Manga i "
                + "WHERE i.regata.idRegata = :idRegata ")
        .setParameter("idRegata", idRegata).list();        

    }
    
    
}
