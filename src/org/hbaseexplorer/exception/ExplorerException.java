package org.hbaseexplorer.exception;

import javax.swing.JOptionPane;

/**
 *
 * @author zaharije
 */
public class ExplorerException extends RuntimeException {
    
    /** 
     * serialVersionUID 
     */  
    private static final long serialVersionUID = 4220322641692761405L;

    public ExplorerException(String msg) {
        super(msg);
        JOptionPane.showMessageDialog(null, msg);
    }
}
