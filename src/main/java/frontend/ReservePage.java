package frontend;

import backend.entity.Response;
import backend.service.ReserveService;
import frontend.obj.InputPrompt;

import javax.swing.*;
import java.awt.event.*;

public class ReservePage {
    private JPanel rootPanel;
    private JPanel reservePanel;
    private JTabbedPane tabGroup;
    private JLabel titleLable;
    private JPanel flightTab;
    private JPanel hotelTab;
    private JPanel busTab;
    private JButton flightBookButton;
    private JTextField flightCustNameText;
    private JTextField flightNumText;
    private JLabel flightCustNameLable;
    private JLabel flightNumLable;
    private JLabel flightTipLable;
    private JButton hotelBookButton;
    private JTextField hotelCustNameText;
    private JTextField hotelLocationText;
    private JLabel hotelTipLable;
    private JLabel hotelCustNameLable;
    private JLabel hotelLocationLable;
    private JButton busBookButton;
    private JTextField busCustNameText;
    private JTextField busLocationText;
    private JLabel busTipLable;
    private JLabel busCustNameLable;
    private JLabel busLocationLable;

    public ReservePage() {
        ReserveService reserveService = new ReserveService();

        // 预订航班
        flightBookButton.addActionListener(new ActionListener() {
            private JDialog dialog;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isInputValid()) {
                    String flight_custName = flightCustNameText.getText();
                    String flight_number = flightNumText.getText();

                    Response response = reserveService.reserveFlight(flight_custName, flight_number);
                    String message = response.getMessage();

                    // 使用全局对话框对象，避免重复创建
                    if (dialog == null) {
                        dialog = new JDialog();
                        dialog.setSize(300, 200);
                        dialog.setLocationRelativeTo(null);
                    }

                    dialog.setTitle("Reserve flight");

                    int messageType = response.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

                    // 反馈预订情况
                    JOptionPane.showMessageDialog(dialog, message, "Reserve flight", messageType);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please enter all required information.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            private boolean isInputValid() {
                return !flightCustNameText.getText().isEmpty() &&
                        !flightNumText.getText().isEmpty() &&
                        !flightCustNameText.getText().equals(InputPrompt.CUSTOMER_NAME) &&
                        !flightNumText.getText().equals(InputPrompt.FLIGHT_NUMBER);
            }
        });
        // 预订酒店
        hotelBookButton.addActionListener(new ActionListener() {
            private JDialog dialog;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isInputValid()) {
                    String hotel_custName = hotelCustNameText.getText();
                    String hotel_location = hotelLocationText.getText();

                    Response response = reserveService.reserveHotel(hotel_custName, hotel_location);
                    String message = response.getMessage();

                    // 使用全局对话框对象，避免重复创建
                    if (dialog == null) {
                        dialog = new JDialog();
                        dialog.setSize(300, 200);
                        dialog.setLocationRelativeTo(null);
                    }

                    dialog.setTitle("Reserve hotel");

                    int messageType = response.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

                    // 反馈预订情况
                    JOptionPane.showMessageDialog(dialog, message, "Reserve hotel", messageType);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please enter all required information.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            private boolean isInputValid() {
                return !hotelCustNameText.getText().isEmpty() &&
                        !hotelLocationText.getText().isEmpty() &&
                        !hotelCustNameText.getText().equals(InputPrompt.CUSTOMER_NAME) &&
                        !hotelLocationText.getText().equals(InputPrompt.CITY);
            }
        });
        // 预订大巴
        busBookButton.addActionListener(new ActionListener() {
            private JDialog dialog;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isInputValid()) {
                    String bus_custName = busCustNameText.getText();
                    String bus_location = busLocationText.getText();

                    Response response = reserveService.reserveBus(bus_custName, bus_location);
                    String message = response.getMessage();

                    // 使用全局对话框对象，避免重复创建
                    if (dialog == null) {
                        dialog = new JDialog();
                        dialog.setSize(300, 200);
                        dialog.setLocationRelativeTo(null);
                    }

                    dialog.setTitle("Reserve bus");

                    int messageType = response.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

                    // 反馈预订情况
                    JOptionPane.showMessageDialog(dialog, message, "Reserve bus", messageType);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please enter all required information.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            private boolean isInputValid() {
                return !busCustNameText.getText().isEmpty() &&
                        !busLocationText.getText().isEmpty() &&
                        !busCustNameText.getText().equals(InputPrompt.CUSTOMER_NAME) &&
                        !busLocationText.getText().equals(InputPrompt.CITY);
            }
        });

        flightCustNameText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (flightCustNameText.getText().equals(InputPrompt.CUSTOMER_NAME)) {
                    flightCustNameText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (flightCustNameText.getText().isEmpty()) {
                    flightCustNameText.setText(InputPrompt.CUSTOMER_NAME);
                }
            }
        });
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
        hotelCustNameText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (hotelCustNameText.getText().equals(InputPrompt.CUSTOMER_NAME)) {
                    hotelCustNameText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (hotelCustNameText.getText().isEmpty()) {
                    hotelCustNameText.setText(InputPrompt.CUSTOMER_NAME);
                }
            }
        });
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
        busCustNameText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示内容，则清空
                if (busCustNameText.getText().equals(InputPrompt.CUSTOMER_NAME)) {
                    busCustNameText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (busCustNameText.getText().isEmpty()) {
                    busCustNameText.setText(InputPrompt.CUSTOMER_NAME);
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
    }

    public JPanel getPanel() {
        return rootPanel;
    }
}
