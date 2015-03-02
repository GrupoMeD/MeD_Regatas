/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.service;

import es.udc.fic.medregatas.model.barco.Barco;
import es.udc.fic.medregatas.model.inscripcion.Inscripcion;
import es.udc.fic.medregatas.model.inscripcion.InscripcionDao;
import es.udc.fic.medregatas.model.manga.Manga;
import es.udc.fic.medregatas.model.posicion.Posicion;
import es.udc.fic.medregatas.model.posicion.PosicionDao;
import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.regata.RegataDao;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.model.tipo.TipoDao;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
@Service("regataService")
public class RegataServiceImpl implements RegataService {

    @Autowired
    private RegataDao regataDao;

    @Autowired
    private TipoDao tipoDao;

    @Autowired
    private PosicionDao posicionDao;

    @Autowired
    private InscripcionDao inscripcionDao;
    
    public RegataServiceImpl() {
    }

    @Override
    public List<Regata> getRegatas() {
        return regataDao.findAll();
    }

    @Override
    public List<Tipo> getTipos() {
        return tipoDao.findAll();
    }

    @Override
    public List<Posicion> getClasificacionManga(Manga manga, Tipo tipo) {
        List<Posicion> poss = new ArrayList<>();

        poss = posicionDao.findByTipoAndManga(tipo.getIdTipo(),
                manga.getIdManga());

        Collections.sort(poss, new BarcosMangaPuntuacionComparator());

        return poss;
    }

    @Override
    public List<List<Posicion>> getClasificacion(Regata regata, final Calendar dia, final Tipo tipo) {

        List<Posicion> posSelecc = new ArrayList<>();

        if (tipo != null) {
            posSelecc = posicionDao
                    .findByTipoAndRegata(regata.getIdRegata(), dia, tipo.getIdTipo());
        }

        //Clasificamos por Barco las Posicion
        Map<Barco, List<Posicion>> posByBarco = new HashMap<Barco, List<Posicion>>();
        for (Posicion posicion : posSelecc) {
            Barco barco = posicion.getBarco();

            if (!posByBarco.containsKey(barco)) {
                posByBarco.put(barco, new ArrayList<Posicion>());
            }

            posByBarco.get(barco).add(posicion);
        }

        //Aplicamos la regla de las 4 mangas
        //eliminando aquella en la que los barcos obtuvieron menor puntuación
        //pasamos el map de listas a lista de listas
        List<List<Posicion>> posicionesBarcoManga
                = new ArrayList<List<Posicion>>(posByBarco.values());

        //Ordenamos la lista de posiciones de cada Barco, por el orden clonológico
        //En el que se obtubieron esas Posiciones, es decir por Mangas
        for (List<Posicion> poss : posicionesBarcoManga) {
            Collections.sort(poss, new MangaTimeComparator());
        }

        //ejecutamos el comparador de listas de posiciones
        //con las posiciones ya ordenadas por fechas sino no vale
        Collections.sort(posicionesBarcoManga, new PosListPointsComparator());

        return posicionesBarcoManga;
    }

    @Override
    public Regata crearRegata(String nombre, String descripcion) {

        Regata regata = new Regata(nombre, descripcion);
        regataDao.save(regata);
        return regata;
    }

    @Override
    public List<Posicion.Penalizacion> getPenalizaciones() {

        return Arrays.asList(Posicion.Penalizacion.values());

    }

