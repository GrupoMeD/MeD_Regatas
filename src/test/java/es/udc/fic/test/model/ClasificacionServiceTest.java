/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.test.model;

import es.udc.fic.medregatas.model.barco.Barco;
import es.udc.fic.medregatas.model.manga.Manga;
import es.udc.fic.medregatas.model.posicion.Posicion;
import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.regata.RegataDao;
import es.udc.fic.medregatas.model.service.InscripcionService;
import es.udc.fic.medregatas.model.service.MangaService;
import es.udc.fic.medregatas.model.service.RegataService;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.model.tipo.TipoDao;
import es.udc.fic.medregatas.util.TimeUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
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
public class ClasificacionServiceTest {

    @Autowired
    private TipoDao tipoDao;

    @Autowired
    private RegataDao regataDao;

    @Autowired
    private RegataService regataService;

    @Autowired
    private MangaService mangaService;

    @Autowired
    private InscripcionService inscripcionService;

    public ClasificacionServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void getTiposTest() {
        List<Tipo> tipos = new ArrayList<Tipo>();
        Tipo t1 = new Tipo("Catamarán", "Desc Catamarán", false);
        tipoDao.save(t1);
        Tipo t2 = new Tipo("Crucero", "Desc Crucero", false);
        tipoDao.save(t2);
        Tipo t3 = new Tipo("Vela ligera", "Desc Vela ligera", true);
        tipoDao.save(t3);
        
        tipos.add(t1);
        tipos.add(t2);
        tipos.add(t3);

        List<Tipo> tipos2 = regataService.getTipos();

        assertTrue(tipos2.contains(t1));
        assertTrue(tipos2.contains(t2));
        assertTrue(tipos2.contains(t3));

    }

    @Test
    public void getRegatasTest() {
        //TODO Falta implementacion
    }

