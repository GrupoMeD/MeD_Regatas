/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.util.exceptions;

import es.udc.fic.medregatas.model.regata.Regata;

/**
 *
 * @author Adrián
 */
public class InvalidMilesException extends InstanceException {
    
        public InvalidMilesException(Object key) {
            super("Millas invalidas", key, String.valueOf(Regata.class));
    }
    
}
