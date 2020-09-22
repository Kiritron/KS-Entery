// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.handler;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.network.CefRequest.TransitionType;

/**
 * An abstract adapter class for receiving load events.
 * The methods in this class are empty.
 * This class exists as convenience for creating handler objects.
 */
public abstract class CefLoadHandlerAdapter implements CefLoadHandler {
    @Override
    public void onLoadingStateChange(
            CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {}
    // TODO: Обратить внимание на это. Возможно получится перехватить.
    @Override
    public void onLoadStart(CefBrowser browser, CefFrame frame, TransitionType transitionType) {}

    @Override
    public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {}

    @Override
    public void onLoadError(CefBrowser browser, CefFrame frame, ErrorCode errorCode,
            String errorText, String failedUrl) {}
}