    @Test
    public void getClasificacionTest() {

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

        Manga manga1 = new Manga(dia1, regata, null,100);
        Manga manga2 = new Manga(dia2, regata, null,100);
        Manga manga3 = new Manga(dia3, regata, null,100);

        List<Posicion> posManga1 = new ArrayList<Posicion>();

        posManga1.add(new Posicion(new Long(3600), Posicion.Penalizacion.ZFP, manga1,
                b1, (long) 0));
        posManga1.add(new Posicion(new Long(3700), Posicion.Penalizacion.NAN, manga1,
                b2, (long) 0));
        posManga1.add(new Posicion(new Long(3750), Posicion.Penalizacion.ZFP, manga1,
                b3, (long) 0));
        posManga1.add(new Posicion(new Long(3900), Posicion.Penalizacion.NAN, manga1,
                b4, (long) 0));
        posManga1.add(new Posicion(new Long(3400), Posicion.Penalizacion.NAN, manga1,
                b5, (long) 0));
        posManga1.add(new Posicion(new Long(2400), Posicion.Penalizacion.SCP, manga1,
                b6, (long) 0));
        manga1.setPosiciones(posManga1);

        mangaService.cerrarYGuardarManga(manga1);
        regata.addManga(manga1);
        
        List<Posicion> posManga2 = new ArrayList<Posicion>();

        posManga2.add(new Posicion(new Long(3400), Posicion.Penalizacion.ZFP, manga2,
                b1, (long) 0));
        posManga2.add(new Posicion(new Long(3600), Posicion.Penalizacion.NAN, manga2,
                b2, (long) 0));
        posManga2.add(new Posicion(new Long(3950), Posicion.Penalizacion.ZFP, manga2,
                b3, (long) 0));
        posManga2.add(new Posicion(new Long(3200), Posicion.Penalizacion.NAN, manga2,
                b4, (long) 0));
        posManga2.add(new Posicion(new Long(3100), Posicion.Penalizacion.ZFP, manga2,
                b5, (long) 0));
        posManga2.add(new Posicion(new Long(2800), Posicion.Penalizacion.SCP, manga2,
                b6, (long) 0));
        manga2.setPosiciones(posManga2);
        mangaService.cerrarYGuardarManga(manga2);
        regata.addManga(manga2);

        List<Posicion> posManga3 = new ArrayList<Posicion>();

        posManga3.add(new Posicion(new Long(13500), Posicion.Penalizacion.SCP, manga3,
                b1, (long) 0));
        posManga3.add(new Posicion(new Long(13200), Posicion.Penalizacion.NAN, manga3,
                b2, (long) 0));
        posManga3.add(new Posicion(new Long(13350), Posicion.Penalizacion.NAN, manga3,
                b3, (long) 0));
        posManga3.add(new Posicion(new Long(13900), Posicion.Penalizacion.ZFP, manga3,
                b4, (long) 0));
        posManga3.add(new Posicion(new Long(14400), Posicion.Penalizacion.NAN, manga3,
                b5, (long) 0));
        posManga3.add(new Posicion(new Long(15400), Posicion.Penalizacion.SCP, manga3,
                b6, (long) 0));
        manga3.setPosiciones(posManga3);
        mangaService.cerrarYGuardarManga(manga3);
        regata.addManga(manga3);

//        //Mostrar los datos:
//        for(List<Posicion> lp : posicionesGeneralFinal){
//            int total = 0;
//            System.out.print(lp.get(0).getBarco().getVela() + ": ");
//            for(Posicion p : lp){
//                System.out.print(p.getPuntos() + " | ");
//                total += p.getPuntos();
//            }
//            System.out.println("Tot: " + total);
//        }

        //Testeamos la clasificacion Final por Tipo
        Tipo tipo = tipoCrucero;
        List<List<Posicion>> posicionesFinalTipo = regataService.getClasificacion(regata,
                null, tipo);

        //Comprobamos que estén todos los barcos y que no están repetido
        assertEquals(inscripcionService.getInscripcionesByTipo(regata, tipo).size(), posicionesFinalTipo.size());
        
        //Comprobamos que hay tantas posiciones como mangas
        for (List<Posicion> posBarco : posicionesFinalTipo) {
            assertEquals(posBarco.size(), regata.getMangas().size());
        }

        //Comprobamos que todas las posiciones de la misma sublista pertenecen 
        // al mismo Barco.
        for (List<Posicion> posBarco : posicionesFinalTipo) {
            Barco barcoAct = posBarco.get(0).getBarco();
            for (Posicion p : posBarco) {
                assertEquals(barcoAct, p.getBarco());
            }
        }

        Calendar fechaAnterior = null;
        //Comprobamos que las listas internas están ordenadas por la fecha de 
        // la manga
        for (List<Posicion> posBarco : posicionesFinalTipo) {
            for (int i = 0; i < posBarco.size(); i++) {
                Calendar fechaActual = posBarco.get(i).getManga().getFecha();
                if (i > 0) {
                    //Comparamos los calendars:
                    assertTrue(fechaAnterior.before(fechaActual));
                }
                fechaAnterior = fechaActual;
            }
        }

        int puntActual;
        int puntAnterior = 0;
        //Comprobamos que el resultado viene bien ordenado por la puntuacion
        for (List<Posicion> posBarco : posicionesFinalTipo) {
            puntActual = 0;
            for (Posicion p : posBarco) {
                puntActual += p.getPuntos();
            }
            assertTrue(puntActual >= puntAnterior);
            puntAnterior = puntActual;
        }
        
        for (List<Posicion> posBarco : posicionesFinalTipo) {
            for (Posicion posicion : posBarco) {
                assertEquals(tipo, posicion.getBarco().getTipo());
            }
        }

        //Testeamos la clasificacion General por Dia
        Calendar dia = dia2;
        List<List<Posicion>> posicionesDiaGeneral = regataService.getClasificacion(regata,
                dia, null);

        for (List<Posicion> posBarco : posicionesDiaGeneral) {
            for (Posicion posicion : posBarco) {
                assertTrue(TimeUtil.compareByDay(dia, posicion.getManga().getFecha()));
            }
        }

        //Testeamos la clisificacion de un Tipo en un Dia
        Tipo tipoEspecifico = tipoCrucero;
        Calendar diaEspecifico = dia2;
        List<List<Posicion>> posicionesDiaTipo = regataService.getClasificacion(regata,
                diaEspecifico, tipoEspecifico);

        for (List<Posicion> posBarco : posicionesDiaTipo) {
            for (Posicion posicion : posBarco) {
                assertEquals(tipoEspecifico, posicion.getBarco().getTipo());
                assertTrue(TimeUtil.compareByDay(diaEspecifico, posicion.getManga().getFecha()));
            }
        }
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

}
