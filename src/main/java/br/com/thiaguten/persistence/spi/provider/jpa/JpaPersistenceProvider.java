/*-
 * #%L
 * Simple DAO JPA
 * %%
 * Copyright (C) 2016 - 2019 Thiago Gutenberg Carvalho da Costa
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
import br.com.thiaguten.persistence.spi.PersistenceProvider;
import java.io.Serializable;
import javax.persistence.EntityManager;

/**
 * JPA Persistence Provider Proxy.
 *
 * @author Thiago Gutenberg Carvalho da Costa
 */
public interface JpaPersistenceProvider extends PersistenceProvider {

  /**
   * Get the entity manager.
   *
   * @return the entity manager
   */
  EntityManager getEntityManager();

  /**
   * Delete an entity by its identifier.
   *
   * @param <ID> the type of the identifier
   * @param <T> the type of the entity
   * @param entityClazz the entity class
   * @param entity the type of the entity
   * @param id the entity identifier
   */
  <ID extends Serializable, T extends Persistable<ID>> void deleteByEntityOrId(
      Class<T> entityClazz, T entity, ID id);

}
