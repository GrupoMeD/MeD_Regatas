package es.udc.fic.medregatas.util.dao;

import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericDaoHibernate<E, PK extends Serializable> implements
        GenericDao<E, PK> {

    private SessionFactory sessionFactory;

    private Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public GenericDaoHibernate() {
        this.entityClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(E entity) {
        getSession().saveOrUpdate(entity);
    }

    @SuppressWarnings("unchecked")
    public E find(PK id) throws InstanceNotFoundException {
        E entity = (E) getSession().get(entityClass, id);
        if (entity == null) {
            throw new InstanceNotFoundException(id, entityClass.getName());
        }
        return entity;
    }

    public void remove(PK id) throws InstanceNotFoundException {
        getSession().delete(find(id));
    }

    @SuppressWarnings("unchecked")
    public List<E> findAll() {
        List<E> objects = null;

        Type EClass = ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        String EClassName = EClass.toString();

        //Obtenemos el nobre de la clase E
        String[] domainNames = EClassName.split("\\.");
        String className = domainNames[domainNames.length - 1];

        Query query = getSession().createQuery("from " + className);
        objects = query.list();
        return objects;
    }

    @Override
    public void merge(E entity) {
        getSession().merge(entity);
    }
}
