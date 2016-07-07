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
package br.com.thiaguten.persistence.demo.jpa;

import br.com.thiaguten.persistence.core.BasePersistence;
import br.com.thiaguten.persistence.demo.User;
import br.com.thiaguten.persistence.demo.UserDAO;
import br.com.thiaguten.persistence.spi.PersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository("userJpaDAO")
public class UserDAOImpl extends BasePersistence<Long, User> implements UserDAO {

    private final PersistenceProvider persistenceProvider;

    @Autowired
    public UserDAOImpl(@Qualifier("jpaPersistenceProvider") PersistenceProvider persistenceContext) {
        this.persistenceProvider = persistenceContext;
    }

    @Override
    public PersistenceProvider getPersistenceProvider() {
        return persistenceProvider;
    }

    @Override
    public List<User> findByName(String name) {
        String jpql = "SELECT u FROM User u WHERE UPPER(u.name) LIKE :name";
        Map<String, String> namedParams = Collections.singletonMap("name", "%" + name.toUpperCase() + "%"); // like IGNORECASE and matchmode ANYWHERE
        List<User> results = persistenceProvider.findByQueryAndNamedParams(getPersistenceClass(), jpql, namedParams);
        if (results.isEmpty()) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(results);
        }
    }
}
