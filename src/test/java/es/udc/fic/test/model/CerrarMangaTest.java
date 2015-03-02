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
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.model.tipo.TipoDao;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
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
public class CerrarMangaTest {

    @Autowired
    private TipoDao tipoDao;

    @Autowired
    private RegataDao regataDao;

    @Autowired
    private InscripcionService inscripcionService;

    @Autowired
    private MangaService mangaService;

    Barco b1, b2, b3, b4, b5, b6;
    Manga manga1, manga2;
    Regata regata;
    Map<Barco, Posicion> posPorBarco;

    @Test
    public void cerrarMangaTRealSinPenal() {
        //Creamos una sola regata con la instancia de todos los objetos en memoria
        Regata regata = new Regata();
        regata.setNombre("Mock Regata");
        regata.setDescripcion("Mock Desc");
        regataDao.save(regata);

        Tipo tipoVLigera = new Tipo("Vela ligera", "Desc Vela ligera", true);
        tipoDao.save(tipoVLigera);
        Tipo tipoLanchas = new Tipo("Lanchas", "Desc Lanchas", true);
        tipoDao.save(tipoLanchas);

        Barco b1 = new Barco(204566, "Juan Sebastian El Cano", tipoVLigera,
                null, "Lagoon 421");
        inscripcionService.inscribir(regata, b1, "Iago Suárez");

        Barco b2 = new Barco(199012, "El Holandes Errante", tipoVLigera,
                null, "SWAN 66 FD");
        inscripcionService.inscribir(regata, b2, "Samu Paredes");

        Barco b3 = new Barco(201402, "La Perla Negra", tipoVLigera,
                null, "X6");
        inscripcionService.inscribir(regata, b3, "Adrian Pallas");

        //Ponemos otro tipo para ver como funciona la clasificacion
        Barco b4 = new Barco(206745, "Apolo", tipoLanchas, null, "Laser Radial");
        inscripcionService.inscribir(regata, b4,
                "Diego Bascoy");

        Calendar dia1 = Calendar.getInstance();
        dia1.add(Calendar.DAY_OF_YEAR, -18);

        Calendar dia2 = Calendar.getInstance();
        dia2.add(Calendar.DAY_OF_YEAR, -18);
        dia2.add(Calendar.HOUR, 2);

        Calendar dia3 = Calendar.getInstance();
        dia3.add(Calendar.DAY_OF_YEAR, -17);

        Manga manga1 = new Manga(dia1, regata, null, 100);

        List<Posicion> posManga1 = new ArrayList<Posicion>();

        //Velas Ligeras
        //Primero -> Puntos 1        
        posManga1.add(new Posicion((long) 3200, Posicion.Penalizacion.NAN, manga1,
                b1, (long) 0));
        //Segundo-> Puntos 2
        posManga1.add(new Posicion((long) 3300, Posicion.Penalizacion.NAN, manga1,
                b2, (long) 0));
        //Tercero -> Puntos 3
        posManga1.add(new Posicion((long) 3400, Posicion.Penalizacion.NAN, manga1,
                b3, (long) 0));

        //Lanchas
        //Primero -> Puntos 1
        posManga1.add(new Posicion((long) 3300, Posicion.Penalizacion.NAN, manga1,
                b4, (long) 0));

        manga1.setPosiciones(posManga1);
        regata.addManga(manga1);
        mangaService.cerrarYGuardarManga(manga1);

        //Comprobamos que todas las posiciones tienen puntos mayores que 0
        for (Posicion p : manga1.getPosiciones()) {
            assertTrue(p.getPuntos() > 0);
        }

        //Comprobamos que los puntos son correctos
        //Guardamos las posiciones por tipos
        Map<Barco, Posicion> posPorBarco = new HashMap<Barco, Posicion>();
        for (Posicion p : manga1.getPosiciones()) {
            //Añadimos la posicionActual
            posPorBarco.put(p.getBarco(), p);
        }

        //Velas Ligeras
        //Primero -> Puntos 1 
        assertEquals(posPorBarco.get(b1).getPuntos(), 1);
        //Segundo-> Puntos 2
        assertEquals(posPorBarco.get(b2).getPuntos(), 2);
        //Tercero -> Puntos 3
        assertEquals(posPorBarco.get(b3).getPuntos(), 3);

        //Lanchas
        //Primero -> Puntos 1
        assertEquals(posPorBarco.get(b4).getPuntos(), 1);

    }

