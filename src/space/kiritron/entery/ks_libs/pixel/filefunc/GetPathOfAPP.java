package space.kiritron.entery.ks_libs.pixel.filefunc;

import space.kiritron.entery.ks_libs.pixel.GetOS;

/**
 * Класс с методами для получения адреса папки в файловой системе, в которой находится исполняемый файл данного ПО и получения слеша исходя из того, в какой ОС запущено данное ПО.
 * @author Киритрон Стэйблкор
 * @version 1.2
 */

public class GetPathOfAPP {
    /**
     * Получить адрес папки, в которой запущено данное ПО.
     * @return возвращает адрес папки.
     */

    public static String GetPath() {
        String path = System.getProperty("user.dir");
        return path;
    }

    /**
     * Получить адрес папки, в которой запущено данное ПО, но со слешем на конце.
     * @return возвращает адрес папки.
     */
    public static String GetPathWithSep() {
        String fpath = System.getProperty("user.dir");
        String spath = GetSep();
        String tpath = fpath + spath;

        return tpath;
    }

    /**
     * Получить слеш, который характерен для ОС, на которой запущено данное ПО. Поддерживаются только Linux и Windows, а ещё экспирементально Mac OS.
     * @return возвращает слеш, но в случае, если систему не удалось опознать - NULL.
     */
    public static String GetSep() {
        String Sep = null;

        if (GetOS.isLinux()) {
            Sep = "/";
        }

        if (GetOS.isMacintosh()) {
            Sep = "/";
        }

        if (GetOS.isWindows()) {
            Sep = "\\";
        }

        return Sep;
    }
}
