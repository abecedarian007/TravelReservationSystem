package frontend;

import backend.entity.*;
import backend.service.QueryService;
import frontend.obj.InputPrompt;
import frontend.obj.ItemType;
import frontend.table.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

public class QueryInfoPage {
    private JPanel rootPanel;
    private JPanel queryInfoPanel;
    private JButton queryButton;
    private JTextField keyValueText;
    private JComboBox itemSelectBox;
    private JRadioButton queryAllButton;
    private JLabel titleLabel;
    private JLabel keyValueLabel;
    private JTable queryResultTable;
    private JScrollPane queryResultScrollPane;

    public QueryInfoPage() {

        itemSelectBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ItemType selectedItem = ItemType.fromString((String) itemSelectBox.getSelectedItem());
                    updateKeyValueLabel(selectedItem, keyValueLabel);
                    updateKeyValueText(selectedItem, keyValueText);
                } catch (IllegalArgumentException ex) {
                    // 捕获异常并提示用户
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Selection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        queryAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ItemType selectedItem = ItemType.fromString((String) itemSelectBox.getSelectedItem());
                    updateKeyValueLabel(selectedItem, keyValueLabel);
                    updateKeyValueText(selectedItem, keyValueText);
                } catch (IllegalArgumentException ex) {
                    // 捕获异常并提示用户
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Selection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        keyValueText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是枚举类InputPrompt的非InputPrompt.NULL类型，则清空
                if(isInputPromptType(keyValueText.getText())) {
                    keyValueText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，检查文本内容是否为InputPrompt.NULL类型，是则根据item选择设置设相应的InputPrompt
                ItemType selectedItem = ItemType.fromString((String) itemSelectBox.getSelectedItem());
                if (keyValueText.getText().equals(InputPrompt.NULL)) {
                    updateKeyValueText(selectedItem, keyValueText);
                }
            }
        });

        queryButton.addActionListener(new ActionListener() {
            private JDialog dialog;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (queryAllButton.isSelected()) {
                    try {
                        ItemType selectedItem = ItemType.fromString((String) itemSelectBox.getSelectedItem());
                        QueryService queryService = new QueryService();
                        String message = null;
                        int messageType = JOptionPane.WARNING_MESSAGE;

                        switch (selectedItem) {
                            case FLIGHT:
                                ResponseBody<List<Flight>> flightResponse = queryService.getAllInfo(ObjectType.FLIGHT);
                                message = flightResponse.getMessage();
                                messageType = flightResponse.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                if (flightResponse.isSuccess()) {
                                    List<Flight> flightList = flightResponse.getResponseObject();
                                    FlightTableModel flightTable = new FlightTableModel(flightList);
                                    queryResultTable.setModel(flightTable);
                                }
                                break;
                            case HOTEL:
                                ResponseBody<List<Hotel>> hotelResponse = queryService.getAllInfo(ObjectType.HOTEL);
                                message = hotelResponse.getMessage();
                                messageType = hotelResponse.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                if(hotelResponse.isSuccess()) {
                                    List<Hotel> hotelList = hotelResponse.getResponseObject();
                                    HotelTableModel hotelTableModel = new HotelTableModel(hotelList);
                                    queryResultTable.setModel(hotelTableModel);
                                }
                                break;
                            case BUS:
                                ResponseBody<List<Bus>> busResponse = queryService.getAllInfo(ObjectType.BUS);
                                message = busResponse.getMessage();
                                messageType = busResponse.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                if (busResponse.isSuccess()) {
                                    List<Bus> busList = busResponse.getResponseObject();
                                    BusTableModel busTableModel = new BusTableModel(busList);
                                    queryResultTable.setModel(busTableModel);
                                }
                                break;
                            case CUSTOMER:
                                ResponseBody<List<Customer>> customerResponse = queryService.getAllInfo(ObjectType.CUSTOMER);
                                message = customerResponse.getMessage();
                                messageType = customerResponse.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                if (customerResponse.isSuccess()) {
                                    List<Customer> customerList = customerResponse.getResponseObject();
                                    CustomerTableModel customerTableModel = new CustomerTableModel(customerList);
                                    queryResultTable.setModel(customerTableModel);
                                }
                                break;
                            case RESERVATION:
                                ResponseBody<List<Reservation>> reservationResponse = queryService.getAllInfo(ObjectType.RESERVATION);
                                message = reservationResponse.getMessage();
                                messageType = reservationResponse.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                if (reservationResponse.isSuccess()) {
                                    List<Reservation> reservationList = reservationResponse.getResponseObject();
                                    ReservationTableModel reservationTableModel = new ReservationTableModel(reservationList);
                                    queryResultTable.setModel(reservationTableModel);
                                }
                                break;
                            case RESERVATION_DETAIL:
                                ResponseBody<List<ReservationDetail>> reservationDetailResponse = queryService.getAllInfo(ObjectType.RESERVATION_DETAIL);
                                message = reservationDetailResponse.getMessage();
                                messageType = reservationDetailResponse.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                if (reservationDetailResponse.isSuccess()) {
                                    List<ReservationDetail> reservationDetailList = reservationDetailResponse.getResponseObject();
                                    ReservationDetailTableModel reservationDetailTableModel = new ReservationDetailTableModel(reservationDetailList);
                                    queryResultTable.setModel(reservationDetailTableModel);
                                }
                                break;
                        }
                        // 使用全局对话框对象，避免重复创建
                        if (dialog == null) {
                            dialog = new JDialog();
                            dialog.setSize(300, 200);
                            dialog.setLocationRelativeTo(null);
                        }
                        dialog.setTitle("Query information");
                        // 反馈查询情况
                        JOptionPane.showMessageDialog(dialog, message, "Query information", messageType);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(dialog, "Invalid selection! Please select an item", "Item not found", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    if (isInputValid()) {
                        try {
                            String queryKey = keyValueText.getText();
                            ItemType selectedItem = ItemType.fromString((String) itemSelectBox.getSelectedItem());
                            QueryService queryService = new QueryService();
                            String message = null;
                            int messageType = JOptionPane.WARNING_MESSAGE;

                            switch (selectedItem) {
                                case FLIGHT:
                                    ResponseBody<Flight> flightResponse = queryService.getSysInfo(ObjectType.FLIGHT, queryKey, Flight.class);
                                    message = flightResponse.getMessage();
                                    messageType = flightResponse.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                    if (flightResponse.isSuccess()) {
                                        List<Flight> flightList = new ArrayList<>();
                                        flightList.add(flightResponse.getResponseObject());
                                        FlightTableModel flightTable = new FlightTableModel(flightList);
                                        queryResultTable.setModel(flightTable);
                                    }
                                    break;
                                case HOTEL:
                                    ResponseBody<Hotel> hotelResponse = queryService.getSysInfo(ObjectType.HOTEL, queryKey, Hotel.class);
                                    message = hotelResponse.getMessage();
                                    messageType = hotelResponse.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                    if (hotelResponse.isSuccess()) {
                                        List<Hotel> hotelList = new ArrayList<>();
                                        hotelList.add(hotelResponse.getResponseObject());
                                        HotelTableModel hotelTableModel = new HotelTableModel(hotelList);
                                        queryResultTable.setModel(hotelTableModel);
                                    }
                                    break;
                                case BUS:
                                    ResponseBody<Bus> busResponse = queryService.getSysInfo(ObjectType.BUS, queryKey, Bus.class);
                                    message = busResponse.getMessage();
                                    messageType = busResponse.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                    if (busResponse.isSuccess()) {
                                        List<Bus> busList = new ArrayList<>();
                                        busList.add(busResponse.getResponseObject());
                                        BusTableModel busTableModel = new BusTableModel(busList);
                                        queryResultTable.setModel(busTableModel);
                                        break;
                                    }
                                case CUSTOMER:
                                    ResponseBody<Customer> customerResponse = queryService.getSysInfo(ObjectType.CUSTOMER, queryKey, Customer.class);
                                    message = customerResponse.getMessage();
                                    messageType = customerResponse.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                    if (customerResponse.isSuccess()) {
                                        List<Customer> customerList = new ArrayList<>();
                                        customerList.add(customerResponse.getResponseObject());
                                        CustomerTableModel customerTableModel = new CustomerTableModel(customerList);
                                        queryResultTable.setModel(customerTableModel);
                                    }
                                    break;
                                case RESERVATION:
                                    ResponseBody<List<Reservation>> reservationResponse = queryService.getReservationInfo(queryKey, false);
                                    message = reservationResponse.getMessage();
                                    messageType = reservationResponse.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                    if (reservationResponse.isSuccess()) {
                                        List<Reservation> reservationList = reservationResponse.getResponseObject();
                                        ReservationTableModel reservationTableModel = new ReservationTableModel(reservationList);
                                        queryResultTable.setModel(reservationTableModel);
                                    }
                                    break;
                                case RESERVATION_DETAIL:
                                    ResponseBody<List<ReservationDetail>> reservationDetailResponse = queryService.getReservationInfo(queryKey, true);
                                    message = reservationDetailResponse.getMessage();
                                    messageType = reservationDetailResponse.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                    if (reservationDetailResponse.isSuccess()) {
                                        List<ReservationDetail> reservationDetailList = reservationDetailResponse.getResponseObject();
                                        ReservationDetailTableModel reservationDetailTableModel = new ReservationDetailTableModel(reservationDetailList);
                                        queryResultTable.setModel(reservationDetailTableModel);
                                    }
                                    break;
                            }
                            // 使用全局对话框对象，避免重复创建
                            if (dialog == null) {
                                dialog = new JDialog();
                                dialog.setSize(300, 200);
                                dialog.setLocationRelativeTo(null);
                            }
                            dialog.setTitle("Query information");
                            // 反馈查询情况
                            JOptionPane.showMessageDialog(dialog, message, "Query information", messageType);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(dialog, "Invalid selection! Please select an item", "Item not found", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Please enter a valid key to query information", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
            private boolean isInputValid() {
                return  !keyValueText.getText().isEmpty() &&
                        !isInputPromptType(keyValueText.getText());
            }
        });
    }

    private void updateKeyValueLabel(ItemType selectedItem, JLabel keyValueLabel) {
        if (!queryAllButton.isSelected()) {
            switch(selectedItem){
                case FLIGHT:
                    keyValueLabel.setText("Query flight information");
                    break;
                case HOTEL:
                    keyValueLabel.setText("Query hotel information");
                    break;
                case BUS:
                    keyValueLabel.setText("Query bus information");
                    break;
                case CUSTOMER:
                    keyValueLabel.setText("Query customer information");
                    break;
                case RESERVATION:
                    keyValueLabel.setText("Query reservation basic information");
                    break;
                case RESERVATION_DETAIL:
                    keyValueLabel.setText("Query reservation detailed information");
                    break;
                default:
                    throw new IllegalArgumentException("Selected item is not in the given range");
            }
        } else {
            switch(selectedItem){
                case FLIGHT:
                    keyValueLabel.setText("Query ALL flight information"); break;
                case HOTEL:
                    keyValueLabel.setText("Query ALL hotel information"); break;
                case BUS:
                    keyValueLabel.setText("Query ALL bus information"); break;
                case CUSTOMER:
                    keyValueLabel.setText("Query ALL customer information"); break;
                case RESERVATION:
                    keyValueLabel.setText("Query ALL reservation basic information"); break;
                case RESERVATION_DETAIL:
                    keyValueLabel.setText("Query ALL reservation detailed information"); break;
                default:
                    throw new IllegalArgumentException("Selected item is not in the given range");
            }
        }
    }

    private void updateKeyValueText(ItemType selectedItem, JTextField keyValueText) {
        if (!queryAllButton.isSelected()) {
            switch(selectedItem){
                case FLIGHT:
                    keyValueText.setText(InputPrompt.FLIGHT_NUMBER);
                    break;
                case HOTEL:
                case BUS:
                    keyValueText.setText(InputPrompt.CITY);
                    break;
                case CUSTOMER:
                    keyValueText.setText(InputPrompt.CUSTOMER_NAME);
                    break;
                case RESERVATION:
                case RESERVATION_DETAIL:
                    keyValueText.setText(InputPrompt.RESERVATION);
                    break;
                default:
                    throw new IllegalArgumentException("Selected item is not in the given range");
            }
            keyValueText.setEditable(true);
        } else {
            keyValueText.setText("CAN'T ENTER");
            keyValueText.setEditable(false);
        }
    }

    private boolean isInputPromptType(@NotNull String text) {
        // 检查文本是否为InputPrompt中定义的类型
        return text.equals(InputPrompt.FLIGHT_NUMBER) ||
                text.equals(InputPrompt.CITY) ||
                text.equals(InputPrompt.PRICE) ||
                text.equals(InputPrompt.FLIGHT_TOTAL_SEATS) ||
                text.equals(InputPrompt.FLIGHT_AVAILABLE_SEATS) ||
                text.equals(InputPrompt.HOTEL_TOTAL_ROOMS) ||
                text.equals(InputPrompt.HOTEL_AVAILABLE_ROOMS) ||
                text.equals(InputPrompt.BUS_TOTAL_BUSES) ||
                text.equals(InputPrompt.BUS_AVAILABLE_BUSES) ||
                text.equals(InputPrompt.CUSTOMER_NAME) ||
                text.equals(InputPrompt.CUSTOMER_ID) ||
                text.equals(InputPrompt.RESERVATION);
    }

    public JPanel getPanel() {
        return rootPanel;
    }
}

