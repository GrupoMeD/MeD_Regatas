/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.test.experiments;

import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.model.tipo.TipoDao;
import es.udc.fic.medregatas.model.tipo.TipoDaoHibernate;
import es.udc.fic.medregatas.util.HibernateUtil;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import java.util.List;
import org.hibernate.Transaction;
import org.springframework.context.annotation.ComponentScan;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 *
 * @author iago
 */
@ComponentScan("es.udc.fic.medregatas.model.tipo")
public class TipoDaoExperiments {

    public static void main(String[] args) {

        TipoDaoHibernate tipoDaoHibernate = new TipoDaoHibernate();
        tipoDaoHibernate.setSessionFactory(HibernateUtil
                .getSessionFactory());
        TipoDao tipoDao = tipoDaoHibernate;

        Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession()
                .beginTransaction();
        try {
            // Create Tipo
            Tipo tipo = new Tipo("Laser", "Descripcion", true);
            tipoDao.save(tipo);

            Long idTipo = tipo.getIdTipo();
            System.out.println("Tipo whith idTipo '" + idTipo
                    + "' has been created");
            System.out.println(tipo);

            // Find tipo.
            tipo = tipoDao.find(idTipo);
            System.out.println("Tipo whith idTipo '" + idTipo
                    + "' has been retrieved");
            System.out.println(tipo);

            Tipo tipo2 = new Tipo("Tipo2", "Descripcion2", false);
            tipoDao.save(tipo2);

            //Borramos el Tipo
            tipoDao.remove(idTipo);
            tipoDao.remove(tipo2.getIdTipo());

            try {
                tipoDao.find(idTipo);
                System.err.println("ERROR: The Tipo alredy exists");
            } catch (InstanceNotFoundException e) {
                System.out.println("Tipo has been removed correctly");
            }

            // ... proceed in the same way for other entities / methods / use cases
            tx.commit();

        } catch (RuntimeException e) {

            tx.rollback();
        } catch (InstanceNotFoundException e) {

            tx.commit();
        } finally {
            HibernateUtil.getSessionFactory().getCurrentSession().close();
        }

        //TODO Que pasa con esto?
        //HibernateUtil.shutdown();
    }
}
