package space.kiritron.entery;

import com.formdev.flatlaf.FlatDarculaLaf;
import space.kiritron.entery.core.MainFrame;
import space.kiritron.entery.core.dialog.WelcomeWindow;
import space.kiritron.entery.ks_libs.duke.httpconn;
import space.kiritron.entery.ks_libs.pixel.CheckerDIR;
import space.kiritron.entery.ks_libs.pixel.filefunc.FileControls;
import space.kiritron.entery.ks_libs.pixel.filefunc.GetPathOfAPP;
import space.kiritron.entery.ks_libs.pixel.logger.genLogMessage;
import space.kiritron.entery.ks_libs.pixel.logger.toConsole;
import space.kiritron.entery.ks_libs.tolchok.TOLF_Handler;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;

/**
 * @author Киритрон Стэйблкор
 * @version 1.0
 */

public class init {
    public final static String NAME_APP = "КС Энтери";
    public final static String VER_APP = "Версия: 1.0-КСЭ Альфа";
    public static String NAME_APP_USER_AGENT = initNameBrowser(true);
    public static String VER_APP_USER_AGENT = initNameBrowser(false);
    public static boolean outdated = false;

    public static final String pathOfEngineOptions = GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "engine-options.tolf";

    public static void main(String[] args) {
        System.out.println("==================");
        System.out.println(NAME_APP);
        System.out.println(VER_APP);
        System.out.println("==================");

        CheckerDIR.Check("cache");
        CheckerDIR.Check("res");
        CheckerDIR.Check("lang");

        genCfg();

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
            toConsole.print(genLogMessage.gen((byte) 1, false, "Инициализирована библиотека Swing FlatLaf от FormDev Software GmbH"));
        } catch (UnsupportedLookAndFeelException e) {
            toConsole.print(genLogMessage.gen((byte) 3, false, "Не удалось инициализировать библиотеку Swing FlatLaf от FormDev Software GmbH. Стиль окна может быть неправильным."));
        }

        boolean osrEnabledArg;
        boolean transparentPaintingEnabledArg;
        boolean createImmediately;
        try {
            if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "off-screen-rendering-enabled").equals("true")) {
                osrEnabledArg = true; } else { osrEnabledArg = false; }
            if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "transparent-painting-enabled").equals("true")) {
                transparentPaintingEnabledArg = true; } else { transparentPaintingEnabledArg = false; }
            if (TOLF_Handler.ReadParamFromData(FileControls.ReadFile(pathOfEngineOptions), "ENGINE-OPTIONS", "create-immediately").equals("true")) {
                createImmediately = true; } else { createImmediately = false; }
        } catch (IOException e) {
            osrEnabledArg = false;
            transparentPaintingEnabledArg = false;
            createImmediately = false;
            toConsole.print(genLogMessage.gen((byte) 3, false, "Не удалось прочитать файл конфигурации движка. Используются значения по умолчанию."));
        }

        URL logo_url = init.class.getResource("icon.png");

        String VER_APP_FILTERED;
        VER_APP_FILTERED = VER_APP.replace("Версия: ", "");
        VER_APP_FILTERED = VER_APP_FILTERED.replace("-КСЭ Альфа", "");


        if (httpconn.checkVersion("https://kiritron.space/versions/entery",false, VER_APP_FILTERED).contains("DIFFERENCE_FINDED")) {
            outdated = true;
        } else {
            outdated = false;
        }

        if (FileControls.SearchFile(GetPathOfAPP.GetPathWithSep() + "cache" + GetPathOfAPP.GetSep() + "Visited Links")) {
            FileControls.DeleteFile(GetPathOfAPP.GetPathWithSep() + "cache" + GetPathOfAPP.GetSep() + "Visited Links");
        }

        MainFrame MF = new MainFrame(osrEnabledArg, transparentPaintingEnabledArg, createImmediately, null, args);
        MF.start(osrEnabledArg, transparentPaintingEnabledArg, createImmediately, null, args);
    }

    private static String initNameBrowser(boolean name) {
        final String pathOfEngineOptions = GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "engine-options.tolf";
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

        final String pathOfEngineOptions = GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "engine-options.tolf";
        final String pathOfWindowOptions = GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "window-options.tolf";

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
                                + "\t[/WINDOW-OPTIONS]\n"
                                + "/>");
            } catch (IOException e) {
                toConsole.print(genLogMessage.gen((byte) 3, false, "Не удалось создать файл конфигурации."));
            }
        }
    }
}
