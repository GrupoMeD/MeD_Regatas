/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.posicion;

import es.udc.fic.medregatas.model.barco.Barco;
import es.udc.fic.medregatas.model.manga.Manga;
import java.io.Serializable;
import java.util.Comparator;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author iago
 */
@Entity
public class Posicion implements Serializable {

    private Long idPosicion;
    private Long segTiempo;
    private Penalizacion penal;
    private Manga manga;
    private Barco barco;
    private int puntos;
    private Long segPenalizacion;

    public Posicion() {
    }

    public Posicion(Long segTiempo, Penalizacion penal, Manga manga, Barco barco, Long segPenalizacion) {
        this.segTiempo = segTiempo;
        this.penal = penal;
        this.manga = manga;
        this.barco = barco;
        this.segPenalizacion = segPenalizacion;
    }

    @Id
    @GeneratedValue
    public Long getIdPosicion() {
        return idPosicion;
    }

    public void setIdPosicion(Long idPosicion) {
        this.idPosicion = idPosicion;
    }

    public Long getSegTiempo() {
        return segTiempo;
    }

    public void setSegTiempo(Long tiempo) {
        this.segTiempo = tiempo;
    }

    public Penalizacion getPenal() {
        return penal;
    }

    public void setPenal(Penalizacion penal) {
        this.penal = penal;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idManga")
    public Manga getManga() {
        return manga;
    }

    public void setManga(Manga manga) {
        this.manga = manga;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idBarco")
    public Barco getBarco() {
        return barco;
    }

    public void setBarco(Barco barco) {
        this.barco = barco;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public Long getSegPenalizacion() {
        return segPenalizacion;
    }

    public void setSegPenalizacion(Long segPenalizacion) {
        this.segPenalizacion = segPenalizacion;
    }

    @Override
    public String toString() {
        return "Posicion{" + "segTiempo=" + segTiempo + ", msPenal=" + penal + ", puntos=" + puntos + '}';
    }

    public static class PosicionesPointComparator implements Comparator<Posicion> {

        @Override
        public int compare(Posicion pos1, Posicion pos2) {
            return pos1.getPuntos() - pos2.getPuntos();
        }

    }

    public static class PosicionesTimeComparator implements Comparator<Posicion> {

        /**
         * Dice cual de los dos barcos ha quedado mejor en la clasificacion
         * PreCnd: Los barcos han de ser del mismo tipo
         *
         * @param pos1
         * @param pos2
         * @return
         */
        @Override
        public int compare(Posicion pos1, Posicion pos2) {

            /*Si ambos tiempos no acarrean la maxima puntuacion */
            if ((!pos1.getPenal().isMaxPointsPenal())
                    && (!pos2.getPenal().isMaxPointsPenal())) {
                /**
                 * Si NO compiten en tiempo compensado
                 */
                if (pos1.getBarco().getTipo().getCompiteTmpReal()) {
                    //TODO Probar bien esto!
                    return (int) ponderarPenalizacion(pos1.getSegTiempo(),
                            pos1.getPenal(), pos1.getSegPenalizacion())
                            .compareTo(ponderarPenalizacion(pos2.getSegTiempo(),
                                            pos2.getPenal(), pos2.getSegPenalizacion()));
                } else {

                    return ponderarPenalizacion(
                            calcTiempoCompensado(pos1.getSegTiempo(), pos1.getBarco(), pos1.getManga()),
                            pos1.getPenal(),
                            pos1.getSegPenalizacion())
                            .compareTo(ponderarPenalizacion(calcTiempoCompensado(
                                                    pos2.getSegTiempo(),
                                                    pos2.getBarco(), pos2.getManga()),
                                            pos2.getPenal(),
                                            pos2.getSegPenalizacion()));
                }
            } else {
                /**
                 * Situacion en la que uno de ellos esta penalizado o ambos lo
                 * están
                 */

                if (pos1.getPenal() == Posicion.Penalizacion.NAN) {

                    return -1;

                } else if (pos2.getPenal() == Posicion.Penalizacion.NAN) {

                    return 1;

                } else {

                    return 0;

                }

            }
        }

        /**
         * Devuelve el tiempo computado en segundos
         *
         * @param tiempoReal
         * @param barco
         * @return
         */
        private Long calcTiempoCompensado(Long tiempoReal, Barco barco, Manga manga) {
            //Tiempo compensado = Tiempo Real + GPH * Nº de millas manga.
            Float res = tiempoReal + barco.getGph() * manga.getMillas();
            return (long) Math.round(res);
        }

        private Long ponderarPenalizacion(Long tiempo, Penalizacion penal, Long tiempoPenal) {
            //ZFP 20% penaliza
            switch (penal) {
                case ZFP:
                    tiempo = Math.round(1.2 * tiempo);
                    break;
                case RDG:
                case DPI:
                case SCP:
                    tiempo += tiempoPenal;
                    break;

            }
            return tiempo;
        }

    }

    //TODO Pequena esplicacion de que es cada una, donde esta?
    //TODO Y si no hay penalizacion? Null?
    public enum Penalizacion {

        /**
         * Sin penalización
         */
        NAN,
        /**
         * No salió; no se acercó a la zona de salida
         */
        DNC,
        /**
         * No salió (distinto a DNC y OCS)
         */
        DNS,
        /**
         * No salió, en el lado del recorrido de la línea de salida al darse su
         * señal de salida y no salió, o infringió la regla:
         *
         * Cuando se ha izado la bandera I y cualquier parte del casco,
         * tripulación o equipo de un barco esté en el lado del recorrido de la
         * línea de salida o de una de sus prolongaciones durante el último
         * minuto previo a su señal de salida,dicho barco deberá a continuacion
         * navegar desde el lado del recorrido al lado de presalida cruzando una
         * de las prolongaciones de la linea antes de salir.
         *
         */
        OCS,
        /**
         * 20% de penalización conforme a la de bandera Z
         *
         * Cuando se ha izado la bandera Z, ninguna parte del casco, tripulación
         * o equipo de un barco estará dentro del triángulo formado por los
         * extremos de la línea de salida y la primera baliza durante el último
         * minuto previo a su señal de salida. Si un barco infringe esta regla y
         * es identificado, recibirá, sin audiencia, una Penalización de
         * Puntuación del 20% calculada de acuerdo con la regla 44.3(c). El
         * barco será penalizado incluso aunque se dé una nueva salida a la
         * prueba o ésta se vuelva a correr, pero no si es aplazada o anulada
         * antes de darse la señal de salida. Si el barco es identificado de la
         * misma forma en un intento subsiguiente de dar la salida a la misma
         * prueba, recibirá una Penalización de Puntuación adicional del 20%
         */
        ZFP,
        /**
         * Aceptó una Penalización de Puntuación conforme a la regla de
         * Penalización por Puntuacion:
         *
         * Un barco acepta una Penalización de Puntuación mostrando una bandera
         * amarilla en la primera oportunidad razonable después del incidente.
         */
        SCP,
        /**
         * No terminó
         */
        DNF,
        /**
         * Retirado
         */
        RET,
        /**
         * Descalificación
         */
        DSQ,
        /**
         * Descalificación (Distinta a DGM) no excluible según la regla:
         *
         * Cuando un sistema de puntuación prevé la exclusión de una o más
         * puntuaciones de la puntuación de un barco en la serie, no se excluirá
         * la puntuación de descalificado por infringir las reglas de penaltys
         * insalvables(deportividad....)En su lugar se excluirá la siguiente
         * peor puntuación.
         */
        DNE,
        /**
         * Descalificación por grave mal comportamiento no excluible según la
         * regla:
         *
         * Cuando un sistema de puntuación prevé la exclusión de una o más
         * puntuaciones de la puntuación de un barco en la serie, no se excluirá
         * la puntuación de descalificado por infringir las reglas de penaltys
         * insalvables(deportividad....)En su lugar se excluirá la siguiente
         * peor puntuación.
         */
        DGM,
        /**
         * Recibió reparación
         */
        RDG,
        /**
         * Penalización discrecional
         */
        DPI;

        /**
         * Establecemos las penalizaciones que son leves y las que no, con esto
         * impedimos que las graves puedan ser eliminadas con la regla de las 4
         * mangas
         *
         * @return
         */
        public Boolean isLowPenal() {
            switch (this) {

                case DNE: //NO EXCLUIBLE
                    return false;
                case DGM: //NO EXCLUIBLE
                    return false;
                default: // TODAS LAS DEMAS PENALIZACIONES SON EXCLUIBLES
                    return true;
            }
        }

        public Boolean isMaxPointsPenal() {
            switch (this) {

                case NAN:
                    return false;
                case ZFP:
                    return false;
                case SCP:
                    return false;
                case RDG:
                    return false;
                case DPI:
                    return false;
                default: // TODAS LAS DEMAS PENALIZACIONES ACARREAN EL MAXIMO DE PUNTOS
                    return true;
            }
        }

        @Override
        public String toString() {

            switch (this) {
                case NAN:
                    return "";
                case DNC:
                    return "DNC";
                case DNS:
                    return "DNS";
                case OCS:
                    return "OCS";
                case ZFP:
                    return "ZFP";
                case SCP:
                    return "SCP";
                case DNF:
                    return "DNF";
                case RET:
                    return "RET";
                case DSQ:
                    return "DSQ";
                case DNE:
                    return "DNE";
                case DGM:
                    return "DGM";
                case RDG:
                    return "RDG";
                case DPI:
                    return "DPI";
                default:
                    return "";
            }
        }
    }
}
