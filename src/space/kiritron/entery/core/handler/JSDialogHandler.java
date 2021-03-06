// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.

package space.kiritron.entery.core.handler;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefJSDialogCallback;
import org.cef.handler.CefJSDialogHandlerAdapter;
import org.cef.misc.BoolRef;
import space.kiritron.pixel.logger.genLogMessage;
import space.kiritron.pixel.logger.toConsole;

/**
 * @author The Chromium Embedded Framework Authors.
 */

public class JSDialogHandler extends CefJSDialogHandlerAdapter {
    @Override
    public boolean onJSDialog(CefBrowser browser, String origin_url, JSDialogType dialog_type,
            String message_text, String default_prompt_text, CefJSDialogCallback callback,
            BoolRef suppress_message) {
        if (message_text.equalsIgnoreCase("Never displayed")) {
            suppress_message.set(true);
            toConsole.print(genLogMessage.gen((byte) 1, false, "The " + dialog_type + " from origin \"" + origin_url + "\" was suppressed."));
            toConsole.print(genLogMessage.gen((byte) 1, false, "   The content of the suppressed dialog was: \"" + message_text + "\""));
        }
        return false;
    }
}
