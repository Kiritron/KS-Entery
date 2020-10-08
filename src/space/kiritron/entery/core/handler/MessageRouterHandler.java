// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.

package space.kiritron.entery.core.handler;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

/**
 * @author The Chromium Embedded Framework Authors.
 */

public class MessageRouterHandler extends CefMessageRouterHandlerAdapter {
    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long query_id, String request,
            boolean persistent, CefQueryCallback callback) {
        if (request.indexOf("BindingTest:") == 0) {
            // Reverse the message and return it to the JavaScript caller.
            String msg = request.substring(12);
            callback.success(new StringBuilder(msg).reverse().toString());
            return true;
        }
        // Not handled.
        return false;
    }
}
