// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

import org.cef.misc.CefPrintSettings;

/**
 * Callback interface for asynchronous continuation of print dialog requests.
 */
public interface CefPrintDialogCallback {
    /**
     * Continue printing with the specified |settings|.
     */
    void Continue(CefPrintSettings settings);

    /**
     * Cancel the printing.
     */
    void cancel();
}