    @Test
    public void cerrarMangaTCompensadoSinPenal() {
        //Creamos una sola regata con la instancia de todos los objetos en memoria
        Regata regata = new Regata();
        regata.setNombre("Mock Regata");
        regata.setDescripcion("Mock Desc");
        regataDao.save(regata);

        Tipo tipoCrucero = new Tipo("Crucero", "Desc Crucero", false);
        tipoDao.save(tipoCrucero);
        Tipo tipoLanchas = new Tipo("Lanchas", "Desc Lanchas", true);
        tipoDao.save(tipoLanchas);

        Barco b1 = new Barco(204566, "Juan Sebastian El Cano", tipoCrucero,
                (float) 1.5, "Lagoon 421");
        inscripcionService.inscribir(regata, b1, "Iago Suárez");

        Barco b2 = new Barco(199012, "El Holandes Errante", tipoCrucero,
                (float) 2.5, "SWAN 66 FD");
        inscripcionService.inscribir(regata, b2, "Samu Paredes");

        Barco b3 = new Barco(201402, "La Perla Negra", tipoCrucero,
                (float) 1.5, "X6");
        inscripcionService.inscribir(regata, b3, "Adrian Pallas");
        //Ponemos otro tipo para ver como funciona la clasificacion
        Barco b4 = new Barco(206745, "Apolo", tipoLanchas, null, "Laser Radial");
        inscripcionService.inscribir(regata, b4,
                "Diego Bascoy");

        Calendar dia1 = Calendar.getInstance();
        dia1.add(Calendar.DAY_OF_YEAR, -18);

        Calendar dia2 = Calendar.getInstance();
        dia2.add(Calendar.DAY_OF_YEAR, -18);
        dia2.add(Calendar.HOUR, 2);

        Calendar dia3 = Calendar.getInstance();
        dia3.add(Calendar.DAY_OF_YEAR, -17);

        Manga manga1 = new Manga(dia1, regata, null, 100);

        List<Posicion> posManga1 = new ArrayList<Posicion>();

        //Velas Ligeras
        //Tiempo -> 3200 + 1.5 * 100  = 3350 -> Primero   
        posManga1.add(new Posicion((long) 3200, Posicion.Penalizacion.NAN, manga1,
                b1, (long) 0));
        //Tiempo-> 3300 + 2.5 * 100 = 3550 -> Segundo
        posManga1.add(new Posicion((long) 3300, Posicion.Penalizacion.NAN, manga1,
                b2, (long) 0));
        //Tiempo -> 3500 + 1.5 * 100 = 3650 -> Tercero
        posManga1.add(new Posicion((long) 3500, Posicion.Penalizacion.NAN, manga1,
                b3, (long) 0));

        //Lanchas
        //Primero -> Puntos 1
        posManga1.add(new Posicion((long) 3300, Posicion.Penalizacion.NAN, manga1,
                b4, (long) 0));

        manga1.setPosiciones(posManga1);
        mangaService.cerrarYGuardarManga(manga1);
        regata.addManga(manga1);

        //Comprobamos que todas las posiciones tienen puntos mayores que 0
        for (Posicion p : manga1.getPosiciones()) {
            assertTrue(p.getPuntos() > 0);
        }

        //Comprobamos que los puntos son correctos
        //Guardamos las posiciones por tipos
        Map<Barco, Posicion> posPorBarco = new HashMap<Barco, Posicion>();
        for (Posicion p : manga1.getPosiciones()) {
            //Añadimos la posicionActual
            posPorBarco.put(p.getBarco(), p);
        }

        //Velas Ligeras
        //Primero -> Puntos 1 
        assertEquals(posPorBarco.get(b1).getPuntos(), 1);
        //Segundo-> Puntos 2
        assertEquals(posPorBarco.get(b2).getPuntos(), 2);
        //Tercero -> Puntos 3
        assertEquals(posPorBarco.get(b3).getPuntos(), 3);

        //Lanchas
        //Primero -> Puntos 1
        assertEquals(posPorBarco.get(b4).getPuntos(), 1);

    }

