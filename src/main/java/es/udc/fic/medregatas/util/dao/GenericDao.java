package es.udc.fic.medregatas.util.dao;

import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import java.io.Serializable;
import java.util.List;

public interface GenericDao<E, PK extends Serializable> {

    void save(E entity);

    E find(PK id) throws InstanceNotFoundException;

    void remove(PK id) throws InstanceNotFoundException;

    List<E> findAll();

    void merge(E entity);
}
