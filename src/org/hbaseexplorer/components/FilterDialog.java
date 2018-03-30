/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FindDialog.java
 *
 * Created on May 22, 2010, 11:47:39 PM
 */

package org.hbaseexplorer.components;

import java.io.IOException;

import javax.swing.DefaultComboBoxModel;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.hbaseexplorer.domain.FilterModel;
import org.hbaseexplorer.domain.Table;
import org.hbaseexplorer.exception.ExplorerException;
import org.jdesktop.application.Action;

/**
 *
 * @author zaharije
 */
public class FilterDialog extends javax.swing.JDialog {

    /** 
     * serialVersionUID 
     */  
    private static final long serialVersionUID = 8897235212920225244L;
    private FilterModel filterModel;
    private Table table;

    /** Creates new form FindDialog */
    public FilterDialog(java.awt.Frame parent, Table table) {
        super(parent, true);
        this.filterModel = new FilterModel();
        this.table = table;
        initComponents();
        fillData();
        jLabelCompare.setText("CompareOP:");
        
    }

    private void fillData() {
        try {
            HColumnDescriptor[] descriptors = table.getHTable().getTableDescriptor().getColumnFamilies();
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            model.addElement("");
            for(HColumnDescriptor desc : descriptors) {
                model.addElement(desc.getNameAsString());
            }
            comboFamily.setModel(model);
        } catch (IOException ex) {
            throw new ExplorerException("Error getting column families.");
        }
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboFamily = new javax.swing.JComboBox();
        textColumn = new javax.swing.JTextField();
        textValue = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxCompare = new javax.swing.JComboBox();
        jLabelCompare = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setName("Form"); // NOI18N
        setResizable(false);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(2, 3));

        jLabel1.setLabelFor(comboFamily);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.hbaseexplorer.HBaseExplorerApp.class).getContext().getResourceMap(FilterDialog.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jPanel2.add(jLabel1);

        jLabel2.setLabelFor(textColumn);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jPanel2.add(jLabel2);

        jLabel3.setLabelFor(textValue);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        jPanel2.add(jLabel3);

        comboFamily.setName("comboFamily"); // NOI18N
        jPanel2.add(comboFamily);

        textColumn.setName("textColumn"); // NOI18N
        jPanel2.add(textColumn);

        textValue.setName("textValue"); // NOI18N
        jPanel2.add(textValue);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.hbaseexplorer.HBaseExplorerApp.class).getContext().getActionMap(FilterDialog.class, this);
        jButton1.setAction(actionMap.get("btnApply")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jLabel6.setName("jLabel6"); // NOI18N

        jComboBoxCompare.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "EQUAL", "LESS", "LESS_OR_EQUAL", "NOT_EQUAL", "GREATER_OR_EQUAL", "GREATER" }));
        jComboBoxCompare.setName("jCompare"); // NOI18N

        jLabelCompare.setLabelFor(comboFamily);
        jLabelCompare.setText(resourceMap.getString("jLabelCompare.text")); // NOI18N
        jLabelCompare.setName("jLabelCompare"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)
                            .add(jLabel6))
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jButton1)
                        .add(45, 45, 45))))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jComboBoxCompare, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 227, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(484, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(28, 28, 28)
                .add(jLabelCompare, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(627, 627, 627))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(24, 24, 24)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 50, Short.MAX_VALUE)
                        .add(jButton1)
                        .add(30, 30, 30))
                    .add(layout.createSequentialGroup()
                        .add(5, 5, 5)
                        .add(jLabelCompare, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBoxCompare, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void btnApply() {
        filterModel.setFamily(comboFamily.getSelectedItem().toString());
        filterModel.setColumn(textColumn.getText());
        filterModel.setValue(textValue.getText());
        filterModel.setCompareOP(jComboBoxCompare.getSelectedItem().toString());
        dispose();
    }

    public FilterModel getFilterModel() {
        return filterModel;
    }

    public void setFilterModel(FilterModel filterModel) {
        this.filterModel = filterModel;
        comboFamily.setSelectedItem(filterModel.getFamily());
        textColumn.setText(filterModel.getColumn());
        textValue.setText(filterModel.getValue());
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox comboFamily;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBoxCompare;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelCompare;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField textColumn;
    private javax.swing.JTextField textValue;
    // End of variables declaration//GEN-END:variables

}
