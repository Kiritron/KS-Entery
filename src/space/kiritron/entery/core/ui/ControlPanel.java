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

package space.kiritron.entery.core.ui;

import static space.kiritron.entery.core.MainFrame.tabManager;
import static space.kiritron.entery.core.handler.BookmarkHandler.AddBookmarkToDataFile;
import static space.kiritron.entery.core.handler.BookmarkHandler.ReadBookmarksFromDataFile;
import static space.kiritron.entery.core.handler.BookmarkHandler.RemoveBookmarkInDataFile;
import static space.kiritron.entery.init.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import javax.swing.*;

import org.cef.CefClient;
import org.cef.OS;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefPdfPrintCallback;
import org.cef.callback.CefRunFileDialogCallback;
import org.cef.callback.CefStringVisitor;
import org.cef.handler.CefDialogHandler.FileDialogMode;
import org.cef.handler.CefFocusHandlerAdapter;
import org.cef.misc.CefPdfPrintSettings;
import org.cef.network.CefCookieManager;

import space.kiritron.entery.init;
import space.kiritron.entery.core.modules.Extensions;
import space.kiritron.entery.core.MainFrame;
import space.kiritron.entery.core.dialog.CookieManagerDialog;
import space.kiritron.entery.core.dialog.DevToolsDialog;
import space.kiritron.entery.core.dialog.DownloadDialog;
import space.kiritron.entery.core.dialog.ShowTextDialog;
import space.kiritron.entery.ks_libs.pixel.filefunc.DirControls;
import space.kiritron.entery.ks_libs.pixel.filefunc.FileControls;
import space.kiritron.entery.ks_libs.pixel.filefunc.GetPathOfAPP;
import space.kiritron.entery.ks_libs.pixel.logger.genLogMessage;
import space.kiritron.entery.ks_libs.pixel.logger.toConsole;
import space.kiritron.entery.ks_libs.tolchok.TOLF_Handler;

