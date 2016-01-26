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
package br.com.thiaguten.persistence.demo.manual;

import br.com.thiaguten.persistence.demo.User;
import br.com.thiaguten.persistence.demo.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Abstract test class
 *
 * @author Thiago Gutenberg
 */
public abstract class AbstractPersistenceProviderManualTest {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPersistenceProviderManualTest.class);

    protected static final String[] INSERT_PARAMS = {"THIAGO", "DAYANA", "VALENTINA"};
    protected static final String[] UPDATE_PARAMS = {"THIAGO2", "DAYANA2", "VALENTINA2"};
    protected static final String[] DELETE_PARAMS = UPDATE_PARAMS;

    protected abstract UserDAO getUserDAO();

    @Test
    public void testIt() {
        create();
        read(INSERT_PARAMS, false);
        update();
        read(UPDATE_PARAMS, false);
        delete();
        read(DELETE_PARAMS, true);
    }

    private void create() {
        logInfoHandler("Creating...");
        for (int i = 0; i < INSERT_PARAMS.length; i++) {

            User user = new User();
            user.setName(INSERT_PARAMS[i]);

            logInfoHandler(user.toString());

            getUserDAO().save(user);
        }
    }

    private void read(String[] params, boolean deleteCheck) {
        logInfoHandler("Reading...");
        for (int i = 0; i < params.length; i++) {

            User user = getUserDAO().findById(i + 1L);
            String name = user != null ? user.getName() : null;

            String mensagem;
            if (deleteCheck) {
                mensagem = "assertNull";
                Assert.assertNull(name);
            } else {
                mensagem = "assertEquals";
                Assert.assertEquals(params[i], name);
            }

            if (user != null) {
                logInfoHandler(user.toString());
            }

            logInfoHandler(mensagem + ":" + params[i] + " -> " + name);
        }
    }

    private void update() {
        logInfoHandler("Updating...");
        for (int i = 0; i < UPDATE_PARAMS.length; i++) {

            User user = getUserDAO().findById(i + 1L);
            user.setName(UPDATE_PARAMS[i]);

            logInfoHandler(user.toString());

            getUserDAO().update(user);
        }
    }

    private void delete() {
        logInfoHandler("Deleting...");
        for (int i = 0; i < DELETE_PARAMS.length; i++) {

            User user = getUserDAO().findById(i + 1L);
            user.setName(DELETE_PARAMS[i]);

            logInfoHandler(user.toString());

            getUserDAO().delete(user);
        }
    }

    protected void logInfoHandler(String str) {
        LOG.info(str);
//        System.out.println(str);
    }

}
