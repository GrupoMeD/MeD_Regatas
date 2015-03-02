/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.util.exceptions;

/**
 *
 * @author Jesús
 */
public class EmptyTextException extends InstanceException {

    public EmptyTextException(Object key) {
        super("Texto vacio", key, String.valueOf(String.class));
    }
}