/**
 * @author Киритрон Стэйблкор, Мистер Рекс(MR.REX) и The Chromium Embedded Framework Authors.
 */

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {
    private final JButton backButton_;
    private final JButton forwardButton_;
    private final JButton homeButton_;
    private final JButton reloadButton_;
    private final JButton pipButton_;
    private final JButton menuButton_;
    private final JButton downloadsButton_;
    private final JTextField address_field_;
    private CefBrowser browser_;
    private boolean CanReloadPage = false;
    private final JPopupMenu PopupMenu;
    private final Frame owner_;
    private final CefCookieManager cookieManager_;
    private final DownloadDialog downloadDialog_;
    private final ControlPanel control_pane_;
    private final JMenu Bookmarks;
    private String last_selected_file_ = "";
    private final CefClient client_;
    private boolean BookmarksLoaded = false;

    private JMenuItem Zoom_Item;
    private double zoomLevel_ = 0;
    
    private final ImageIcon CanRefreshIcon = refreshIcon;
    private final ImageIcon CantRefreshIcon = cross_refreshIcon;

    String status_reloadWithoutCache;
    //String status_transparentPainting;
    //String status_offrender;
    String status_renameBrowser;
    String status_mediaStream;
    String selectedTheme;
    String status_clearweb;
    
    class SaveAs implements CefStringVisitor {
        private PrintWriter fileWriter_;

        public SaveAs(String fName) throws FileNotFoundException, UnsupportedEncodingException {
            fileWriter_ = new PrintWriter(fName, "UTF-8");
        }

        @Override
        public void visit(String string) {
            fileWriter_.write(string);
            fileWriter_.close();
        }
    }
    
    public ControlPanel(MainFrame owner, CefBrowser browser, DownloadDialog downloadDialog, CefCookieManager cookieManager, CefClient client) {
        assert browser != null;
        browser_ = browser;
        owner_ = owner;
        cookieManager_ = cookieManager;
        downloadDialog_ = downloadDialog;
        control_pane_ = this;
        client_ = client;

        // НАЧАЛО БЛОКА - Инициализация статуса настроек
            // Киритрон: Вообще весь этот код был где-то посередине, но для того, чтобы привести всё в порядок, я решил его
            // переместить вот сюда. Проект увеличивается в плане кода, рассортировка участков необходима.

            try {
                if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "reloadWithoutCache").equals("true")) {
                    status_reloadWithoutCache = " (Вкл.)";
                } else {
                    status_reloadWithoutCache = " (Выкл.)";
                }
            } catch (IOException e) { status_reloadWithoutCache = " (Выкл.)"; }

            /*
            try {
                if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "transparent-painting-enabled").equals("true")) {
                    status_transparentPainting = " (Вкл.)";
                } else {
                    status_transparentPainting = " (Выкл.)";
                }
            } catch (IOException e) { status_transparentPainting = " (Выкл.)"; }
             */

            /*
            try {
                if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "off-screen-rendering-enabled").equals("true")) {
                    status_offrender = " (Вкл.)";
                } else {
                    status_offrender = " (Выкл.)";
                }
            } catch (IOException e) { status_offrender = " (Выкл.)"; }
             */

            try {
                if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "fakeNameBrowser").equals("true")) {
                    status_renameBrowser = " (Вкл.)";
                } else {
                    status_renameBrowser = " (Выкл.)";
                }
            } catch (IOException e) { status_renameBrowser = " (Выкл.)"; }

            try {
                if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "enable-media-stream").equals("true")) {
                    status_mediaStream = " (Вкл.)";
                } else {
                    status_mediaStream = " (Выкл.)";
                }
            } catch (IOException e) { status_mediaStream = " (Выкл.)"; }

            try {
                if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfWindowOptions), "WINDOW-OPTIONS", "theme").equals("dark")) {
                    selectedTheme = " (Тёмная)";
                } else {
                    selectedTheme = " (Светлая)";
                }
            } catch (IOException e) { selectedTheme = " (Тёмная)"; }

            try {
                if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfClearWebOptions), "CLEARWEB-OPTIONS", "enabled").equals("true")) {
                    status_clearweb = " (Вкл.)";
                } else {
                    status_clearweb = " (Выкл.)";
                }
            } catch (IOException e) { status_clearweb = " (Выкл.)"; }
        // КОНЕЦ БЛОКА

        // НАЧАЛО БЛОКА - PopupMenu
        if (true) { // Киритрон: Заглушка, чтобы в IDE автоматически данный блок автоматически сдвигался в сторону
            PopupMenu = new JPopupMenu();

            // Киритрон: Решил объявить содержание меню в заранее и в виде дерева
            JMenuItem NewWindow_Item =                      new JMenuItem       ("Новое окно");
                                                                                // РАЗДЕЛИТЕЛЬ
            JMenuItem DownloadManager_Item =                new JMenuItem       ("Загрузки");
            JMenu Bookmarks_SubMenu =                       new JMenu           ("Закладки"); Bookmarks = Bookmarks_SubMenu;
            JMenuItem Bookmark_SubMenuAddThis_Item =        new JMenuItem           ("Создать закладку (текущая страница)");
            JMenuItem Bookmark_SubMenuAddCustom_Item =      new JMenuItem           ("Создать закладку");
            JMenuItem Bookmark_SubMenuRemoveList_Item =     new JMenuItem           ("Удалить все закладки");
                                                                                // РАЗДЕЛИТЕЛЬ
            JMenuItem Print_Item =                          new JMenuItem       ("Печать");
            JMenu Util_SubMenu =                            new JMenu           ("Дополнительные инструменты");
            JMenuItem Util_SubMenuOpenFile =                new JMenuItem           ("Открыть...");
            JMenuItem Util_SubMenuSaveAs =                  new JMenuItem           ("Сохранить как...");
            JMenuItem Util_SubMenuSaveToPDF =               new JMenuItem           ("Сохранить в PDF...");
            JMenuItem Util_SubMenuPiP =                     new JMenuItem           ("Режим \"Картинка в картинке\"");
            JMenuItem Util_SubMenuTextFromPage =            new JMenuItem           ("Текст со страницы");
            JMenuItem Util_SubMenuCodeFromPage =            new JMenuItem           ("Исходный код страницы");
            JMenuItem Util_SubMenuShowDevTools =            new JMenuItem           ("Инструменты разработчика");
                                                                                // РАЗДЕЛИТЕЛЬ
            JMenu Settings_SubMenu =                        new JMenu           ("Настройки");
            JMenuItem Settings_SebMenuHomePage =            new JMenuItem           ("Домашняя страница");
            JMenuItem Settings_SubMenuCookie =              new JMenuItem           ("Просмотр cookie-файлов");
                                                                                    // РАЗДЕЛИТЕЛЬ
            JMenuItem Settings_SubMenureloadWithoutCache =  new JMenuItem           ("Загрузка страниц без кеша" + status_reloadWithoutCache);
            //JMenuItem Settings_SubMenutransparentPainting = new JMenuItem           ("Прозрачный фон окна" + status_transparentPainting);
            //JMenuItem Settings_SubMenuoffrender =           new JMenuItem           ("Внеэкранный рендеринг" + status_offrender);
            JMenuItem Settings_SubMenurenameBrowser =       new JMenuItem           ("Маскировать браузер" + status_renameBrowser);
            JMenuItem Settings_SubMenumediaStream =         new JMenuItem           ("Потоковые данные WebRTC" + status_mediaStream);
            JMenuItem Settings_SubMenuclearweb =            new JMenuItem           ("Энтэри ЧистыйВеб" + status_clearweb);
                                                                                    // РАЗДЕЛИТЕЛЬ
            JMenuItem Settings_SubMenuselectTheme =         new JMenuItem           ("Тема оформления" + selectedTheme);
                                                                                    // РАЗДЕЛИТЕЛЬ
            JMenuItem Settings_SubMenuClearCache =          new JMenuItem           ("Удалить кеш браузера");
            JMenuItem Settings_SubMenuRecreateCFG =         new JMenuItem           ("Пересоздать файлы конфигураций");
                                                                                // РАЗДЕЛИТЕЛЬ
            JMenuItem PlusZoom_Item =                       new JMenuItem       ("Увеличить масштаб");
                      Zoom_Item =                           new JMenuItem       ("0.0");
            JMenuItem MinusZoom_Item =                      new JMenuItem       ("Уменьшить масштаб");
                                                                                // РАЗДЕЛИТЕЛЬ
            JMenu Info_SubMenu =                            new JMenu           ("Справка");
            JMenuItem Info_SubMenuHelp =                    new JMenuItem           ("Учебник");
            JMenuItem Info_SubMenuAboutProg =               new JMenuItem           ("О программе");
                                                                                // ~Плавающий разделитель~
            JMenuItem UpdateIsHere_Item =                   new JMenuItem       ("Доступна новая версия");

            PopupMenu.add(NewWindow_Item);                                      // Новое окно
            PopupMenu.addSeparator();                                           // РАЗДЕЛИТЕЛЬ
            PopupMenu.add(DownloadManager_Item);                                // Загрузки
            PopupMenu.add(Bookmarks_SubMenu);                                   // Закладки
                Bookmarks_SubMenu.add(Bookmark_SubMenuAddThis_Item);                // Добавить закладку (текущая страница)
                Bookmarks_SubMenu.add(Bookmark_SubMenuAddCustom_Item);              // Добавить закладку
                Bookmarks_SubMenu.add(Bookmark_SubMenuRemoveList_Item);             // Удалить все закладки
                if (!BookmarksLoaded) { loadBookmarks();BookmarksLoaded = true; }   // ~Плавающий разделителЬ~
            PopupMenu.addSeparator();                                           // РАЗДЕЛИТЕЛЬ
            PopupMenu.add(Print_Item);                                          // Печать...
            PopupMenu.add(Util_SubMenu);                                        // Дополнительные инструменты
                Util_SubMenu.add(Util_SubMenuOpenFile);                             // Открыть...
                Util_SubMenu.add(Util_SubMenuSaveAs);                               // Сохранить как...
                Util_SubMenu.add(Util_SubMenuSaveToPDF);                            // Сохранить в PDF...
                Util_SubMenu.add(Util_SubMenuPiP);                                  // Режим "Картинка в картинке"
                Util_SubMenu.add(Util_SubMenuTextFromPage);                         // Текст со страницы
                Util_SubMenu.add(Util_SubMenuCodeFromPage);                         // Исходный код страницы
                Util_SubMenu.add(Util_SubMenuShowDevTools);                         // Инструменты разработчика
            PopupMenu.addSeparator();                                           // РАЗДЕЛИТЕЛЬ
            PopupMenu.add(Settings_SubMenu);                                    // Настройки
                Settings_SubMenu.add(Settings_SebMenuHomePage);                     // Домашняя страница
                Settings_SubMenu.add(Settings_SubMenuCookie);                       // Просмотр cookie-файлов
                Settings_SubMenu.addSeparator();                                    // РАЗДЕЛИТЕЛЬ
                Settings_SubMenu.add(Settings_SubMenureloadWithoutCache);           // Перезагрузка без кеша
                //Settings_SubMenu.add(Settings_SubMenutransparentPainting);          // Прозрачный фон окна
                //Settings_SubMenu.add(Settings_SubMenuoffrender);                    // Внеэкранный рендеринг
                Settings_SubMenu.add(Settings_SubMenurenameBrowser);                // Маскировка браузера
                Settings_SubMenu.add(Settings_SubMenumediaStream);                  // Потоковые данные WebRTC
                Settings_SubMenu.add(Settings_SubMenuclearweb);                     // Энтэри ЧистыйВеб
                Settings_SubMenu.addSeparator();                                    // РАЗДЕЛИТЕЛЬ
                Settings_SubMenu.add(Settings_SubMenuselectTheme);                  // Тема оформления
                Settings_SubMenu.addSeparator();                                    // РАЗДЕЛИТЕЛЬ
                Settings_SubMenu.add(Settings_SubMenuClearCache);                   // Очистка кеша
                Settings_SubMenu.add(Settings_SubMenuRecreateCFG);                  // Пересоздание файлов конфигураций
            PopupMenu.addSeparator();                                           // РАЗДЕЛИТЕЛЬ
            PopupMenu.add(PlusZoom_Item);                                       // Увеличение масштаба страницы
            PopupMenu.add(Zoom_Item);                                           // Показатель масштаба страницы
            PopupMenu.add(MinusZoom_Item);                                      // Уменьшение масштаба страницы
            PopupMenu.addSeparator();                                           // РАЗДЕЛИТЕЛЬ
            PopupMenu.add(Info_SubMenu);                                        // Справка
                Info_SubMenu.add(Info_SubMenuHelp);                                 // Учебник
                Info_SubMenu.add(Info_SubMenuAboutProg);                            // О программе

            // Киритрон: Плавающая кнопка. Появляется только тогда, когда при инициализации обнаружилось обновление.
            if (outdated) {
                PopupMenu.addSeparator();
                PopupMenu.add(UpdateIsHere_Item);
                UpdateIsHere_Item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tabManager.OpenTabFromTab("https://kiritron.space/projects/entery/");
                    }});
            }

            // Киритрон: Фуф! Закончил. Теперь по сравнению с кодом первой версии, всё стало намного лучше.
            // По крайне мере мне нравится. Меньше каши и больше порядка.
            // Теперь раздадим всем иконки и можно заняться другими частями приложения.
            NewWindow_Item.setIcon(menu_frameIcon);
            DownloadManager_Item.setIcon(downloadIcon);
            Bookmarks_SubMenu.setIcon(menu_bookmarkIcon);
                Bookmark_SubMenuAddThis_Item.setIcon(menu_addIcon);
                Bookmark_SubMenuAddCustom_Item.setIcon(menu_writeIcon);
                Bookmark_SubMenuRemoveList_Item.setIcon(menu_trashIcon);
            Print_Item.setIcon(menu_printIcon);
            Util_SubMenu.setIcon(menu_toolIcon);
                Util_SubMenuOpenFile.setIcon(menu_openIcon);
                Util_SubMenuSaveAs.setIcon(menu_saveIcon);
                Util_SubMenuSaveToPDF.setIcon(menu_pdfIcon);
                Util_SubMenuPiP.setIcon(menu_pipIcon);
                Util_SubMenuTextFromPage.setIcon(menu_fontIcon);
                Util_SubMenuCodeFromPage.setIcon(menu_writeIcon);
                Util_SubMenuShowDevTools.setIcon(menu_toolIcon);
            Settings_SubMenu.setIcon(menu_settingsIcon);
                Settings_SebMenuHomePage.setIcon(menu_homeIcon);
                Settings_SubMenuCookie.setIcon(menu_cookieIcon);
                Settings_SubMenureloadWithoutCache.setIcon(menu_settingsIcon);
                //Settings_SubMenutransparentPainting.setIcon(menu_frameIcon);
                //Settings_SubMenuoffrender.setIcon(menu_imageIcon);
                Settings_SubMenurenameBrowser.setIcon(menu_maskIcon);
                Settings_SubMenumediaStream.setIcon(menu_microphoneIcon);
                Settings_SubMenuclearweb.setIcon(clearwebIcon);
                Settings_SubMenuselectTheme.setIcon(menu_themeSelectIcon);
                Settings_SubMenuClearCache.setIcon(menu_trashIcon);
                Settings_SubMenuRecreateCFG.setIcon(menu_writeIcon);
            PlusZoom_Item.setIcon(menu_pluszoomIcon);
            MinusZoom_Item.setIcon(menu_minuszoomIcon);
            Info_SubMenu.setIcon(menu_bookIcon);
                Info_SubMenuHelp.setIcon(helpbookIcon);
                Info_SubMenuAboutProg.setIcon(aboutIcon);
            UpdateIsHere_Item.setIcon(updateIcon);

            // Киритрон: Ну и тут уже события, последующие шаги после нажатий на кнопки...
            NewWindow_Item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final MainFrame frame = new MainFrame(OS.isLinux(), false, false, null, null);
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    int sizeWidth;
                    int sizeHeight;
                    try {
                        sizeWidth = Integer.parseInt(TOLF_Handler.ReadParamFromData(FileControls.ReadFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf"), "WINDOW-OPTIONS", "width"));
                        sizeHeight = Integer.parseInt(TOLF_Handler.ReadParamFromData(FileControls.ReadFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf"), "WINDOW-OPTIONS", "height"));
                    } catch (IOException | NumberFormatException er) {
                        toConsole.print(genLogMessage.gen((byte) 1, false, "Не удалось загрузить настройки окна браузера из конфига window-options.tolf."));
                        sizeWidth = 800;
                        sizeHeight = 600;
                    }

                    frame.setIconImage(logo);

                    int locationX = (screenSize.width - sizeWidth) / 2;
                    int locationY = (screenSize.height - sizeHeight) / 2;
                    frame.setSize(sizeWidth, sizeHeight);
                    frame.setBounds(locationX, locationY, sizeWidth, sizeHeight);
                    frame.setVisible(true);
                }
            });

            DownloadManager_Item.setIcon(menu_downloadIcon);
            DownloadManager_Item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    downloadDialog_.setVisible(true);
                }
            });

            Bookmark_SubMenuAddThis_Item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addBookmark(owner_.getTitle(), control_pane_.getAddress());
                }
            });

            Bookmark_SubMenuAddCustom_Item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object[] options = {"Создать закладку", "Отмена"};

                    JPanel panel = new JPanel();
                    panel.add(new JLabel("Имя закладки"));
                    JTextField nameField = new JTextField(20);
                    panel.add(nameField);
                    panel.add(new JLabel(" | "));
                    panel.add(new JLabel("Ссылка"));
                    JTextField linkField = new JTextField(20);
                    panel.add(linkField);

                    int dialogResult = JOptionPane.showOptionDialog(owner_, panel,
                            "Создание новой закладки", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, options, options[0]);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        addBookmark(nameField.getText(), linkField.getText());
                    }
                }
            });

            Bookmark_SubMenuRemoveList_Item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int dialogResult = JOptionPane.showConfirmDialog(owner_, "Вы собираетесь удалить все закладки. Вы уверены?",
                            "Удаление всех закладок", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        FileControls.DeleteFile(GetPathOfAPP.GetPathWithSep() + "userdata" + GetPathOfAPP.GetSep() + "bookmarks.data");
                    }
                }
            });

            Print_Item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    browser_.print();
                }
            });

            Util_SubMenuOpenFile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fc = new JFileChooser(new File(last_selected_file_));
                    fc.showOpenDialog(owner_);
                    File selectedFile = fc.getSelectedFile();
                    if (selectedFile != null) {
                        last_selected_file_ = selectedFile.getAbsolutePath();
                        browser_.loadURL("file:///" + selectedFile.getAbsolutePath());
                    }
                }
            });

            Util_SubMenuSaveAs.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    CefRunFileDialogCallback callback = new CefRunFileDialogCallback() {
                        @Override
                        public void onFileDialogDismissed(
                                int selectedAcceptFilter, Vector<String> filePaths) {
                            if (!filePaths.isEmpty()) {
                                try {
                                    SaveAs saveContent = new SaveAs(filePaths.get(0));
                                    browser_.getSource(saveContent);
                                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                                    browser_.executeJavaScript("alert(\"Нельзя сохранить этот файл...\");",
                                            control_pane_.getAddress(), 0);
                                }
                            }
                        }
                    };
                    browser_.runFileDialog(FileDialogMode.FILE_DIALOG_SAVE, owner_.getTitle(), "index.html", null, 0, callback);
                }
            });

            Util_SubMenuSaveToPDF.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fc = new JFileChooser();
                    fc.showSaveDialog(owner_);
                    File selectedFile = fc.getSelectedFile();
                    if (selectedFile != null) {
                        CefPdfPrintSettings pdfSettings = new CefPdfPrintSettings();
                        pdfSettings.header_footer_enabled = true;
                        // A4 page size
                        pdfSettings.page_width = 210000;
                        pdfSettings.page_height = 297000;
                        browser.printToPDF(
                                selectedFile.getAbsolutePath(), pdfSettings, new CefPdfPrintCallback() {
                                    @Override
                                    public void onPdfPrintFinished(String path, boolean ok) {
                                        SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (ok) {
                                                    JOptionPane.showMessageDialog(owner_,
                                                            "PDF сохранён по адресу " + path, "Успех",
                                                            JOptionPane.INFORMATION_MESSAGE);
                                                } else {
                                                    JOptionPane.showMessageDialog(owner_, "PDF сохранить не удалось",
                                                            "Провал", JOptionPane.ERROR_MESSAGE);
                                                }
                                            }
                                        });
                                    }
                                }
                        );
                    }
                }
            });

            Util_SubMenuPiP.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    browser_.executeJavaScript(Extensions.PiP, control_pane_.getAddress(), 0);
                }
            });

            Util_SubMenuTextFromPage.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ShowTextDialog visitor = new ShowTextDialog(owner_, "Текстовый контент страницы по адресу \"" + control_pane_.getAddress() + "\"");
                    browser_.getText(visitor);
                }
            });

            Util_SubMenuCodeFromPage.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ShowTextDialog visitor = new ShowTextDialog(owner_, "Исходники страницы по адресу \"" + control_pane_.getAddress() + "\"");
                    browser_.getSource(visitor);
                }
            });

            Util_SubMenuShowDevTools.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DevToolsDialog devToolsDlg = new DevToolsDialog(owner_, "Инструмент разработки", browser_);
                    devToolsDlg.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentHidden(ComponentEvent e) {
                            Util_SubMenuShowDevTools.setEnabled(true);
                        }
                    });
                    devToolsDlg.setVisible(true);
                    Util_SubMenuShowDevTools.setEnabled(false);
                }
            });

            Settings_SebMenuHomePage.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object[] options = { "Изменить ссылку домашней страницы", "Стандартная домашняя страница(DuckDuckGo)", "Отмена"};

                    JPanel panel = new JPanel();
                    JTextField custom_home_page = new JTextField();
                    panel.add(new JLabel("Введите URL адрес домашней страницы"));
                    panel.add(custom_home_page);

                    if (AddressFromArgs == null) {
                        custom_home_page.setText(init.HomePage);
                    } else {
                        custom_home_page.setText(init.AddressFromArgs);
                    }

                    int dialogResult = JOptionPane.showOptionDialog(owner_, panel,
                            "Домашняя страница", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, options, options[1]);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        AddressFromArgs = custom_home_page.getText();
                        try {
                            if (!FileControls.SearchFile(GetPathOfAPP.GetPathWithSep() + "userdata" + GetPathOfAPP.GetSep() + "homepage")) {
                                FileControls.CreateFile(GetPathOfAPP.GetPathWithSep() + "userdata" + GetPathOfAPP.GetSep() + "homepage");
                            }

                            FileControls.writeToFile(GetPathOfAPP.GetPathWithSep() + "userdata" + GetPathOfAPP.GetSep() + "homepage",
                                    custom_home_page.getText());
                        } catch (IOException EeEeE) {
                            // Ничего
                        }
                    } else if (dialogResult == JOptionPane.NO_OPTION) {
                        AddressFromArgs = null;
                        if (FileControls.SearchFile(GetPathOfAPP.GetPathWithSep() + "userdata" + GetPathOfAPP.GetSep() + "homepage")) {
                            FileControls.DeleteFile(GetPathOfAPP.GetPathWithSep() + "userdata" + GetPathOfAPP.GetSep() + "homepage");
                        }
                    }
                }
            });

            Settings_SubMenuCookie.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    CookieManagerDialog cookieManager =
                            new CookieManagerDialog(owner_, "Куки менеджер", cookieManager_);
                    cookieManager.setVisible(true);
                }
            });

            Settings_SubMenureloadWithoutCache.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String stringOnOff;
                    if (status_reloadWithoutCache.equals(" (Выкл.)")) {
                        stringOnOff = "Включить";
                    } else {
                        stringOnOff = "Выключить";
                    }
                    int dialogResult = JOptionPane.showConfirmDialog(owner_,
                            stringOnOff + " перезагрузку страницы без кеша?\n" +
                                    "Не использует кеш при загрузке страницы.\n" +
                                    "Полезно для веб-разработчиков, которым необходимо\n" +
                                    "проверить изменения в своём продукте несколько раз\n" +
                                    "за короткий промежуток времени.",
                            "Перезагрузка страницы без кеша", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        String option = null;

                        try {
                            option = FileControls.ReadFile(pathOfEngineOptions);
                        } catch (Exception iwj) {
                            // Ничего не происходит
                        }

                        if (option != null) {
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                if (stringOnOff.equals("Включить")) {
                                    option = TOLF_Handler.EditParamInData(option, "ENGINE-OPTIONS", "reloadWithoutCache", "true");
                                    status_reloadWithoutCache = " (Вкл.)";
                                    Settings_SubMenureloadWithoutCache.setText("Перезагрузка страницы без кеша" + status_reloadWithoutCache);
                                } else {
                                    option = TOLF_Handler.EditParamInData(option, "ENGINE-OPTIONS", "reloadWithoutCache", "false");
                                    status_reloadWithoutCache = " (Выкл.)";
                                    Settings_SubMenureloadWithoutCache.setText("Перезагрузка страницы без кеша" + Settings_SubMenureloadWithoutCache);
                                }
                            }

                            if (!option.contains("FAILED")) {
                                try {
                                    FileControls.writeToFile(pathOfEngineOptions, option);
                                } catch (Exception exp) {
                                    // Ничего не происходит
                                }
                            }
                        }
                    }
                }
            });

            /* Киритрон: Отключено, так как данная функция пока не нужна

            String finaltransparentPainting = status_transparentPainting;
            Settings_SubMenutransparentPainting.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String stringOnOff;
                    if (finaltransparentPainting.equals(" (Выкл.)")) {
                        stringOnOff = "Включить";
                    } else {
                        stringOnOff = "Выключить";
                    }
                    int dialogResult = JOptionPane.showConfirmDialog(owner_,
                            stringOnOff + " прозрачную прорисовку?\n" +
                                    "Если на сайте не установлен фон, то он будет прозрачным.\n" +
                                    "Требуется перезагрузка браузера.",
                            "Прозрачная прорисовка", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        String option = null;

                        try {
                            option = FileControls.ReadFile(pathOfEngineOptions);
                        } catch (Exception iwj) {
                            // Ничего не происходит
                        }

                        if (option != null) {
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                if (stringOnOff.equals("Включить")) {
                                    option = TOLF_Handler.EditParamInData(option, "ENGINE-OPTIONS", "transparent-painting-enabled", "true");
                                } else {
                                    option = TOLF_Handler.EditParamInData(option, "ENGINE-OPTIONS", "transparent-painting-enabled", "false");
                                }
                            }

                            if (!option.contains("FAILED")) {
                                try {
                                    FileControls.writeToFile(pathOfEngineOptions, option);
                                } catch (Exception exp) {
                                    // Ничего не происходит
                                }
                            }
                        }
                    }
                }
            });

             */

            /* Киритрон: Отключено, так как данная функция пока не нужна

            String finalStatus_offrender = status_offrender;
            Settings_SubMenuoffrender.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String stringOnOff;
                    if (finalStatus_offrender.equals(" (Выкл.)")) {
                        stringOnOff = "Включить";
                    } else {
                        stringOnOff = "Выключить";
                    }
                    int dialogResult = JOptionPane.showConfirmDialog(owner_,
                            stringOnOff + " внеэкранный рендеринг?\n" +
                                    "Иначе говоря предварительный просмотр страницы.\n" +
                                    "Требуется перезагрузка браузера.",
                            "Внеэкранный рендеринг", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        String option = null;

                        try {
                            option = FileControls.ReadFile(pathOfEngineOptions);
                        } catch (Exception iwj) {
                            // Ничего не происходит
                        }

                        if (option != null) {
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                if (stringOnOff.equals("Включить")) {
                                    option = TOLF_Handler.EditParamInData(option, "ENGINE-OPTIONS", "off-screen-rendering-enabled", "true");
                                } else {
                                    option = TOLF_Handler.EditParamInData(option, "ENGINE-OPTIONS", "off-screen-rendering-enabled", "false");
                                }
                            }

                            if (!option.contains("FAILED")) {
                                try {
                                    FileControls.writeToFile(pathOfEngineOptions, option);
                                } catch (Exception exp) {
                                    // Ничего не происходит
                                }
                            }
                        }
                    }
                }
            });

             */

            Settings_SubMenurenameBrowser.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String stringOnOff;

                    if (status_renameBrowser.equals(" (Выкл.)")) {
                        stringOnOff = "Включить";
                    } else {
                        stringOnOff = "Выключить";
                    }

                    int dialogResult = JOptionPane.showConfirmDialog(owner_, "Браузеры отправляют сайтам своё название и Энтэри не исключение.\n" +
                                    "Однако многие сайты не знакомы с Энтэри и работают неправильно.\n" +
                                    "Вы можете заставить Энтэри притворяться другим известным браузером,\n" +
                                    "что, возможно, поможет решить проблему с сайтом.\n" +
                                    "Это так же может помочь вашей конфиденциальности, так как вы\n" +
                                    "скрываете истинное название браузера, которым пользуетесь.\n\n" +
                                    stringOnOff + " режим притворства?\n\n" +
                                    "(Требуется перезапуск браузера)",
                            "Притвориться другим браузером", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                    String option = null;

                    try {
                        option = FileControls.ReadFile(pathOfEngineOptions);
                    } catch (Exception iwj) {
                        // Ничего не происходит
                    }

                    if (option != null) {
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            if (stringOnOff.equals("Включить")) {
                                option = TOLF_Handler.EditParamInData(option, "ENGINE-OPTIONS", "fakeNameBrowser", "true");
                            } else {
                                option = TOLF_Handler.EditParamInData(option, "ENGINE-OPTIONS", "fakeNameBrowser", "false");
                            }
                        }

                        if (!option.contains("FAILED")) {
                            try {
                                FileControls.writeToFile(pathOfEngineOptions, option);
                            } catch (Exception exp) {
                                // Ничего не происходит
                            }
                        }
                    }
                }
            });

            Settings_SubMenumediaStream.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String stringOnOff;

                    if (status_mediaStream.equals(" (Выкл.)")) {
                        stringOnOff = "Разрешить";
                    } else {
                        stringOnOff = "Запретить";
                    }

                    int dialogResult = JOptionPane.showConfirmDialog(owner_,
                            "Эта опция позволяет сайтам получать доступ к вашим\n" +
                                    "микрофону и камере(если имеются) по первому\n" +
                                    "требованию. Это требуется для записи голосовых\n" +
                                    "сообщений ВКонтакте, звонков в Дискорд и пр., но\n" +
                                    "в данной версии Энтэри это даёт доступ ВСЕМ сайтам\n" +
                                    "к вашему микрофону и камере, если они этого\n" +
                                    "потребуют. Используйте данную функцию с\n" +
                                    "осторожностью. Выключайте её, когда она вам\n" +
                                    "не нужна.\n\n" +
                                    stringOnOff + " потоковые данные WebRTC?\n\n" +
                                    "(Требуется перезапуск браузера)",
                            "Потоковые данные WebRTC", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                    String option = null;

                    try {
                        option = FileControls.ReadFile(pathOfEngineOptions);
                    } catch (Exception iwj) {
                        // Ничего не происходит
                    }

                    if (option != null) {
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            if (stringOnOff.equals("Разрешить")) {
                                option = TOLF_Handler.EditParamInData(option, "ENGINE-OPTIONS", "enable-media-stream", "true");
                            } else {
                                option = TOLF_Handler.EditParamInData(option, "ENGINE-OPTIONS", "enable-media-stream", "false");
                            }
                        }

                        if (!option.contains("FAILED")) {
                            try {
                                FileControls.writeToFile(pathOfEngineOptions, option);
                            } catch (Exception exp) {
                                // Ничего не происходит
                            }
                        }
                    }
                }
            });

            Settings_SubMenuclearweb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object[] options = {"Включить", "Выключить", "Отмена"};

                    JPanel panel = new JPanel(new BorderLayout());
                    JCheckBox ksDB = new JCheckBox("<html>Использовать сигнатуры из базы данных КС<br>(Может увеличиться время загрузки страниц)</html>");

                    if (ClearWebKSDBStatus) {
                        ksDB.setSelected(true);
                    } else {
                        ksDB.setSelected(false);
                    }

                    panel.add(new JLabel("<html><center>Данный модуль содержит блокировщик рекламы и аналитики.<br>" +
                            "Модуль на стадии тестирования, но с блокировкой рекламы уже<br>" +
                            "справляется неплохо.<br><br>&nbsp;</center></html>"), BorderLayout.PAGE_START);
                    panel.add(ksDB, BorderLayout.CENTER);

                    int dialogResult = JOptionPane.showOptionDialog(owner_, panel,
                            "Энтэри ЧистыйВеб", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, options, options[0]);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        ClearWebStatus = true;
                        try {
                            FileControls.writeToFile(pathOfClearWebOptions, TOLF_Handler.EditParamInData(FileControls.ReadFile(pathOfClearWebOptions), "CLEARWEB-OPTIONS", "enabled", "true"));
                        } catch (IOException EeEeE) {
                            // Ничего
                        }
                        if (ksDB.isSelected()) {
                            ClearWebKSDBStatus = true;
                            try {
                                FileControls.writeToFile(pathOfClearWebOptions, TOLF_Handler.EditParamInData(FileControls.ReadFile(pathOfClearWebOptions), "CLEARWEB-OPTIONS", "ks-database", "true"));
                            } catch (IOException EeEeE) {
                                // Ничего
                            }
                        } else {
                            ClearWebKSDBStatus = false;
                            try {
                                FileControls.writeToFile(pathOfClearWebOptions, TOLF_Handler.EditParamInData(FileControls.ReadFile(pathOfClearWebOptions), "CLEARWEB-OPTIONS", "ks-database", "false"));
                            } catch (IOException EeEeE) {
                                // Ничего
                            }
                        }

                        status_clearweb = " (Вкл.)";
                        Settings_SubMenuclearweb.setText("Энтэри ЧистыйВеб" + status_clearweb);
                    } else if (dialogResult == JOptionPane.NO_OPTION) {
                        ClearWebStatus = false;
                        try {
                            FileControls.writeToFile(pathOfClearWebOptions, TOLF_Handler.EditParamInData(FileControls.ReadFile(pathOfClearWebOptions), "CLEARWEB-OPTIONS", "enabled", "false"));
                        } catch (IOException EeEeE) {
                            // Ничего
                        }
                        if (ksDB.isSelected()) {
                            ClearWebKSDBStatus = true;
                            try {
                                FileControls.writeToFile(pathOfClearWebOptions, TOLF_Handler.EditParamInData(FileControls.ReadFile(pathOfClearWebOptions), "CLEARWEB-OPTIONS", "ks-database", "true"));
                            } catch (IOException EeEeE) {
                                // Ничего
                            }
                        } else {
                            ClearWebKSDBStatus = false;
                            try {
                                FileControls.writeToFile(pathOfClearWebOptions, TOLF_Handler.EditParamInData(FileControls.ReadFile(pathOfClearWebOptions), "CLEARWEB-OPTIONS", "ks-database", "false"));
                            } catch (IOException EeEeE) {
                                // Ничего
                            }
                        }

                        status_clearweb = " (Выкл.)";
                        Settings_SubMenuclearweb.setText("Энтэри ЧистыйВеб" + status_clearweb);
                    }
                }
            });

            String finalselectedTheme = selectedTheme;
            Settings_SubMenuselectTheme.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String stringOnOff;

                    if (finalselectedTheme.equals(" (Тёмная)")) {
                        stringOnOff = "светлую";
                    } else {
                        stringOnOff = "тёмную";
                    }

                    int dialogResult = JOptionPane.showConfirmDialog(owner_, "Установить " +
                                    stringOnOff + " тему оформления интерфейса?\n\n" +
                                    "(Требуется перезапуск браузера)",
                            "Изменение темы оформления интерфейса", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                    String option = null;

                    try {
                        option = FileControls.ReadFile(pathOfWindowOptions);
                    } catch (Exception iwj) {
                        // Ничего не происходит
                    }

                    if (option != null) {
                        //boolean DarculaTheme = true;
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            if (stringOnOff.equals("светлую")) {
                                option = TOLF_Handler.EditParamInData(option, "WINDOW-OPTIONS", "theme", "light");
                                //DarculaTheme = false;
                            } else {
                                option = TOLF_Handler.EditParamInData(option, "WINDOW-OPTIONS", "theme", "dark");
                                //DarculaTheme = true;
                            }
                        }

                        if (!option.contains("FAILED")) {
                            try {
                                FileControls.writeToFile(pathOfWindowOptions, option);
                            } catch (Exception exp) {
                                // Ничего не происходит
                            }
                        }

                        /*
                        Киритрон: Плохо работает.
                        TODO: Подумать, как можно поменять тему во всём браузере без его перезагрузки и чтобы это потом
                         нормально выглядело.

                        try {
                            if (DarculaTheme) {
                                UIManager.setLookAndFeel(new FlatDarculaLaf());
                            } else {
                                UIManager.setLookAndFeel(new FlatLightLaf());
                            }
                            toConsole.print(genLogMessage.gen((byte) 1, false, "Ре-инициализирована библиотека Swing FlatLaf от FormDev Software GmbH"));
                        } catch (UnsupportedLookAndFeelException sgsd) {
                            toConsole.print(genLogMessage.gen((byte) 3, false, "Не удалось ре-инициализировать библиотеку Swing FlatLaf от FormDev Software GmbH. Стиль окна может быть неправильным."));
                        }*/
                    }
                }
            });

            Settings_SubMenuClearCache.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int dialogResult = JOptionPane.showConfirmDialog(owner_,
                            "Данное действие удалит кеш и куки браузера Энтэри.\n" +
                                    "Это может привести к закрытию сеанса на некоторых сайтах.\n" +
                                    "Вы уверены?",
                            "Обратите внимание", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        DirControls.ForceDeleteDir(GetPathOfAPP.GetPathWithSep() + "cache");
                        DirControls.CreateDir(GetPathOfAPP.GetPathWithSep() + "cache");
                    }
                }
            });

            Settings_SubMenuRecreateCFG.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int dialogResult = JOptionPane.showConfirmDialog(owner_,
                            "Данное действие удалит файлы конфигурации Энтэри, если\n" +
                                    "таковые имеются, и создаст новые.\n" +
                                    "Вы уверены?",
                            "Обратите внимание", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        FileControls.DeleteFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "engine-options.tolf");
                        FileControls.DeleteFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf");

                        init.genCfg();
                    }
                }
            });

            MinusZoom_Item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PlusZoom(false);
                }
            });

            PlusZoom_Item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PlusZoom(true);
                }
            });

            Info_SubMenuHelp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tabManager.OpenTabFromTab("entery://res/help");
                }
            });

            Info_SubMenuAboutProg.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tabManager.OpenTabFromTab("entery://res/about");
                }
            });
        } // Киритрон: Закрытие объявленной ранее "заглушки"
        // КОНЕЦ БЛОКА -  PopupMenu
        
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(Box.createHorizontalStrut(5));

        backButton_ = new JButton(leftarrowIcon);
        backButton_.setFont(new Font("Serif", Font.PLAIN, 18));
        backButton_.setFocusable(false);
        backButton_.setAlignmentX(LEFT_ALIGNMENT);
        backButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.goBack();
            }
        });
        add(backButton_);
        add(Box.createHorizontalStrut(6));

        forwardButton_ = new JButton(rightarrowIcon);
        forwardButton_.setFont(new Font("Serif", Font.PLAIN, 15));
        forwardButton_.setFocusable(false);
        forwardButton_.setAlignmentX(LEFT_ALIGNMENT);
        forwardButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.goForward();
            }
        });
        add(forwardButton_);
        add(Box.createHorizontalStrut(5));

        homeButton_ = new JButton(homeIcon);
        homeButton_.setFont(new Font("Serif", Font.PLAIN, 18));
        homeButton_.setFocusable(false);
        homeButton_.setAlignmentX(LEFT_ALIGNMENT);
        homeButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (AddressFromArgs == null) {
                    tabManager.OpenTab(init.HomePage);
                } else {
                    tabManager.OpenTab(init.AddressFromArgs);
                }
            }
        });
        add(homeButton_);
        add(Box.createHorizontalStrut(6));

        reloadButton_ = new JButton(CanRefreshIcon);
        reloadButton_.setFont(new Font("Serif", Font.PLAIN, 18));
        reloadButton_.setFocusable(false);
        reloadButton_.setAlignmentX(LEFT_ALIGNMENT);
        reloadButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CanReloadPage) {
                    reloadPage();
                } else {
                    browser_.stopLoad();
                }
            }
        });
        add(reloadButton_);
        add(Box.createHorizontalStrut(1));

        JLabel addressLabel = new JLabel("");
        addressLabel.setFont(new Font("Serif", Font.PLAIN, 17));
        addressLabel.setAlignmentX(LEFT_ALIGNMENT);
        add(addressLabel);
        add(Box.createHorizontalStrut(5));

        address_field_ = new JTextField(100);
        address_field_.setToolTipText("Адресная строка. Введите адрес сайта или поисковой запрос.");
        address_field_.setAlignmentX(LEFT_ALIGNMENT);
        address_field_.setEditable(true);
        address_field_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.loadURL(getAddress());
            }
        });
        add(address_field_);

        // Киритрон: Мистер Рекс обратил внимание, что в новом интерфейсе появился баг с фокусом
        // адресной строки, когда фокус адресной строки конфликтует с фокусом компонента самого
        // браузера. Я решил эту проблему переносом и адаптацией данного блока из MainFrame.java
        // НАЧАЛО БЛОКА
        control_pane_.getAddressField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!owner.browserFocus_) return;
                owner.browserFocus_ = false;
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                control_pane_.getAddressField().requestFocus();
            }
        });

        client_.addFocusHandler(new CefFocusHandlerAdapter() {
            @Override
            public void onGotFocus(CefBrowser browser) {
                if (owner.browserFocus_) return;
                owner.browserFocus_ = true;
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                browser.setFocus(true);
            }

            @Override
            public void onTakeFocus(CefBrowser browser, boolean next) {
                owner.browserFocus_ = false;
            }
        });
        // КОНЕЦ БЛОКА

        add(Box.createHorizontalStrut(6));
        address_field_.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        pipButton_ = new JButton(pipIcon);
        pipButton_.setFont(new Font("Serif", Font.PLAIN, 18));
        pipButton_.setFocusable(false);
        pipButton_.setAlignmentX(LEFT_ALIGNMENT);
        pipButton_.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                browser_.executeJavaScript(Extensions.PiP, control_pane_.getAddress(), 0);
            }
        });
        add(pipButton_);
        add(Box.createHorizontalStrut(6));

        menuButton_ = new JButton(menuIcon);
        menuButton_.setFont(new Font("Serif", Font.PLAIN, 18));
        menuButton_.setFocusable(false);
        menuButton_.setAlignmentX(LEFT_ALIGNMENT);
        menuButton_.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	PopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        add(menuButton_);
        add(Box.createHorizontalStrut(6));

        // Киритрон - TODO: Возможно в будущем потребуется убрать эту кнопку из панели, так как не так уж часто ей приходится пользоваться
        downloadsButton_ = new JButton(downloadIcon);
        downloadsButton_.setFont(new Font("Serif", Font.PLAIN, 18));
        downloadsButton_.setFocusable(false);
        downloadsButton_.setAlignmentX(LEFT_ALIGNMENT);
        downloadsButton_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadDialog_.setVisible(true);
            }
        });
        add(downloadsButton_);
        add(Box.createHorizontalStrut(4));

        this.setPreferredSize(new Dimension(this.getPreferredSize().width, 40));
    }

    public void update (CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
        if (browser == browser_) {
            backButton_.setEnabled(canGoBack);
            forwardButton_.setEnabled(canGoForward);
            reloadButton_.setIcon(isLoading ? CantRefreshIcon : CanRefreshIcon);
            CanReloadPage = !isLoading;
        }
    }

    public String getAddress() {
        String address = address_field_.getText();
        // If the URI format is unknown "new URI" will throw an
        // exception. In this case we interpret the value of the
        // address field as search request. Therefore we simply add
        // the "search" scheme.
        try {
            address = address.replaceAll(" ", "%20");
            URI test = new URI(address);
            if (test.getScheme() != null) return address;
            if (test.getHost() != null && test.getPath() != null) return address;
            String specific = test.getSchemeSpecificPart();
            if (specific.indexOf('.') == -1)
                throw new URISyntaxException(specific, "No dot inside domain");
        } catch (URISyntaxException e1) {
            address = "https://duckduckgo.com/?q=" + address;
        }
        return address;
    }

    public void setAddress(CefBrowser browser, String address) {
        if (browser == browser_) address_field_.setText(address);
    }

    public JTextField getAddressField() {
        return address_field_;
    }

    public void PlusZoom(boolean plus) {
        if (plus) { browser_.setZoomLevel(++zoomLevel_); } else { browser_.setZoomLevel(--zoomLevel_); }
        Zoom_Item.setText(String.valueOf(zoomLevel_));
    }

    public void reloadPage() {
        boolean param;

        try {
            if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "engine-options.tolf"), "ENGINE-OPTIONS", "reloadWithoutCache").equals("true")) {
                param = true;
            } else {
                param = false;
            }
        } catch (IOException e) {
            param = false;
            e.printStackTrace();
        }

        if (param) {
            browser_.reloadIgnoreCache();
        } else {
            browser_.reload();
        }
    }
    
    private void addBookmark(String name, String URL) {
        if (Bookmarks == null) return;

        Component[] entries = Bookmarks.getMenuComponents();
        for (Component itemEntry : entries) {
            if (!(itemEntry instanceof JMenuItem)) continue;

            JMenuItem item = (JMenuItem) itemEntry;
            if (item.getText().equals(name)) {
                item.setActionCommand(URL);
                return;
            }
        }

        JMenuItem menuItem = new JMenuItem(name.replaceAll(" — Энтэри", ""));
        menuItem.setActionCommand(URL);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = { "Открыть страницу", "Удалить закладку"};

                JPanel panel = new JPanel();
                panel.add(new JLabel("Что необходимо сделать с данной закладкой?"));

                int dialogResult = JOptionPane.showOptionDialog(owner_, panel,
                        "Что необходимо сделать?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    browser_.loadURL(e.getActionCommand());
                } else {
                	Bookmarks.remove(menuItem);
                    RemoveBookmarkInDataFile(name.replaceAll(" — Энтэри", ""), URL);
                }
            }
        });

        Bookmarks.add(menuItem);
        AddBookmarkToDataFile(name.replaceAll(" — Энтэри", ""), URL);
    }
    
    private void loadBookmarks() {
        try {
            String[] bookmarks = ReadBookmarksFromDataFile();
            if (bookmarks != null) {
                String name;
                String link;

                int countOfElements = bookmarks.length;


                for (int a = 0; a < countOfElements; ) {
                    if (a == 0) {
                        name = bookmarks[a].substring(7, bookmarks[a].indexOf(":00:00:"));
                    } else {
                        name = bookmarks[a].substring(9, bookmarks[a].indexOf(":00:00:"));
                    }
                    link = bookmarks[a].substring(bookmarks[a].indexOf(":00:00:") + 7, bookmarks[a].indexOf("?0?0?0?"));

                    JMenuItem menuItem = new JMenuItem(name);
                    menuItem.setActionCommand(link);
                    String finalName = name.replaceAll(" — Энтэри", "");
                    String finalLink = link;
                    menuItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Object[] options = {"Открыть страницу", "Удалить закладку"};

                            JPanel panel = new JPanel();
                            panel.add(new JLabel("Что необходимо сделать с данной закладкой?"));

                            int dialogResult = JOptionPane.showOptionDialog(owner_, panel,
                                    "Что необходимо сделать?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                                    null, options, options[0]);
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                browser_.loadURL(e.getActionCommand());
                            } else {
                            	Bookmarks.remove(menuItem);
                                RemoveBookmarkInDataFile(finalName, finalLink);
                            }
                        }
                    });

                    if (a == 0) {
                        Bookmarks.addSeparator();
                    }

                    Bookmarks.add(menuItem);

                    a++;
                }
            }
        } catch (Exception e) {
            toConsole.print(genLogMessage.gen((byte) 3, false, "Не могу загрузить закладки. Возможно их нет."));
        }
    }
}
