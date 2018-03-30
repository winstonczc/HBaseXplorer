package org.hbaseexplorer.domain;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.hbaseexplorer.exception.ExplorerException;

/**
 *
 * @author zaharije
 */
public class Table implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1176934932361671353L;
    private HTableDescriptor tableDescriptor;
    private Connection connection;
    private org.apache.hadoop.hbase.client.Table hTable;

    public Table(HTableDescriptor tableDescriptor, Connection connection) {
        this.tableDescriptor = tableDescriptor;
        this.connection = connection;
        hTable = null;
    }

    public Connection getConnection() {
        return connection;
    }

    public org.apache.hadoop.hbase.client.Table getHTable() {
        try {
            if (hTable == null) {
                hTable = connection.getHbaseConnection().getTable(tableDescriptor.getTableName());
                return hTable;
            } else {
                return hTable;
            }
        } catch (IOException ioe) {
            throw new ExplorerException("Error creating HTable for table " + getFullName());
        }
    }

    public String getName() {
        return new String(tableDescriptor.getTableName().getName(), Charset.forName("UTF8"));
    }

    public String getFullName() {
        return getName() + "@" + connection.getName();
    }

    @Override
    public String toString() {
        return getName();
    }
}
