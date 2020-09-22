// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

/**
 * Callback interface used for asynchronous continuation of JavaScript dialog
 * requests.
 */
public interface CefJSDialogCallback {
    /**
     * Continue the JS dialog request.
     *
     * @param success Set to true if the OK button was pressed.
     * @param user_input The value should be specified for prompt dialogs.
     */
    public void Continue(boolean success, String user_input);
}
