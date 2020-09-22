// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package space.kiritron.entery.core.dialog;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.cef.callback.CefRequestCallback;
import org.cef.handler.CefLoadHandler.ErrorCode;

public class CertErrorDialog implements Runnable {
    private final Frame owner_;
    private final ErrorCode cert_error_;
    private final String request_url_;
    private final CefRequestCallback callback_;

    public CertErrorDialog(
            Frame owner, ErrorCode cert_error, String request_url, CefRequestCallback callback) {
        owner_ = owner;
        cert_error_ = cert_error;
        request_url_ = request_url;
        callback_ = callback;
    }

    @Override
    public void run() {
        int dialogResult = JOptionPane.showConfirmDialog(owner_,
                "У данного сайта проблемы с сертификатом SSL.(" + cert_error_ + ")."
                        + "\nАдрес сайта: " + request_url_
                        + "\nКод ошибки: " + cert_error_
                        + "\nПродолжить подключение?",
                "Проблемы с сертификатом SSL сайта", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        callback_.Continue(dialogResult == JOptionPane.YES_OPTION);
    }
}
