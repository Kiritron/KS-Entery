// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

import java.util.Vector;

class CefFileDialogCallback_N extends CefNativeAdapter implements CefFileDialogCallback {
    CefFileDialogCallback_N() {}

    @Override
    protected void finalize() throws Throwable {
        Cancel();
        super.finalize();
    }

    @Override
    public void Continue(int selectedAcceptFilter, Vector<String> filePaths) {
        try {
            N_Continue(getNativeRef(null), selectedAcceptFilter, filePaths);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    @Override
    public void Cancel() {
        try {
            N_Cancel(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Continue(
            long self, int selectedAcceptFilter, Vector<String> filePaths);
    private final native void N_Cancel(long self);
}
