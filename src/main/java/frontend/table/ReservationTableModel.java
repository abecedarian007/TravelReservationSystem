package frontend.table;

import backend.entity.Reservation;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ReservationTableModel extends AbstractTableModel{

    private final List<Reservation> reservationList;
    private final String[] columnNames = {"Reservation Key", "Customer Name", "Reservation Type"};

    public ReservationTableModel(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @Override
    public int getRowCount() {
        return reservationList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Reservation reservation = reservationList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return reservation.getResvKey();
            case 1:
                return reservation.getCustName();
            case 2:
                return reservation.getResvType();
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
        if (columnIndex == 2) {
            return Integer.class; // Set the class type to Integer for sorting
        }
        return super.getColumnClass(columnIndex);
    }
}
