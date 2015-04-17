/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ibm.bbva.util;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class VisualizacionInterfaz {
    protected File file;
    protected final Log logger = LogFactory.getLog(this.getClass());

    public abstract byte[] getByteArray();

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }



}