    @Test
    public void cerrarMangaTRealPenalizado() {
        //Creamos una sola regata con la instancia de todos los objetos en memoria
        Regata regata = new Regata();
        regata.setNombre("Mock Regata");
        regata.setDescripcion("Mock Desc");
        regataDao.save(regata);

        Tipo tipoVLigera = new Tipo("Vela ligera", "Desc Vela ligera", true);
        tipoDao.save(tipoVLigera);
        Tipo tipoLanchas = new Tipo("Lanchas", "Desc Lanchas", true);
        tipoDao.save(tipoLanchas);

        Barco b1 = new Barco(204566, "Juan Sebastian El Cano", tipoVLigera,
                null, "Laser Standar");
        inscripcionService.inscribir(regata, b1, "Iago Suárez");

        Barco b2 = new Barco(199012, "El Holandes Errante", tipoVLigera,
                null, "Laser Radial");
        inscripcionService.inscribir(regata, b2, "Samu Paredes");
        Barco b3 = new Barco(201402, "La Perla Negra", tipoVLigera,
                null, "Laser 4.7");
        inscripcionService.inscribir(regata, b3, "Adrian Pallas");
        Barco b4 = new Barco(202102, "La Pinta", tipoVLigera,
                null, "Laser 4.7");
        inscripcionService.inscribir(regata, b4, "Pedro Cabalar");
        Barco b5 = new Barco(182345, "Venus", tipoVLigera,
                null, "Laser Standar");
        //Ponemos otro tipo para ver como funciona la clasificacion
        inscripcionService.inscribir(regata, b5, "Jesus Lopez");
        Barco b6 = new Barco(206745, "Apolo", tipoLanchas, null, "Motora");
        inscripcionService.inscribir(regata, b6,
                "Diego Bascoy");

        Calendar dia1 = Calendar.getInstance();
        dia1.add(Calendar.DAY_OF_YEAR, -18);

        Calendar dia2 = Calendar.getInstance();
        dia2.add(Calendar.DAY_OF_YEAR, -18);
        dia2.add(Calendar.HOUR, 2);

        Calendar dia3 = Calendar.getInstance();
        dia3.add(Calendar.DAY_OF_YEAR, -17);

        // COMPROBAMOS LAS DESCALIFICACIONES QUE NO ACARREAN EL MAX DE PUNT.
        Manga manga1 = new Manga(dia1, regata, null, 100);

        List<Posicion> posManga1 = new ArrayList<Posicion>();

        //NAN -> 0 penal
        //ZFP -> 20% penal
        //SCP RDG DPI -> Penal en tiempo
        //Velas Ligeras
        // ZFP 3600s -> Tercero
        posManga1.add(new Posicion((long) 3000, Posicion.Penalizacion.ZFP, manga1,
                b1, (long) 0));
        //SCP 3450s -> Segundo
        posManga1.add(new Posicion((long) 3300, Posicion.Penalizacion.SCP, manga1,
                b2, (long) 150));
        //NAN 3400 -> Primero
        posManga1.add(new Posicion((long) 3400, Posicion.Penalizacion.NAN, manga1,
                b3, (long) 0));
        //RDG 3750 -> Cuarto
        posManga1.add(new Posicion((long) 3750, Posicion.Penalizacion.RDG, manga1,
                b4, (long) 0));
        //DPI 3860 -> Ultimo
        posManga1.add(new Posicion((long) 3800, Posicion.Penalizacion.DPI, manga1,
                b5, (long) 60));
        //Lanchas
        //Primero -> Puntos 1
        posManga1.add(new Posicion((long) 3300, Posicion.Penalizacion.ZFP, manga1,
                b6, (long) 0));

        manga1.setPosiciones(posManga1);
        mangaService.cerrarYGuardarManga(manga1);
        regata.addManga(manga1);

        //Comprobamos que todas las posiciones tienen puntos mayores que 0
        for (Posicion p : manga1.getPosiciones()) {
            assertTrue(p.getPuntos() > 0);
        }

        //Comprobamos que los puntos son correctos
        //Guardamos las posiciones por tipos
        Map<Barco, Posicion> posPorBarco = new HashMap<Barco, Posicion>();
        for (Posicion p : manga1.getPosiciones()) {
            //Añadimos la posicionActual
            posPorBarco.put(p.getBarco(), p);
        }

        //Velas Ligeras
        // ZFP 3600s -> Tercero
        assertEquals(posPorBarco.get(b1).getPuntos(), 3);
        //SCP 3450s -> Segundo
        assertEquals(posPorBarco.get(b2).getPuntos(), 2);
        //NAN 3400 -> Primero 
        assertEquals(posPorBarco.get(b3).getPuntos(), 1);
        //RDG 3750 -> Cuarto
        assertEquals(posPorBarco.get(b4).getPuntos(), 4);
        //DPI 3860 -> Ultimo
        assertEquals(posPorBarco.get(b5).getPuntos(), 5);
        //Lanchas
        //Primero
        assertEquals(posPorBarco.get(b6).getPuntos(), 1);

        // COMPROBAMOS LAS DESCALIFICACIONES QUE ACARREAN EL MAX DE PUNT.
        Manga manga2 = new Manga(dia2, regata, null, 100);

        List<Posicion> posManga2 = new ArrayList<Posicion>();

        //DNC DNS -> No Salio                
        // DNF RET DSQ -> No termino / Retirado / Descalificado 
        // DNE DGM -> Descalificaciones Graves
        // BFD -> Bandera negra
        //Velas Ligeras
        // ZFP 3600s -> Primero (unico el llegar)
        posManga2.add(new Posicion((long) 3000, Posicion.Penalizacion.ZFP, manga1,
                b1, (long) 0));
        //DNF No termino -> Ultimo -> 6 Puntos
        posManga2.add(new Posicion((long) 3300, Posicion.Penalizacion.DNF, manga1,
                b2, null));
        //DNE Descalificado -> Ultimo -> 6 Puntos
        posManga2.add(new Posicion((long) 3400, Posicion.Penalizacion.DNE, manga1,
                b3, null));
        //DNE Descalificado -> Ultimo -> 6 Puntos
        posManga2.add(new Posicion((long) 3750, Posicion.Penalizacion.DNE, manga1,
                b4, null));
        //DGM Descalificado -> Ultimo -> 6 Puntos
        posManga2.add(new Posicion((long) 3800, Posicion.Penalizacion.DGM, manga1,
                b5, null));
        //Lanchas
        //RET Retirado -> Puntos 2
        posManga2.add(new Posicion((long) 3300, Posicion.Penalizacion.RET, manga1,
                b6, null));

        manga2.setPosiciones(posManga2);
        mangaService.cerrarYGuardarManga(manga2);
        regata.addManga(manga2);

        //Comprobamos que todas las posiciones tienen puntos mayores que 0
        for (Posicion p : manga2.getPosiciones()) {
            assertTrue(p.getPuntos() > 0);
        }

        //Comprobamos que los puntos son correctos
        //Guardamos las posiciones por tipos
        Map<Barco, Posicion> posPorBarco2 = new HashMap<Barco, Posicion>();
        for (Posicion p : manga2.getPosiciones()) {
            //Añadimos la posicionActual
            posPorBarco2.put(p.getBarco(), p);
        }

        //ZFP 3600s -> Primero (unico el llegar)
        assertEquals(posPorBarco2.get(b1).getPuntos(), 1);
        //DNF No termino -> Ultimo -> 5 Puntos
        assertEquals(posPorBarco2.get(b2).getPuntos(), 6);
        //DNE Descalificado -> Ultimo -> 5 Puntos
        assertEquals(posPorBarco2.get(b3).getPuntos(), 6);
        //BFD Bandera Negra -> Ultimo -> 5 Puntos
        assertEquals(posPorBarco2.get(b4).getPuntos(), 6);
        //DGM Descalificado -> Ultimo -> 5 Puntos
        assertEquals(posPorBarco2.get(b5).getPuntos(), 6);
        //Lanchas
        //Primero
        assertEquals(posPorBarco2.get(b6).getPuntos(), 2);
    }

