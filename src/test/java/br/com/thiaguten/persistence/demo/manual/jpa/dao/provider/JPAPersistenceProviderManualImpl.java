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
package br.com.thiaguten.persistence.demo.manual.jpa.dao.provider;

import br.com.thiaguten.persistence.Persistable;
import br.com.thiaguten.persistence.spi.provider.jpa.JPAPersistenceProvider;
import br.com.thiaguten.persistence.spi.provider.jpa.JPAUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * JPA persistence strategy
 *
 * @author Thiago Gutenberg
 */
public class JPAPersistenceProviderManualImpl extends JPAPersistenceProvider {

    static {
        JPAUtils.createEntityManagerFactory("jpaPU");
    }

    @Override
    public EntityManager getEntityManager() {
        return JPAUtils.getEntityManager();
    }

    @Override
    public <T extends Persistable<? extends Serializable>, PK extends Serializable> void deleteById(Class<T> entityClazz, PK pk) {
        try {
            JPAUtils.beginTransaction();
            super.deleteById(entityClazz, pk);
            JPAUtils.commitTransaction();
        } catch (Exception e) {
            JPAUtils.rollbackTransaction();
            throw new PersistenceException("Erro ao deletar: " + e.getLocalizedMessage());
        } finally {
            JPAUtils.closeEntityManager();
        }
    }

    @Override
    public <T extends Persistable<? extends Serializable>> void delete(Class<T> entityClazz, T entity) {
        try {
            JPAUtils.beginTransaction();
            super.delete(entityClazz, entity);
            JPAUtils.commitTransaction();
        } catch (Exception e) {
            JPAUtils.rollbackTransaction();
            throw new PersistenceException("Erro ao deletar: " + e.getLocalizedMessage());
        } finally {
            JPAUtils.closeEntityManager();
        }
    }

    @Override
    public <T extends Persistable<? extends Serializable>> T update(T entity) {
        return this.save(entity);
    }

    @Override
    public <T extends Persistable<? extends Serializable>> T save(T entity) {
        try {
            JPAUtils.beginTransaction();
            T t = super.save(entity);
            JPAUtils.commitTransaction();
            return t;
        } catch (Exception e) {
            JPAUtils.rollbackTransaction();
            throw new PersistenceException("Erro ao salvar/atualizar: " + e.getLocalizedMessage());
        } finally {
            JPAUtils.closeEntityManager();
        }
    }

    @Override
    public <T extends Persistable<? extends Serializable>> long countAll(Class<T> entityClazz) {
        try {
            return super.countAll(entityClazz);
        } finally {
            JPAUtils.closeEntityManager();
        }
    }

    @Override
    public <T extends Persistable<? extends Serializable>> List<T> findByNamedQueryAndNamedParams(Class<T> entityClazz, String queryName, Map<String, ?> params) {
        try {
            return super.findByNamedQueryAndNamedParams(entityClazz, queryName, params);
        } finally {
            JPAUtils.closeEntityManager();
        }
    }

    @Override
    public <T extends Persistable<? extends Serializable>> List<T> findByNamedQuery(Class<T> entityClazz, String queryName, Object... params) {
        try {
            return super.findByNamedQuery(entityClazz, queryName, params);
        } finally {
            JPAUtils.closeEntityManager();
        }
    }

    @Override
    public <T extends Persistable<? extends Serializable>> List<T> findAll(Class<T> entityClazz, int firstResult, int maxResults) {
        try {
            return super.findAll(entityClazz, firstResult, maxResults);
        } finally {
            JPAUtils.closeEntityManager();
        }
    }

    @Override
    public <T extends Persistable<? extends Serializable>> List<T> findAll(Class<T> entityClazz) {
        try {
            return super.findAll(entityClazz);
        } finally {
            JPAUtils.closeEntityManager();
        }
    }

    @Override
    public <T extends Persistable<? extends Serializable>, PK extends Serializable> T findById(Class<T> entityClazz, PK pk) {
        try {
            return super.findById(entityClazz, pk);
        } finally {
            JPAUtils.closeEntityManager();
        }
    }

    @Override
    public <T extends Persistable<? extends Serializable>> List<T> findByQueryAndNamedParams(Class<T> entityClazz, String query, Map<String, ?> params) {
        try {
            return super.findByQueryAndNamedParams(entityClazz, query, params);
        } finally {
            JPAUtils.closeEntityManager();
        }
    }

    @Override
    public <T extends Number> T countByNamedQueryAndNamedParams(Class<T> resultClazz, String queryName, Map<String, ?> params) {
        try {
            return super.countByNamedQueryAndNamedParams(resultClazz, queryName, params);
        } finally {
            JPAUtils.closeEntityManager();
        }
    }

    @Override
    public <T extends Number> T countByQueryAndNamedParams(Class<T> resultClazz, String query, Map<String, ?> params) {
        try {
            return super.countByQueryAndNamedParams(resultClazz, query, params);
        } finally {
            JPAUtils.closeEntityManager();
        }
    }
}
