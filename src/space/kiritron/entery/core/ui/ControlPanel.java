// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package space.kiritron.entery.core.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cef.OS;
import org.cef.browser.CefBrowser;
import space.kiritron.entery.core.dialog.CookieManagerDialog;
import space.kiritron.entery.ks_libs.pixel.filefunc.FileControls;
import space.kiritron.entery.ks_libs.pixel.filefunc.GetPathOfAPP;
import space.kiritron.entery.ks_libs.pixel.logger.genLogMessage;
import space.kiritron.entery.ks_libs.pixel.logger.toConsole;
import space.kiritron.entery.ks_libs.tolchok.TOLF_Handler;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {
    private final JButton backButton_;
    private final JButton forwardButton_;
    private final JButton reloadButton_;
    //private final JButton otherButton_;
    private final JTextField address_field_;
    private double zoomLevel_ = 0;
    private final CefBrowser browser_;

    public ControlPanel(CefBrowser browser) {
        assert browser != null;
        browser_ = browser;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(Box.createHorizontalStrut(5));
        add(Box.createHorizontalStrut(5));

        backButton_ = new JButton("❮");
        backButton_.setFont(new Font("Serif", Font.PLAIN, 18));
        backButton_.setFocusable(false);
        backButton_.setAlignmentX(LEFT_ALIGNMENT);
        backButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.goBack();
            }
        });
        add(backButton_);
        add(Box.createHorizontalStrut(5));

        forwardButton_ = new JButton("❯");
        forwardButton_.setFont(new Font("Serif", Font.PLAIN, 15));
        forwardButton_.setFocusable(false);
        forwardButton_.setAlignmentX(LEFT_ALIGNMENT);
        forwardButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.goForward();
            }
        });
        add(forwardButton_);
        add(Box.createHorizontalStrut(5));

        reloadButton_ = new JButton("⟲");
        reloadButton_.setFont(new Font("Serif", Font.PLAIN, 18));
        reloadButton_.setFocusable(false);
        reloadButton_.setAlignmentX(LEFT_ALIGNMENT);
        reloadButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (reloadButton_.getText().equalsIgnoreCase("↺")) {
                    reloadPage();
                } else {
                    browser_.stopLoad();
                }
            }
        });
        add(reloadButton_);
        add(Box.createHorizontalStrut(1));

        JLabel addressLabel = new JLabel("");
        addressLabel.setFont(new Font("Serif", Font.PLAIN, 17));
        addressLabel.setAlignmentX(LEFT_ALIGNMENT);
        add(addressLabel);
        add(Box.createHorizontalStrut(5));

        address_field_ = new JTextField(100);
        address_field_.setToolTipText("Адресная строка. Введите адрес сайта или поисковой запрос.");
        address_field_.setAlignmentX(LEFT_ALIGNMENT);
        address_field_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.loadURL(getAddress());
            }
        });
        add(address_field_);
        //add(Box.createHorizontalStrut(5));

        JButton goButton = new JButton("➔");
        goButton.setFont(new Font("Serif", Font.PLAIN, 18));
        goButton.setFocusable(false);
        goButton.setAlignmentX(LEFT_ALIGNMENT);
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.loadURL(getAddress());
            }
        });
        add(goButton);
        add(Box.createHorizontalStrut(5));

        //otherButton_ = new JButton("Меню");
        //otherButton_.setFont(new Font("Dialog", Font.PLAIN, 18));
        //otherButton_.setFocusable(false);
        //otherButton_.setAlignmentX(LEFT_ALIGNMENT);
        //otherButton_.addActionListener(new ActionListener() {
        //    @Override
        //    public void actionPerformed(ActionEvent e) {
                //CookieManagerDialog cookieManager = new CookieManagerDialog();
                //cookieManager.setVisible(true);
        //    }
        //});
        //add(otherButton_);
        //add(Box.createHorizontalStrut(5));
    }

    public void update(
            CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
        if (browser == browser_) {
            backButton_.setEnabled(canGoBack);
            forwardButton_.setEnabled(canGoForward);
            reloadButton_.setText(isLoading ? "❌" : "↺");
        }
    }

    public String getAddress() {
        String address = address_field_.getText();
        // If the URI format is unknown "new URI" will throw an
        // exception. In this case we interpret the value of the
        // address field as search request. Therefore we simply add
        // the "search" scheme.
        try {
            address = address.replaceAll(" ", "%20");
            URI test = new URI(address);
            if (test.getScheme() != null) return address;
            if (test.getHost() != null && test.getPath() != null) return address;
            String specific = test.getSchemeSpecificPart();
            if (specific.indexOf('.') == -1)
                throw new URISyntaxException(specific, "No dot inside domain");
        } catch (URISyntaxException e1) {
            address = "https://duckduckgo.com/?q=" + address;
        }
        return address;
    }

    public void setAddress(CefBrowser browser, String address) {
        if (browser == browser_) address_field_.setText(address);
    }

    public JTextField getAddressField() {
        return address_field_;
    }

    public void reloadPage() {
        boolean param;

        try {
            if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "engine-options.tolf"), "ENGINE-OPTIONS", "reloadWithoutCache").equals("true")) {
                param = true;
            } else {
                param = false;
            }
        } catch (IOException e) {
            param = false;
            e.printStackTrace();
        }

        if (param) {
            browser_.reloadIgnoreCache();
        } else {
            browser_.reload();
        }
    }
}
