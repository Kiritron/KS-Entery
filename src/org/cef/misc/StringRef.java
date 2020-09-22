// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.misc;

/**
 * Helper class for passing String values by reference.
 */
public class StringRef {
    private String value_;

    public StringRef() {}

    public StringRef(String value) {
        value_ = value;
    }

    public void set(String value) {
        value_ = value;
    }

    public String get() {
        return value_;
    }
}
