package org.hbaseexplorer.components;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.hadoop.conf.Configuration;
import org.buddy.javatools.Utils;
import org.hbaseexplorer.HBaseExplorerView;
import org.hbaseexplorer.domain.Connection;
import org.hbaseexplorer.domain.HTableWapper;

/**
 *
 * @author zaharije
 */
public class ConnectionTree extends JTree {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2355154241501959396L;

    private HTableWapper currTable;

    private DefaultMutableTreeNode currTableNode;

    private Connection currConn;

    private DefaultMutableTreeNode currConnNode;

    private HBaseExplorerView mainApp;

    public static String TABLE_REGX = "";

    public ConnectionTree() {
        super();
        ConnectionTree that = this;

        final JPopupMenu pop = new JPopupMenu();

        pop.add(new AbstractAction("刷新") {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                Connection currConn = that.getCurrConn();
                if (currConn != null) {
                    refreshCurrConnTables(TABLE_REGX);
                } else {
                    JOptionPane.showMessageDialog(null, "please select any connection you want to refresh");
                }
            }
        });
        pop.add(new AbstractAction("断开") {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                Connection currConn = that.getCurrConn();
                if (currConn != null) {
                    that.removeConnectionFromTree(currConn);
                } else {
                    JOptionPane.showMessageDialog(null, "please select any connection you want to disconnect");
                }
            }
        });

        pop.add(new javax.swing.JPopupMenu.Separator());
        pop.add(new AbstractAction("删除表") {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                HTableWapper currTable = that.getCurrTable();
                DefaultMutableTreeNode currTableNode = that.getCurrTableNode();
                Connection currConn = that.getCurrConn();
                DefaultMutableTreeNode currConnNode = that.getCurrConnNode();
                if (currTable != null && currConn != null) {
                    int selected
                        = JOptionPane.showConfirmDialog(null,
                            "are you sure to delete table[" + currTable.getName() + "]", "Confirm",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (JOptionPane.YES_OPTION == selected) {
                        try {
                            currConn.getHbaseAdmin().disableTable(currTable.getName());
                            currConn.getHbaseAdmin().deleteTable(currTable.getName());
                            ((DefaultMutableTreeNode)currConnNode.getChildAt(0)).remove(currTableNode);
                            that.setCurrTable(null);
                            that.setCurrTableNode(null);
                            that.reloadTree();
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(null, "delete table exception");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "please select any table you want to delete");
                }
            }
        });

        // click handler
        this.addMouseListener(
            new MouseAdapter() {

                public void mouseReleased(MouseEvent e) {
                    if (e.isMetaDown()) {
                        pop.show(that, e.getX(), e.getY());
                    }
                }

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
        if (userObject instanceof HTableWapper) {
            this.currTable = (HTableWapper)userObject;
            this.currTableNode = selectedNode;
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
        log.info("single click time time:" + (System.currentTimeMillis() - start));

    }

    // double click
    public void doubleClick(TreePath selectionPath) {
        Log log = Utils.getLog();
        long start = System.currentTimeMillis();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selectionPath.getLastPathComponent();

        Object userObject = selectedNode.getUserObject();
        if (userObject instanceof HTableWapper) {
            this.mainApp.getTabPane().showTable((HTableWapper)userObject);
            this.currTable = (HTableWapper)userObject;
            this.currTableNode = selectedNode;
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

    public void reloadTree() {
        DefaultTreeModel defTreeModel = (DefaultTreeModel)getModel();
        defTreeModel.reload();
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
    }

    public void refreshCurrConnTables(String regx) {
        if (this.getCurrConn() != null) {
            this.getCurrConn().refreshTables(regx);
            DefaultMutableTreeNode tablesNode = (DefaultMutableTreeNode)this.getCurrConnNode().getChildAt(0);
            tablesNode.removeAllChildren();

            for (HTableWapper mtable : this.getCurrConn().getTableList()) {
                DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(mtable.getName(), true);
                tableNode.setUserObject(mtable);
                tablesNode.add(tableNode);
            }

            reloadTree();
        }
    }

    // add conn to tree
    public void addConnectionToTree(Connection conn) {
        Log log = Utils.getLog();
        DefaultTreeModel defTreeModel = (DefaultTreeModel)getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)getModel().getRoot();

        //
        log.debug(conn.getName());

        DefaultMutableTreeNode connNode = new DefaultMutableTreeNode(conn.getName(), true);
        DefaultMutableTreeNode tablesNode = new DefaultMutableTreeNode("Tables", true);

        connNode.setUserObject(conn);
        connNode.add(tablesNode);

        for (HTableWapper mtable : conn.getTableList()) {
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
            this.currConnNode = connNode;
        }
    }

    // remove curr conn to tree
    public void removeConnectionFromTree(Connection conn) {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)getModel().getRoot();
        Enumeration<?> er = rootNode.children();
        while (er.hasMoreElements()) {
            DefaultMutableTreeNode cn = (DefaultMutableTreeNode)er.nextElement();
            if (cn.getUserObject().equals(conn)) {
                rootNode.remove(cn);
                break;
            }
        }
        this.reloadTree();
    }

    public HTableWapper getCurrTable() {
        return currTable;
    }

    public void setCurrTable(HTableWapper currTable) {
        this.currTable = currTable;
    }

    public DefaultMutableTreeNode getCurrTableNode() {
        return currTableNode;
    }

    public void setCurrTableNode(DefaultMutableTreeNode currTableNode) {
        this.currTableNode = currTableNode;
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
