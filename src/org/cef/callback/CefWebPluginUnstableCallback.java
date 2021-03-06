// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package org.cef.callback;

/**
 * Interface to implement for receiving unstable plugin information. The methods of this class will
 * be called on the browser process IO thread.
 */
public interface CefWebPluginUnstableCallback {
    /**
     * Method that will be called for the requested plugin.
     *
     * @param path Plugin file path (DLL/bundle/library).
     * @param unstable True if the plugin has reached the crash count threshold of 3 times in 120
     *         seconds.
     */
    public void isUnstable(String path, boolean unstable);
}
