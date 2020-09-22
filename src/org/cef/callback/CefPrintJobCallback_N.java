// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

class CefPrintJobCallback_N extends CefNativeAdapter implements CefPrintJobCallback {
    CefPrintJobCallback_N() {}

    @Override
    protected void finalize() throws Throwable {
        Continue();
        super.finalize();
    }

    @Override
    public void Continue() {
        try {
            N_Continue(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Continue(long self);
}
