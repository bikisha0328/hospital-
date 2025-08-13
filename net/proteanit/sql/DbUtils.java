package net.proteanit.sql;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public final class DbUtils {
    private DbUtils() {}

    public static TableModel resultSetToTableModel(ResultSet rs) {
        try {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = meta.getColumnLabel(i) != null && !meta.getColumnLabel(i).isEmpty()
                        ? meta.getColumnLabel(i)
                        : meta.getColumnName(i);
            }
            DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
            return model;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert ResultSet to TableModel", e);
        }
    }
}