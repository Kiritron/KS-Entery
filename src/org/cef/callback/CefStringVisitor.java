// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

/**
 * Public interface to receive string values asynchronously.
 */
public interface CefStringVisitor {
    /**
     * Called when the string is available.
     * @param string Requested string.
     */
    void visit(String string);
}