    @Test
    public void cerrarMangaSiTodasPosiciones() {

        //Creamos una sola regata con la instancia de todos los objetos en memoria
        regata = new Regata();
        regata.setNombre("Mock Regata");
        regata.setDescripcion("Mock Desc");
        regataDao.save(regata);

        Tipo tipoCruceros = new Tipo("Cruceros", "Desc Cruceros", false);
        tipoDao.save(tipoCruceros);
        Tipo tipoLanchas = new Tipo("Lanchas", "Desc Lanchas", false);
        tipoDao.save(tipoLanchas);

        b1 = new Barco(204566, "Juan Sebastian El Cano", tipoCruceros,
                (float) 1.5, "X6");
        inscripcionService.inscribir(regata, b1, "Iago Suárez");

        b2 = new Barco(199012, "El Holandes Errante", tipoCruceros,
                (float) 1.8, "X2");
        inscripcionService.inscribir(regata, b2, "Samu Paredes");
        b3 = new Barco(201402, "La Perla Negra", tipoCruceros,
                (float) 1.0, "X8");
        inscripcionService.inscribir(regata, b3, "Adrian Pallas");
        b4 = new Barco(202102, "La Pinta", tipoCruceros,
                (float) 2.0, "X7");
        inscripcionService.inscribir(regata, b4, "Pedro Cabalar");
        b5 = new Barco(182345, "Venus", tipoCruceros,
                (float) 1.5, "X7");
        //Ponemos otro tipo para ver como funciona la clasificacion
        inscripcionService.inscribir(regata, b5, "Jesus Lopez");
        b6 = new Barco(206745, "Apolo", tipoLanchas, null, "Motora");
        inscripcionService.inscribir(regata, b6,
                "Diego Bascoy");

        Calendar dia1 = Calendar.getInstance();
        dia1.add(Calendar.DAY_OF_YEAR, -18);

        // COMPROBAMOS LAS DESCALIFICACIONES QUE NO ACARREAN EL MAX DE PUNT.
        manga1 = new Manga(dia1, regata, null, 100);

        List<Posicion> posManga1 = new ArrayList<Posicion>();

        //NAN -> 0 penal
        //ZFP -> 20% penal
        //SCP RDG DPI -> Penal en tiempo
        //Cruceros
        // ZFP (3000 + 1.5* 100 )* 1.20 = 3780s -> Tercero
        posManga1.add(new Posicion((long) 3000, Posicion.Penalizacion.ZFP, manga1,
                b1, null));
        //SCP (3300 + 1.5 * 100) + 150s  = 3600s -> Segundo
        posManga1.add(new Posicion((long) 3300, Posicion.Penalizacion.SCP, manga1,
                b2, (long) 150));
        //NAN (3400 + 1.0 * 100)         = 3500s -> Primero
        posManga1.add(new Posicion((long) 3400, Posicion.Penalizacion.NAN, manga1,
                b3, (long) 0));

        // b4 DNC
        // b5 DNC
        
        //Lanchas
        //Primero -> Puntos 1
        posManga1.add(new Posicion((long) 3300, Posicion.Penalizacion.ZFP, manga1,
                b6, (long) 0));

        manga1.setPosiciones(posManga1);
        mangaService.cerrarYGuardarManga(manga1);
        regata.addManga(manga1);

        //Comprobamos que se han creado todas las posiciones
        assertEquals(6, manga1.getPosiciones().size());

        
        
        
        
        assertEquals(posManga1.get(0).getPenal(), Posicion.Penalizacion.ZFP);
        assertEquals(posManga1.get(0).getPuntos(), 3);

        assertEquals(posManga1.get(1).getPenal(), Posicion.Penalizacion.SCP);
        assertEquals(posManga1.get(1).getPuntos(), 2);

        assertEquals(posManga1.get(2).getPenal(), Posicion.Penalizacion.NAN);
        assertEquals(posManga1.get(2).getPuntos(), 1);

        boolean estab4 = false;
        
        for(Posicion p : manga1.getPosiciones()){
            if((p.getBarco() == b4) && 
                    (p.getPenal() == Posicion.Penalizacion.DNC) &&
                    (p.getPuntos() == 6)){
                estab4 = true;
            }
        }
        assertTrue(estab4);
        
        boolean estab5 = false;
        
        for(Posicion p : manga1.getPosiciones()){
            if((p.getBarco() == b5) && 
                    (p.getPenal() == Posicion.Penalizacion.DNC)&&
                    (p.getPuntos() == 6)){
                estab5 = true;
            }
        }
        assertTrue(estab5);
        
        assertEquals(posManga1.get(3).getPenal(), Posicion.Penalizacion.ZFP);
        assertEquals(posManga1.get(3).getPuntos(), 1);
    }

