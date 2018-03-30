package org.hbaseexplorer.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTabbedPane;

import org.apache.commons.lang.StringUtils;
import org.buddy.javatools.Utils;
import org.hbaseexplorer.domain.HTableWapper;

/**
 *
 * @author zaharije
 */
public class DataTabPane extends JTabbedPane {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5295901442628109309L;

    private ArrayList<HTableWapper> tables;

    public DataTabPane() {
        super();
        tables = new ArrayList<HTableWapper>();

        // click handler
        this.addMouseListener(
            new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        DataTabPane tabPane = (DataTabPane)e.getComponent();
                        int idx = tabPane.indexAtLocation(e.getX(), e.getY());
                        if (idx >= 0) {
                            EditTableDataPanel com = (EditTableDataPanel)tabPane.getComponentAt(idx);
                            tabPane.remove(idx);
                            tables.remove(com.getTable());
                            com = null;
                        }
                    }
                }
            });
    }

    public void showTable(HTableWapper table) {
        int index = tableExists(table);

        if (index == -1) {
            tables.add(table);
            String tabName = table.getFullName().length() > 50 ? StringUtils.left(table.getFullName(), 50) + "..."
                : table.getFullName();
            this.addTab(tabName, null, new EditTableDataPanel(table), table.getFullName());
            index = tables.size() - 1;
        }

        this.getModel().setSelectedIndex(index);
    }

    public int tableExists(HTableWapper table) {
        for (int i = 0; i < tables.size(); i++) {
            HTableWapper t = tables.get(i);
            if (t.getFullName().equals(table.getFullName())) {
                return i;
            }
        }
        return -1;
    }
}
