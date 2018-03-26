package org.hbaseexplorer.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.hadoop.conf.Configuration;
import org.buddy.javatools.Utils;
import org.hbaseexplorer.HBaseExplorerView;
import org.hbaseexplorer.domain.Connection;
import org.hbaseexplorer.domain.Table;

/**
 *
 * @author zaharije
 */
public class ConnectionTree extends JTree {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2355154241501959396L;

    private Table table;

    private Connection currConn;

    private DefaultMutableTreeNode currConnNode;

    private HBaseExplorerView mainApp;

    public ConnectionTree() {
        super();

        // Double click handler
        addMouseListener(
            new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    int selRow = getRowForLocation(e.getX(), e.getY());
                    TreePath selPath = getPathForLocation(e.getX(), e.getY());
                    if (selRow != -1) {
                        if (e.getClickCount() == 1) {
                            // mySingleClick(selRow, selPath);
                            singleClick(selPath);
                        } else if (e.getClickCount() == 2) {
                            // myDoubleClick(selRow, selPath);
                            doubleClick(selPath);
                        }
                    }
                }
            });

    }

    public void createConnection(Configuration conf) {
        if (conf != null) {
            try {
                Connection conn = new Connection(conf);
                conn.connect();
                addConnectionToTree(conn);
            } catch (Exception ex) {
                Logger.getLogger(ConnectionTree.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // double click
    public void singleClick(TreePath selectionPath) {
        Log log = Utils.getLog();
        long start = System.currentTimeMillis();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selectionPath.getLastPathComponent();
        Object userObject = selectedNode.getUserObject();

        DefaultMutableTreeNode currNode = selectedNode;
        while (userObject == null || !(userObject instanceof Connection)) {
            currNode = (DefaultMutableTreeNode)currNode.getParent();
            if (currNode == null) {
                break;
            }
            userObject = currNode.getUserObject();
        }
        if (userObject != null && userObject instanceof Connection) {
            this.currConn = (Connection)userObject;
            this.currConnNode = currNode;
        }
        log.info("single click time time:" + (System.currentTimeMillis() - start));

    }

    // double click
    public void doubleClick(TreePath selectionPath) {
        Log log = Utils.getLog();
        long start = System.currentTimeMillis();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selectionPath.getLastPathComponent();

        Object userObject = selectedNode.getUserObject();
        if (userObject instanceof Table) {
            mainApp.getTabPane().showTable((Table)userObject);
        }

        DefaultMutableTreeNode currNode = selectedNode;
        while (userObject == null || !(userObject instanceof Connection)) {
            currNode = (DefaultMutableTreeNode)currNode.getParent();
            if (currNode == null) {
                break;
            }
            userObject = currNode.getUserObject();
        }
        if (userObject != null && userObject instanceof Connection) {
            this.currConn = (Connection)userObject;
            this.currConnNode = currNode;
        }
        log.info("double click time time:" + (System.currentTimeMillis() - start));

    }

    public void refreshCurrConnTables(String regx) {
        if (this.getCurrConn() != null) {
            this.getCurrConn().refreshTables(regx);
            DefaultMutableTreeNode tablesNode = (DefaultMutableTreeNode)this.getCurrConnNode().getChildAt(0);
            tablesNode.removeAllChildren();

            for (Table mtable : this.getCurrConn().getTableList()) {
                DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(mtable.getName(), true);
                tableNode.setUserObject(mtable);
                tablesNode.add(tableNode);
            }

            DefaultTreeModel defTreeModel = (DefaultTreeModel)getModel();
            defTreeModel.reload();
            for (int i = 0; i < getRowCount(); i++) {
                expandRow(i);
            }
        }
    }

    // add table to list
    private void addConnectionToTree(Connection conn) {
        Log log = Utils.getLog();
        DefaultTreeModel defTreeModel = (DefaultTreeModel)getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)getModel().getRoot();

        //
        log.debug(conn.getName());

        DefaultMutableTreeNode connNode = new DefaultMutableTreeNode(conn.getName(), true);
        DefaultMutableTreeNode tablesNode = new DefaultMutableTreeNode("Tables", true);

        connNode.setUserObject(conn);
        connNode.add(tablesNode);

        for (Table mtable : conn.getTableList()) {
            // log.info(mtable);
            DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(mtable.getName(), true);
            tableNode.setUserObject(mtable);
            tablesNode.add(tableNode);
        }

        DefaultMutableTreeNode countNode
            = new DefaultMutableTreeNode("Tables Count:" + tablesNode.getChildCount(), true);
        connNode.add(countNode);
        rootNode.add(connNode);

        defTreeModel.setRoot(rootNode);

        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
        if (this.getRowCount() == 1) {
            this.currConn = conn;
        }
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Connection getCurrConn() {
        return currConn;
    }

    public void setCurrConn(Connection currConn) {
        this.currConn = currConn;
    }

    public DefaultMutableTreeNode getCurrConnNode() {
        return currConnNode;
    }

    public void setCurrConnNode(DefaultMutableTreeNode currConnNode) {
        this.currConnNode = currConnNode;
    }

    public HBaseExplorerView getMainApp() {
        return mainApp;
    }

    public void setMainApp(HBaseExplorerView mainApp) {
        this.mainApp = mainApp;
    }
}
