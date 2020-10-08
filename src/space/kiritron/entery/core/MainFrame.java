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

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import org.cef.CefApp;
import org.cef.CefApp.CefVersion;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.handler.CefDisplayHandlerAdapter;
import org.cef.handler.CefFocusHandlerAdapter;
import org.cef.handler.CefLoadHandlerAdapter;
import org.cef.network.CefCookieManager;

import space.kiritron.entery.init;
import space.kiritron.entery.core.dialog.DownloadDialog;
import space.kiritron.entery.core.dialog.WelcomeWindow;
import space.kiritron.entery.core.handler.AppHandler;
import space.kiritron.entery.core.handler.ContextMenuHandler;
import space.kiritron.entery.core.handler.DragHandler;
import space.kiritron.entery.core.handler.JSDialogHandler;
import space.kiritron.entery.core.handler.KeyboardHandler;
import space.kiritron.entery.core.handler.MessageRouterHandler;
import space.kiritron.entery.core.handler.MessageRouterHandlerEx;
import space.kiritron.entery.core.handler.RequestHandler;
import space.kiritron.entery.core.ui.ControlPanel;
import space.kiritron.entery.core.ui.TabManager;
import space.kiritron.entery.core.util.DataUri;
import space.kiritron.entery.ks_libs.pixel.GetOS;
import space.kiritron.entery.ks_libs.pixel.filefunc.FileControls;
import space.kiritron.entery.ks_libs.pixel.filefunc.GetPathOfAPP;
import space.kiritron.entery.ks_libs.pixel.logger.genLogMessage;
import space.kiritron.entery.ks_libs.pixel.logger.toConsole;
import space.kiritron.entery.ks_libs.tolchok.TOLF_Handler;

import static space.kiritron.entery.init.*;

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

public class MainFrame extends BrowserFrame {
    public static MainFrame frame = null;
    public static TabManager tabManager = null;

