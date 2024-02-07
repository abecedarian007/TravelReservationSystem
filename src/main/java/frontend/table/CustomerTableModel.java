package frontend.table;

import backend.entity.Customer;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CustomerTableModel extends AbstractTableModel {
    private final List<Customer> customerList;
    private final String[] columnNames = {"Name", "ID"};

    public CustomerTableModel(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @Override
    public int getRowCount() {
        return customerList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer customer = customerList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return customer.getCustName();
            case 1:
                return customer.getCustID();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }
}
