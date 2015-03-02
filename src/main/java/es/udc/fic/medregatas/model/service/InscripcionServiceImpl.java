/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.model.service;

import es.udc.fic.medregatas.model.barco.Barco;
import es.udc.fic.medregatas.model.barco.BarcoDao;
import es.udc.fic.medregatas.model.inscripcion.Inscripcion;
import es.udc.fic.medregatas.model.inscripcion.InscripcionDao;
import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author iago
 */
@Transactional
@Service("inscripcionService")
public class InscripcionServiceImpl implements InscripcionService {

    @Autowired
    private InscripcionDao inscripcionDao;
    @Autowired
    private BarcoDao barcoDao;

    @Override
    public Inscripcion inscribir(Regata regata, Barco barco, String patron) {
        //Si el barco no está persistido, po guardamos
        if (barco.getIdBarco() == null) {
            barcoDao.save(barco);
        }
        Inscripcion inscripcion = new Inscripcion(regata, barco, patron);
        inscripcionDao.save(inscripcion);
        return inscripcion;
    }

    @Override
    public List<Inscripcion> getInscripciones(Regata regata) {
        return inscripcionDao.findByRegata(regata.getIdRegata());
    }

    @Override
    public Barco guardarBarco(Barco barco) {
        barcoDao.save(barco);
        return barco;
    }

    @Override
    public List<Barco> findAllBarcos() {
        return barcoDao.findAll();
    }

    @Override
    public List<Inscripcion> getInscripcionesByTipo(Regata regata, Tipo tipo) {
        return inscripcionDao.fndByRegataAndTipo(regata.getIdRegata(), tipo.getIdTipo());
    }

    @Override
    public List<Barco> findBarcosByRegata(Regata regata) {
        List<Inscripcion> inscrip = inscripcionDao.findByRegata(regata.getIdRegata());
        List<Barco> result = new ArrayList<Barco>();
        for (Inscripcion i : inscrip) {
            result.add(i.getBarco());
        }
        return result;
    }

    @Override
    public void borrarInscripcion(Inscripcion inscripcion) throws InstanceNotFoundException {

        inscripcionDao.remove(inscripcion.getIdInscripcion());

    }

}
