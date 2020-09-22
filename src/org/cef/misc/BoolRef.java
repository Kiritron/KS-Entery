// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.misc;

/**
 * Helper class for passing boolean values by reference.
 */
public class BoolRef {
    private boolean value_;

    public BoolRef() {}

    public BoolRef(boolean value) {
        value_ = value;
    }

    public void set(boolean value) {
        value_ = value;
    }

    public boolean get() {
        return value_;
    }
}
