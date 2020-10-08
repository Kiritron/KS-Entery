/*
 * Copyright 2020 Kiritron's Space
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.kiritron.entery.core.handler;

import org.cef.callback.CefCallback;
import org.cef.handler.CefLoadHandler.ErrorCode;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import space.kiritron.entery.ks_libs.pixel.logger.genLogMessage;
import space.kiritron.entery.ks_libs.pixel.logger.toConsole;

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

public class ResourceSetErrorHandler extends CefResourceHandlerAdapter {
    @Override
    public boolean processRequest(CefRequest request, CefCallback callback) {
        toConsole.print(genLogMessage.gen((byte) 1, false, "processRequest: " + request));
        callback.Continue();
        return true;
    }

    @Override
    public void getResponseHeaders(
            CefResponse response, IntRef response_length, StringRef redirectUrl) {
        response.setError(ErrorCode.ERR_NOT_IMPLEMENTED);
        toConsole.print(genLogMessage.gen((byte) 1, false, "getResponseHeaders: " + response));
    }
}