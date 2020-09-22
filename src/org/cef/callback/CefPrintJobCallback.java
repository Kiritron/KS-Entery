// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

/**
 * Callback interface for asynchronous continuation of print job requests.
 */
public interface CefPrintJobCallback {
    /**
     * Indicate completion of the print job.
     */
    void Continue();
}
