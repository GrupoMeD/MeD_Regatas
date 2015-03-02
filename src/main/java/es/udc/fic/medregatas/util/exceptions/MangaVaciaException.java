/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.util.exceptions;

import es.udc.fic.medregatas.model.manga.Manga;

/**
 *
 * @author Jesús
 */
public class MangaVaciaException extends InstanceException {

    public MangaVaciaException(Object key) {
        super("Manga vacia", key, String.valueOf(Manga.class));
    }

}
