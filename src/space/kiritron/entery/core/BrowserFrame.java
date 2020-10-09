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

package space.kiritron.entery.core;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.cef.CefApp;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefLifeSpanHandlerAdapter;
import space.kiritron.pixel.logger.genLogMessage;
import space.kiritron.pixel.logger.toConsole;

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

public class BrowserFrame extends JFrame {
    private volatile boolean isClosed_ = false;
    private CefBrowser browser_ = null;
    private static int browserCount_ = 0;
    private Runnable afterParentChangedAction_ = null;

    public BrowserFrame() {
        this(null);
    }

    public BrowserFrame(String title) {
        super(title);

        // Browser window closing works as follows:
        //   1. Clicking the window X button calls WindowAdapter.windowClosing.
        //   2. WindowAdapter.windowClosing calls CefBrowser.close(false).
        //   3. CEF calls CefLifeSpanHandler.doClose() which calls CefBrowser.doClose()
        //      which returns true (canceling the close).
        //   4. CefBrowser.doClose() triggers another call to WindowAdapter.windowClosing.
        //   5. WindowAdapter.windowClosing calls CefBrowser.close(true).
        //   6. For windowed browsers CEF destroys the native window handle. For OSR
        //      browsers CEF calls CefLifeSpanHandler.doClose() which calls
        //      CefBrowser.doClose() again which returns false (allowing the close).
        //   7. CEF calls CefLifeSpanHandler.onBeforeClose and the browser is destroyed.
        //
        // On macOS pressing Cmd+Q results in a call to CefApp.handleBeforeTerminate
        // which calls CefBrowser.close(true) for each existing browser. CEF then calls
        // CefLifeSpanHandler.onBeforeClose and the browser is destroyed.
        //
        // Application shutdown works as follows:
        //   1. CefLifeSpanHandler.onBeforeClose calls CefApp.getInstance().dispose()
        //      when the last browser window is destroyed.
        //   2. CefAppHandler.stateHasChanged terminates the application by calling
        //      System.exit(0) when the state changes to CefAppState.TERMINATED.

        /*

        Киритрон: Отключил данный слушатель, так как решил закрывать
        браузер иным способом.

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (browser_ == null) {
                    // If there's no browser we can dispose immediately.
                    isClosed_ = true;
                    toConsole.print(genLogMessage.gen((byte) 1, false, "BrowserFrame.windowClosing Frame.dispose"));
                    dispose();
                    return;
                }

                boolean isClosed = isClosed_;

                if (isClosed) {
                    // Cause browser.doClose() to return false so that OSR browsers
                    // can close.
                    browser_.setCloseAllowed();
                }

                // Results in another call to this method.
                toConsole.print(genLogMessage.gen((byte) 1, false, "BrowserFrame.windowClosing CefBrowser.close(" + isClosed + ")"));
                browser_.close(isClosed);
                if (!isClosed_) {
                    isClosed_ = true;
                }
                if (isClosed) {
                    // Dispose after the 2nd call to this method.
                    toConsole.print(genLogMessage.gen((byte) 1, false, "BrowserFrame.windowClosing Frame.dispose"));
                    dispose();
                }
            }
        });

         */
    }

    public void MainFrameClosed() {
        if (browser_ == null) {
            // If there's no browser we can dispose immediately.
            isClosed_ = true;
            toConsole.print(genLogMessage.gen((byte) 1, false, "BrowserFrame.windowClosing Frame.dispose"));
            dispose();
            return;
        }

        boolean isClosed = isClosed_;

        if (isClosed) {
            // Cause browser.doClose() to return false so that OSR browsers
            // can close.
            browser_.setCloseAllowed();
        }

        // Results in another call to this method.
        toConsole.print(genLogMessage.gen((byte) 1, false, "BrowserFrame.windowClosing CefBrowser.close(" + isClosed + ")"));
        browser_.close(isClosed);
        if (!isClosed_) {
            isClosed_ = true;
        }
        if (isClosed) {
            // Dispose after the 2nd call to this method.
            toConsole.print(genLogMessage.gen((byte) 1, false, "BrowserFrame.windowClosing Frame.dispose"));
            dispose();
        }
    }

    public void setBrowser(CefBrowser browser) {
        if (browser_ == null) browser_ = browser;

        browser_.getClient().removeLifeSpanHandler();
        browser_.getClient().addLifeSpanHandler(new CefLifeSpanHandlerAdapter() {
            @Override
            public void onAfterCreated(CefBrowser browser) {
                toConsole.print(genLogMessage.gen((byte) 1, false, "BrowserFrame.onAfterCreated id=" + browser.getIdentifier()));
                browserCount_++;
            }

            @Override
            public void onAfterParentChanged(CefBrowser browser) {
                toConsole.print(genLogMessage.gen((byte) 1, false, "BrowserFrame.onAfterParentChanged id=" + browser.getIdentifier()));
                if (afterParentChangedAction_ != null) {
                    SwingUtilities.invokeLater(afterParentChangedAction_);
                    afterParentChangedAction_ = null;
                }
            }

            @Override
            public boolean doClose(CefBrowser browser) {
                boolean result = browser.doClose();
                toConsole.print(genLogMessage.gen((byte) 1, false, "BrowserFrame.doClose id=" + browser.getIdentifier()
                        + " CefBrowser.doClose=" + result));
                return result;
            }

            @Override
            public void onBeforeClose(CefBrowser browser) {
                toConsole.print(genLogMessage.gen((byte) 1, false, "BrowserFrame.onBeforeClose id=" + browser.getIdentifier()));
                if (--browserCount_ == 0) {
                    toConsole.print(genLogMessage.gen((byte) 1, false, "BrowserFrame.onBeforeClose CefApp.dispose"));
                    CefApp.getInstance().dispose();
                }
            }
        });
    }

    public void removeBrowser(Runnable r) {
        toConsole.print(genLogMessage.gen((byte) 1, false, "BrowserFrame.removeBrowser"));
        afterParentChangedAction_ = r;
        remove(browser_.getUIComponent());
        // The removeNotify() notification should be sent as a result of calling remove().
        // However, it isn't in all cases so we do it manually here.
        browser_.getUIComponent().removeNotify();
        browser_ = null;
    }

    public CefBrowser getBrowser() {
        return browser_;
    }
}
