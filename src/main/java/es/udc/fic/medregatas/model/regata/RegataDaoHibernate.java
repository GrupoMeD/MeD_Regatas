/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.regata;

import es.udc.fic.medregatas.util.dao.GenericDaoHibernate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jesús
 */
@Repository("regataDao")
public class RegataDaoHibernate extends GenericDaoHibernate<Regata, Long> implements
        RegataDao {


}
