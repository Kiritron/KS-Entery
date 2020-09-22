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