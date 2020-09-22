// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.handler;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;

/**
 * An abstract adapter class for receiving life span events.
 * The methods in this class are empty.
 * This class exists as convenience for creating handler objects.
 */
public abstract class CefLifeSpanHandlerAdapter implements CefLifeSpanHandler {
    @Override
    public boolean onBeforePopup(
            CefBrowser browser, CefFrame frame, String target_url, String target_frame_name) {
        return false;
    }

    @Override
    public void onAfterCreated(CefBrowser browser) {}

    @Override
    public void onAfterParentChanged(CefBrowser browser) {}

    @Override
    public boolean doClose(CefBrowser browser) {
        return false;
    }

    @Override
    public void onBeforeClose(CefBrowser browser) {}
}
