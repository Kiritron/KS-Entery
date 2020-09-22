// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

class CefJSDialogCallback_N extends CefNativeAdapter implements CefJSDialogCallback {
    CefJSDialogCallback_N() {}

    @Override
    protected void finalize() throws Throwable {
        Continue(false, "");
        super.finalize();
    }

    @Override
    public void Continue(boolean success, String user_input) {
        try {
            N_Continue(getNativeRef(null), success, user_input);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Continue(long self, boolean success, String user_input);
}
