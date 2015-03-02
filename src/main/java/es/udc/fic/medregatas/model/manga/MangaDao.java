/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.fic.medregatas.model.manga;

import es.udc.fic.medregatas.util.dao.GenericDao;
import java.util.List;

/**
 *
 * @author Jesús
 */
public interface MangaDao extends GenericDao<Manga, Long>{
    
    
    public List<Manga> findByRegata(Long idRegata);
    
}