    public void addData() {

        //Creamos una sola regata con la instancia de todos los objetos en memoria
        regata = new Regata();
        regata.setNombre("Mock Regata");
        regata.setDescripcion("Mock Desc");
        regataDao.save(regata);

        Tipo tipoCruceros = new Tipo("Cruceros", "Desc Cruceros", false);
        tipoDao.save(tipoCruceros);
        Tipo tipoLanchas = new Tipo("Lanchas", "Desc Lanchas", false);
        tipoDao.save(tipoLanchas);

        b1 = new Barco(204566, "Juan Sebastian El Cano", tipoCruceros,
                (float) 1.5, "X6");
        inscripcionService.inscribir(regata, b1, "Iago Suárez");

        b2 = new Barco(199012, "El Holandes Errante", tipoCruceros,
                (float) 1.8, "X2");
        inscripcionService.inscribir(regata, b2, "Samu Paredes");
        b3 = new Barco(201402, "La Perla Negra", tipoCruceros,
                (float) 1.0, "X8");
        inscripcionService.inscribir(regata, b3, "Adrian Pallas");
        b4 = new Barco(202102, "La Pinta", tipoCruceros,
                (float) 2.0, "X7");
        inscripcionService.inscribir(regata, b4, "Pedro Cabalar");
        b5 = new Barco(182345, "Venus", tipoCruceros,
                (float) 1.5, "X7");
        //Ponemos otro tipo para ver como funciona la clasificacion
        inscripcionService.inscribir(regata, b5, "Jesus Lopez");
        b6 = new Barco(206745, "Apolo", tipoLanchas, null, "Motora");
        inscripcionService.inscribir(regata, b6,
                "Diego Bascoy");

        Calendar dia1 = Calendar.getInstance();
        dia1.add(Calendar.DAY_OF_YEAR, -18);

        Calendar dia2 = Calendar.getInstance();
        dia2.add(Calendar.DAY_OF_YEAR, -18);
        dia2.add(Calendar.HOUR, 2);

        Calendar dia3 = Calendar.getInstance();
        dia3.add(Calendar.DAY_OF_YEAR, -17);

        // COMPROBAMOS LAS DESCALIFICACIONES QUE NO ACARREAN EL MAX DE PUNT.
        manga1 = new Manga(dia1, regata, null, 100);

        List<Posicion> posManga1 = new ArrayList<Posicion>();

        //NAN -> 0 penal
        //ZFP -> 20% penal
        //SCP RDG DPI -> Penal en tiempo
        //Cruceros
        // ZFP (3000 + 1.5* 100 )* 1.20 = 3780s -> Tercero
        posManga1.add(new Posicion((long) 3000, Posicion.Penalizacion.ZFP, manga1,
                b1, null));
        //SCP (3300 + 1.5 * 100) + 150s  = 3600s -> Segundo
        posManga1.add(new Posicion((long) 3300, Posicion.Penalizacion.SCP, manga1,
                b2, (long) 150));
        //NAN (3400 + 1.0 * 100)         = 3500s -> Primero
        posManga1.add(new Posicion((long) 3400, Posicion.Penalizacion.NAN, manga1,
                b3, (long) 0));
        //RDG (3750 + 2.0 *100 )        = 3950s -> Cuarto
        posManga1.add(new Posicion((long) 3750, Posicion.Penalizacion.RDG, manga1,
                b4, (long) 0));
        //DPI (3800+60) +(1.5 *100)      = 4010s -> Quinto
        posManga1.add(new Posicion((long) 3800, Posicion.Penalizacion.DPI, manga1,
                b5, (long) 60));
        //Lanchas
        //Primero -> Puntos 1
        posManga1.add(new Posicion((long) 3300, Posicion.Penalizacion.ZFP, manga1,
                b6, (long) 0));

        manga1.setPosiciones(posManga1);
        mangaService.cerrarYGuardarManga(manga1);
        regata.addManga(manga1);

        //Comprobamos que los puntos son correctos
        //Guardamos las posiciones por tipos
        posPorBarco = new HashMap<Barco, Posicion>();
        for (Posicion p : manga1.getPosiciones()) {
            //Añadimos la posicionActual
            posPorBarco.put(p.getBarco(), p);
        }

        // COMPROBAMOS LAS DESCALIFICACIONES QUE ACARREAN EL MAX DE PUNT.
        manga2 = new Manga(dia2, regata, null, 100);

        List<Posicion> posManga2 = new ArrayList<Posicion>();

        //DNC DNS -> No Salio                
        // DNF RET DSQ -> No termino / Retirado / Descalificado 
        // DNE DGM -> Descalificaciones Graves
        // BFD -> Bandera negra
        //Velas Ligeras
        // ZFP 3600s -> Primero (unico el llegar)
        posManga2.add(new Posicion((long) 3000, Posicion.Penalizacion.ZFP, manga1,
                b1, (long) 0));
        //DNF No termino -> Ultimo -> 6 Puntos
        posManga2.add(new Posicion((long) 3300, Posicion.Penalizacion.DNF, manga1,
                b2, null));
        //DNE Descalificado -> Ultimo -> 6 Puntos
        posManga2.add(new Posicion((long) 3400, Posicion.Penalizacion.DNE, manga1,
                b3, null));
        //DNE Descalificado -> Ultimo -> 6 Puntos
        posManga2.add(new Posicion((long) 3750, Posicion.Penalizacion.DNE, manga1,
                b4, null));
        //DGM Descalificado -> Ultimo -> 6 Puntos
        posManga2.add(new Posicion((long) 3800, Posicion.Penalizacion.DGM, manga1,
                b5, null));
        //Lanchas
        //RET Retirado -> Puntos 2
        posManga2.add(new Posicion((long) 3300, Posicion.Penalizacion.RET, manga1,
                b6, null));

        manga2.setPosiciones(posManga2);
        mangaService.cerrarYGuardarManga(manga2);
        regata.addManga(manga2);

    }

