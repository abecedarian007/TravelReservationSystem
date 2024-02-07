package frontend.table;

import backend.entity.Hotel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class HotelTableModel extends AbstractTableModel {

    private final List<Hotel> hotelList;
    private final String[] columnNames = {"Location", "Price", "Total Rooms", "Available Rooms"};

    public HotelTableModel(List<Hotel> hotelList){
        this.hotelList = hotelList;
    }

    @Override
    public int getRowCount() {
        return hotelList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Hotel hotel = hotelList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return hotel.getLocation();
            case 1:
                return hotel.getPrice();
            case 2:
                return hotel.getNumRooms();
            case 3:
                return hotel.getNumAvail();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 1 || columnIndex == 2 || columnIndex == 3) {
            return Integer.class; // Set the class type to Integer for sorting
        }
        return super.getColumnClass(columnIndex);
    }
}
