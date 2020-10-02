package space.kiritron.entery;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import space.kiritron.entery.core.MainFrame;
import space.kiritron.entery.ks_libs.duke.httpconn;
import space.kiritron.entery.ks_libs.pixel.CheckerDIR;
import space.kiritron.entery.ks_libs.pixel.GetOS;
import space.kiritron.entery.ks_libs.pixel.filefunc.FileControls;
import space.kiritron.entery.ks_libs.pixel.filefunc.GetPathOfAPP;
import space.kiritron.entery.ks_libs.pixel.logger.genLogMessage;
import space.kiritron.entery.ks_libs.pixel.logger.toConsole;
import space.kiritron.entery.ks_libs.tolchok.TOLF_Handler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Киритрон Стэйблкор
 * @version 2.0
 */

public class init {
    public final static String NAME_APP = "КС Энтери";
    public final static String VER_APP = "Версия: 2.0-КСЭ Бета";
    public final static String VER_APP_FILTERED = "2.0";

    public final static String pathOfEngineOptions = GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "engine-options.tolf";
    public final static String pathOfWindowOptions = GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf";

    public static String NAME_APP_USER_AGENT = initNameBrowser(true);
    public static String VER_APP_USER_AGENT = initNameBrowser(false);

    public static boolean outdated = false;
    public static boolean outdated_major = false;
    
    public static String HomePage = "https://duckduckgo.com/";

    public static Image logo;
    public static ImageIcon addIcon;
    public static ImageIcon crossIcon;
    public static ImageIcon crosslightIcon;
    public static ImageIcon downloadIcon;
    public static ImageIcon homeIcon;
    public static ImageIcon leftarrowIcon;
    public static ImageIcon rightarrowIcon;
    public static ImageIcon menuIcon;
    public static ImageIcon menu_addIcon;
    public static ImageIcon menu_bookIcon;
    public static ImageIcon menu_bookmarkIcon;
    public static ImageIcon menu_chromeIcon;
    public static ImageIcon menu_cookieIcon;
    public static ImageIcon menu_downloadIcon;
    public static ImageIcon menu_fontIcon;
    public static ImageIcon menu_frameIcon;
    public static ImageIcon menu_imageIcon;
    public static ImageIcon menu_maskIcon;
    public static ImageIcon menu_microphoneIcon;
    public static ImageIcon menu_openIcon;
    public static ImageIcon menu_pdfIcon;
    public static ImageIcon menu_printIcon;
    public static ImageIcon menu_saveIcon;
    public static ImageIcon menu_settingsIcon;
    public static ImageIcon menu_terminalIcon;
    public static ImageIcon menu_toolIcon;
    public static ImageIcon menu_trashIcon;
    public static ImageIcon menu_writeIcon;
    public static ImageIcon menu_pipIcon;
    public static ImageIcon menu_themeSelectIcon;
    public static ImageIcon menu_pluszoomIcon;
    public static ImageIcon menu_minuszoomIcon;
    public static ImageIcon pipIcon;
    public static ImageIcon refreshIcon;
    public static ImageIcon cross_refreshIcon;
    public static ImageIcon updateIcon;
    public static ImageIcon helpbookIcon;
    public static ImageIcon aboutIcon;

