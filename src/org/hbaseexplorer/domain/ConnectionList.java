package org.hbaseexplorer.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author zaharije
 */
public class ConnectionList implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2278553441723985257L;

    private ArrayList<Connection> connectionList;

    public ConnectionList() {
        connectionList = new ArrayList<Connection>();
    }

    public void addConnection(Connection conn) {
        connectionList.add(conn);
    }

}
