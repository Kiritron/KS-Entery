// Copyright (c) 2015 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.handler;

import java.awt.Rectangle;

import org.cef.browser.CefBrowser;

/**
 * An abstract adapter class for receiving windowed render events.
 * The methods in this class are empty.
 * This class exists as convenience for creating handler objects.
 */
public abstract class CefWindowHandlerAdapter implements CefWindowHandler {
    @Override
    public Rectangle getRect(CefBrowser browser) {
        return new Rectangle(0, 0, 0, 0);
    }

    @Override
    public void onMouseEvent(
            CefBrowser browser, int event, int screenX, int screenY, int modifier, int button) {}
}