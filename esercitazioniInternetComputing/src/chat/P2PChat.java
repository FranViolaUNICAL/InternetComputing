package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class P2PChat {
    private static final int PORT = 2222;
    private JFrame frame;
    private DefaultListModel<String> hostListModel;
    private JList<String> hostList;

    public P2PChat() {
        setupGUI();
        startServer();
    }

    private void setupGUI() {
        frame = new JFrame("P2P Chat");
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        hostListModel = new DefaultListModel<>();
        hostList = new JList<>(hostListModel);
        frame.add(new JScrollPane(hostList), BorderLayout.CENTER);

        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> connectToHost());
        frame.add(connectButton, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Sample hosts (Modify based on actual IP addresses in a real scenario)
        hostListModel.addElement("localhost");
        hostListModel.addElement("192.168.1.100");
        hostListModel.addElement("192.168.1.101");
    }

    private void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                while (true) {
                    Socket socket = serverSocket.accept();
                    new ChatWindow(socket); // Open chat window on new connection
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void connectToHost() {
        String selectedHost = hostList.getSelectedValue();
        if (selectedHost != null) {
            try {
                Socket socket = new Socket(selectedHost, PORT);
                new ChatWindow(socket);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Cannot connect to " + selectedHost);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(P2PChat::new);
    }
}

