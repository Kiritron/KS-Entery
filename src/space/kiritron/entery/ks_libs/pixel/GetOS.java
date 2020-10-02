package space.kiritron.entery.ks_libs.pixel;

import space.kiritron.entery.ks_libs.pixel.filefunc.FileControls;

import java.io.IOException;

/**
 * Класс с методами, которые позволяют получить информацию об ОС.
 * @author Киритрон Стэйблкор и MR.REX
 * @version 1.2
 */

//Киритрон: Вношу изменения уже второй раз, так как немного обосрался с контролем версий и потерял
//новую версию данного класса.
public class GetOS {
    public static String Name = "os.name",
                         Arch = "os.arch",
                         Version = "os.version",
                         User = "user.name",
                         UserDir = "user.dir",
                         UserHome = "user.home";

    /**
     * Узнать имя ОС, на которой запущено данное ПО.
     * @return возвращает имя операционной системы.
     */
    public static String Get() {
        return System.getProperty("os.name");
    }

    /**
     * Узнать на Windows ли сейчас работает данная программа.
     * @return возвращает boolean значение.
     */
    public static final boolean isWindows() {
        return Get().toLowerCase().contains("windows");
    }

    /**
     * Узнать на OS X ли сейчас работает данная программа.
     * @return возвращает boolean значение.
     */
    public static final boolean isMacintosh() {
        return Get().toLowerCase().contains("mac");
    }

    /**
     * Узнать на системе с ядром Linux сейчас работает данная программа или нет.
     * @return возвращает boolean значение.
     */
    public static final boolean isLinux() {
        return Get().toLowerCase().contains("linux");
    }

    /**
     * Узнать на КС Догиру ли сейчас работает данная программа.
     * @return возвращает boolean значение.
     */
    public static final boolean isDogiru() {
        boolean out;

        if (Get().toLowerCase().contains("linux")) {
            try {
                if (FileControls.ReadFile("/etc/release").toLowerCase().contains("dogiru")) {
                    out = true;
                } else {
                    out = false;
                }
            } catch (IOException e) {
                out = false;
            }
        } else {
            out = false;
        }

        return out;
    }

    /**
     * Позволяет получить данные об ОС, на которой запущено данное ПО.
     * @param Type Какую информацию необходимо получить об ОС. Доступны Name, Arch, Version, User, UserDir, UserHome.
     * @return возвращает ту информацию об ОС, которую вы запросили.
     */
    public static String GetOther(String Type) {
        return System.getProperty(Type);
    }
}
