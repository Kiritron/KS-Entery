// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

/**
 * Generic callback interface used for asynchronous completion.
 */
public interface CefCompletionCallback {
    /**
     * Method that will be called once the task is complete.
     */
    public abstract void onComplete();
}
