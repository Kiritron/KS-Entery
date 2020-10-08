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

import org.cef.CefApp;
import org.cef.CefApp.CefAppState;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefSchemeHandlerFactory;
import org.cef.callback.CefSchemeRegistrar;
import org.cef.handler.CefAppHandlerAdapter;
import org.cef.handler.CefResourceHandler;
import org.cef.network.CefRequest;
import space.kiritron.entery.core.MainFrame;
import space.kiritron.entery.ks_libs.pixel.logger.genLogMessage;
import space.kiritron.entery.ks_libs.pixel.logger.toConsole;

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

public class AppHandler extends CefAppHandlerAdapter {
    // We're registering our own schemes to demonstrate how to use
    // CefAppHandler.onRegisterCustomSchemes() in combination with
    // CefApp.registerSchemeHandlerFactory().
    public AppHandler(String[] args) {
        super(args);
    }

    // (1) First of all we have to register our custom schemes by implementing
    //     the method "onRegisterCustomSchemes. The scheme names are added by
    //     calling CefSchemeRegistrar.addCustomScheme.
    @Override
    public void onRegisterCustomSchemes(CefSchemeRegistrar registrar) {
        //if (registrar.addCustomScheme(
        //            SearchSchemeHandler.scheme, true, false, false, false, true, false, false)) {
        //    toConsole.print(genLogMessage.gen((byte) 1, false, "Added scheme " + SearchSchemeHandler.scheme + "://"));
        //}
        if (registrar.addCustomScheme(
                   ClientSchemeHandler.scheme, true, false, false, false, true, false, false)) {
            toConsole.print(genLogMessage.gen((byte) 1, false, "Added scheme " + ClientSchemeHandler.scheme + "://"));
        }
    }

    // (2) At the next step we have to register a SchemeHandlerFactory which is
    //     called if an user enters our registered scheme.
    //
    //     This is done via the CefApp.registerSchemeHandlerFactory() method.
    //     A good place to call this function is from
    //     CefAppHandler.onContextInitialized().
    //
    //     The empty |domain_name| value will cause the factory to match all
    //     domain names. A set |domain_name| will only be valid for the entered
    //     domain.
    @Override
    public void onContextInitialized() {
        CefApp cefApp = CefApp.getInstance();
        //cefApp.registerSchemeHandlerFactory(
        //        SearchSchemeHandler.scheme, SearchSchemeHandler.domain, new SchemeHandlerFactory());
        cefApp.registerSchemeHandlerFactory(
                ClientSchemeHandler.scheme, ClientSchemeHandler.domain, new SchemeHandlerFactory());
    }

    // (3) The SchemeHandlerFactory creates a new ResourceHandler instance for each
    //     request the user has send to the browser. The ResourceHandler is the
    //     responsible class to process and return the result of a received
    //     request.
    private class SchemeHandlerFactory implements CefSchemeHandlerFactory {
        @Override
        public CefResourceHandler create(
                CefBrowser browser, CefFrame frame, String schemeName, CefRequest request) {
            //if (schemeName.equals(SearchSchemeHandler.scheme))
            //    return new SearchSchemeHandler(browser);else
            if (schemeName.equals(ClientSchemeHandler.scheme))
                return new ClientSchemeHandler();
            return null;
        }
    }

    @Override
    public void stateHasChanged(CefAppState state) {
        toConsole.print(genLogMessage.gen((byte) 1, false, "AppHandler.stateHasChanged: " + state));
        if (state == CefAppState.TERMINATED) {
            System.exit(0);
        }
    }
}
