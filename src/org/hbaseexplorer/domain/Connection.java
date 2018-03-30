package org.hbaseexplorer.domain;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.buddy.javatools.Utils;
import org.hbaseexplorer.common.HConfConstants;
import org.hbaseexplorer.exception.ExplorerException;

/**
 *
 * @author zaharije
 */
public class Connection implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7250890083202179517L;

    private Configuration hbaseConfiguration;

    private org.apache.hadoop.hbase.client.Connection hbaseConnection;

    private ArrayList<Table> tableList;

    public Connection(Configuration configuration) {
        hbaseConfiguration = HBaseConfiguration.create(configuration);
        String resource = "./conf/hbase.xml";
        Path path = new Path(resource);
        hbaseConfiguration.addResource(path);
        tableList = new ArrayList<Table>();
    }

    public void connect() throws ZooKeeperConnectionException, IOException {
        try {
            this.hbaseConnection = ConnectionFactory.createConnection(hbaseConfiguration);
            if (StringUtils.isNotBlank(this.hbaseConfiguration.get(HConfConstants.TABLE_FILTER_REGX))) {
                refreshTables(this.hbaseConfiguration.get(HConfConstants.TABLE_FILTER_REGX));
            } else {
                refreshTables(null);
            }
        } catch (MasterNotRunningException me) {
            throw new ExplorerException("Cannot connect to cluster");
        }
    }

    public void refreshTables(String regx) {
        try {
            tableList = new ArrayList<Table>();
            HTableDescriptor[] hTables = null;
            if (StringUtils.isNotBlank(regx)) {
                hTables = this.hbaseConnection.getAdmin().listTables(regx);
            } else {
                hTables = this.hbaseConnection.getAdmin().listTables();
            }

            Log log = Utils.getLog();
            log.info("*****table");
            log.info(hTables.length);

            for (HTableDescriptor tableDescriptor : hTables) {
                tableList.add(new Table(tableDescriptor, this));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new ExplorerException("Error while getting table list!");
        }
    }

    public String getName() {
        return hbaseConfiguration.get("hbase.zookeeper.quorum");
    }

    public ArrayList<Table> getTableList() {
        return this.tableList;
    }

    public Configuration getHbaseConfiguration() {
        return this.hbaseConfiguration;
    }

    public org.apache.hadoop.hbase.client.Connection getHbaseConnection() {
        return this.hbaseConnection;
    }

    public Admin getHbaseAdmin() {
        try {
            return this.hbaseConnection.getAdmin();
        } catch (IOException e) {
            Utils.getLog().error("getHbaseAdmin exception", e);
        }
        return null;
    }

    public String toString() {
        return getName();
    }
}
