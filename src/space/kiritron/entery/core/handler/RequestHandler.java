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

package space.kiritron.entery.core.handler;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefAuthCallback;
import org.cef.callback.CefRequestCallback;
import org.cef.handler.CefLoadHandler.ErrorCode;
import org.cef.handler.CefRequestHandler;
import org.cef.handler.CefResourceHandler;
import org.cef.handler.CefResourceRequestHandler;
import org.cef.handler.CefResourceRequestHandlerAdapter;
import org.cef.misc.BoolRef;
import org.cef.network.CefPostData;
import org.cef.network.CefPostDataElement;
import org.cef.network.CefRequest;

import java.awt.Frame;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import space.kiritron.entery.core.dialog.CertErrorDialog;
import space.kiritron.entery.core.dialog.PasswordDialog;
import space.kiritron.entery.core.modules.Entery_ClearWeb;
import space.kiritron.entery.ks_libs.pixel.logger.genLogMessage;
import space.kiritron.entery.ks_libs.pixel.logger.toConsole;

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

public class RequestHandler extends CefResourceRequestHandlerAdapter implements CefRequestHandler {
    private final Frame owner_;

    public RequestHandler(Frame owner) {
        owner_ = owner;
    }

    @Override
    public boolean onBeforeBrowse(CefBrowser browser, CefFrame frame, CefRequest request,
            boolean user_gesture, boolean is_redirect) {
        CefPostData postData = request.getPostData();
        if (postData != null) {
            Vector<CefPostDataElement> elements = new Vector<CefPostDataElement>();
            postData.getElements(elements);
            for (CefPostDataElement el : elements) {
                int numBytes = el.getBytesCount();
                if (numBytes <= 0) continue;

                byte[] readBytes = new byte[numBytes];
                if (el.getBytes(numBytes, readBytes) <= 0) continue;

                String readString = new String(readBytes);
                if (readString.indexOf("ignore") > -1) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(owner_,
                                    "The request was rejected because you've entered \"ignore\" into the form.");
                        }
                    });
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public CefResourceRequestHandler getResourceRequestHandler(CefBrowser browser, CefFrame frame,
            CefRequest request, boolean isNavigation, boolean isDownload, String requestInitiator,
            BoolRef disableDefaultHandling) {
        return this;
    }

    @Override
    public boolean onBeforeResourceLoad(CefBrowser browser, CefFrame frame, CefRequest request) {
        // Киритрон: Как я понял, это обработка перед загрузкой ресурсов с какого-то адреса.
        if (request.getMethod().equalsIgnoreCase("POST")
                && request.getURL().equals("https://duckduckgo.com/")) {
            String forwardTo = "https://duckduckgo.com/?q=";
            CefPostData postData = request.getPostData();
            boolean sendAsGet = false;
            if (postData != null) {
                Vector<CefPostDataElement> elements = new Vector<CefPostDataElement>();
                postData.getElements(elements);
                for (CefPostDataElement el : elements) {
                    int numBytes = el.getBytesCount();
                    if (numBytes <= 0) continue;

                    byte[] readBytes = new byte[numBytes];
                    if (el.getBytes(numBytes, readBytes) <= 0) continue;

                    String readString = new String(readBytes).trim();
                    String[] stringPairs = readString.split("&");
                    for (String s : stringPairs) {
                        int startPos = s.indexOf('=');
                        if (s.startsWith("searchFor"))
                            forwardTo += s.substring(startPos + 1);
                        else if (s.startsWith("sendAsGet")) {
                            sendAsGet = true;
                        }
                    }
                }
                if (sendAsGet) postData.removeElements();
            }
            if (sendAsGet) {
                request.setFlags(0);
                request.setMethod("GET");
                request.setURL(forwardTo);
                request.setFirstPartyForCookies(forwardTo);
                HashMap<String, String> headerMap = new HashMap<>();
                request.getHeaderMap(headerMap);
                headerMap.remove("Content-Type");
                headerMap.remove("Origin");
                request.setHeaderMap(headerMap);
            }
        }

        if (Entery_ClearWeb.checkRequest(request)) {
            request.setURL("entery://res/blackhole");
        }

        return false;
    }

    @Override
    public CefResourceHandler getResourceHandler(
            CefBrowser browser, CefFrame frame, CefRequest request) {
        // Киритрон: Тут может быть перехват запроса и последующая его обработка.
        // Например, если запрос отправляется на http://www.foo.bar, то
        // то будет возвращена пустая страница на entery://res/empty
        if (request.getURL().endsWith("foo.bar/")) {
            browser.stopLoad();
            browser.loadURL("entery://res/empty");
            return null;
        }

        //if (request.getURL().endsWith("foo.bar/")) {
        //    return new ResourceSetErrorHandler();
        //}

        return null;
    }

    @Override
    public boolean getAuthCredentials(CefBrowser browser, String origin_url, boolean isProxy,
            String host, int port, String realm, String scheme, CefAuthCallback callback) {
        SwingUtilities.invokeLater(new PasswordDialog(owner_, callback));
        return true;
    }

    @Override
    public boolean onQuotaRequest(
            CefBrowser browser, String origin_url, long new_size, CefRequestCallback callback) {
        return false;
    }

    @Override
    public boolean onCertificateError(CefBrowser browser, ErrorCode cert_error, String request_url,
            CefRequestCallback callback) {
        SwingUtilities.invokeLater(new CertErrorDialog(owner_, cert_error, request_url, callback));
        return true;
    }

    @Override
    public void onPluginCrashed(CefBrowser browser, String pluginPath) {
        toConsole.print(genLogMessage.gen((byte) 3, false, "Plugin " + pluginPath + "CRASHED"));
    }

    @Override
    public void onRenderProcessTerminated(CefBrowser browser, TerminationStatus status) {
        toConsole.print(genLogMessage.gen((byte) 3, false, "render process terminated: " + status));
    }
}
