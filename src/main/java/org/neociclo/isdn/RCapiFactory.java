/**
 * The Accord Project, http://accordproject.org
 * Copyright (C) 2005-2013 Rafael Marins, http://rafaelmarins.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neociclo.isdn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import net.sourceforge.jcapi.rcapi.RCapi;

import org.neociclo.capi20.Capi;
import org.neociclo.capi20.jcapi.JcapiAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 */
public class RCapiFactory implements CapiFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RCapiFactory.class);

    private Capi capiInstance;

    private String host;
    private int port;
    private String username;
    private String password;
    private File rcapiControllers;

    public RCapiFactory(String host, int port) {
        this(host, port, null, null);
    }

    public RCapiFactory(String host, int port, String user, String pswd) {
        super();
        assert host != null : "host";
        this.host = host;
        this.port = port;
        this.username = user;
        this.password = pswd;
    }

    public Capi getCapi() {
        if (capiInstance == null) {
            rcapiControllers = createPropertiesFile();
            capiInstance = new JcapiAdapter(createRemoteCapi());
        }
        return capiInstance;
    }

    private RCapi createRemoteCapi() {
        RCapi rcapi = null;
        try {
            rcapi = new RCapi(new File[] { rcapiControllers });
            LOGGER.debug("RCapi created: {}.", rcapi);
        } catch (Throwable e) {
            // ignore
            LOGGER.error("Error creating RCapi. Shouldn't get down here.", e);
        } finally {
            rcapiControllers.delete();
            rcapiControllers = null;
        }
        return rcapi;
    }

    private File createPropertiesFile() {

        File tmpFile = null;

        // create temp file
        try {
            tmpFile = File.createTempFile("rcapi", ".controllers");
        } catch (IOException e) {
            LOGGER.error("Error creating RCapi. Cannot create temp file.", e);
            return null;
        }

        // create the properties
        Properties props = new Properties();
        props.setProperty("CAPI_HOST", host);

        if (port > 0) {
            props.setProperty("CAPI_PORT", String.valueOf(port));
        } else {
            LOGGER.warn("Using default CAPI port.");
        }

        if (username != null) {
            props.setProperty("CAPI_USER", username);
        }

        if (password != null) {
            props.setProperty("CAPI_PASSWD", password);
        }

        // write down properties to the file
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(tmpFile);
            props.store(outStream, String.format("Remote CAPI definition - %s", Calendar.getInstance().toString()));
            outStream.flush();
        } catch (Throwable t) {
            LOGGER.error("Error creating RCapi. Cannot open output file.", t);
            return null;
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        return tmpFile;
    }

    public void releaseExternalResources() {
        // TODO Auto-generated method stub
    }

}
