/*
 * #%L
 * %%
 * Copyright (C) 2015 - 2016 Thiago Gutenberg Carvalho da Costa.
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Thiago Gutenberg Carvalho da Costa. nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package br.com.thiaguten.persistence.spi.provider.jpa;

import br.com.thiaguten.persistence.core.Persistable;
import br.com.thiaguten.persistence.spi.PersistenceProvider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * JPA implementation of the PersistenceProvider.
 *
 * @author Thiago Gutenberg
 */
public abstract class JPAPersistenceProvider implements PersistenceProvider {

    /**
     * Get the entity manager.
     *
     * @return the entity manager
     */
    public abstract EntityManager getEntityManager();

    /**
     * {@inheritDoc}
     */
    @Override
    public <ID extends Serializable, T extends Persistable<ID>> T findById(Class<T> entityClazz, ID id) {
        return getEntityManager().find(entityClazz, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Persistable<? extends Serializable>> List<T> findAll(Class<T> entityClazz) {
        return findAll(entityClazz, -1, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Persistable<? extends Serializable>> List<T> findAll(Class<T> entityClazz, int firstResult, int maxResults) {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClazz);
        TypedQuery<T> createQuery = getEntityManager().createQuery(cq.select(cq.from(entityClazz)));
        return queryRange(createQuery, firstResult, maxResults).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Persistable<? extends Serializable>> List<T> findByNamedQuery(Class<T> entityClazz, String queryName, Object... params) {
        TypedQuery<T> typedQuery = getEntityManager().createNamedQuery(queryName, entityClazz);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                typedQuery.setParameter(i + 1, params[i]);
            }
        }
        return typedQuery.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Persistable<? extends Serializable>> List<T> findByNamedQueryAndNamedParams(Class<T> entityClazz, String queryName, Map<String, ?> params) {
        TypedQuery<T> typedQuery = getEntityManager().createNamedQuery(queryName, entityClazz);
        if (params != null) {
            for (final Map.Entry<String, ?> param : params.entrySet()) {
                typedQuery.setParameter(param.getKey(), param.getValue());
            }
        }
        return typedQuery.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Persistable<? extends Serializable>> List<T> findByQueryAndNamedParams(Class<T> entityClazz, String query, Map<String, ?> params) {
        TypedQuery<T> typedQuery = getEntityManager().createQuery(query, entityClazz);
        if (params != null) {
            for (final Map.Entry<String, ?> param : params.entrySet()) {
                typedQuery.setParameter(param.getKey(), param.getValue());
            }
        }
        return typedQuery.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Persistable<? extends Serializable>> long countAll(Class<T> entityClazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(entityClazz)));
        return getEntityManager().createQuery(criteriaQuery).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Number> T countByNamedQueryAndNamedParams(Class<T> resultClazz, String queryName, Map<String, ?> params) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(queryName, resultClazz);
        if (params != null && !params.isEmpty()) {
            for (final Map.Entry<String, ?> param : params.entrySet()) {
                query.setParameter(param.getKey(), param.getValue());
            }
        }
        return query.getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Number> T countByQueryAndNamedParams(Class<T> resultClazz, String query, Map<String, ?> params) {
        TypedQuery<T> typedQuery = getEntityManager().createQuery(query, resultClazz);
        if (params != null && !params.isEmpty()) {
            for (final Map.Entry<String, ?> param : params.entrySet()) {
                typedQuery.setParameter(param.getKey(), param.getValue());
            }
        }
        return typedQuery.getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Persistable<? extends Serializable>> T save(T entity) {
        if (entity.getId() != null) {
            entity = getEntityManager().merge(entity);
        } else {
            getEntityManager().persist(entity);
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Persistable<? extends Serializable>> T update(T entity) {
        return save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Persistable<? extends Serializable>> void delete(Class<T> entityClazz, T entity) {
        deleteByEntityOrId(entityClazz, entity, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <ID extends Serializable, T extends Persistable<ID>> void deleteById(Class<T> entityClazz, ID id) {
        deleteByEntityOrId(entityClazz, null, id);
    }

    @SuppressWarnings("unchecked")
    private <ID extends Serializable, T extends Persistable<? extends Serializable>> void deleteByEntityOrId(Class<T> entityClazz, T entity, ID id) {
        if (id == null && (entity == null || entity.getId() == null)) {
            throw new PersistenceException("Could not delete. ID is null.");
        }

        ID _id = id;
        if (_id == null) {
            _id = (ID) entity.getId();
        }
//        T t = findById(entityClazz, _id); // throws exception: entity must be managed to call remove: try merging the detached and try the remove again.
        T t = getEntityManager().getReference(entityClazz, _id);
        getEntityManager().remove(t);
    }

    private <T extends Persistable<? extends Serializable>> TypedQuery<T> queryRange(TypedQuery<T> query, int firstResult, int maxResults) {
        if (query != null) {
            if (maxResults >= 0) {
                query.setMaxResults(maxResults);
            }
            if (firstResult >= 0) {
                query.setFirstResult(firstResult);
            }
        }
        return query;
    }

}
