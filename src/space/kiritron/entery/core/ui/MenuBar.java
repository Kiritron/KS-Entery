// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

package space.kiritron.entery.core.ui;

import org.cef.CefSettings;
import org.cef.OS;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefPdfPrintCallback;
import org.cef.callback.CefRunFileDialogCallback;
import org.cef.callback.CefStringVisitor;
import org.cef.handler.CefDialogHandler.FileDialogMode;
import org.cef.misc.CefPdfPrintSettings;
import org.cef.network.CefCookieManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.Vector;

import javax.swing.*;

import space.kiritron.entery.core.Extensions;
import space.kiritron.entery.core.MainFrame;
import space.kiritron.entery.core.dialog.*;
import space.kiritron.entery.init;
import space.kiritron.entery.ks_libs.pixel.filefunc.DirControls;
import space.kiritron.entery.ks_libs.pixel.filefunc.FileControls;
import space.kiritron.entery.ks_libs.pixel.filefunc.GetPathOfAPP;
import space.kiritron.entery.ks_libs.pixel.genhash.Gen;
import space.kiritron.entery.ks_libs.pixel.logger.genLogMessage;
import space.kiritron.entery.ks_libs.pixel.logger.toConsole;
import space.kiritron.entery.ks_libs.tolchok.TOLF_Handler;

