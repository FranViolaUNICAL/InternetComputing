package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatWindow {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private PrintWriter out;
    private BufferedReader in;

    public ChatWindow(Socket socket) {
        setupGUI();
        setupCommunication(socket);
    }

    private void setupGUI() {
        frame = new JFrame("Chat");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        messageField = new JTextField();
        frame.add(messageField, BorderLayout.SOUTH);
        messageField.addActionListener(e -> sendMessage());

        frame.setVisible(true);
    }

    private void setupCommunication(Socket socket) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        chatArea.append(message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            out.println("Me: " + message);
            chatArea.append("Me: " + message + "\n");
            messageField.setText("");
        }
    }
}
