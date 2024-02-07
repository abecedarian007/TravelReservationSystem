package frontend;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import java.util.Collections;

public class HomePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private JPanel rootPanel;
    private JPanel homePanel;
    private JLabel homeTitle;
    private JButton reserveButton;
    private JButton checkTourRoutesButton;
    private JButton loadDataButton;
    private JButton queryButton;

    public HomePage() {
        loadDataButton.addActionListener(new ActionListener() {
            private JDialog dialog;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (dialog != null && dialog.isVisible()) {
                    dialog.dispose();
                }

                dialog = new JDialog();
                dialog.setTitle("Manage Data");
                dialog.setSize(550, 450);
                dialog.setLocationRelativeTo(null);

                ManageDataPage loadDataPage = new ManageDataPage();
                JPanel subPanel = loadDataPage.getPanel();
                dialog.getContentPane().add(subPanel);

                dialog.setVisible(true);
            }
        });

        queryButton.addActionListener(new ActionListener() {
            private JDialog dialog;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (dialog != null && dialog.isVisible()) {
                    dialog.dispose();
                }

                dialog = new JDialog();
                dialog.setTitle("Query Information");
                dialog.setSize(800, 500);
                dialog.setLocationRelativeTo(null);

                QueryInfoPage queryInfoPage = new QueryInfoPage();
                JPanel subPanel = queryInfoPage.getPanel();
                dialog.getContentPane().add(subPanel);

                dialog.setVisible(true);
            }
        });

        reserveButton.addActionListener(new ActionListener() {
            private JDialog dialog;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (dialog != null && dialog.isVisible()) {
                    dialog.dispose();
                }

                dialog = new JDialog();
                dialog.setTitle("Reserve Items");
                dialog.setSize(500, 300);
                dialog.setLocationRelativeTo(null);

                ReservePage reservePage = new ReservePage();
                JPanel subPanel = reservePage.getPanel();
                dialog.getContentPane().add(subPanel);

                dialog.setVisible(true);
            }
        });

        checkTourRoutesButton.addActionListener(new ActionListener() {
            private JDialog dialog;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (dialog != null && dialog.isVisible()) {
                    dialog.dispose();
                }

                dialog = new JDialog();
                dialog.setTitle("Check Routes");
                dialog.setSize(500, 400);
                dialog.setLocationRelativeTo(null);

                RoutePage routePage = new RoutePage();
                JPanel subPanel = routePage.getPanel();
                dialog.getContentPane().add(subPanel);

                dialog.setVisible(true);
            }
        });
    }

    private static Image iconImage; // 声明 iconImage 成员变量

    private static void setWindowIcon() {
        // 获取程序图标的 URL
        URL iconURL = null;
        try {
            iconURL = new File("E:/Learning_Record/database/xdu/TravelReservationSystem/ico/travel.png").toURI().toURL();
        } catch (MalformedURLException e) {
            logger.error("Get icon URL failed", e);
        }
//        if (iconURL != null) {
//            System.out.println("Icon URL: " + iconURL.toString());
//        } else {
//            System.out.println("Icon URL is null");
//        }

        // 创建 ImageIcon 对象
        assert iconURL != null;
        ImageIcon icon = new ImageIcon(iconURL);
//        if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
//            System.out.println("Error loading icon image");
//        }

        // 获取当前屏幕的工具包
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        // 获取 Image 对象
        Image image = icon.getImage();
        // 设置程序图标
        JFrame.setDefaultLookAndFeelDecorated(true);
        iconImage = toolkit.createImage(image.getSource()); // 设置 iconImage
        setTaskbarIcon(iconImage);
    }

    private static void setTaskbarIcon(Image image) {
        try {
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                TrayIcon trayIcon = new TrayIcon(image);
                tray.add(trayIcon);
            }
        } catch (Exception e) {
            logger.error("Setting taskbar icon failed", e);
        }
    }

    public static void startHomePage() {
        FlatMacLightLaf.setup();
        JFrame frame = new JFrame("HomePage");
        setWindowIcon(); // 设置 JFrame 的图标
        frame.setContentPane(new HomePage().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setIconImages(Collections.singletonList(iconImage));  // 设置窗口图标
    }
}