    private static final long serialVersionUID = -2295538706810864538L;
    public void start(boolean osrEnabledArg, boolean transparentPaintingEnabledArg, boolean createImmediately, String link, String[] args) {
        // Perform startup initialization on platforms that require it.
        if (!CefApp.startup(args)) {
            toConsole.print(genLogMessage.gen((byte) 1, false, "Сбой в инициализации движка."));
            return;
        }

        toConsole.print(genLogMessage.gen((byte) 1, false, "Offscreen rendering " + (osrEnabledArg ? "enabled" : "disabled")));

        // MainFrame keeps all the knowledge to display the embedded browser
        // frame.
        frame = new MainFrame(osrEnabledArg, transparentPaintingEnabledArg, createImmediately, link, args);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int sizeWidth;
        int sizeHeight;
        try {
            sizeWidth = Integer.parseInt(TOLF_Handler.ReadParamFromData(FileControls.ReadFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf"), "WINDOW-OPTIONS", "width"));
            sizeHeight = Integer.parseInt(TOLF_Handler.ReadParamFromData(FileControls.ReadFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf"), "WINDOW-OPTIONS", "height"));
        } catch (IOException | NumberFormatException e) {
            toConsole.print(genLogMessage.gen((byte) 1, false, "Не удалось загрузить настройки окна браузера из конфига window-options.tolf."));
            sizeWidth = 800;
            sizeHeight = 600;
        }

        Image img = new ImageIcon(init.class.getResource("res/logo.png")).getImage();
        frame.setIconImage(img);

        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        frame.setSize(sizeWidth, sizeHeight);
        frame.setBounds(locationX, locationY, sizeWidth, sizeHeight);

        try {
            if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf"), "WINDOW-OPTIONS", "fullscreen").equals("true")) {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                frame.setExtendedState(JFrame.NORMAL);
            }
        } catch (IOException e) {
            toConsole.print(genLogMessage.gen((byte) 3, false, "Не удалось получить последнее состояние окна."));
            frame.setExtendedState(JFrame.NORMAL);
        }

        frame.setVisible(true);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if ((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == 0) {
                    try {
                        String data = FileControls.ReadFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf");
                        data = TOLF_Handler.EditParamInData(data, "WINDOW-OPTIONS", "fullscreen", "false");
                        FileControls.writeToFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf", data);
                    } catch (IOException ioException) {
                        toConsole.print(genLogMessage.gen((byte) 3, false, "Не удалось сохранить состояние окна в window-options.tolf"));
                    }
                } else {
                    try {
                        String data = FileControls.ReadFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf");
                        data = TOLF_Handler.EditParamInData(data, "WINDOW-OPTIONS", "fullscreen", "true");
                        FileControls.writeToFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf", data);
                    } catch (IOException ioException) {
                        toConsole.print(genLogMessage.gen((byte) 3, false, "Не удалось сохранить состояние окна в window-options.tolf"));
                    }
                }
            }

            /*
            TODO: Возможно в будущем потребуется сохранять позицию окна
            @Override
            public void componentMoved(ComponentEvent e) {
                Point frameLocation = frame.getLocation();
            }
            */
        });

        // Киритрон: Добавил слушатель, который отлавливает попытку закрыть окно пользователем
        // и браузер интересуется, уверен ли пользователь в этом. Если да, то убиваются экземпляры
        // браузеров(по крайней мере так задумывалось) и программа жестко завершается через
        // System.exit(0).
        //
        // Однако это вызвало проблему, что по сути программа завершается с крашем, пусть и код
        // завершения равен 0. Данный баг провоцируется, если открыто более одной вкладки.
        //
        // UPD: Ниже был реализован костыль в блоке for, который перед закрытием закрывает все вкладки и убивает тем самым
        // экземпляры браузеров. Баг, который был описан выше, в принципе исправлен, но есть подозрения, что он перешёл в
        // плавающий баг, то есть баг, который как бы есть, но его как бы и нет. Требуется больше тестов.
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // Но сначала необходимо предотвратить закрытие окна по нажатию на крестик
        frame.addWindowListener(new WindowAdapter() { // Сам слушатель
            public void windowClosing(WindowEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog(frame, "Вы собираетесь закрыть браузер Энтэри.\n" +
                                "Вы уверены, что хотите этого?",
                        "Закрытие Энтэри", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (dialogResult == JOptionPane.YES_OPTION) {
                    // Блок try и костыль внутри
                    try {
                        int CountOfTabs = tabManager.getTabCount() - 1;

                        for (int sd = CountOfTabs; sd != 0;) {
                            tabManager.CloseTab(sd-1);
                            sd--;
                        }
                    } catch (Exception eee) {
                        // Ничего
                    }

                    MainFrameClosed();
                    System.exit(0);
                }
            }
        });

        if (!FileControls.SearchFile(GetPathOfAPP.GetPathWithSep() + ".opened")) {
            try {
                FileControls.CreateFile(GetPathOfAPP.GetPathWithSep() + ".opened");
                WelcomeWindow WW = new WelcomeWindow(frame);
                WW.setVisible(true);
            } catch (IOException vyg) {
                // Ничего
            }
        }

        if (outdated_major) {
            if (!FileControls.SearchFile(GetPathOfAPP.GetPathWithSep() + ".majorupdateopened")) {
                try {
                    FileControls.CreateFile(GetPathOfAPP.GetPathWithSep() + ".majorupdateopened");
                    int dialogResult = JOptionPane.showConfirmDialog(this, "Доброго времени суток!\n" +
                                    "Вы видите данное сообщение потому, что при инициализации\n" +
                                    "было обнаружено, что для Энтэри вышло мажорное или же\n" +
                                    "крупное обновление, которое наверняка содержит много\n" +
                                    "исправлений и нововведений. Открыть страницу загрузки?\n\n" +
                                    "Данное сообщение появляется лишь раз.",
                            "Доступно крупное обновление для Энтэри", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        tabManager.OpenTabFromTab("https://kiritron.space/projects/entery/");
                    }
                } catch (IOException vywg) {
                    // Ничего
                }
            }
        }
    }

    private final CefClient client_;
    private String errorMsg_ = "";
    private ControlPanel control_pane_;
    public boolean browserFocus_ = true;
    private boolean osr_enabled_;
    private boolean transparent_painting_enabled_;

    public MainFrame(boolean osrEnabled, boolean transparentPaintingEnabled,
        boolean createImmediately, String link, String[] args) {
            this.osr_enabled_ = osrEnabled;
            this.transparent_painting_enabled_ = transparentPaintingEnabled;
            
            CefApp myApp;
            if (CefApp.getState() != CefApp.CefAppState.INITIALIZED) {
                // 1) CefApp is the entry point for JCEF. You can pass
                //    application arguments to it, if you want to handle any
                //    chromium or CEF related switches/attributes in
                //    the native world.
                CefSettings settings = new CefSettings();
                settings.windowless_rendering_enabled = osrEnabled;
                // try to load URL "about:blank" to see the background color
                //settings.background_color = settings.new ColorType(0, 24, 24, 24);
                myApp = CefApp.getInstance(args, settings);

                CefVersion version = myApp.getVersion();
                toConsole.print(genLogMessage.gen((byte) 1, false, "КС Энтери использует Хромиум: " + version.toString().replaceAll("\n", " | ")));

                //    We're registering our own AppHandler because we want to
                //    add an own schemes (search:// and client://) and its corresponding
                //    protocol handlers. So if you enter "search:something on the web", your
                //    search request "something on the web" is forwarded to www.google.com
                CefApp.addAppHandler(new AppHandler(args));
            } else {
                myApp = CefApp.getInstance();
            }

            //    By calling the method createClient() the native part
            //    of JCEF/CEF will be initialized and an  instance of
            //    CefClient will be created. You can create one to many
            //    instances of CefClient.
            client_ = myApp.createClient();

            // 2) You have the ability to pass different handlers to your
            //    instance of CefClient. Each handler is responsible to
            //    deal with different informations (e.g. keyboard input).
            //
            //    For each handler (with more than one method) adapter
            //    classes exists. So you don't need to override methods
            //    you're not interested in.
            DownloadDialog downloadDialog = new DownloadDialog(this);
            client_.addContextMenuHandler(new ContextMenuHandler(this));
            client_.addDownloadHandler(downloadDialog);
            client_.addDragHandler(new DragHandler());
            client_.addJSDialogHandler(new JSDialogHandler());
            client_.addKeyboardHandler(new KeyboardHandler());
            client_.addRequestHandler(new RequestHandler(this));

            //    Beside the normal handler instances, we're registering a MessageRouter
            //    as well. That gives us the opportunity to reply to JavaScript method
            //    calls (JavaScript binding). We're using the default configuration, so
            //    that the JavaScript binding methods "cefQuery" and "cefQueryCancel"
            //    are used.
            CefMessageRouter msgRouter = CefMessageRouter.create();
            msgRouter.addHandler(new MessageRouterHandler(), true);
            msgRouter.addHandler(new MessageRouterHandlerEx(client_), false);
            client_.addMessageRouter(msgRouter);

            // 2.2) To disable/enable navigation buttons and to display a progress bar
            //      which indicates the load state of our website, we're overloading
            //      the CefLoadHandler as nested anonymous class. Beside this, the
            //      load handler is responsible to deal with (load) errors as well.
            //      For example if you navigate to a URL which does not exist, the
            //      browser will show up an error message.

            client_.addLoadHandler(new CefLoadHandlerAdapter() {
                @Override
                public void onLoadingStateChange(CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
                    tabManager.controlPanels.get(tabManager.getSelectedIndex() + 1).update(
                            browser,
                            isLoading,
                            canGoBack,
                            canGoForward
                    );
                    //status_panel_.setIsInProgress(isLoading);
                }

                @Override
                public void onLoadError(CefBrowser browser, CefFrame frame, ErrorCode errorCode,
                        String errorText, String failedUrl) {
                    if (errorCode != ErrorCode.ERR_NONE && errorCode != ErrorCode.ERR_ABORTED) {
                        String charset;
                        String bg_color;
                        String text_color;
                        if (GetOS.isWindows()) {
                            charset = "<meta charset=\"windows-1251\">";
                        } else {
                            charset = "<meta charset=\"UTF-8\">";
                        }

                        if (DarculaTheme) {
                            bg_color = "212121";
                            text_color = "DCDCDC";
                        } else {
                            bg_color = "DCDCDC";
                            text_color = "212121";
                        }

                        errorMsg_ = "<html><head>";
                        errorMsg_ += charset + "<title>Ошибка при загрузке страницы</title>";
                        errorMsg_ += "</head><body style='font-family: sans-serif; background-color: #" + bg_color + "; color: #" + text_color + ";'>";
                        errorMsg_ += "<center><div style='margin-top: 10%;'>";
                        errorMsg_ += "<h1>Сбой в загрузке страницы</h1>";
                        errorMsg_ += "<h3>Адрес страницы - " + failedUrl + "</h3>";
                        errorMsg_ += "<p>" + "Код ошибки: " + (errorText == null ? "" : errorText) + "</p>";
                        errorMsg_ += "</div></center>";
                        errorMsg_ += "</body></html>";

                        browser.stopLoad();

                        browser.loadURL(DataUri.create("text/html", errorMsg_));
                        errorMsg_ = "";
                        charset = null;
                        bg_color = null;
                        text_color = null;
                    }
                }
            });

            // Create the browser.
            
            CefBrowser browser;
            if (link == null) {
                browser = client_.createBrowser(
                        init.HomePage, osrEnabled, transparentPaintingEnabled, null);
            } else {
                browser = client_.createBrowser(
                        link, osrEnabled, transparentPaintingEnabled, null);
            }
            //setBrowser(browser);

            // Set up the UI for this example implementation.
            JPanel contentPanel = createContentPanel(this, null, control_pane_, downloadDialog, CefCookieManager.getGlobalManager(), client_);
            getContentPane().add(contentPanel, BorderLayout.CENTER);

            // Clear focus from the browser when the address field gains focus.
            control_pane_.getAddressField().addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (!browserFocus_) return;
                    browserFocus_ = false;
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                    control_pane_.getAddressField().requestFocus();
                }
            });

