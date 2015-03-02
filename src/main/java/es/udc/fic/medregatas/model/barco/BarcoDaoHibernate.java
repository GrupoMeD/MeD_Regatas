/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.fic.medregatas.model.barco;

import es.udc.fic.medregatas.util.dao.GenericDaoHibernate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jesús
 */
@Repository("barcoDao")
public class BarcoDaoHibernate extends GenericDaoHibernate<Barco, Long> implements
        BarcoDao{
    
}
