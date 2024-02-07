package frontend;

import backend.entity.ResponseBody;
import backend.service.TravelService;
import frontend.obj.InputPrompt;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

public class RoutePage {
    private JPanel rootPanel;
    private JPanel routePanel;
    private JTextField custNameText;
    private JTabbedPane tabGroup;
    private JTextArea multiOriginText;
    private JTextField oneOriginText;
    private JList<String> routeList;
    private JLabel routeTitleLabel;
    private JLabel custNameLabel;
    private JPanel singleOriginTab;
    private JPanel multiOriginTab;
    private JLabel originTipLabel;
    private JLabel integralityTipLabel;
    private JLabel integralityValueLabel;
    private JButton oneOriginCheckButton;
    private JButton multiOriginCheckButton;
    private JScrollPane multiOriginScrollPane;
    private JScrollPane routeScrollPane;

    public RoutePage() {
        oneOriginCheckButton.addActionListener(new ActionListener() {
            private JDialog dialog;
            @Override
            public void actionPerformed(ActionEvent e) {
                TravelService travelService = new TravelService();
                if (isInputValid()) {
                    String customerName = custNameText.getText();
                    String originCity = oneOriginText.getText();

                    ResponseBody<List<List<String>>> routesResponse = travelService.getTravelRoute(customerName, originCity);
                    ResponseBody<Boolean> integralityResponse = travelService.checkTravelRouteIntegrality(customerName, originCity);

                    List<List<String>> routesList = routesResponse.getResponseObject();
                    Boolean isRouteIntegrality = integralityResponse.getResponseObject();
                    String message;
                    if (!routesResponse.getMessage().equals(integralityResponse.getMessage())) {
                        message = routesResponse.getMessage() + "\n" + integralityResponse.getMessage();
                    } else {
                        message = routesResponse.getMessage();
                    }
                    int messageType = (routesResponse.isSuccess() && integralityResponse.isSuccess()) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

                    if (routesResponse.isSuccess()) {
                        showRoutes(routesList);
                        oneOriginText.setText("");
                    } else {
                        clearJList();
                    }
                    if (integralityResponse.isSuccess()) {
                        showRouteIntegrality(isRouteIntegrality);
                        oneOriginText.setText("");
                    } else {
                        integralityValueLabel.setText("");
                    }

                    // 使用全局对话框对象，避免重复创建
                    if (dialog == null) {
                        dialog = new JDialog();
                        dialog.setSize(300, 200);
                        dialog.setLocationRelativeTo(null);
                    }
                    dialog.setTitle("Check Route");
                    // 反馈查询情况
                    JOptionPane.showMessageDialog(dialog, message, "Check Route", messageType);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please enter valid customer name and origin", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            private boolean isInputValid() {
                return  !custNameText.getText().isEmpty() &&
                        !oneOriginText.getText().isEmpty() &&
                        !custNameText.getText().equals(InputPrompt.CUSTOMER_NAME) &&
                        !oneOriginText.getText().equals(InputPrompt.CITY);
            }
        });

        multiOriginCheckButton.addActionListener(new ActionListener() {
            private JDialog dialog;
            @Override
            public void actionPerformed(ActionEvent e) {
                TravelService travelService = new TravelService();
                if (isInputValid()) {
                    String customerName = custNameText.getText();
                    String multiOriginInput = multiOriginText.getText();
                    List<String> originCities = parseCities(multiOriginInput);

                    ResponseBody<List<List<String>>> routesResponse = travelService.getTravelRoute(customerName, originCities);
                    ResponseBody<Boolean> integralityResponse = travelService.checkTravelRouteIntegrality(customerName, originCities);

                    List<List<String>> routesList = routesResponse.getResponseObject();
                    Boolean isRouteIntegrality = integralityResponse.getResponseObject();
                    String message;
                    if (!routesResponse.getMessage().equals(integralityResponse.getMessage())) {
                        message = routesResponse.getMessage() + "\n" + integralityResponse.getMessage();
                    } else {
                        message = routesResponse.getMessage();
                    }
                    int messageType = (routesResponse.isSuccess() && integralityResponse.isSuccess()) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

                    if (routesResponse.isSuccess()) {
                        showRoutes(routesList);
                        multiOriginText.setText("");
                    } else {
                        clearJList();
                    }
                    if (integralityResponse.isSuccess()) {
                        showRouteIntegrality(isRouteIntegrality);
                        multiOriginText.setText("");
                    } else {
                        integralityValueLabel.setText("");
                    }

                    // 使用全局对话框对象，避免重复创建
                    if (dialog == null) {
                        dialog = new JDialog();
                        dialog.setSize(300, 200);
                        dialog.setLocationRelativeTo(null);
                    }
                    dialog.setTitle("Check Route");
                    // 反馈查询情况
                    JOptionPane.showMessageDialog(dialog, message, "Check Route", messageType);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please enter valid customer name and origins", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            private boolean isInputValid() {
                return  !custNameText.getText().isEmpty() &&
                        !multiOriginText.getText().isEmpty() &&
                        !custNameText.getText().equals(InputPrompt.CUSTOMER_NAME) &&
                        !multiOriginText.getText().equals(InputPrompt.CITIES);
            }
        });

        custNameText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示输入用户名的字符串，则清空
                if(custNameText.getText().equals(InputPrompt.CUSTOMER_NAME)) {
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

        oneOriginText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示输入起点城市的字符串，则清空
                if(oneOriginText.getText().equals(InputPrompt.CITY)) {
                    oneOriginText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (oneOriginText.getText().isEmpty()) {
                    oneOriginText.setText(InputPrompt.CITY);
                }
            }
        });

        multiOriginText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 在获得焦点时，如果文本框内容是提示输入起点城市的字符串，则清空
                if(multiOriginText.getText().equals(InputPrompt.CITIES)) {
                    multiOriginText.setText(InputPrompt.NULL);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // 在失去焦点时，如果文本框内容为空，则显示提示内容
                if (multiOriginText.getText().isEmpty()) {
                    multiOriginText.setText(InputPrompt.CITIES);
                }
            }
        });
    }

    public JPanel getPanel() {
        return rootPanel;
    }

    public List<String> formatRoute(@NotNull List<List<String>> routesList) {
        List<String> formattedRouteList = new ArrayList<String>();
        int cnt = 0;
        for (List<String> route : routesList) {
            String formattedRoute = String.join(" -> ", route);
            cnt = cnt + 1;
            formattedRoute = "[Route" + cnt + "] " + formattedRoute;
            formattedRouteList.add(formattedRoute);
        }
        return formattedRouteList;
    }

    private @NotNull List<String> parseCities(@NotNull String input) {
        // 使用换行符分割用户输入的城市名称
        String[] cityArray = input.split("\n");
        List<String> cities = new ArrayList<>();
        for (String city : cityArray) {
            cities.add(city.trim());
        }
        return cities;
    }

    private void showRoutes(List<List<String>> routesList) {
        List<String> formattedRouteList = formatRoute(routesList);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String formattedRoute : formattedRouteList) {
            listModel.addElement(formattedRoute);
        }
        routeList.setModel(listModel);
    }

    private void showRouteIntegrality(Boolean isRouteIntegrality) {
        if (isRouteIntegrality != null) {
            if (isRouteIntegrality) {
                integralityValueLabel.setText("complete");
            } else {
                integralityValueLabel.setText("incomplete");
            }
        } else {
            integralityValueLabel.setText("CAN'T CHECK");
        }
    }

    private void clearJList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.clear();
        routeList.setModel(listModel);
    }
}