    /**
     * Devuelve la puntuación final, descartando la peor manga de cada 4.
     *
     * @param posiciones
     * @return
     */
    @Override
    public int getPuntuacionFinal(List<Posicion> posiciones) {

        //Ordenamos pos puntuacion las poiciones
        List<Posicion> posOrdenadas = new ArrayList<Posicion>(posiciones);
        Collections.sort(posOrdenadas, new Posicion.PosicionesPointComparator());

        //Calculamos cuantos descartes tenemos que hacer
        int descartesPendientes = posOrdenadas.size() / 4;
        List<Posicion> descartes = new ArrayList<Posicion>();

        // Seleccionamos aquellas posiciones que hay que descartar comenzando
        // por atrás.
        for (int i = posOrdenadas.size() - 1; (i >= 0) && (descartesPendientes > 0); i--) {
            if (posOrdenadas.get(i).getPenal().isLowPenal()) {
                descartes.add(posOrdenadas.get(i));
                descartesPendientes--;
            }
        }

        // Contamos los puntos, sin tener en cuenta las posiciones descartadas.
        int puntuacionTot = 0;
        for (Posicion p : posOrdenadas) {
            if (!descartes.contains(p)) {
                puntuacionTot += p.getPuntos();
            }
        }

        return puntuacionTot;
    }

    @Override
    public Regata findRegata(Long idRegata) throws InstanceNotFoundException {
        return regataDao.find(idRegata);
    }

    @Override
    public void borrarRegata(Long idRegata) throws InstanceNotFoundException {

        regataDao.remove(idRegata);

    }

    @Override
    public Tipo añadirTipo(String nombre, String descripcion, boolean compTiempoReal) {

        Tipo tipo = new Tipo(nombre, descripcion, compTiempoReal);
        tipoDao.save(tipo);

        return tipo;
    }

    @Override
    public Tipo updateTipo(Long idTipoViejo, Tipo tipo) {

        try {
            Tipo viejo = tipoDao.find(idTipoViejo);
            viejo.setNombre(tipo.getNombre());
            viejo.setDescripcion(tipo.getDescripcion());
            viejo.setCompiteTmpReal(tipo.getCompiteTmpReal());
            tipoDao.merge(viejo);
        } catch (InstanceNotFoundException ex) {
            Logger.getLogger(RegataServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tipo;
    }

    @Override
    public Regata editarInfoRegata(Regata regata) throws InstanceNotFoundException {

        if (regata.getIdRegata() == null) {
            throw new InstanceNotFoundException(null, regata.getClass().getName());
        }
        regataDao.find(regata.getIdRegata());
        regataDao.merge(regata);
        return regata;
    }

    @Override
    public List<Tipo> getTiposAsociadosByRegata(Regata regata) {
        List<Tipo> result = new ArrayList<Tipo>();
        
        for(Inscripcion i : inscripcionDao.findByRegata(regata.getIdRegata())){
            if(!result.contains(i.getBarco().getTipo())){
                result.add(i.getBarco().getTipo());
            }
        }
        return result;
    }

    /**
     * Compara dos listas de posiciones para ver cual de ellas suma el menor
     * numero de puntos.
     *
     * @precondition: las listas de posiciones deben estar ordenadas y tener la
     * misma longitud.
     *
     */
    class PosListPointsComparator implements Comparator<List<Posicion>> {

        @Override
        public int compare(List<Posicion> poss1, List<Posicion> poss2) {
            Integer puntuacionTot1 = getPuntuacionFinal(poss1);
            Integer puntuacionTot2 = getPuntuacionFinal(poss2);

            return puntuacionTot1.compareTo(puntuacionTot2);
        }

    }

    /**
     * Compara dos Posiciones por el momento en el que tubieron lugar sus Mangas
     */
    class MangaTimeComparator implements Comparator<Posicion> {

        @Override
        public int compare(Posicion pos1, Posicion pos2) {
            return pos1.getManga().getFecha().compareTo(pos2.getManga().getFecha());
        }

    }

    class BarcosMangaPuntuacionComparator implements Comparator<Posicion> {

        @Override
        public int compare(Posicion p1, Posicion p2) {
            return Integer.compare(p1.getPuntos(), p2.getPuntos());
        }
    }

    @Override
    public void removeTipo(Long idTipo) {
        try {
            tipoDao.remove(idTipo);
        } catch (InstanceNotFoundException ex) {
            Logger.getLogger(RegataServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
