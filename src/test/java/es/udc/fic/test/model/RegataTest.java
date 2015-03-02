/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.test.model;

import es.udc.fic.medregatas.model.barco.Barco;
import es.udc.fic.medregatas.model.manga.Manga;
import es.udc.fic.medregatas.model.posicion.Posicion;
import es.udc.fic.medregatas.model.posicion.Posicion.Penalizacion;
import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.regata.RegataDao;
import es.udc.fic.medregatas.model.service.InscripcionService;
import es.udc.fic.medregatas.model.service.MangaService;
import es.udc.fic.medregatas.model.service.RegataService;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.model.tipo.TipoDao;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class RegataTest {

    @Autowired
    private RegataDao regataDao;

    @Autowired
    private TipoDao tipoDao;

    @Autowired
    private InscripcionService inscripcionService;

    @Autowired
    private MangaService mangaService;

    @Autowired
    private RegataService regataService;

     @Test
     public void testGetRegatasCrearEditarFindBorrar() throws InstanceNotFoundException {
      //Creamos una sola regata con la instancia de todos los objetos en memoria
        Regata regata = new Regata();
        regata.setNombre("Mock Regata");
        regata.setDescripcion("Mock Desc");
        regataDao.save(regata);

        Regata regata1 = new Regata();
        regata1.setNombre("Mock Regata1");
        regata1.setDescripcion("Mock Desc1");
        regataDao.save(regata1);

        Regata regata2 = new Regata();
        regata2.setNombre("Mock Regata2");
        regata2.setDescripcion("Mock Desc2");
        regataDao.save(regata2);
      
        Regata regata3 = new Regata();
        regata3.setNombre("Mock Regata3");
        regata3.setDescripcion("Mock Desc3");
        regataDao.save(regata3);
      
        Regata regata4 = new Regata();
        regata4.setNombre("Mock Regata4");
        regata4.setDescripcion("Mock Desc4");
        regataDao.save(regata4);
        
        
        regata.setNombre("nombreEditado");
        regata.setDescripcion("descriocion1");
        Regata regataExpected = regataService.editarInfoRegata(regata);
        Regata regataAlmacenada =regataService.findRegata(regata.getIdRegata());
        assertEquals(regataExpected,regataAlmacenada);
        //Cinco regatas que creamos ahora mas dos nuevas
        List<Regata> regatas =  regataService.getRegatas();
        assertEquals(regatas.size(),7);
        assertTrue(regatas.contains( regata));
        assertTrue(regatas.contains( regata1));
        assertTrue(regatas.contains( regata2));
        assertTrue(regatas.contains( regata3));
        assertTrue(regatas.contains( regata4));
        
        regataService.borrarRegata(regata.getIdRegata());
        regatas =  regataService.getRegatas();
        assertEquals(regatas.size(),6);
        assertFalse(regatas.contains(regata));

     }
    
     
     @Test
     public void testGetTipos() {
        Tipo tipoCatamaran = new Tipo("Catamarán", "Desc Catamarán", false);
        tipoDao.save(tipoCatamaran);
        
        Tipo tipoCrucero = new Tipo("Crucero", "Desc Crucero", false);
        tipoDao.save(tipoCrucero);

        Tipo tipoLigero = new Tipo("Vela ligera", "Desc Vela ligera", true);
        tipoDao.save(tipoLigero);
        Tipo tipoExpected =regataService.añadirTipo("tipo1", "descripcion1", true);
      
        List<Tipo> tipos= regataService.getTipos();
        
        //como ya hay tres tipos creados son 6
        assertEquals(tipos.size(),7);
        assertTrue(tipos.contains( tipoCatamaran));
        assertTrue(tipos.contains( tipoCrucero));
        assertTrue(tipos.contains( tipoLigero));
        assertTrue(tipos.contains( tipoExpected));
        
        regataService.updateTipo(tipoExpected.getIdTipo(), new Tipo("tipo2", "descripcion2", false));
        tipos= regataService.getTipos();
     
        assertEquals(tipos.size(),7);
        assertTrue(tipos.contains( tipoCatamaran));
        assertTrue(tipos.contains( tipoCrucero));
        assertTrue(tipos.contains( tipoLigero));
        assertTrue(tipos.contains( tipoExpected));
        assertEquals(tipoExpected.getNombre(),"tipo2");
        assertEquals(tipoExpected.getDescripcion(),"descripcion2");
        assertFalse(tipoExpected.getCompiteTmpReal());
        regataService.removeTipo(tipoExpected.getIdTipo());
        tipos= regataService.getTipos();
     
        assertEquals(tipos.size(),6);
        assertTrue(tipos.contains( tipoCatamaran));
        assertTrue(tipos.contains( tipoCrucero));
        assertTrue(tipos.contains( tipoLigero));
        assertFalse(tipos.contains( tipoExpected));
        
     }
    @Test
    public void testDiaIniDiaFin() {

        //Creamos una sola regata con la instancia de todos los objetos en memoria
        Regata regata = new Regata();
        regata.setNombre("Mock Regata");
        regata.setDescripcion("Mock Desc");
        regataDao.save(regata);

        Tipo tipoCatamaran = new Tipo("Catamarán", "Desc Catamarán", false);
        tipoDao.save(tipoCatamaran);

        Tipo tipoCrucero = new Tipo("Crucero", "Desc Crucero", false);
        tipoDao.save(tipoCrucero);

        Tipo tipoLigero = new Tipo("Vela ligera", "Desc Vela ligera", true);
        tipoDao.save(tipoLigero);

        Barco b1 = new Barco(204566, "Juan Sebastian El Cano", tipoCatamaran,
                new Float(1.5), "Lagoon 421");
        inscripcionService.inscribir(regata, b1, "Iago Suárez");
        Barco b2 = new Barco(199012, "El Holandes Errante", tipoCrucero,
                new Float(2.5), "SWAN 66 FD");
        inscripcionService.inscribir(regata, b2, "Samu Paredes");
        Barco b3 = new Barco(201402, "La Perla Negra", tipoCrucero,
                new Float(1.5), "X6");
        inscripcionService.inscribir(regata, b3, "Adrian Pallas");
        Barco b4 = new Barco(202102, "La Pinta", tipoCrucero,
                new Float(1.5), "X6");
        inscripcionService.inscribir(regata, b4, "Pedro Cabalar");
        Barco b5 = new Barco(182345, "Venus", tipoLigero, null, "Laser Standar");
        inscripcionService.inscribir(regata, b5,
                "Jesus Lopez");
        Barco b6 = new Barco(206745, "Apolo", tipoLigero, null, "Laser Radial");
        inscripcionService.inscribir(regata, b6,
                "Diego Bascoy");

        Calendar dia1 = Calendar.getInstance();
        dia1.add(Calendar.DAY_OF_YEAR, -18);

        Calendar dia2 = Calendar.getInstance();
        dia2.add(Calendar.DAY_OF_YEAR, -18);
        dia2.add(Calendar.HOUR, 2);

        Calendar dia3 = Calendar.getInstance();
        dia3.add(Calendar.DAY_OF_YEAR, -17);

        Manga manga1 = new Manga(dia1, regata, null, 100);
        Manga manga2 = new Manga(dia2, regata, null, 100);
        Manga manga3 = new Manga(dia3, regata, null, 100);

        List<Posicion> posManga1 = new ArrayList<Posicion>();

        posManga1.add(new Posicion(new Long(3600), Penalizacion.DNC, manga1,
                b1, (long) 0));
        posManga1.add(new Posicion(new Long(3700), Penalizacion.OCS, manga1,
                b2, (long) 0));
        posManga1.add(new Posicion(new Long(3750), Penalizacion.NAN, manga1,
                b3, (long) 0));
        posManga1.add(new Posicion(new Long(3900), Penalizacion.NAN, manga1,
                b4, (long) 0));
        posManga1.add(new Posicion(new Long(3400), Penalizacion.SCP, manga1,
                b5, (long) 0));
        posManga1.add(new Posicion(new Long(2400), Penalizacion.NAN, manga1,
                b6, (long) 0));
        manga1.setPosiciones(posManga1);
        mangaService.cerrarYGuardarManga(manga1);
        regata.addManga(manga1);

        List<Posicion> posManga2 = new ArrayList<Posicion>();

        posManga2.add(new Posicion(new Long(3400), Penalizacion.NAN, manga2,
                b1, (long) 0));
        posManga2.add(new Posicion(new Long(3600), Penalizacion.NAN, manga2,
                b2, (long) 0));
        posManga2.add(new Posicion(new Long(3950), Penalizacion.NAN, manga2,
                b3, (long) 0));
        posManga2.add(new Posicion(new Long(3200), Penalizacion.RDG, manga2,
                b4, (long) 0));
        posManga2.add(new Posicion(new Long(3100), Penalizacion.NAN, manga2,
                b5, (long) 0));
        posManga2.add(new Posicion(new Long(2800), Penalizacion.RET, manga2,
                b6, (long) 0));
        manga2.setPosiciones(posManga2);
        mangaService.cerrarYGuardarManga(manga2);
        regata.addManga(manga2);

        List<Posicion> posManga3 = new ArrayList<Posicion>();

        posManga3.add(new Posicion(new Long(13500), Penalizacion.RDG, manga3,
                b1, (long) 0));
        posManga3.add(new Posicion(new Long(13200), Penalizacion.NAN, manga3,
                b2, (long) 0));
        posManga3.add(new Posicion(new Long(13350), Penalizacion.DGM, manga3,
                b3, (long) 0));
        posManga3.add(new Posicion(new Long(13900), Penalizacion.NAN, manga3,
                b4, (long) 0));
        posManga3.add(new Posicion(new Long(14400), Penalizacion.DNE, manga3,
                b5, (long) 0));
        posManga3.add(new Posicion(new Long(15400), Penalizacion.NAN, manga3,
                b6, (long) 0));
        manga3.setPosiciones(posManga3);
        mangaService.cerrarYGuardarManga(manga3);
        regata.addManga(manga3);

        assertEquals(dia1, regata.getDiaIni());
        assertEquals(dia3, regata.getDiaFin());

        assertEquals(regata.getDiasManga().size(), 2);
        assertEquals(regata.getDiasManga().get(0), dia1);
        assertEquals(regata.getDiasManga().get(1), dia3);
    }
    
    
    @Test
    public void getPuntuacionFinalTest() {

        List<Posicion> posiciones = new ArrayList<Posicion>();

        Tipo tipo = new Tipo("Crucero", "Desc Crucero", false);

        Barco mockBarco = new Barco(201402, "La Perla Negra", tipo,
                new Float(1.5), "X6");

        Posicion posicion;

        //Comprobacion de eliminacion de mayor puntuacion en casos normales
        //(sin penalizaciones ni historias)
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(8);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(9);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(10);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(7);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(20);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(21);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(23);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(22);
        posiciones.add(posicion);

        //TODO Revisar esto xq creo que a conta está mal botada
        assertEquals(75, regataService.getPuntuacionFinal(posiciones));

        posiciones.clear();

        //probamos el caso de que el numero de posiciones no sea multiplo de cuatro
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(8);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(9);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(10);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(7);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(1);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(2);
        posiciones.add(posicion);

        assertEquals(27, regataService.getPuntuacionFinal(posiciones));

        posiciones.clear();

        //probamos el caso de penalizacion insalvable
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(8);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(9);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.DNE, null,
                mockBarco, (long) 0);
        posicion.setPuntos(10);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(7);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(1);
        posiciones.add(posicion);
        posicion = new Posicion(new Long(2800), Posicion.Penalizacion.NAN, null,
                mockBarco, (long) 0);
        posicion.setPuntos(2);
        posiciones.add(posicion);

        assertEquals(28, regataService.getPuntuacionFinal(posiciones));

        posiciones.clear();

    }

    
    @Test
    public void editarInfoRegataTest() throws InstanceNotFoundException {

        Regata regata = regataService.crearRegata("Nombre reg", "Desc reg");

        String nuevoNombre = "new Name";
        String nuevoDesc = "New Desc";
        regata.setNombre(nuevoNombre);
        regata.setDescripcion(nuevoDesc);

        Regata foundRegata = regataService.editarInfoRegata(regata);

        assertEquals(foundRegata.getNombre(), nuevoNombre);
        assertEquals(foundRegata.getDescripcion(), nuevoDesc);
    }

    @Test(expected = InstanceNotFoundException.class)
    public void editarMangaNoExistenteTest() throws InstanceNotFoundException {
        Regata regata = new Regata("Name", "Desc");

        regataService.editarInfoRegata(regata);

    }
}
