/*
 * HBaseExplorerView.java
 */
package org.hbaseexplorer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HConstants;
import org.buddy.javatools.BuddyFile;
import org.buddy.javatools.Utils;
import org.hbaseexplorer.common.HConfConstants;
import org.hbaseexplorer.components.ConnectionTree;
import org.hbaseexplorer.components.DataTabPane;
import org.hbaseexplorer.domain.Query;
import org.hbaseexplorer.domain.QueryResult;
import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.TaskMonitor;

/**
 * The application's main frame.
 */
public final class HBaseExplorerView extends FrameView {

    private Configuration localconf;

    public HBaseExplorerView(SingleFrameApplication app) {
        super(app);
        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        newConnectionAction();
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = HBaseExplorerApp.getApplication().getMainFrame();
            aboutBox = new HBaseExplorerAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        HBaseExplorerApp.getApplication().show(aboutBox);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        conTree = new org.hbaseexplorer.components.ConnectionTree();
        tabPane = new DataTabPane();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Connections");
        conTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        conTree.setName("conTree"); // NOI18N
        jScrollPane1.setViewportView(conTree);

        jSplitPane1.setLeftComponent(jScrollPane1);

        tabPane.setName("tabPane"); // NOI18N
        jSplitPane1.setRightComponent(tabPane);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE));

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
            .getInstance(org.hbaseexplorer.HBaseExplorerApp.class).getContext().getResourceMap(HBaseExplorerView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap
            = org.jdesktop.application.Application.getInstance(org.hbaseexplorer.HBaseExplorerApp.class).getContext()
                .getActionMap(HBaseExplorerView.class, this);
        jMenuItem2.setAction(actionMap.get("runQueryAction")); // NOI18N
        jMenuItem2.setAccelerator(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.setToolTipText("only support create、disable、enable、drop table now");
        fileMenu.add(jMenuItem2);

        jMenuItem3.setAction(actionMap.get("filterTableAction")); // NOI18N
        jMenuItem3.setAccelerator(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem2"); // NOI18N
        jMenuItem3.setToolTipText("use regx to filter current connection tables");
        fileMenu.add(jMenuItem3);

        jMenuItem1.setAction(actionMap.get("newConnectionAction")); // NOI18N
        jMenuItem1.setAccelerator(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        fileMenu.add(jMenuItem1);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setAccelerator(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setValue(10);
        progressBar.setName("progressBar"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
                .add(statusPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(statusMessageLabel)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 491, Short.MAX_VALUE)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(statusAnimationLabel)
                    .addContainerGap()));
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(statusPanelLayout.createSequentialGroup()
                    .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(statusMessageLabel)
                        .add(statusAnimationLabel)
                        .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                            org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                            org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(3, 3, 3)));

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void newConnectionAction() {

        String strConfig = "";
        try {
            String config = BuddyFile.get("hbasexplorerconfig.ini");
            if (config.length() > 0) {
                strConfig = config;
            }
        } catch (FileNotFoundException ex) {
            Utils.getLog().error(null, ex);
        } catch (IOException ex) {
            Utils.getLog().error(null, ex);
        }

        Utils.getLog().debug("hbasexplorerconfig.ini ZK:" + strConfig);
        JOptionPane.getRootFrame().setAlwaysOnTop(true);

        String confJson = "";
        Map<String, String> confs = null;
        JTextArea msg = new JTextArea(strConfig);
        msg.setRows(10);
        msg.setColumns(100);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(msg);
        int selected
            = JOptionPane.showConfirmDialog(null, scrollPane, "input hbase conf(json)", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (JOptionPane.YES_OPTION == selected) {
            confJson = msg.getText();
            if (StringUtils.isNotBlank(confJson)) {
                confs = new Gson().fromJson(confJson, new TypeToken<Map<String, String>>() {}.getType());
            } else {
                JOptionPane.showMessageDialog(null, "please input the conf(json)");
            }
        }

        new TaskMonitor(getApplication().getContext());

        Utils.getLog().error("input conf:" + confJson);

        if (MapUtils.isNotEmpty(confs) && StringUtils.isNotBlank(confs.get(HConstants.ZOOKEEPER_QUORUM))) {
            try {
                Configuration conf = new Configuration();
                conf.set(HConstants.ZOOKEEPER_QUORUM, confs.get(HConstants.ZOOKEEPER_QUORUM));
                conf.set(HConstants.ZOOKEEPER_CLIENT_PORT,
                    MapUtils.getString(confs, HConstants.ZOOKEEPER_CLIENT_PORT, "2181"));
                conf.set(HConstants.HBASE_CLIENT_RETRIES_NUMBER,
                    MapUtils.getString(confs, HConstants.HBASE_CLIENT_RETRIES_NUMBER, "3"));

                if (StringUtils.isNotBlank(confs.get(HConstants.ZOOKEEPER_ZNODE_PARENT))) {
                    conf.set(HConstants.ZOOKEEPER_ZNODE_PARENT, confs.get(HConstants.ZOOKEEPER_ZNODE_PARENT));
                }
                if (StringUtils.isNotBlank(confs.get(HConfConstants.TABLE_FILTER_REGX))) {
                    conf.set(HConfConstants.TABLE_FILTER_REGX, confs.get(HConfConstants.TABLE_FILTER_REGX));
                    ConnectionTree.TABLE_REGX = confs.get(HConfConstants.TABLE_FILTER_REGX);
                }

                getTree().createConnection(conf);
                getTree().setMainApp(this);
                // @TODO:add by xinqiyang
                // save it to file
                try {
                    BuddyFile.write("hbasexplorerconfig.ini", confJson, false);
                } catch (IOException ex) {
                    Utils.getLog().error(null, ex);
                }

            } catch (Exception ex) {
                Utils.getLog().error(null, ex);
            }
        }
    }

    public ConnectionTree getTree() {
        return (ConnectionTree)conTree;
    }

    public DataTabPane getTabPane() {
        return (DataTabPane)tabPane;
    }

    // @TODO: Need update
    // do run query
    @Action
    public void runQueryAction() throws IOException {
        if (this.getTree().getCurrConn() == null) {
            JOptionPane.showMessageDialog(null, "please select a connection to run query first");
            return;
        }
        StringBuilder tips = new StringBuilder("command support:" + System.lineSeparator());
        for (String cmd : Query.SUPPORT_CMDS) {
            tips.append(cmd).append(System.lineSeparator());
        }
        JTextArea msg = new JTextArea(tips.toString());
        msg.setRows(6);
        msg.setColumns(100);
        msg.setForeground(Color.GRAY);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);
        msg.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (msg.getText().startsWith("command support")) {
                    msg.setText("");
                    msg.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (msg.getText().isEmpty()) {
                    msg.setForeground(Color.GRAY);
                    msg.setText(tips.toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(msg);

        int selected = JOptionPane.showConfirmDialog(null, scrollPane, "input the hbase command",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (JOptionPane.YES_OPTION == selected) {
            String runQuery = msg.getText();
            if (StringUtils.isNotBlank(runQuery)) {
                // run the query
                Query q = new Query(this.getTree().getCurrConn(), runQuery);
                QueryResult result = q.runQuery();
                JOptionPane.showMessageDialog(null, result.getMsg());
                if (result.isSucc()) {
                    // reload tree and data
                    // getTree().setMainApp(this);
                    getTree().refreshCurrConnTables(ConnectionTree.TABLE_REGX);
                }

            } else {
                JOptionPane.showMessageDialog(null, "please input the query string");
            }
        }
    }

    @Action
    public void filterTableAction() {
        if (this.getTree().getCurrConn() == null) {
            JOptionPane.showMessageDialog(null, "please click any connection you want to filter first");
            return;
        }
        JTextField msg = new JTextField(ConnectionTree.TABLE_REGX);
        msg.setColumns(100);

        JScrollPane scrollPane = new JScrollPane(msg);

        int selected = JOptionPane.showConfirmDialog(null, scrollPane, "input the regx expression",
            JOptionPane.OK_CANCEL_OPTION);
        if (JOptionPane.YES_OPTION == selected) {
            String regx = msg.getText();
            if (StringUtils.isNotBlank(regx)) {
                ConnectionTree.TABLE_REGX = regx;
                this.getTree().refreshCurrConnTables(regx);
            } else {
                JOptionPane.showMessageDialog(null, "please input the regx expression");
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree conTree;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTabbedPane tabPane;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
}
