// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

class CefAuthCallback_N extends CefNativeAdapter implements CefAuthCallback {
    CefAuthCallback_N() {}

    @Override
    protected void finalize() throws Throwable {
        cancel();
        super.finalize();
    }

    @Override
    public void Continue(String username, String password) {
        try {
            N_Continue(getNativeRef(null), username, password);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    @Override
    public void cancel() {
        try {
            N_Cancel(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Continue(long self, String username, String password);
    private final native void N_Cancel(long self);
}
