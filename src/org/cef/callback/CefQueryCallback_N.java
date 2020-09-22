// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

class CefQueryCallback_N extends CefNativeAdapter implements CefQueryCallback {
    CefQueryCallback_N() {}

    @Override
    protected void finalize() throws Throwable {
        failure(-1, "Unexpected call to CefQueryCallback_N::finalize()");
        super.finalize();
    }

    @Override
    public void success(String response) {
        try {
            N_Success(getNativeRef(null), response);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    @Override
    public void failure(int error_code, String error_message) {
        try {
            N_Failure(getNativeRef(null), error_code, error_message);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Success(long self, String response);
    private final native void N_Failure(long self, int error_code, String error_message);
}
