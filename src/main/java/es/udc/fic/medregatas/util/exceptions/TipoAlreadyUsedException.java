/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.util.exceptions;

import es.udc.fic.medregatas.model.tipo.Tipo;

/**
 *
 * @author Jesús
 */
public class TipoAlreadyUsedException extends InstanceException {

    public TipoAlreadyUsedException(Object key) {
        super("Tipo ya usado", key, Tipo.class.getName());
    }
}
