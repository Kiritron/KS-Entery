// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package space.kiritron.entery.core.ui;

import org.cef.browser.CefBrowser;
import space.kiritron.entery.core.Extensions;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel {
    private final JProgressBar progressBar_;
    private final JLabel status_field_;
    private final JLabel zoom_label_;
    private final CefBrowser browser_;
    private final ControlPanel control_pane_;
    private double zoomLevel_ = 0;

    public StatusPanel(CefBrowser browser, ControlPanel control_pane_) {
        this.control_pane_ = control_pane_;
        assert browser != null;
        browser_ = browser;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(Box.createHorizontalStrut(5));
        add(Box.createHorizontalStrut(5));

        progressBar_ = new JProgressBar();
        Dimension progressBarSize = progressBar_.getMaximumSize();
        progressBarSize.width = 100;
        progressBarSize.height = 10;
        progressBar_.setMinimumSize(progressBarSize);
        progressBar_.setMaximumSize(progressBarSize);
        add(progressBar_);
        add(Box.createHorizontalStrut(5));

        status_field_ = new JLabel("Загрузка...");
        status_field_.setAlignmentX(LEFT_ALIGNMENT);
        add(status_field_);
        add(Box.createHorizontalStrut(5));
        add(Box.createVerticalStrut(21));

        JButton pip = new JButton("Картинка в картинке");
        //pip.setToolTipText("Функция, которая переносит видео-плеер в отдельное плавающее окно.\n" +
        //                    "Нажмите, если на сайте есть видео-плеер, который вы бы хотели перенести\n" +
        //                    "в отдельное окно. Пример таких сайтов: YouTube, онлайн-кинотеатры.");
        pip.setFont(new Font("Dialog", Font.PLAIN, 13));
        pip.setFocusable(false);
        pip.setAlignmentX(CENTER_ALIGNMENT);
        pip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.executeJavaScript(Extensions.PiP, StatusPanel.this.control_pane_.getAddress(), 0);
            }
        });
        add(pip);

        JButton minusButton = new JButton("➖");
        minusButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        minusButton.setFocusable(false);
        minusButton.setAlignmentX(CENTER_ALIGNMENT);
        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.setZoomLevel(--zoomLevel_);
                zoom_label_.setText(new Double(zoomLevel_).toString());
            }
        });
        add(minusButton);

        zoom_label_ = new JLabel("0.0");
        zoom_label_.setFont(new Font("Dialog", Font.PLAIN, 12));
        add(zoom_label_);

        JButton plusButton = new JButton("➕");
        plusButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        plusButton.setFocusable(false);
        plusButton.setAlignmentX(CENTER_ALIGNMENT);
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.setZoomLevel(++zoomLevel_);
                zoom_label_.setText(new Double(zoomLevel_).toString());
            }
        });
        add(plusButton);
    }

    public void setIsInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar_.setVisible(true);
            progressBar_.setIndeterminate(inProgress);
        } else {
            progressBar_.setVisible(false);
            progressBar_.setIndeterminate(inProgress);
        }
    }

    public void setStatusText(String text) {
        status_field_.setText(text);
    }
}
