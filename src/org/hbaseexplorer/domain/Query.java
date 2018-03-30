/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hbaseexplorer.domain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.io.hfile.Compression.Algorithm;

/**
 *
 * @author xinqiyang
 */
public class Query {

    private HBaseAdmin hbaseAdmin;
    private String queryStr;

    public static final String[] SUPPORT_CMDS = new String[] {"create", "disable", "enable", "drop"};

    public Query(Configuration conf, String queryStr) throws MasterNotRunningException {
        Connection conn = new Connection(conf);
        this.hbaseAdmin = conn.getHbaseAdmin();
        this.queryStr = queryStr;
    }

    public Query(Connection conn, String queryStr) throws MasterNotRunningException {
        this.hbaseAdmin = conn.getHbaseAdmin();
        this.queryStr = queryStr;
    }

    public Query(HBaseAdmin hbaseAdmin, String queryStr) throws MasterNotRunningException {
        this.hbaseAdmin = hbaseAdmin;
        this.queryStr = queryStr;
    }

    public QueryResult runQuery() throws IOException {
        QueryResult result = null;
        String cmd = "";
        String query = this.queryStr;
        query = query.replaceAll("\\s{1,}", " ");
        if (query != null && query.length() > 0) {
            List<String> list = Arrays.asList(query.split(" "));
            if (!list.isEmpty()) {
                cmd = list.get(0).toLowerCase();
                String tableName = list.get(1);
                String columnFamily = list.get(1);
                if (StringUtils.isNotBlank(tableName)) {
                    for (int i = 0; i < SUPPORT_CMDS.length; i++) {
                        if (cmd.equals(SUPPORT_CMDS[i])) {
                            if (i == 0) {
                                HTableDescriptor td = new HTableDescriptor(tableName);
                                td.addFamily(new HColumnDescriptor(columnFamily).setCompressionType(Algorithm.GZ));
                                this.hbaseAdmin.createTable(td);
                                result = QueryResult.succ("create table ok");
                            }

                            if (i == 1) {
                                this.hbaseAdmin.disableTable(tableName);
                                result = QueryResult.succ("disable table ok");
                            }

                            if (i == 2) {
                                this.hbaseAdmin.enableTable(tableName);
                                result = QueryResult.succ("enable table ok");
                            }

                            if (i == 3) {
                                this.hbaseAdmin.disableTable(tableName);
                                this.hbaseAdmin.deleteTable(tableName);
                                result = QueryResult.succ("drop table ok");
                            }
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }
}
