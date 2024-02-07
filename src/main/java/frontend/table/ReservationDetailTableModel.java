package frontend.table;

import backend.entity.ReservationDetail;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ReservationDetailTableModel extends AbstractTableModel {
    private final List<ReservationDetail> reservationDetailList;
    private final String[] columnNames = {"Reservation Key", "Customer Name", "Reservation Type",
            "Price", "Quantity", "Availability", "Departure", "Destination"};

    public ReservationDetailTableModel(List<ReservationDetail> reservationDetailList) {
        this.reservationDetailList = reservationDetailList;
    }

    @Override
    public int getRowCount() {
        return reservationDetailList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ReservationDetail reservationDetail = reservationDetailList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return reservationDetail.getResvKey();
            case 1:
                return reservationDetail.getCustName();
            case 2:
                return reservationDetail.getResvType();
            case 3:
                return reservationDetail.getPrice();
            case 4:
                return reservationDetail.getQuantity();
            case 5:
                return reservationDetail.getAvailability();
            case 6:
                return reservationDetail.getLocationFrom();
            case 7:
                return reservationDetail.getLocationTo();
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
        if (columnIndex == 2 || columnIndex == 3 || columnIndex == 4 || columnIndex == 5) {
            return Integer.class; // Set the class type to Integer for sorting
        }
        return super.getColumnClass(columnIndex);
    }
}
