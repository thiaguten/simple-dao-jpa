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

import br.com.thiaguten.persistence.demo.User;
import br.com.thiaguten.persistence.demo.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

@ContextConfiguration(locations = {"classpath:spring/persistence-jpa-appContext.xml"})
public class JPAPersistenceProviderImplTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final Logger log = LoggerFactory.getLogger(JPAPersistenceProviderImplTest.class);

    @Autowired
    @Qualifier("userJpaDAO")
    private UserDAO userDAO;

    @BeforeClass
    public static void init() {
        log.info("**************************");
        log.info("Java Persistence API - JPA");
        log.info("**************************");
    }

    @Test
    public void crudTest() {
        // create
        User user = new User("THIAGO");
        User userSave = userDAO.create(user);
        log.info("Created: " + userSave);

        List<User> users = userDAO.findByName(userSave.getName());
        log.info("Searched: " + userSave);
        assertNotNull(users);
        assertEquals(users.get(0).getName(), userSave.getName());

        // read
        User userSavedFound = userDAO.read(userSave.getId());
        log.info("Read: " + userSavedFound);
        assertNotNull(userSavedFound);
        assertEquals(userSavedFound.getName(), userSave.getName());

        // update
        userSavedFound.setName("VALENTINA");
        assertNotNull(userSavedFound.getId());
        User userUpdate = userDAO.update(userSavedFound);
        log.info("Updated: " + userUpdate);

        // read
        assertNotNull(userUpdate.getId());
        User userUpdatedFound = userDAO.read(userUpdate.getId());
        log.info("Read: " + userUpdatedFound);
        assertNotNull(userUpdatedFound);
        assertEquals(userUpdatedFound.getName(), userUpdate.getName());

        // delete
        userDAO.delete(userUpdatedFound);
        log.info("Deleted: " + userUpdatedFound);

        // read
        assertNotNull(userUpdatedFound.getId());
        User userDeleted = userDAO.read(userUpdatedFound.getId());
        log.info("Read: " + userDeleted);
        assertNull(userDeleted);
    }

}
