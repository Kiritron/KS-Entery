// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.browser;

import org.cef.CefClient;

/**
 * Creates a new instance of CefBrowser according the passed values
 */
public class CefBrowserFactory {
    public static CefBrowser create(CefClient client, String url, boolean isOffscreenRendered,
            boolean isTransparent, CefRequestContext context) {
        if (isOffscreenRendered) return new CefBrowserOsr(client, url, isTransparent, context);
        return new CefBrowserWr(client, url, context);
    }
}
