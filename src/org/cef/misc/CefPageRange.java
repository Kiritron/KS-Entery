// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.misc;

/**
 * Class representing a print job page range.
 */
public class CefPageRange {
    public int from;
    public int to;

    public CefPageRange() {}

    public CefPageRange(int from, int to) {
        this.from = from;
        this.to = to;
    }
}