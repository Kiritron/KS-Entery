// Copyright (c) 2017 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.browser;

import org.cef.callback.CefNativeAdapter;

/**
 * This class represents all methods which are connected to the
 * native counterpart CEF.
 * The visibility of this class is "package".
 */
class CefFrame_N extends CefNativeAdapter implements CefFrame {
    CefFrame_N() {}

    @Override
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    @Override
    public void dispose() {
        try {
            N_Dispose(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    @Override
    public long getIdentifier() {
        try {
            return N_GetIdentifier(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return -1;
        }
    }

    @Override
    public String getURL() {
        try {
            return N_GetURL(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }

    @Override
    public String getName() {
        try {
            return N_GetName(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isMain() {
        try {
            return N_IsMain(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isValid() {
        try {
            return N_IsValid(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isFocused() {
        try {
            return N_IsFocused(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }

    @Override
    public CefFrame getParent() {
        try {
            return N_GetParent(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }

    @Override
    public void executeJavaScript(String code, String url, int line) {
        try {
            N_ExecuteJavaScript(getNativeRef(null), code, url, line);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Dispose(long self);
    private final native long N_GetIdentifier(long self);
    private final native String N_GetURL(long self);
    private final native String N_GetName(long self);
    private final native boolean N_IsMain(long self);
    private final native boolean N_IsValid(long self);
    private final native boolean N_IsFocused(long self);
    private final native CefFrame N_GetParent(long self);
    private final native void N_ExecuteJavaScript(long self, String code, String url, int line);
}