            // Clear focus from the address field when the browser gains focus.
            client_.addFocusHandler(new CefFocusHandlerAdapter() {
                @Override
                public void onGotFocus(CefBrowser browser) {
                    if (browserFocus_) return;
                    browserFocus_ = true;
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                    browser.setFocus(true);
                }

                @Override
                public void onTakeFocus(CefBrowser browser, boolean next) {
                    browserFocus_ = false;
                }
            });

            if (createImmediately) browser.createImmediately();

            tabManager = new TabManager(this, contentPanel, browser, control_pane_, client_, osrEnabled, transparentPaintingEnabled, downloadDialog, CefCookieManager.getGlobalManager());
            //contentPanel.add(TabManager, BorderLayout.CENTER);
            if (AddressFromArgs == null) {
                tabManager.OpenTab(init.HomePage);
            } else {
                tabManager.OpenTab(init.AddressFromArgs);
            }
            
            // 2.1) We're overriding CefDisplayHandler as nested anonymous class
            //      to update our address-field, the title of the panel as well
            //      as for updating the status-bar on the bottom of the browser
            client_.addDisplayHandler(new CefDisplayHandlerAdapter() {
                @Override
                public void onAddressChange(CefBrowser browser, CefFrame frame, String url) {
                    //control_pane_.setAddress(browser, url);
                    tabManager.UpdateTab(browser, null, url);
                }
                @Override
                public void onTitleChange(CefBrowser browser, String title) {
                    setTitle(title + " — Энтэри");
                    tabManager.UpdateTab(browser, title, null);
                }
                @Override
                public void onStatusMessage(CefBrowser browser, String value) {
                    //status_panel_.setStatusText(value);
                }
            });
    }

    private JPanel createContentPanel(MainFrame owner, CefBrowser browser, ControlPanel control_pane, DownloadDialog downloadDialog, CefCookieManager cookieManager, CefClient client_) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        control_pane_ = new ControlPanel(owner, browser, downloadDialog, cookieManager, client_);
        //tabManager.controlPanels.add(control_pane_);
        //contentPanel.add(control_pane_, BorderLayout.NORTH);
        //contentPanel.add(status_panel_, BorderLayout.SOUTH);
        return contentPanel;
    }

    public boolean isOsrEnabled() {
        return osr_enabled_;
    }

    public boolean isTransparentPaintingEnabled() {
        return transparent_painting_enabled_;
    }
}
