package frontend.table;
import backend.entity.Flight;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FlightTableModel extends AbstractTableModel {
    private final List<Flight> flightList;
    private final String[] columnNames = {"Flight Number", "Price", "Total Seats", "Available Seats", "Departure", "Destination"};

    public FlightTableModel(List<Flight> flightList) {
        this.flightList = flightList;
    }

    @Override
    public int getRowCount() {
        return flightList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Flight flight = flightList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return flight.getFlightNum();
            case 1:
                return flight.getPrice();
            case 2:
                return flight.getNumSeats();
            case 3:
                return flight.getNumAvail();
            case 4:
                return flight.getFromCity();
            case 5:
                return flight.getArivCity();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 1 || columnIndex == 2 || columnIndex == 3) {
            return Integer.class; // Set the class type to Integer for sorting
        }
        return super.getColumnClass(columnIndex);
    }
}
