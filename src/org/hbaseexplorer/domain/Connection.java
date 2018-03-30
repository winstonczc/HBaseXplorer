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
import org.apache.hadoop.hbase.client.HBaseAdmin;
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

    private HBaseAdmin hbaseAdmin;

    private ArrayList<HTableWapper> tableList;

    public Connection(Configuration configuration) {
        hbaseConfiguration = HBaseConfiguration.create(configuration);
        String resource = "./conf/hbase.xml";
        Path path = new Path(resource);
        hbaseConfiguration.addResource(path);
        tableList = new ArrayList<HTableWapper>();
    }

    public void connect() throws ZooKeeperConnectionException, IOException {
        try {
            hbaseAdmin = new HBaseAdmin(hbaseConfiguration);
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
            tableList = new ArrayList<HTableWapper>();
            HTableDescriptor[] hTables = null;
            if (StringUtils.isNotBlank(regx)) {
                hTables = this.hbaseAdmin.listTables(regx);
            } else {
                hTables = this.hbaseAdmin.listTables();
            }

            Log log = Utils.getLog();
            log.info("*****table");
            log.info(hTables.length);

            for (HTableDescriptor tableDescriptor : hTables) {
                tableList.add(new HTableWapper(tableDescriptor, this));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new ExplorerException("Error while getting table list!");
        }
    }

    public String getName() {
        return hbaseConfiguration.get("hbase.zookeeper.quorum");
    }

    public ArrayList<HTableWapper> getTableList() {
        return tableList;
    }

    public Configuration getHbaseConfiguration() {
        return hbaseConfiguration;
    }

    public HBaseAdmin getHbaseAdmin() {
        return this.hbaseAdmin;
    }

    public String toString() {
        return getName();
    }
}