import static space.kiritron.entery.core.handler.BookmarkHandler.*;
import static space.kiritron.entery.init.*;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
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

    private final MainFrame owner_;
    private final CefBrowser browser_;
    private final JMenu bookmarkMenu_;
    private String last_selected_file_ = "";
    private final ControlPanel control_pane_;
    private final DownloadDialog downloadDialog_;
    private final CefCookieManager cookieManager_;

    public MenuBar(MainFrame owner, CefBrowser browser, ControlPanel control_pane,
            DownloadDialog downloadDialog, CefCookieManager cookieManager) {
        owner_ = owner;
        browser_ = browser;
        control_pane_ = control_pane;
        downloadDialog_ = downloadDialog;
        cookieManager_ = cookieManager;

        setEnabled(browser_ != null);

        JMenu fileMenu = new JMenu("Страница");

        JMenuItem openFileItem = new JMenuItem("Открыть локальный файл...");
        openFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = new JFileChooser(new File(last_selected_file_));
                // Show open dialog; this method does not return until the dialog is closed.
                fc.showOpenDialog(owner_);
                File selectedFile = fc.getSelectedFile();
                if (selectedFile != null) {
                    last_selected_file_ = selectedFile.getAbsolutePath();
                    browser_.loadURL("file:///" + selectedFile.getAbsolutePath());
                }
            }
        });
        fileMenu.add(openFileItem);

        JMenuItem openFileDialog = new JMenuItem("Сохранить страницу как...");
        openFileDialog.addActionListener(new ActionListener() {
            @Override
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
                browser_.runFileDialog(FileDialogMode.FILE_DIALOG_SAVE, owner_.getTitle(),
                        "index.html", null, 0, callback);
            }
        });
        fileMenu.add(openFileDialog);

        JMenuItem printItem = new JMenuItem("Печать страницы...");
        printItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.print();
            }
        });
        fileMenu.add(printItem);

        JMenuItem printToPdfItem = new JMenuItem("Сохранить страницу в PDF");
        printToPdfItem.addActionListener(new ActionListener() {
            @Override
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
                            });
                }
            }
        });
        fileMenu.add(printToPdfItem);

        fileMenu.addSeparator();

        /*
        JMenuItem viewSource = new JMenuItem("Просмотр исходного кода страницы");
        viewSource.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.viewSource();
            }
        });
        fileMenu.add(viewSource);
         */

        JMenuItem getSource = new JMenuItem("Просмотр исходного кода страницы");
        getSource.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowTextDialog visitor = new ShowTextDialog(
                        owner_, "Исходники страницы по адресу \"" + control_pane_.getAddress() + "\"");
                browser_.getSource(visitor);
            }
        });
        fileMenu.add(getSource);

        JMenuItem getText = new JMenuItem("Получить текст со страницы");
        getText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowTextDialog visitor = new ShowTextDialog(
                        owner_, "Текстовый контент страницы по адресу \"" + control_pane_.getAddress() + "\"");
                browser_.getText(visitor);
            }
        });
        fileMenu.add(getText);

        JMenu utilMenu = new JMenu("Утилиты");

        JMenuItem showDownloads = new JMenuItem("Загрузки");
        showDownloads.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadDialog_.setVisible(true);
            }
        });
        utilMenu.add(showDownloads);

        JMenuItem showCookies = new JMenuItem("Файлы куки");
        showCookies.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CookieManagerDialog cookieManager =
                        new CookieManagerDialog(owner_, "Куки менеджер", cookieManager_);
                cookieManager.setVisible(true);
            }
        });
        utilMenu.add(showCookies);

        utilMenu.addSeparator();

        final JMenuItem pip = new JMenuItem("Картинка в картинке");
        pip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.executeJavaScript(Extensions.PiP, MenuBar.this.control_pane_.getAddress(), 0);
            }
        });
        utilMenu.add(pip);

        final JMenuItem showDevTools = new JMenuItem("Инструмент разработки");
        showDevTools.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DevToolsDialog devToolsDlg = new DevToolsDialog(owner_, "Инструмент разработки", browser_);
                devToolsDlg.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentHidden(ComponentEvent e) {
                        showDevTools.setEnabled(true);
                    }
                });
                devToolsDlg.setVisible(true);
                showDevTools.setEnabled(false);
            }
        });
        utilMenu.add(showDevTools);

        String status_renameBrowser;
        try {
            if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "fakeNameBrowser").equals("true")) {
                status_renameBrowser = " (✔)";
            } else {
                status_renameBrowser = " (❌)";
            }
        } catch (IOException e) {
            status_renameBrowser = " (❌)";
        }
        final JMenuItem renameBrowser = new JMenuItem("Притвориться другим браузером" + status_renameBrowser);
        String finalStatus_renameBrowser = status_renameBrowser;
        renameBrowser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stringOnOff;

                if (finalStatus_renameBrowser.equals(" (❌)")) {
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
        utilMenu.add(renameBrowser);

        utilMenu.addSeparator();

        JMenuItem newwindow = new JMenuItem("Новое окно");
        newwindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final MainFrame frame = new MainFrame(OS.isLinux(), false, false, null,null);
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

                Image img = new ImageIcon(init.class.getResource("res/logo.png")).getImage();
                frame.setIconImage(img);

                int locationX = (screenSize.width - sizeWidth) / 2;
                int locationY = (screenSize.height - sizeHeight) / 2;
                frame.setSize(sizeWidth, sizeHeight);
                frame.setBounds(locationX, locationY, sizeWidth, sizeHeight);
                frame.setVisible(true);
            }
        });
        utilMenu.add(newwindow);

        /*
        TODO: Постараться сделать поддержку плагинов
        JMenuItem showPlugins = new JMenuItem("Показать плагины");
        showPlugins.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WebPluginManagerDialog pluginManager =
                        new WebPluginManagerDialog(owner_, "Менеджер плагинов");
                pluginManager.setVisible(true);
            }
        });
        utilMenu.add(showPlugins);

         */

        bookmarkMenu_ = new JMenu("Закладки");

        JMenuItem addBookmarkItem = new JMenuItem("Добавить закладку(текущая страница)");
        addBookmarkItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBookmark(owner_.getTitle(), control_pane_.getAddress());
            }
        });
        bookmarkMenu_.add(addBookmarkItem);
        JMenuItem addBookmarkItemWithOption = new JMenuItem("Добавить закладку(расширенное)");
        addBookmarkItemWithOption.addActionListener(new ActionListener() {
            @Override
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
        bookmarkMenu_.add(addBookmarkItemWithOption);
        JMenuItem removeAllBookmarks = new JMenuItem("Удалить все закладки");
        removeAllBookmarks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog(owner_, "Вы собираетесь удалить все закладки. Вы уверены?",
                        "Удаление всех закладок", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    FileControls.DeleteFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "bookmarks.data");
                }
            }
        });
        bookmarkMenu_.add(removeAllBookmarks);
        bookmarkMenu_.addSeparator();
        loadBookmarks();

        JMenu optionsMenu = new JMenu("Опции");

        String status_offrender;
        try {
            if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "off-screen-rendering-enabled").equals("true")) {
                status_offrender = " (✔)";
            } else {
                status_offrender = " (❌)";
            }
        } catch (IOException e) {
            status_offrender = " (❌)";
        }
        final JMenuItem offrender = new JMenuItem("Внеэкранный рендеринг" + status_offrender);
        String finalStatus_offrender = status_offrender;
        offrender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stringOnOff;
                if (finalStatus_offrender.equals(" (❌)")) {
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
        optionsMenu.add(offrender);

        String status_transparentPainting;
        try {
            if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "transparent-painting-enabled").equals("true")) {
                status_transparentPainting = " (✔)";
            } else {
                status_transparentPainting = " (❌)";
            }
        } catch (IOException e) {
            status_transparentPainting = " (❌)";
        }
        final JMenuItem transparentPainting = new JMenuItem("Прозрачная прорисовка" + status_transparentPainting);
        String finaltransparentPainting = status_transparentPainting;
        transparentPainting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stringOnOff;
                if (finaltransparentPainting.equals(" (❌)")) {
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
        optionsMenu.add(transparentPainting);

        final String[] status_reloadWithoutCache = new String[1];
        try {
            if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "reloadWithoutCache").equals("true")) {
                status_reloadWithoutCache[0] = " (✔)";
            } else {
                status_reloadWithoutCache[0] = " (❌)";
            }
        } catch (IOException e) {
            status_reloadWithoutCache[0] = " (❌)";
        }
        final JMenuItem reloadWithoutCache = new JMenuItem("Перезагрузка страницы без кеша" + status_reloadWithoutCache[0]);
        final String[] finalreloadWithoutCache = {status_reloadWithoutCache[0]};
        reloadWithoutCache.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stringOnOff;
                if (finalreloadWithoutCache[0].equals(" (❌)")) {
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
                                finalreloadWithoutCache[0] = " (✔)";
                                reloadWithoutCache.setText("Перезагрузка страницы без кеша" + finalreloadWithoutCache[0]);
                            } else {
                                option = TOLF_Handler.EditParamInData(option, "ENGINE-OPTIONS", "reloadWithoutCache", "false");
                                finalreloadWithoutCache[0] = " (❌)";
                                reloadWithoutCache.setText("Перезагрузка страницы без кеша" + finalreloadWithoutCache[0]);
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
        optionsMenu.add(reloadWithoutCache);

        JMenu otherMenu = new JMenu("Дополнительно");

        final JMenuItem clearCache = new JMenuItem("Удалить кеш");
        clearCache.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog(owner_,
                        "Данное действие удалит кеш и куки браузера Энтери.\n" +
                                "Это может привести к закрытию сеанса на некоторых сайтах.\n" +
                                "Вы уверены?",
                        "Обратите внимание", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    DirControls.ForceDeleteDir(GetPathOfAPP.GetPathWithSep() + "cache");
                    DirControls.CreateDir(GetPathOfAPP.GetPathWithSep() + "cache");
                }
            }
        });
        otherMenu.add(clearCache);

        final JMenuItem regenCFG = new JMenuItem("Пересоздать файлы конфигураций");
        regenCFG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog(owner_,
                        "Данное действие удалит файлы конфигурации Энтэри, если\n" +
                                "таковые имеются, и создаст новые.\n" +
                                "Вы уверены?",
                        "Обратите внимание", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    //DirControls.ForceDeleteDir(GetPathOfAPP.GetPathWithSep() + "cfg");
                    FileControls.DeleteFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "engine-options.tolf");
                    FileControls.DeleteFile(GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf");

                    init.genCfg();
                }
            }
        });
        otherMenu.add(regenCFG);

        otherMenu.addSeparator();

        final JMenuItem Help = new JMenuItem("Справка");
        Help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HelpWindow hpw = new HelpWindow(owner_);
                hpw.setVisible(true);
            }
        });
        otherMenu.add(Help);

        otherMenu.addSeparator();

        final JMenuItem AboutProg = new JMenuItem("О программе");
        AboutProg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutProgWindow apw = new AboutProgWindow(owner_);
                apw.setVisible(true);
            }
        });
        otherMenu.add(AboutProg);

        JMenuItem updateButton = new JMenuItem("ДОСТУПНО ОБНОВЛЕНИЕ");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final MainFrame frame = new MainFrame(OS.isLinux(), false, false, "https://kiritron.space/entery",null);
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

                Image img = new ImageIcon(init.class.getResource("res/logo.png")).getImage();
                frame.setIconImage(img);

                int locationX = (screenSize.width - sizeWidth) / 2;
                int locationY = (screenSize.height - sizeHeight) / 2;
                frame.setSize(sizeWidth, sizeHeight);
                frame.setBounds(locationX, locationY, sizeWidth, sizeHeight);
                frame.setVisible(true);
            }
        });

        add(fileMenu);
        add(utilMenu);
        add(bookmarkMenu_);
        add(optionsMenu);
        add(otherMenu);
        if (init.outdated) { add(updateButton); }
    }

    private void addBookmark(String name, String URL) {
        if (bookmarkMenu_ == null) return;

        Component[] entries = bookmarkMenu_.getMenuComponents();
        for (Component itemEntry : entries) {
            if (!(itemEntry instanceof JMenuItem)) continue;

            JMenuItem item = (JMenuItem) itemEntry;
            if (item.getText().equals(name)) {
                item.setActionCommand(URL);
                return;
            }
        }

        JMenuItem menuItem = new JMenuItem(name.replaceAll("\\[Энтэри\\] ", ""));
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
                    bookmarkMenu_.remove(menuItem);
                    RemoveBookmarkInDataFile(name.replaceAll("\\[Энтэри\\] ", ""), URL);
                }
            }
        });

        bookmarkMenu_.add(menuItem);
        AddBookmarkToDataFile(name.replaceAll("\\[Энтэри\\] ", ""), URL);

        //validate();
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
                    String finalName = name.replaceAll("\\[Энтэри\\] ", "");
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
                                bookmarkMenu_.remove(menuItem);
                                RemoveBookmarkInDataFile(finalName, finalLink);
                            }
                        }
                    });

                    bookmarkMenu_.add(menuItem);

                    a++;
                }
            }
        } catch (Exception e) {
            toConsole.print(genLogMessage.gen((byte) 3, false, "Не могу загрузить закладки. Возможно их нет."));
        }
    }
}
