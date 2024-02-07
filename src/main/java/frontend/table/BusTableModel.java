package frontend.table;

import backend.entity.Bus;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BusTableModel extends AbstractTableModel {
    private final List<Bus> busList;
    private final String[] columnName = {"Location", "Price", "Total Buses", "Available Buses"};

    public BusTableModel(List<Bus> busList) {
        this.busList = busList;
    }

    @Override
    public int getRowCount() {
        return busList.size();
    }

    @Override
    public int getColumnCount() {
        return columnName.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Bus bus = busList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return bus.getLocation();
            case 1:
                return bus.getPrice();
            case 2:
                return bus.getNumBus();
            case 3:
                return bus.getNumAvail();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnName[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 1 || columnIndex == 2 || columnIndex == 3) {
            return Integer.class; // Set the class type to Integer for sorting
        }
        return super.getColumnClass(columnIndex);
    }
}
