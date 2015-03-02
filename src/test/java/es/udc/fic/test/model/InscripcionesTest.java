/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.test.model;

import es.udc.fic.medregatas.model.barco.Barco;
import es.udc.fic.medregatas.model.inscripcion.Inscripcion;
import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.service.InscripcionService;
import es.udc.fic.medregatas.model.service.RegataService;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author iago
 */
@ContextConfiguration(locations = "classpath:/spring-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class InscripcionesTest {
    
    @Autowired
    private RegataService regataService;
    
    @Autowired
    private InscripcionService insService;
    
    
    
    
    Regata regata,regata2;
    Barco b1,b2,b3,b4;
    Tipo tipo,tipo2;
    
    
    public void addData() {
        regata = 
                regataService.crearRegata("testRegata", "Desc test regata");
        regata2 = 
                regataService.crearRegata("testRegata2", "Desc test regata2");
        tipo = new Tipo("as","as",true);
        
        tipo2 = new Tipo("as2","as2",true);
        
        
        tipo = regataService.añadirTipo("as","as",true);
        tipo2 = regataService.añadirTipo("as2","as2",true);
        
        
        b1 = new Barco(204566, "Juan Sebastian El Cano", tipo,
                (float) 1.5, "X6");
        
        
        
        b2 = new Barco(2045662, "Juan Sebastian El Cano2", tipo,
                (float) 1.5, "X6");
        b3 = new Barco(2045663, "Juan Sebastian El Cano3", tipo2,
                (float) 1.5, "X6");
        
    }
    
    @Test
    public void getEmptyInscripciones(){
        
        addData();

        
        List<Inscripcion> result = insService.getInscripciones(regata);
        
        
        assertTrue(result.isEmpty());
        
    }
    
    @Test
    public void inscribirTest() {
        
        addData();
        
        
        Inscripcion ins = insService.inscribir(regata, b1, "pepe");
        
        List<Inscripcion> result = insService.getInscripciones(regata);
        
        
        assertTrue(result.contains(ins));
        
        
        
    }
    
    @Test
    public void getInscripcionesTest() {
        
        
        addData();
        
        
        Inscripcion ins = insService.inscribir(regata, b2, "pepe1");
        Inscripcion ins2 = insService.inscribir(regata, b3, "pepe2");
        Inscripcion ins3 = insService.inscribir(regata, b2, "pepe3");
        
        
        
        List<Inscripcion> result = insService.getInscripciones(regata);
        
        
        assertTrue(result.contains(ins));
        assertTrue(result.contains(ins2));
        assertTrue(result.contains(ins3));
        
        
    }
    
    @Test
    public void getInscripcionesByTipoTest() {

        addData();
        
        
        Inscripcion ins = insService.inscribir(regata, b1, "pepe1");
        Inscripcion ins2 = insService.inscribir(regata, b2, "pepe2");
        Inscripcion ins3 = insService.inscribir(regata, b3, "pepe3");
        
        
        
        List<Inscripcion> result = insService.getInscripcionesByTipo(regata,tipo);
        
        
        assertTrue(result.contains(ins));
        assertTrue(!result.contains(ins3));
        
        result = insService.getInscripcionesByTipo(regata,tipo2);
        
        assertTrue(!result.contains(ins));
        assertTrue(result.contains(ins3));
        
    }
    
    
    
    
   @Test
    public void guardarBarcoTest() {
        addData();
        
        Barco b = insService.guardarBarco(b1);
        
        assertTrue(b == b1);
        
        List<Barco> barcos = insService.findAllBarcos();
        
        assertTrue(barcos.contains(b1));
        
        
        

    }


    public void findAllBarcosTest() {
        
        addData();
        
        insService.guardarBarco(b1);
        insService.guardarBarco(b2);
        insService.guardarBarco(b3);
        
        
        List<Barco> barcos = insService.findAllBarcos();
        
        assertTrue(barcos.contains(b1));
        assertTrue(barcos.contains(b2));
        assertTrue(barcos.contains(b3));
        
        
    }

    public void findBarcosByRegataTest() {
        
        
        addData();
        
        insService.guardarBarco(b1);
        insService.guardarBarco(b2);
        insService.guardarBarco(b3);
        
        Inscripcion ins = insService.inscribir(regata2, b1, "pepe1");
        Inscripcion ins2 = insService.inscribir(regata2, b2, "pepe2");
        Inscripcion ins3 = insService.inscribir(regata, b2, "pepe2");
        Inscripcion ins4 = insService.inscribir(regata, b3, "pepe3");
        
        List<Barco> barcos = insService.findBarcosByRegata(regata2);
        
        assertTrue(barcos.contains(b1));
        assertTrue(barcos.contains(b2));
        assertTrue(!barcos.contains(b3));
        
        barcos = insService.findBarcosByRegata(regata);
        
        assertTrue(!barcos.contains(b1));
        assertTrue(!barcos.contains(b2));
        assertTrue(barcos.contains(b3));
    }

    public void borrarInscripcionTest() {
    
        addData();
        
        Inscripcion ins = insService.inscribir(regata2, b1, "pepe1");
        Inscripcion ins2 = insService.inscribir(regata2, b2, "pepe2");
        Inscripcion ins3 = insService.inscribir(regata, b2, "pepe2");
        Inscripcion ins4 = insService.inscribir(regata, b3, "pepe3");
        
        try {
            insService.borrarInscripcion(ins4);
        } catch(InstanceNotFoundException e) {
            assertTrue(false);
        }
        
        
        List<Inscripcion> inscripciones = insService.getInscripciones(regata);
        
        assertTrue(!inscripciones.contains(ins4));
        
        boolean excep = false;
        
        try {
            insService.borrarInscripcion(ins4);
        } catch(InstanceNotFoundException e) {
            excep = true;
        }
        
        assertTrue(excep);
        
    }  

}
    
    
   
