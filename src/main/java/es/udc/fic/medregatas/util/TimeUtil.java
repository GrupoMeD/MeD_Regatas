/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.util;

import java.util.Calendar;

/**
 *
 * @author iago
 */
public class TimeUtil {

    /**
     * Devuelve true si ambos calendar tienen el mismo día y false en caso
     * contrario
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static boolean compareByDay(Calendar cal1, Calendar cal2) {
        return ((cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                && (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)));
    }

    /**
     * Devuelve un String con el tiempo en horas-minutos y segundos
     *
     * @param segundos
     * @return String
     */
    public static String conversorSecToHMinSec(Long segundos) {
        Long h, min, seg;
        String tiempo;
        h = segundos / 3600;
        min = (segundos % 3600) / 60;
        seg = (segundos % 3600) % 60;
        if ((h == 0)) {
            tiempo = min.toString() + "m " + seg.toString() + "s";
        } else {
            tiempo = h.toString() + "h " + min.toString() + "m " + seg.toString() + "s";
        }

        return tiempo;
    }
    
    public static Long conversorStringToSeg(String hours, String min, String seg){
        return Long.parseLong(hours) * 3600
                + Long.parseLong(min) * 60
                + Long.parseLong(seg);
    }
}
