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

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.cef.callback.CefStringVisitor;

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

@SuppressWarnings("serial")
public class ShowTextDialog extends JDialog implements CefStringVisitor {
    private final JTextArea textArea_ = new JTextArea();

    public ShowTextDialog(Frame owner, String title) {
        super(owner, title, false);
        setLayout(new BorderLayout());
        setSize(800, 600);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        controlPanel.add(doneButton);

        add(new JScrollPane(textArea_));
        add(controlPanel, BorderLayout.SOUTH);
    }

    @Override
    public void visit(String string) {
        if (!isVisible()) {
            setVisible(true);
        }
        textArea_.append(string);
    }
}