    //TODO Iago
    @Test
    public void cerrarMangaTCompensadoConPenal() {

        addData();

        //Comprobamos que todas las posiciones tienen puntos mayores que 0
        for (Posicion p : manga1.getPosiciones()) {
            assertTrue(p.getPuntos() > 0);
        }

        //LOS BARCOS COMPITEN EN TIEMPO REAL,POR TANTO SE OMITE EL TRATING
        // ZFP (3000 * 1.5)* 1.20 = 5400s -> Segundo
        assertEquals(posPorBarco.get(b1).getPuntos(), 3);
        //(3300 * 1.8) + 150s  = 6090s -> Cuarto
        assertEquals(posPorBarco.get(b2).getPuntos(), 2);
        //NAN 3400 +0       = 3400s -> Primero
        assertEquals(posPorBarco.get(b3).getPuntos(), 1);
        //RDG (3750 * 2.0 )        = 7500s -> Quinto
        assertEquals(posPorBarco.get(b4).getPuntos(), 4);
        //DPI (3800+60) * 1.5      = 5790s -> Tercero
        assertEquals(posPorBarco.get(b5).getPuntos(), 5);
        //Primero -> Puntos 1
        assertEquals(posPorBarco.get(b6).getPuntos(), 1);

        //Comprobamos que todas las posiciones tienen puntos mayores que 0
        for (Posicion p : manga2.getPosiciones()) {
            assertTrue(p.getPuntos() > 0);
        }

        //Comprobamos que los puntos son correctos
        //Guardamos las posiciones por tipos
        Map<Barco, Posicion> posPorBarco2 = new HashMap<Barco, Posicion>();
        for (Posicion p : manga2.getPosiciones()) {
            //Añadimos la posicionActual
            posPorBarco2.put(p.getBarco(), p);
        }

        //ZFP 3600s -> Primero (unico el llegar)
        assertEquals(posPorBarco2.get(b1).getPuntos(), 1);
        //DNF No termino -> Ultimo -> 5 Puntos
        assertEquals(posPorBarco2.get(b2).getPuntos(), 6);
        //DNE Descalificado -> Ultimo -> 5 Puntos
        assertEquals(posPorBarco2.get(b3).getPuntos(), 6);
        //BFD Bandera Negra -> Ultimo -> 5 Puntos
        assertEquals(posPorBarco2.get(b4).getPuntos(), 6);
        //DGM Descalificado -> Ultimo -> 5 Puntos
        assertEquals(posPorBarco2.get(b5).getPuntos(), 6);
        //Lanchas
        //Primero
        assertEquals(posPorBarco2.get(b6).getPuntos(), 2);
    }

    @Test
    public void findAllMangasTest() {

        addData();

        List<Manga> mangas = mangaService.findAllMangas();

        assertTrue(mangas.contains(manga2));

        assertTrue(mangas.contains(manga1));

    }

    @Test
    public void borrarPosicionTest() {

        addData();

        List<Manga> mangas = mangaService.findAllMangas();

        assertTrue(mangas.contains(manga2));

        assertTrue(mangas.contains(manga1));

    }

    @Test
    public void borrarMangaTest() {

        addData();

        List<Manga> mangas = mangaService.findAllMangas();

        Manga manga = mangas.get(0);

        mangaService.borrarManga(manga);

        mangas = mangaService.findAllMangas();

        assertTrue(!mangas.contains(manga));

    }

}
