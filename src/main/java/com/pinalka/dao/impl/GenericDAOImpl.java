package com.pinalka.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.pinalka.dao.GenericDAO;
import org.springframework.stereotype.Repository;

/**
 * Generic implementation of the GenericDAO
 *
 * @param <T> exact type of the entity
 */
@Repository
public abstract class GenericDAOImpl<T> implements GenericDAO<T> {
    
    @PersistenceContext
    protected EntityManager em;

    private final String queryForList;
    private final Class<T> clazz;

    protected GenericDAOImpl(final String queryForList) {
        this.queryForList = queryForList;
        this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    @Override
    public T getById(final Long id) {
        return em.find(clazz, id);
    }

    @Override
    public List<T> getList() {
        return em.createQuery(queryForList).getResultList();
    }

    @Override
    public List<T> getList(int page) {
        return getList(page, DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<T> getList(int page, int pageSize) {
        return em.createQuery(queryForList).
                setFirstResult(page * pageSize).
                setMaxResults(pageSize).
                getResultList();
    }

    @Override
    public long count() {
        return 0;//TODO implement
    }

    @Override
    public void create(final T object) {
        em.persist(object);
    }

    @Override
    public void update(final T object) {
        em.merge(object);
    }

    @Override
    public void delete(final T object) {
        em.remove(object);
    }

    @Override
    public void deleteById(final Long id) {
        em.remove(em.find(clazz, id));
    }
}
