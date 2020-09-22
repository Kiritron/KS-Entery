// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

/**
 * Callback interface used to asynchronously modify download status.
 */
public interface CefDownloadItemCallback {
    /**
     * Call to cancel the download.
     */
    public void cancel();

    /**
     * Call to pause the download.
     */
    public void pause();

    /**
     * Call to resume the download.
     */
    public void resume();
}
