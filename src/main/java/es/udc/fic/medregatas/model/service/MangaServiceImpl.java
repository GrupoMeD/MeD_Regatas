/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.service;

import es.udc.fic.medregatas.model.inscripcion.Inscripcion;
import es.udc.fic.medregatas.model.manga.Manga;
import es.udc.fic.medregatas.model.manga.MangaDao;
import es.udc.fic.medregatas.model.posicion.Posicion;
import es.udc.fic.medregatas.model.posicion.PosicionDao;
import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author iago
 */
@Transactional
@Service("mangaService")
public class MangaServiceImpl implements MangaService {

    @Autowired
    private MangaDao mangaDao;

    @Autowired
    private PosicionDao posicionDao;

    @Autowired
    private InscripcionService inscripcionService;

    @Override
    public Manga cerrarYGuardarManga(Manga manga) {

        // Si la manga no está persistida
        if (manga.getIdManga() == null) {
            mangaDao.save(manga);
        } else {
            mangaDao.merge(manga);
        }
        //Si las posiciones no están persistidas
        for (Posicion p : manga.getPosiciones()) {
            if (p.getIdPosicion() == null) {
                posicionDao.save(p);
            } else {
                posicionDao.merge(p);
            }
        }
        /**
         * Antes de empezar a comparar puntos obtenemos los barcos registrados
         * en la regata y los comparamos con los que han participado en la
         * manga, aquellos que esten registrados y no sean anotados se les
         * aplicara la penalización DNC
         */
        // añadimos todos los que no acabaron a posiciones de la manga
        for (Inscripcion i : inscripcionService.getInscripciones(manga.getRegata())) {

            boolean finish = false;
            for (Posicion p : manga.getPosiciones()) {
                if (p.getBarco().getVela() == i.getBarco().getVela()) {
                    finish = true;
                }
            }

            if (!finish) {
                // TODO Con esto se añade a la lista de manga.getPosiciones() o
                // tenemos que hacer manualmente el add?
                Posicion p = new Posicion(new Long(0), Posicion.Penalizacion.DNC,
                        manga, i.getBarco(), new Long(0));
                        
                posicionDao.save(p);
                
                manga.getPosiciones().add(p);

            }

        }

        //Guardamos las posiciones por tipos
        Map<Tipo, List<Posicion>> posPorTipo = new HashMap<Tipo, List<Posicion>>();

        for (Posicion p : manga.getPosiciones()) {
            Tipo tipoActual = p.getBarco().getTipo();
            //En caso de que no haya una entrada para ese tipo la creamos
            if (!posPorTipo.containsKey(tipoActual)) {
                posPorTipo.put(tipoActual, new ArrayList<Posicion>());
            }

            //Añadimos la posicionActual
            posPorTipo.get(tipoActual).add(p);

        }
        //Para cada tipo, le asignamos puntuaciones.
        for (Tipo tipo : posPorTipo.keySet()) {

            List<Posicion> posDelTipo = posPorTipo.get(tipo);

            //Asignamos puntos por orden de llegada
            Collections.sort(posDelTipo, new Posicion.PosicionesTimeComparator());
            //Asignamos los puntos comenzando en 1
            for (int i = 1; i <= posDelTipo.size(); i++) {
                Posicion posAPuntuar = posDelTipo.get(i - 1);
                //El barco ha llegado a la meta

                if (posAPuntuar.getPenal().isMaxPointsPenal()) {
                    //TODO Es necesario usar el dao para hacer el save??
                    posAPuntuar.setPuntos(posDelTipo.size() + 1);
                    // En caso de que un barco no alcanze la meta, será penalizado
                    // con tantos puntos como participantes +1
                } else {
                    //TODO Es necesario usar el dao para hacer el save??
                    posAPuntuar.setPuntos(i);

                }
            }
        }
        //TODO esto está ok, o deberiamos de actualizar de algun modo la manga?
        return manga;
    }

    @Override
    public List<Manga> findAllMangas() {
        return mangaDao.findAll();
    }

    @Override
    public void borrarManga(Manga manga) {
        try {
            mangaDao.remove(manga.getIdManga());
        } catch (InstanceNotFoundException ex) {
            Logger.getLogger(MangaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void borrarPosicion(Posicion p) {
        try {
            posicionDao.remove(p.getIdPosicion());
        } catch (InstanceNotFoundException ex) {
            Logger.getLogger(MangaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Manga> findByRegata(Regata regata) {
        
        return mangaDao.findByRegata(regata.getIdRegata());
        
  
        
        
    }
}
