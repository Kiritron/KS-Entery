/*
 * Copyright 2020 Kiritron's Space
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

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
