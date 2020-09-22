// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

/**
 * Generic callback interface used for asynchronous continuation.
 */
public interface CefCallback {
    /**
     * Continue processing.
     */
    void Continue();

    /**
     * Cancel processing.
     */
    void cancel();
}
