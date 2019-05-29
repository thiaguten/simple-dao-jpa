/*
 * #%L
 * %%
 * Copyright (C) 2015 - 2016 Thiago Gutenberg Carvalho da Costa.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package br.com.thiaguten.persistence.spi.provider.jpa;

import br.com.thiaguten.persistence.core.Persistable;

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
 * @author Thiago Gutenberg Carvalho da Costa
 */
public abstract class AbstractJpaPersistenceProvider implements JpaPersistenceProvider {

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
  public <ID extends Serializable, T extends Persistable<ID>> List<T> findAll(Class<T> entityClazz) {
    return findAll(entityClazz, -1, -1);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <ID extends Serializable, T extends Persistable<ID>> List<T> findAll(Class<T> entityClazz, int firstResult, int maxResults) {
    CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClazz);
    TypedQuery<T> createQuery = getEntityManager().createQuery(cq.select(cq.from(entityClazz)));
    return queryRange(createQuery, firstResult, maxResults).getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <ID extends Serializable, T extends Persistable<ID>> List<T> findByNamedQuery(Class<T> entityClazz, String queryName, Object... params) {
    TypedQuery<T> typedQuery = getEntityManager().createNamedQuery(queryName, entityClazz);
    if (params != null) {
      for (int i = 0; i < params.length; i++) {
        typedQuery.setParameter(i + 1, params[i]); // JPQL Positional Parameters starts from 1
      }
    }
    return typedQuery.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <ID extends Serializable, T extends Persistable<ID>> List<T> findByNamedQueryAndNamedParams(Class<T> entityClazz, String queryName, Map<String, ?> params) {
    TypedQuery<T> typedQuery = getEntityManager().createNamedQuery(queryName, entityClazz);
    if (params != null) {
      params.forEach(typedQuery::setParameter);
    }
    return typedQuery.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <ID extends Serializable, T extends Persistable<ID>> List<T> findByQuery(Class<T> entityClazz, String query, Object... params) {
    TypedQuery<T> typedQuery = getEntityManager().createQuery(query, entityClazz);
    if (params != null) {
      for (int i = 0; i < params.length; i++) {
        typedQuery.setParameter(i + 1, params[i]); // JPQL Positional Parameters starts from 1
      }
    }
    return typedQuery.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <ID extends Serializable, T extends Persistable<ID>> List<T> findByQueryAndNamedParams(Class<T> entityClazz, String query, Map<String, ?> params) {
    TypedQuery<T> typedQuery = getEntityManager().createQuery(query, entityClazz);
    if (params != null) {
      params.forEach(typedQuery::setParameter);
    }
    return typedQuery.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <ID extends Serializable, T extends Persistable<ID>> long countAll(Class<T> entityClazz) {
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
    TypedQuery<T> typedQuery = getEntityManager().createNamedQuery(queryName, resultClazz);
    if (params != null) {
      params.forEach(typedQuery::setParameter);
    }
    return typedQuery.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends Number> T countByQueryAndNamedParams(Class<T> resultClazz, String query, Map<String, ?> params) {
    TypedQuery<T> typedQuery = getEntityManager().createQuery(query, resultClazz);
    if (params != null) {
      params.forEach(typedQuery::setParameter);
    }
    return typedQuery.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <ID extends Serializable, T extends Persistable<ID>> T save(T entity) {
    ID id = entity.getId();

    if (id != null) {
      entity = getEntityManager().merge(entity);
    } else {
      getEntityManager().persist(entity);
//      getEntityManager().flush();
//      id = entity.getId();
//      id = (ID) getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
//      id = (ID) getEntityManager().unwrap(Session.class).getIdentifier(entity);
    }

//    Class<T> entityClazz = (Class<T>) entity.getClass();
//    T t = getEntityManager().getReference(entityClazz, id);

//    return t;
    return entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <ID extends Serializable, T extends Persistable<ID>> T update(T entity) {
    return save(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <ID extends Serializable, T extends Persistable<ID>> void delete(Class<T> entityClazz, T entity) {
    deleteByEntityOrId(entityClazz, entity, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <ID extends Serializable, T extends Persistable<ID>> void deleteById(Class<T> entityClazz, ID id) {
    deleteByEntityOrId(entityClazz, null, id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <ID extends Serializable, T extends Persistable<ID>> void deleteByEntityOrId(Class<T> entityClazz, T entity, ID id) {
    if (id == null && (entity == null || entity.getId() == null)) {
      throw new PersistenceException("Could not delete. ID is null.");
    }

    ID _id = id;
    if (_id == null) {
      _id = entity.getId();
    }

//    T t = findById(entityClazz, _id); // throws exception: entity must be managed to call remove: try merging the detached and try the remove again.
    T t = getEntityManager().getReference(entityClazz, _id);

    getEntityManager().remove(t);
  }

  private <ID extends Serializable, T extends Persistable<ID>> TypedQuery<T> queryRange(TypedQuery<T> query, int firstResult, int maxResults) {
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
