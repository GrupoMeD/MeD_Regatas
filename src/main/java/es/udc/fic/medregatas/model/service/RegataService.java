/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.service;

import es.udc.fic.medregatas.model.manga.Manga;
import es.udc.fic.medregatas.model.posicion.Posicion;
import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author iago
 */
public interface RegataService {

    /**
     * Devuelve una lista con las regatas del sistema
     *
     * @return
     */
    public List<Regata> getRegatas();

    /**
     * Devuelve los tipos de Barcos que existen actualmente en el sistema
     *
     * @return
     */
    public List<Tipo> getTipos();

    /**
     * Añade una nueva regata y la devuelve
     *
     * @param nombre
     * @param descripcion
     * @return
     */
    public Regata crearRegata(String nombre, String descripcion);

    /**
     * Permite editar la información asociada a una regata, es decir, el nombre
     * y la descripcion
     *
     * @param regata
     * @return
     * @throws es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException
     */
    public Regata editarInfoRegata(Regata regata) throws InstanceNotFoundException;

    /**
     * Obtiene la puntuacion total dada una lista de posiciones en mangas
     * consecutivas
     *
     * @param posiciones
     * @return
     */
    public int getPuntuacionFinal(List<Posicion> posiciones);

    /**
     * Cierra y añade formalmente la nueva manga a su regata (como ultima manga)
     *
     * @return
     */
    public List<Posicion.Penalizacion> getPenalizaciones();

    /**
     * Devuleve la lista de Posiciones para una manga, asociadas a un tipo
     * concreto de barco, y ordenadas por puntos.
     *
     * @param manga
     * @param tipo
     * @return
     */
    public List<Posicion> getClasificacionManga(Manga manga, Tipo tipo);

    /**
     * Para una regata determinada, devuelve las posiciones de un tipo de Barco
     * en un día determinado. En caso de que alguno de estos parámetros sea
     * null, este parámetro no se tendrá en cuenta.
     *
     * @param regata La regata de la cual queremos obtener la clasificación
     * @param dia El dia del cual queremos obtener la clasificación o null, si
     * deseamos una clasificación final
     * @param tipo El tipo del Barco de que queremos obtener una clasificación o
     * null en caso contrario.
     * @return La lista ordenada por puntuacion de las listas de posiciones en
     * las que han quedado los barcos, y esta lista anterior ordenada también
     * por la fecha de las Mangas.
     */
    public List<List<Posicion>> getClasificacion(Regata regata, Calendar dia, Tipo tipo);

    /**
     * Busca una regata especifica y la devuelve
     *
     * @param idRegata
     * @return la regata a buscar
     * @throws InstanceNotFoundException
     */
    public Regata findRegata(Long idRegata) throws InstanceNotFoundException;

    /**
     * Borra una regata especifica
     *
     * @param idRegata
     * @throws InstanceNotFoundException
     */
    public void borrarRegata(Long idRegata) throws InstanceNotFoundException;

    /**
     * Guarda una nueva clase
     *
     * @param nombre
     * @param descripcion
     * @param compTiempoReal
     * @return
     */
    public Tipo añadirTipo(String nombre, String descripcion, boolean compTiempoReal);

    /**
     * Actualiza una clase dada
     *
     * @param tipo
     * @return
     */
    public Tipo updateTipo(Long idTipoViejo, Tipo tipo);

    /**
     * Borra un tipo de BD
     *
     * @param idTipo
     */
    public void removeTipo(Long idTipo);

    /**
     * Devuelve los tipos asociados a una regata
     * 
     * @param regata
     * @return 
     */
    public List<Tipo> getTiposAsociadosByRegata(Regata regata);
}
