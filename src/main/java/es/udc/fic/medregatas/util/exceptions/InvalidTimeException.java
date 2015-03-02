/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.util.exceptions;

import es.udc.fic.medregatas.model.posicion.Posicion;

/**
 *
 * @author Adrián
 */
public class InvalidTimeException extends InstanceException {
    
        public InvalidTimeException(Object key) {
            super("Tiempo invalido", key, String.valueOf(Posicion.class));
    }
    
}
