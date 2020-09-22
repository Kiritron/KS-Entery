// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package space.kiritron.entery.core.handler;

import org.cef.browser.CefBrowser;
import org.cef.handler.CefKeyboardHandlerAdapter;
import space.kiritron.entery.core.ui.ControlPanel;

public class KeyboardHandler extends CefKeyboardHandlerAdapter {

    @Override
    public boolean onKeyEvent(CefBrowser browser, CefKeyEvent event) {
        ControlPanel CP = new ControlPanel(browser);
        //if (!event.focus_on_editable_field && event.windows_key_code == 0x74) {
        if (event.windows_key_code == 0x74) {
            // Special handling for the space character when an input element does not
            // have focus. Handling the event in OnPreKeyEvent() keeps the event from
            // being processed in the renderer. If we instead handled the event in the
            // OnKeyEvent() method the space key would cause the window to scroll in
            // addition to showing the alert box.
            //if (event.type == CefKeyEvent.EventType.KEYEVENT_RAWKEYDOWN) {
            //    browser.executeJavaScript("alert('You pressed the space bar!');", "", 0);
            //}

            if (event.type == CefKeyEvent.EventType.KEYEVENT_RAWKEYDOWN) {
                CP.reloadPage();
            }
            return true;
        }
        return false;
    }
}
