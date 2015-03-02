/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.fic.test.model;

import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.model.tipo.TipoDao;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jesús
 */
@ContextConfiguration(locations = "classpath:/spring-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TipoDaoTest {
    
    @Autowired
    TipoDao tipoDao;
    
    @Test
    public void TestOperations() throws InstanceNotFoundException{
    
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
    
    }
}
