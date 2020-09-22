// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package space.kiritron.entery.core.dialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.cef.callback.CefAuthCallback;

@SuppressWarnings("serial")
public class PasswordDialog extends JDialog implements Runnable {
    private final JTextField username_ = new JTextField(20);
    private final JPasswordField password_ = new JPasswordField(20);
    private final CefAuthCallback callback_;

    public PasswordDialog(Frame owner, CefAuthCallback callback) {
        super(owner, "Требуется авторизация", true);
        callback_ = callback;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (screenSize.width - 400) / 2;
        int locationY = (screenSize.height - 100) / 2;
        setSize(400, 100);
        setBounds(locationX, locationY, 400, 100);
        setLayout(new GridLayout(0, 2));
        add(new JLabel("Логин:"));
        add(username_);
        add(new JLabel("Пароль:"));
        add(password_);

        JButton abortButton = new JButton("Отмена");
        abortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callback_.cancel();
                setVisible(false);
                dispose();
            }
        });
        add(abortButton);

        JButton okButton = new JButton("Окей");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (username_.getText().isEmpty()) return;
                String password = new String(password_.getPassword());
                callback_.Continue(username_.getText(), password);
                setVisible(false);
                dispose();
            }
        });
        add(okButton);
    }

    @Override
    public void run() {
        setVisible(true);
    }
}
