/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.util;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author diego
 */
public class FilterUtils {

    /**
     * Logical predicate whit need an type T to be evaluated
     *
     * @param <T>
     */
    public interface Predicate<T> {

        /**
         *
         * @param object
         * @return true if the object makes true the Predicate
         */
        boolean evaluate(T object);
    }

    /**
     * Filter a collection of T parameters, using the predicate.
     *
     * @param <T>
     * @param collection
     * @param predicate
     */
    public static <T> void filter(Collection<T> collection, Predicate<T> predicate) {
        if ((collection != null) && (predicate != null)) {
            Iterator<T> itr = collection.iterator();
            while (itr.hasNext()) {
                T obj = itr.next();
                if (!predicate.evaluate(obj)) {
                    itr.remove();
                }
            }
        }
    }

}
