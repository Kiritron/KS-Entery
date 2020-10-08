/*
 * Copyright 2020 Kiritron's Space
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.kiritron.entery.core.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.cef.callback.CefWebPluginInfoVisitor;
import org.cef.network.CefWebPluginInfo;
import org.cef.network.CefWebPluginManager;

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

@SuppressWarnings("serial")
public class WebPluginManagerDialog extends JDialog {
    private final CefWebPluginManager manager = CefWebPluginManager.getGlobalManager();
    private final PluginTableModel tblModel = new PluginTableModel();

    public WebPluginManagerDialog(Frame owner, String title) {
        super(owner, title, false);
        setLayout(new BorderLayout());
        setSize(800, 600);

        JTable pluginTable = new JTable(tblModel);
        pluginTable.setFillsViewportHeight(true);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        JButton delButton = new JButton("Remove selected plugins");
        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tblModel.removeSelected();
            }
        });
        controlPanel.add(delButton);

        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        controlPanel.add(doneButton);

        add(new JScrollPane(pluginTable));
        add(controlPanel, BorderLayout.SOUTH);

        if (manager == null) throw new NullPointerException("Plugin manager is null");
        manager.visitPlugins(tblModel);
    }

    private class PluginTableModel extends AbstractTableModel implements CefWebPluginInfoVisitor {
        private final String[] columnNames;
        private Vector<Object[]> rowData = new Vector<>();

        public PluginTableModel() {
            super();
            columnNames = new String[] {"Имя", "Путь", "Версия", "Описание", ""};
        }

        // add an entry to the table
        @Override
        public boolean visit(CefWebPluginInfo info, int count, int total) {
            Object[] entry = {info.getName(), info.getPath(), info.getVersion(),
                    info.getDescription(), new Boolean(false)};
            int row = rowData.size();
            rowData.addElement(entry);
            fireTableRowsInserted(row, row);

            return true;
        }

        public void removeSelected() {
            for (int i = 0; i < rowData.size(); ++i) {
                if ((Boolean) rowData.get(i)[4]) {
                    String path = (String) rowData.get(i)[1];
                    rowData.remove(i);
                    fireTableRowsDeleted(i, i);
                    i--;
                }
            }
            manager.refreshPlugins();
        }

        public int getRowCount() {
            return rowData.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (rowData.size() > 0) return rowData.get(0)[columnIndex].getClass();
            return Object.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return (columnIndex == 4);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return rowData.get(rowIndex)[columnIndex];
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            rowData.get(rowIndex)[columnIndex] = aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
}
