package org.hbaseexplorer.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTabbedPane;

import org.apache.commons.lang.StringUtils;
import org.hbaseexplorer.domain.Table;

/**
 *
 * @author zaharije
 */
public class DataTabPane extends JTabbedPane {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5295901442628109309L;

    private ArrayList<Table> tables;

    public DataTabPane() {
        super();

        tables = new ArrayList<Table>();
    }

    public void showTable(Table table) {
        int index = tableExists(table);

        if (index == -1) {
            tables.add(table);
            String tabName = table.getFullName().length() > 50 ? StringUtils.left(table.getFullName(), 50) + "..."
                : table.getFullName();
            this.addTab(tabName, null, new EditTableData(table), table.getFullName());
            index = tables.size() - 1;
        }
        // click handler
        this.addMouseListener(
            new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        DataTabPane tabPane = (DataTabPane)e.getComponent();
                        int idx = tabPane.indexAtLocation(e.getX(), e.getY());
                        if (idx >= 0) {
                            EditTableData com = (EditTableData)tabPane.getComponentAt(idx);
                            tabPane.remove(idx);
                            tables.remove(com.getTable());
                        }
                    }
                }
            });

        this.getModel().setSelectedIndex(index);
    }

    public int tableExists(Table table) {
        for (int i = 0; i < tables.size(); i++) {
            Table t = tables.get(i);
            if (t.getFullName().equals(table.getFullName())) {
                return i;
            }
        }
        return -1;
    }
}
