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
