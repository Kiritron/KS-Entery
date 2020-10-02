// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package space.kiritron.entery.core.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import space.kiritron.entery.init;
import space.kiritron.entery.ks_libs.pixel.GetOS;

import static space.kiritron.entery.init.VER_APP;

public class ClientSchemeHandler extends CefResourceHandlerAdapter {
    public static final String scheme = "entery";
    public static final String domain = "res";

    private byte[] data_;
    private String mime_type_;
    private int offset_ = 0;

    public ClientSchemeHandler() {
        super();
    }

    @Override
    public synchronized boolean processRequest(CefRequest request, CefCallback callback) {
        boolean handled = false;
        String url = request.getURL();
        if (url.indexOf("empty") != -1) {
            String html;
            html = getHead("Пустая страница") +
                    "<body></body></html>";
            data_ = html.getBytes();
            handled = true;
            mime_type_ = "text/html";
        } else if (url.indexOf("about") != -1) {
            String html;
            html = getHead("О программе") +
                        "<body " + getDefaultBodyStyle() + ">" +
                            "<center>" +
                                "<div style='margin-top: 5%;'>" +
                                    "<img src='entery://res/logo2.png'>" +
                                    "<br>" +
                                    "<img src='entery://res/author.png'>" +
                                    "<br>" +
                                    "<img src='entery://res/withCEF.png'>" +
                                    "<br><br>" +
                                    "<b>Отдельное спасибо:</b>" +
                                    "<br>" +
                                    "- Разработчикам из FormDev Software GmbH за FlatLaF, стиль интерфейса,<br>который используется для реализации интерфейса КС Энтэри." +
                                    "<br>" +
                                    "- Мистеру Рексу за систему вкладок для браузера и новый минималистичный интерфейс." +
                                    "<br>" +
                                    "<h3>" + VER_APP + "</h3>" +
                                "</div>" +
                            "</center>" +
                        "</body>" +
                    "</html>";
            data_ = html.getBytes();
            handled = true;
            mime_type_ = "text/html";
        } else if (url.indexOf("help") != -1) {
            String html;
            html = getHead("Учебник") +
                        "<body " + getDefaultBodyStyle() + ">" +
                            "<center>" +
                                "<div style='margin-top: 3%; margin-bottom: 3%;'>" +
                                    "<img src='entery://res/logo2.png'>" +
                                    "<br>" +
                                    "<h2>Интерфейс</h2>" +
                                    "<img src='entery://res/hinterface.png'>" +
                                    "<br><br>" +
                                    "<span style='font-size: 15px;'><b>" +
                                        "1. Назад; 2. Вперёд; 3. Домашняя страница(Загружается в текущей);<br>" +
                                        "4. Перезагрузить страницу/Остановить загрузку страницы;<br>" +
                                        "5. Режим \"Картинка в картинке\"*; 6. Меню; 7. Менеджер загрузок;<br>" +
                                        "8. Закрыть вкладку; 9. Открыть вкладку." +
                                    "</b></span><br><br>" +
                                    "* - Режим \"Картинка в картинке\" позволяет перенести видео контент, если он<br>" +
                                    "есть на сайте, в новое окно, которое позиционируется поверх других окон.<br><br>" +
                                    "Обратите внимание: Менеджер загрузок пустой, если не было загрузок в текущей сессии.<br><br>" +
                                    "<h2>Горячие клавиши</h2>" +
                                    "Их немного." +
                                    "<br>" +
                                    "<span style='font-size: 15px;'><b>" +
                                    "F5 - Перезагрузить страницу в текущей вкладке<br>" +
                                    "CTRL + T - Новая вкладка<br>" +
                                    "CTRL + R - Вернуть последнюю закрытую вкладку<br>" +
                                    "CTRL + W - Закрытие текущей вкладки<br>" +
                                    "CTRL + (+) - Увеличить масштаб текущей страницы<br>" +
                                    "CTRL + (-) - Уменьшить масштаб текущей страницы" +
                                    "</b></span>" +
                                "</div>" +
                            "</center>" +
                        "</body>" +
                    "</html>";
            data_ = html.getBytes();
            handled = true;
            mime_type_ = "text/html";
        } else if (url.endsWith(".png")) {
            handled = loadContent(url.substring(url.lastIndexOf('/') + 1));
            mime_type_ = "image/png";
        } else if (url.endsWith(".html")) {
            handled = loadContent(url.substring(url.lastIndexOf('/') + 1));
            mime_type_ = "text/html";
            if (!handled) {
                String html = "<html><head><title>Error 404</title></head>";
                html += "<body><h1>Error 404</h1>";
                html += "File  " + url.substring(url.lastIndexOf('/') + 1) + " ";
                html += "does not exist</body></html>";
                data_ = html.getBytes();
                handled = true;
            }
        }

        if (handled) {
            // Indicate the headers are available.
            callback.Continue();
            return true;
        }

        return false;
    }

    @Override
    public void getResponseHeaders(
            CefResponse response, IntRef response_length, StringRef redirectUrl) {
        response.setMimeType(mime_type_);
        response.setStatus(200);

        // Set the resulting response length
        response_length.set(data_.length);
    }

    @Override
    public synchronized boolean readResponse(
            byte[] data_out, int bytes_to_read, IntRef bytes_read, CefCallback callback) {
        boolean has_data = false;

        if (offset_ < data_.length) {
            // Copy the next block of data into the buffer.
            int transfer_size = Math.min(bytes_to_read, (data_.length - offset_));
            System.arraycopy(data_, offset_, data_out, 0, transfer_size);
            offset_ += transfer_size;

            bytes_read.set(transfer_size);
            has_data = true;
        } else {
            offset_ = 0;
            bytes_read.set(0);
        }

        return has_data;
    }

    private boolean loadContent(String resName) {
        InputStream inStream = init.class.getResourceAsStream("res/" + resName);
        if (inStream != null) {
            try {
                ByteArrayOutputStream outFile = new ByteArrayOutputStream();
                int readByte = -1;
                while ((readByte = inStream.read()) >= 0) outFile.write(readByte);
                data_ = outFile.toByteArray();
                return true;
            } catch (IOException e) {
                // Ничего
            }
        }
        return false;
    }

    private String getHead(String Title) {
        String charset;
        if (GetOS.isWindows()) {
            charset = "<meta charset=\"windows-1251\">";
        } else {
            charset = "<meta charset=\"UTF-8\">";
        }
        return "<html>" +
                    "<head>" +
                        "<title>" + Title + "</title>" +
                        charset +
                    "</head>";
    }

    private String getDefaultBodyStyle() {
        return "style='font-family: sans-serif; background-color: #212121; color: #DCDCDC;'";
    }
}
