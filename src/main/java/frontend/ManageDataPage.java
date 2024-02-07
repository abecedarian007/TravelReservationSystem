package frontend;

import backend.entity.Response;
import backend.service.LoadService;
import frontend.obj.InputPrompt;
import frontend.obj.OperationType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.*;

public class ManageDataPage {
    // 面板
    private JPanel rootPanel;
    private JPanel manageDataPanel;
    // 标题
    private JLabel titleLabel;
    // 多页签组件
    private JTabbedPane tabGroup;
    private JPanel flightTab;
    private JPanel hotelTab;
    private JPanel busTab;
    private JPanel customerTab;
    // 飞机信息组件
    private JButton flightSubmitButton;
    private JTextField flightNumText;
    private JTextField flightPriceText;
    private JTextField availSeatsText;
    private JTextField totalSeatsText;
    private JTextField fromCityText;
    private JTextField arivCityText;
    private JLabel flightNumberLabel;
    private JLabel flightPriceLabel;
    private JLabel availSeatsLabel;
    private JLabel totalSeatsLabel;
    private JLabel fromCityLabel;
    private JLabel arivCityLabel;
    // 酒店信息组件
    private JButton hotelSubmitButton;
    private JTextField hotelLocationText;
    private JTextField hotelPriceText;
    private JTextField totalRoomsText;
    private JTextField availRoomsText;
    private JLabel hotelLocationLabel;
    private JLabel hotelPriceLabel;
    private JLabel totalRoomsLabel;
    private JLabel availRoomsLabel;
    // 大巴信息组件
    private JButton busSubmitButton;
    private JTextField busLocationText;
    private JTextField busPriceText;
    private JTextField totalBusText;
    private JTextField availBusText;
    private JLabel busLocationLabel;
    private JLabel busPriceLabel;
    private JLabel totalBusLabel;
    private JLabel availBusLabel;
    // 客户信息组件
    private JButton customerSubmitButton;
    private JTextField custNameText;
    private JTextField custIdText;
    private JLabel custNameLabel;
    private JLabel custIdLabel;
    // 选择入库数据或更新数据（单选）
    private JRadioButton loadRadioButton;
    private JRadioButton updateRadioButton;

