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

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.cef.callback.CefRequestCallback;
import org.cef.handler.CefLoadHandler.ErrorCode;

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

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
