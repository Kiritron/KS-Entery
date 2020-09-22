package space.kiritron.entery.ks_libs.pixel;

import space.kiritron.entery.ks_libs.pixel.filefunc.GetPathOfAPP;
import static space.kiritron.entery.ks_libs.pixel.filefunc.DirControls.CreateDir;
import static space.kiritron.entery.ks_libs.pixel.filefunc.DirControls.SearchDir;

/**
 * Класс с методами для проверки наличия каталогов и их последующего создания, если их нет.
 * @author Киритрон Стэйблкор
 * @version 1.0
 */

public class CheckerDIR {
    /**
     * Поиск каталога в директории, где запущено данное ПО. Если каталога нет, то он будет создан.
     * @param dir - Путь до каталога.
     */
    public static void Check(String dir) {
        if (SearchDir(GetPathOfAPP.GetPathWithSep() + dir) == false) {
            CreateDir(GetPathOfAPP.GetPathWithSep() + dir);
        }
    }

    /**
     * Поиск каталога вне директории, где запущено данное ПО. Если каталога нет, то он будет создан.
     * @param path - Путь до каталога.
     */
    public static void CheckOut(String path) {
        if (SearchDir(path) == false) {
            CreateDir(path);
        }
    }
}