    public ManageDataPage(){
        LoadService loadService = new LoadService();

        ButtonGroup manageDataGroupButton = new ButtonGroup();
        manageDataGroupButton.add(loadRadioButton);
        manageDataGroupButton.add(updateRadioButton);

        loadRadioButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setButtonsText(OperationType.LOAD);
                }
            }
        });
        updateRadioButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setButtonsText(OperationType.UPDATE);
                }
            }
        });

        // 提交航班信息
        flightSubmitButton.addActionListener(new ActionListener() {
            private JDialog dialog;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loadRadioButton.isSelected() || updateRadioButton.isSelected()) {
                    if (isInputValid()) {
                        try {
                            String flight_number = flightNumText.getText();
                            int flight_price = Integer.parseInt(flightPriceText.getText());
                            int flight_totalSeat = Integer.parseInt(totalSeatsText.getText());
                            int flight_availSeat = Integer.parseInt(availSeatsText.getText());
                            String flight_fromCity = fromCityText.getText();
                            String flight_arivCity = arivCityText.getText();

                            Response response;
                            if (loadRadioButton.isSelected()) {
                                response = loadService.loadFlightData(flight_number, flight_price, flight_totalSeat,
                                        flight_availSeat, flight_fromCity, flight_arivCity);
                            } else {
                                response = loadService.updateFlightData(flight_number, flight_price, flight_totalSeat,
                                        flight_availSeat, flight_fromCity, flight_arivCity);
                            }
                            String message = response.getMessage();

                            // 使用全局对话框对象，避免重复创建
                            if (dialog == null) {
                                dialog = new JDialog();
                                dialog.setSize(300, 200);
                                dialog.setLocationRelativeTo(null);
                            }

                            dialog.setTitle("Manage flight information");

                            int messageType = response.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

                            // 反馈提交情况
                            JOptionPane.showMessageDialog(dialog, message, "Manage flight information", messageType);
                        } catch (NumberFormatException ex) {
                            // 处理用户输入不是整数的情况
                            JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter valid integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Please enter all required information.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }  else {
                    // 用户未选择任何操作
                    JOptionPane.showMessageDialog(dialog, "Please select 'Load' or 'Update' operation.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            private boolean isInputValid() {
                return  !flightNumText.getText().isEmpty() &&
                        !flightPriceText.getText().isEmpty() &&
                        !totalSeatsText.getText().isEmpty() &&
                        !availSeatsText.getText().isEmpty() &&
                        !fromCityText.getText().isEmpty() &&
                        !arivCityText.getText().isEmpty() &&
                        !flightNumText.getText().equals(InputPrompt.FLIGHT_NUMBER) &&
                        !flightPriceText.getText().equals(InputPrompt.PRICE) &&
                        !totalSeatsText.getText().equals(InputPrompt.FLIGHT_TOTAL_SEATS) &&
                        !availSeatsText.getText().equals(InputPrompt.FLIGHT_AVAILABLE_SEATS) &&
                        !fromCityText.getText().equals(InputPrompt.CITY) &&
                        !arivCityText.getText().equals(InputPrompt.CITY);
            }
        });

        // 提交酒店信息
        hotelSubmitButton.addActionListener(new ActionListener() {
            private JDialog dialog;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loadRadioButton.isSelected() || updateRadioButton.isSelected()) {
                    if (isInputValid()){
                        try {
                            String hotel_location = hotelLocationText.getText();
                            int hotel_price = Integer.parseInt(hotelPriceText.getText());
                            int hotel_totalRoom = Integer.parseInt(totalRoomsText.getText());
                            int hotel_availRoom = Integer.parseInt(availRoomsText.getText());

                            Response response;
                            if (loadRadioButton.isSelected()) {
                                response = loadService.loadHotelData(hotel_location, hotel_price, hotel_totalRoom, hotel_availRoom);
                            } else {
                                response = loadService.updateHotelData(hotel_location, hotel_price, hotel_totalRoom, hotel_availRoom);
                            }
                            String message = response.getMessage();

                            // 使用全局对话框对象，避免重复创建
                            if (dialog == null) {
                                dialog = new JDialog();
                                dialog.setSize(300, 200);
                                dialog.setLocationRelativeTo(null);
                            }
                            dialog.setTitle("Manage hotel information");

                            int messageType = response.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

                            // 反馈提交情况
                            JOptionPane.showMessageDialog(dialog, message, "Manage hotel information", messageType);
                        } catch (NumberFormatException ex) {
                            // 处理用户输入不是整数的情况
                            JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter valid integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Please enter all required information.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }  else {
                    // 用户未选择任何操作
                    JOptionPane.showMessageDialog(dialog, "Please select 'Load' or 'Update' operation.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            private boolean isInputValid() {
                return !hotelLocationText.getText().isEmpty() &&
                        !hotelPriceText.getText().isEmpty() &&
                        !totalRoomsText.getText().isEmpty() &&
                        !availRoomsText.getText().isEmpty() &&
                        !hotelLocationText.getText().equals(InputPrompt.CITY) &&
                        !hotelPriceText.getText().equals(InputPrompt.PRICE) &&
                        !totalRoomsText.getText().equals(InputPrompt.HOTEL_TOTAL_ROOMS) &&
                        !availRoomsText.getText().equals(InputPrompt.HOTEL_AVAILABLE_ROOMS);
            }
        });

        // 提交大巴信息
        busSubmitButton.addActionListener(new ActionListener() {
            private JDialog dialog;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loadRadioButton.isSelected() || updateRadioButton.isSelected()) {
                    if (isInputValid()) {
                        try {
                            String bus_location = busLocationText.getText();
                            int bus_price = Integer.parseInt(busPriceText.getText());
                            int bus_totalBus = Integer.parseInt(totalBusText.getText());
                            int bus_availBus = Integer.parseInt(availBusText.getText());

                            Response response;
                            if (loadRadioButton.isSelected()) {
                                response = loadService.loadBusData(bus_location, bus_price, bus_totalBus, bus_availBus);
                            } else {
                                response = loadService.updateBusData(bus_location, bus_price, bus_totalBus, bus_availBus);
                            }
                            String message = response.getMessage();

                            // 使用全局对话框对象，避免重复创建
                            if (dialog == null) {
                                dialog = new JDialog();
                                dialog.setSize(300, 200);
                                dialog.setLocationRelativeTo(null);
                            }
                            dialog.setTitle("Manage bus information");

                            int messageType = response.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

                            // 反馈提交情况
                            JOptionPane.showMessageDialog(dialog, message, "Manage bus information", messageType);
                        } catch (NumberFormatException ex) {
                            // 处理用户输入不是整数的情况
                            JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter valid integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Please enter all required information.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }  else {
                    // 用户未选择任何操作
                    JOptionPane.showMessageDialog(dialog, "Please select 'Load' or 'Update' operation.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            private boolean isInputValid() {
                return !busLocationText.getText().isEmpty() &&
                        !busPriceText.getText().isEmpty() &&
                        !totalBusText.getText().isEmpty() &&
                        !availBusText.getText().isEmpty() &&
                        !busLocationText.getText().equals(InputPrompt.CITY) &&
                        !busPriceText.getText().equals(InputPrompt.PRICE) &&
                        !totalBusText.getText().equals(InputPrompt.BUS_TOTAL_BUSES) &&
                        !availBusText.getText().equals(InputPrompt.BUS_AVAILABLE_BUSES);
            }
        });

        // 提交客户信息
        customerSubmitButton.addActionListener(new ActionListener() {
            private JDialog dialog;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loadRadioButton.isSelected() || updateRadioButton.isSelected()) {
                    if (isInputValid()) {
                        try {
                            String customer_name = custNameText.getText();
                            int customer_id = Integer.parseInt(custIdText.getText());

                            Response response;
                            if (loadRadioButton.isSelected()) {
                                response = loadService.loadCustomerData(customer_name, customer_id);
                            } else {
                                response = loadService.updateCustomerData(customer_name, customer_id);
                            }
                            String message = response.getMessage();

                            // 使用全局对话框对象，避免重复创建
                            if (dialog == null) {
                                dialog = new JDialog();
                                dialog.setSize(300, 200);
                                dialog.setLocationRelativeTo(null);
                            }
                            dialog.setTitle("Manage customer information");

                            int messageType = response.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

                            // 反馈提交情况
                            JOptionPane.showMessageDialog(dialog, message, "Manage customer information", messageType);
                        } catch (NumberFormatException ex) {
                            // 处理用户输入不是整数的情况/.
                            JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter valid integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Please enter all required information.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }  else {
                    // 用户未选择任何操作
                    JOptionPane.showMessageDialog(dialog, "Please select 'Load' or 'Update' operation.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            private boolean isInputValid() {
                return !custNameText.getText().isEmpty() &&
                        !custIdText.getText().isEmpty() &&
                        !custNameText.getText().equals(InputPrompt.CUSTOMER_NAME) &&
                        !custIdText.getText().equals(InputPrompt.CUSTOMER_ID);
            }
        });

        // 飞机信息提示
        flightNumText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (flightNumText.getText().equals(InputPrompt.FLIGHT_NUMBER)) {
                    flightNumText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (flightNumText.getText().isEmpty()) {
                    flightNumText.setText(InputPrompt.FLIGHT_NUMBER);
                }
            }
        });
        flightPriceText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (flightPriceText.getText().equals(InputPrompt.PRICE)) {
                    flightPriceText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (flightPriceText.getText().isEmpty()) {
                    flightPriceText.setText(InputPrompt.PRICE);
                }
            }
        });
        totalSeatsText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (totalSeatsText.getText().equals(InputPrompt.FLIGHT_TOTAL_SEATS)) {
                    totalSeatsText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (totalSeatsText.getText().isEmpty()) {
                    totalSeatsText.setText(InputPrompt.FLIGHT_TOTAL_SEATS);
                }
            }
        });
        availSeatsText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (availSeatsText.getText().equals(InputPrompt.FLIGHT_AVAILABLE_SEATS)) {
                    availSeatsText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (availSeatsText.getText().isEmpty()) {
                    availSeatsText.setText(InputPrompt.FLIGHT_AVAILABLE_SEATS);
                }
            }
        });
        fromCityText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (fromCityText.getText().equals(InputPrompt.CITY)) {
                    fromCityText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (fromCityText.getText().isEmpty()) {
                    fromCityText.setText(InputPrompt.CITY);
                }
            }
        });
        arivCityText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (arivCityText.getText().equals(InputPrompt.CITY)) {
                    arivCityText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (arivCityText.getText().isEmpty()) {
                    arivCityText.setText(InputPrompt.CITY);
                }
            }
        });
        // 酒店信息提示
        hotelLocationText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (hotelLocationText.getText().equals(InputPrompt.CITY)) {
                    hotelLocationText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (hotelLocationText.getText().isEmpty()) {
                    hotelLocationText.setText(InputPrompt.CITY);
                }
            }
        });
        hotelPriceText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (hotelPriceText.getText().equals(InputPrompt.PRICE)) {
                    hotelPriceText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (hotelPriceText.getText().isEmpty()) {
                    hotelPriceText.setText(InputPrompt.PRICE);
                }
            }
        });
        totalRoomsText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (totalRoomsText.getText().equals(InputPrompt.HOTEL_TOTAL_ROOMS)) {
                    totalRoomsText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (totalRoomsText.getText().isEmpty()) {
                    totalRoomsText.setText(InputPrompt.HOTEL_TOTAL_ROOMS);
                }
            }
        });
        availRoomsText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (availRoomsText.getText().equals(InputPrompt.HOTEL_AVAILABLE_ROOMS)) {
                    availRoomsText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (availRoomsText.getText().isEmpty()) {
                    availRoomsText.setText(InputPrompt.HOTEL_AVAILABLE_ROOMS);
                }
            }
        });
        busLocationText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (busLocationText.getText().equals(InputPrompt.CITY)) {
                    busLocationText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (busLocationText.getText().isEmpty()) {
                    busLocationText.setText(InputPrompt.CITY);
                }
            }
        });
        busPriceText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (busPriceText.getText().equals(InputPrompt.PRICE)) {
                    busPriceText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (busPriceText.getText().isEmpty()) {
                    busPriceText.setText(InputPrompt.PRICE);
                }
            }
        });
        totalBusText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (totalBusText.getText().equals(InputPrompt.BUS_TOTAL_BUSES)) {
                    totalBusText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (totalBusText.getText().isEmpty()) {
                    totalBusText.setText(InputPrompt.BUS_TOTAL_BUSES);
                }
            }
        });
        availBusText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (availBusText.getText().equals(InputPrompt.BUS_AVAILABLE_BUSES)) {
                    availBusText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (availBusText.getText().isEmpty()) {
                    availBusText.setText(InputPrompt.BUS_AVAILABLE_BUSES);
                }
            }
        });
        custNameText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (custNameText.getText().equals(InputPrompt.CUSTOMER_NAME)) {
                    custNameText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (custNameText.getText().isEmpty()) {
                    custNameText.setText(InputPrompt.CUSTOMER_NAME);
                }
            }
        });
        custIdText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (custIdText.getText().equals(InputPrompt.CUSTOMER_ID)) {
                    custIdText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (custIdText.getText().isEmpty()) {
                    custIdText.setText(InputPrompt.CUSTOMER_ID);
                }
            }
        });
    }

    public JPanel getPanel() {
        return rootPanel;
    }

    private void setButtonsText(@NotNull OperationType operationType) {
        flightSubmitButton.setText(operationType.getButtonText());
        hotelSubmitButton.setText(operationType.getButtonText());
        busSubmitButton.setText(operationType.getButtonText());
        customerSubmitButton.setText(operationType.getButtonText());
    }
}
