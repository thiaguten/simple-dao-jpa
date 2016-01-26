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

import javax.persistence.*;
import java.io.Serializable;

/**
 * Utility class to create an entity manager factory and an entity manager.
 *
 * @author Thiago Gutenberg
 */
public final class JPAUtils {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    private JPAUtils() {
        // suppress default constructor
        // for noninstantiability
        throw new AssertionError();
    }

    /**
     * Create entity manager factory
     *
     * @param persistenceUnitName persistence unit name
     * @return entity manager factory
     */
    public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
        return emf;
    }

    /**
     * Get entity manager
     *
     * @return entity manager
     */
    public static EntityManager getEntityManager() {
        if (emf != null && emf.isOpen()) {
            if (!em.isOpen()) {
                em = emf.createEntityManager();
            }
            return em;
        }
        throw new ExceptionInInitializerError("EntityManagerFactory not created or is closed. Should be re-created through the method createEntityManagerFactory");

    }

    /**
     * Get entity manager transaction
     *
     * @return entity manager transaction
     */
    public static EntityTransaction getTransaction() {
        return getEntityManager().getTransaction();
    }

    /**
     * Close entity manager
     */
    public static void closeEntityManager() {
        if (getEntityManager().isOpen()) {
            getEntityManager().close();
        }
    }

    /**
     * Begin transaction
     */
    public static void beginTransaction() {
        if (!isTransactionActive()) {
            getTransaction().begin();
        }
    }

    /**
     * Commit transaction
     */
    public static void commitTransaction() {
        if (isTransactionActive()) {
            getTransaction().commit();
        }
    }

    /**
     * Rollback transaction
     */
    public static void rollbackTransaction() {
        if (isTransactionActive()) {
            getTransaction().rollback();
        }
    }

    /**
     * Checks if a transaction is active
     *
     * @return true if the transaction is active, otherwise false
     */
    public static boolean isTransactionActive() {
        return getTransaction().isActive();
    }

    /**
     * Apply typed query range
     *
     * @param query       query
     * @param firstResult first result
     * @param maxResults  max result
     * @param <T>         entity
     * @return typed query
     */
    public static <T extends br.com.thiaguten.persistence.Identificable<? extends Serializable> & Serializable> TypedQuery<T> queryRange(TypedQuery<T> query, int firstResult, int maxResults) {
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
