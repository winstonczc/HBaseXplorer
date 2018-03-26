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
    private String queryS;

    public Query(Configuration conf, String queryStr) throws MasterNotRunningException {

        Connection con = new Connection(conf);
        this.hbaseAdmin = con.getHbaseAdmin();
        this.queryS = queryStr;
    }

    public String runQuery() throws IOException {

        String cmds[] = new String[] {"create", "disable", "enable", "drop"};

        String valRet = "";
        String cmd = "";

        String query = this.queryS;
        query = query.replaceAll("\\s{1,}", " ");
        if (query != null && query.length() > 0) {
            List<String> list = Arrays.asList(query.split(" "));
            if (!list.isEmpty()) {
                cmd = list.get(0).toLowerCase();
                String tableName = list.get(1);
                String columnFamily = list.get(1);
                if (StringUtils.isNotBlank(tableName)) {
                    for (int i = 0; i < 4; i++) {
                        if (cmd.equals(cmds[i])) {
                            // run logic then return
                            if (i == 0) {
                                // run create
                                HTableDescriptor td = new HTableDescriptor(tableName);
                                td.addFamily(new HColumnDescriptor(columnFamily).setCompressionType(Algorithm.GZ));
                                this.hbaseAdmin.createTable(td);
                                valRet = "create table ok";
                            }

                            if (i == 1) {
                                this.hbaseAdmin.disableTable(tableName);
                                valRet = "disable table ok";
                            }

                            if (i == 2) {
                                this.hbaseAdmin.enableTable(tableName);
                                valRet = "enable table ok";
                            }

                            if (i == 3) {
                                this.hbaseAdmin.disableTable(tableName);
                                this.hbaseAdmin.deleteTable(tableName);
                                valRet = "drop table ok";
                            }

                            // valRet = head;
                            break;
                        }
                    }
                }
            }
        }
        if (!valRet.isEmpty()) {
            return valRet;
        }
        return "run error,please check!";
    }
}
