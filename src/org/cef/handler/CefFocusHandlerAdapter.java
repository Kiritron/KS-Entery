// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.handler;

import org.cef.browser.CefBrowser;

/**
 * An abstract adapter class for receiving focus events.
 * The methods in this class are empty.
 * This class exists as convenience for creating handler objects.
 */
public abstract class CefFocusHandlerAdapter implements CefFocusHandler {
    @Override
    public void onTakeFocus(CefBrowser browser, boolean next) {
        return;
    }

    @Override
    public boolean onSetFocus(CefBrowser browser, FocusSource source) {
        return false;
    }

    @Override
    public void onGotFocus(CefBrowser browser) {}
}
