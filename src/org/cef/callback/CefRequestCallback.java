// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

/**
 * Callback interface used for asynchronous continuation of quota requests.
 */
public interface CefRequestCallback {
    /**
     * Continue the url request.
     *
     * @param allow If set to true the request will be continued.
     *   Otherwise, the request will be canceled.
     */
    void Continue(boolean allow);

    /**
     * Cancel the url request.
     */
    void Cancel();
}