    public static void main(String[] args) {
        if (!(args.length == 0)) {
            if (args[0].equals("--get-version")) {
                if (GetOS.isDogiru()) {
                    System.out.println(VER_APP_FILTERED);
                    System.exit(0);
                }
            }
        }

        // Инициализация русского языка для данного ПО.
        Locale.setDefault(new Locale("ru"));
        JOptionPane.setDefaultLocale(new Locale("ru"));

        System.out.println("==================");
        System.out.println(NAME_APP);
        System.out.println(VER_APP);
        System.out.println("==================");

        CheckerDIR.Check("cache");
        CheckerDIR.Check("res");
        CheckerDIR.Check("lang");

        genCfg();

        boolean osrEnabledArg = false; // Киритрон: Функция принудительно отключена. Пока не нужна.
        boolean transparentPaintingEnabledArg = false; // Киритрон: Функция принудительно отключена. Пока не нужна.
        boolean createImmediately = false; // Киритрон: Функция принудительно отключена. Пока не нужна.
        boolean enableMediaStream;
        boolean DarculaTheme;
        try {
            //if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "off-screen-rendering-enabled").equals("true")) {
            //    osrEnabledArg = true; } else { osrEnabledArg = false; }
            //if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "transparent-painting-enabled").equals("true")) {
            //    transparentPaintingEnabledArg = true; } else { transparentPaintingEnabledArg = false; }
            //if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "create-immediately").equals("true")) {
            //    createImmediately = true; } else { createImmediately = false; }
            if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "enable-media-stream").equals("true")) {
                enableMediaStream = true; } else { enableMediaStream = false; }
            if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfWindowOptions), "WINDOW-OPTIONS", "theme").equals("dark")) {
                DarculaTheme = true; } else { DarculaTheme = false; }
        } catch (IOException e) {
            //osrEnabledArg = false;
            //transparentPaintingEnabledArg = false;
            //createImmediately = false;
            enableMediaStream = false;
            DarculaTheme = false;
            toConsole.print(genLogMessage.gen((byte) 3, false, "Не удалось прочитать файл конфигурации движка. Используются значения по умолчанию."));
        }

        String colorTheme;
        try {
            if (DarculaTheme) {
                UIManager.setLookAndFeel(new FlatDarculaLaf());
                colorTheme = "dark";
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
                colorTheme = "light";
            }
            toConsole.print(genLogMessage.gen((byte) 1, false, "Инициализирована библиотека Swing FlatLaf от FormDev Software GmbH"));
        } catch (UnsupportedLookAndFeelException e) {
            toConsole.print(genLogMessage.gen((byte) 3, false, "Не удалось инициализировать библиотеку Swing FlatLaf от FormDev Software GmbH. Стиль окна может быть неправильным."));
            colorTheme = "dark";
        }

        String enableMediaStreamString = "";
        if (enableMediaStream) {
            enableMediaStreamString = "--enable-media-stream";
        }

        String[] param = {enableMediaStreamString};

        logo = new ImageIcon(init.class.getResource("res/logo.png")).getImage();
        // Киритрон: Большую часть иконок сделал Мистер Рекс
        addIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/add.png"));
        crossIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/cross.png"));
        crosslightIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/cross-light.png"));
        downloadIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/download.png"));
        homeIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/home.png"));
        leftarrowIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/left-arrow.png"));
        rightarrowIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/right-arrow.png"));
        menuIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu.png"));
        menu_addIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-add.png"));
        menu_bookIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-book.png"));
        menu_bookmarkIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-bookmark.png"));
        menu_chromeIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-chrome.png"));
        menu_cookieIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-cookie.png"));
        menu_downloadIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-download.png"));
        menu_fontIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-font.png"));
        menu_frameIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-frame.png"));
        menu_imageIcon= new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-image.png"));
        menu_maskIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-mask.png"));
        menu_microphoneIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-microphone.png"));
        menu_openIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-open.png"));
        menu_pdfIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-pdf.png"));
        menu_printIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-print.png"));
        menu_saveIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-save.png"));
        menu_settingsIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-settings.png"));
        menu_terminalIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-terminal.png"));
        menu_toolIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-tool.png"));
        menu_trashIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-trash.png"));
        menu_writeIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-write.png"));
        // Киритрон: Но некоторые я сделал сам, так как их не хватало
        menu_pipIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-pip.png"));
        menu_themeSelectIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-selecttheme.png"));
        menu_pluszoomIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-pluszoom.png"));
        menu_minuszoomIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/menu-minuszoom.png"));
        pipIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/pip.png"));
        refreshIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/refresh.png"));
        cross_refreshIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/refresh-cross.png"));
        updateIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/update.png"));
        helpbookIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/helpbook.png"));
        aboutIcon = new ImageIcon(init.class.getResource("res/" + colorTheme + "/about.png"));
        colorTheme = null;

        String checkVersionOut = httpconn.checkVersion("https://kiritron.space/versions/entery",false, true, VER_APP_FILTERED);
        if (checkVersionOut.contains("DIFFERENCE_FINDED. MAJOR.")) {
            outdated_major = true;
        } else if (checkVersionOut.contains("DIFFERENCE_FINDED")) {
            outdated = true;
        } else {
            if (FileControls.SearchFile(GetPathOfAPP.GetPathWithSep() + ".majorupdateopened")) {
                FileControls.DeleteFile(GetPathOfAPP.GetPathWithSep() + ".majorupdateopened");
            }
        }
        checkVersionOut = null;

        if (FileControls.SearchFile(GetPathOfAPP.GetPathWithSep() + "cache" + GetPathOfAPP.GetSep() + "Visited Links")) {
            FileControls.DeleteFile(GetPathOfAPP.GetPathWithSep() + "cache" + GetPathOfAPP.GetSep() + "Visited Links");
        }

        new MainFrame(osrEnabledArg, transparentPaintingEnabledArg, createImmediately, null, param).start(osrEnabledArg, transparentPaintingEnabledArg, createImmediately, null, param);
    }

    private static String initNameBrowser(boolean name) {
        String out;

        if (name) {
            try {
                if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "fakeNameBrowser").equals("true")) {
                    out = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36";
                } else {
                    out = "Entery/" + VER_APP.substring(8, 11);
                }
            } catch (IOException e) {
                out = "Entery/" + VER_APP.substring(8, 11);
            }
        } else {
            try {
                if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "fakeNameBrowser").equals("true")) {
                    out = "";
                } else {
                    out = VER_APP.substring(8, 11);
                }
            } catch (IOException e) {
                out = VER_APP.substring(8, 11);
            }
        }

        return out;
    }

    public static void genCfg() {
        CheckerDIR.Check("cfg");

        if (FileControls.SearchFile(pathOfEngineOptions) == false) {
            try {
                FileControls.CreateFile(pathOfEngineOptions);
                FileControls.writeToFile(pathOfEngineOptions,
                        "</\n"
                                + "\t[ENGINE-OPTIONS]\n"
                                + "\t\t- off-screen-rendering-enabled: false;\n"
                                + "\t\t- transparent-painting-enabled: false;\n"
                                + "\t\t- create-immediately: false;\n"
                                + "\t\t- reloadWithoutCache: false;\n"
                                + "\t\t- fakeNameBrowser: false;\n"
                                + "\t\t- enable-media-stream: false;\n"
                                + "\t[/ENGINE-OPTIONS]\n"
                                + "/>");
            } catch (IOException e) {
                toConsole.print(genLogMessage.gen((byte) 3, false, "Не удалось создать файл конфигурации."));
            }
        }
        if (FileControls.SearchFile(pathOfWindowOptions) == false) {
            try {
                FileControls.CreateFile(pathOfWindowOptions);
                FileControls.writeToFile(pathOfWindowOptions,
                        "</\n"
                                + "\t[WINDOW-OPTIONS]\n"
                                + "\t\t- width: 1278;\n"
                                + "\t\t- height: 728;\n"
                                + "\t\t- fullscreen: false;\n"
                                + "\t\t- theme: dark;\n"
                                + "\t[/WINDOW-OPTIONS]\n"
                                + "/>");
            } catch (IOException e) {
                toConsole.print(genLogMessage.gen((byte) 3, false, "Не удалось создать файл конфигурации."));
            }
        }
    }
}
