/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.service;

import es.udc.fic.medregatas.model.manga.Manga;
import es.udc.fic.medregatas.model.posicion.Posicion;
import es.udc.fic.medregatas.model.regata.Regata;
import java.util.List;

/**
 *
 * @author iago
 */
public interface MangaService {

    /**
     * Adjudica a cada Barco su puntuación cuando ya se han registrado en
     * posiciones todos los barcos que hán competido. PreCnd: Se supone que hay
     * una posicion creada para cada barco inscrito a la regata. Si un barco no
     * ha llegado o se le ha penalizado de alguna manera tendra una penalización
     * distinta de NAN y se le dara la puntuacion (barcosregistrados + 1)
     */
    public Manga cerrarYGuardarManga(Manga manga);

    public List<Manga> findAllMangas();

    public List<Manga> findByRegata(Regata regata);
    
    public void borrarManga(Manga manga);

    public void borrarPosicion(Posicion p);
    
    

}
