/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewColumnDialog.java
 *
 * Created on May 22, 2010, 9:28:03 PM
 */

package org.hbaseexplorer.components;

import org.jdesktop.application.Action;

/**
 * @description
 * @author <a href="mailto:zhicong.czc@alibaba-inc.com">zhicong.czc</a>
 * @since 2017年10月28日
 * @version 1.0.0
 */
public class NewRowDialog extends javax.swing.JDialog {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -9055717435302019569L;
    private String rowKey;

    /** Creates new form NewColumnDialog */
    public NewRowDialog(java.awt.Frame parent) {
        super(parent, true);
        this.rowKey = null;
        initComponents();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rowKeyLabel = new javax.swing.JLabel();
        rowKeyTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
            .getInstance(org.hbaseexplorer.HBaseExplorerApp.class).getContext().getResourceMap(NewRowDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setModal(true);
        setName("Form"); // NOI18N
        setResizable(false);

        rowKeyLabel.setText(resourceMap.getString("rowKey.text")); // NOI18N
        rowKeyLabel.setName("rowKey"); // NOI18N

        rowKeyTextField.setText(resourceMap.getString("rowKeyTextField.text")); // NOI18N
        rowKeyTextField.setName("rowKeyTextField"); // NOI18N

        javax.swing.ActionMap actionMap
            = org.jdesktop.application.Application.getInstance(org.hbaseexplorer.HBaseExplorerApp.class).getContext()
                .getActionMap(NewRowDialog.class, this);
        jButton1.setAction(actionMap.get("btnAddClickAction")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setAction(actionMap.get("btnCancelClickAction")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(rowKeyLabel))
                            .add(18, 18, 18)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(rowKeyTextField, 0, 248, Short.MAX_VALUE))
                            .addContainerGap())
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                            .add(jButton1)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                            .add(jButton2)
                            .add(68, 68, 68)))));
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(rowKeyLabel)
                        .add(rowKeyTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                            org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                            org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                        .add(jButton2)
                        .add(jButton1))
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void btnAddClickAction() {
        this.rowKey = rowKeyTextField.getText();
        dispose();
    }

    @Action
    public void btnCancelClickAction() {
        dispose();
    }

    public String getRowKey() {
        return rowKey;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel rowKeyLabel;
    private javax.swing.JTextField rowKeyTextField;
    // End of variables declaration//GEN-END:variables

}
