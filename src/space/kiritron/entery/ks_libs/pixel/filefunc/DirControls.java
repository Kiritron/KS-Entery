package space.kiritron.entery.ks_libs.pixel.filefunc;

import java.io.File;

/**
 * Класс с методами по управлению каталогами.
 * @author Киритрон Стэйблкор
 * @version 1.0
 */

public class DirControls {
    /**
     * Поиск каталога.
     * @param dirname - Путь до каталога.
     * @return возвращает результат поиска. TRUE - каталог есть, FALSE - каталога нет.
     */
    public static boolean SearchDir(String dirname) {
        File TargetDir = new File(dirname);
        boolean DirSearched = TargetDir.exists();
        return DirSearched;
    }

    /**
     * Создать каталог.
     * @param dirname - Путь до каталога.
     * @return возвращает результат создания. TRUE - каталог создан, FALSE - что-то пошло не так при создании каталога.
     */
    public static boolean CreateDir(String dirname) {
        File TargetDir = new File(dirname);
        boolean DirCreated = TargetDir.mkdir();
        return DirCreated;
    }

    /**
     * Удаление каталога.
     * @param dirname - Путь до каталога.
     * @return возвращает результат удаления. TRUE - каталог удалён, FALSE - каталог не удалось удалить.
     */
    public static boolean DeleteDir(String dirname) {
        File TargetDir = new File(dirname);
        boolean DirDeleted = TargetDir.delete();
        return DirDeleted;
    }

    /**
     * Принудительное удаление. Удаляет каталог со всем, что есть внутри. (Экспериментально)
     * @param dirname - Путь до каталога.
     * @return возвращает результат удаления. TRUE - каталог удалён, FALSE - каталог не удалось удалить.
     */
    public static boolean ForceDeleteDir(String dirname) {
        File TargetDir = new File(dirname);

        File[] files = TargetDir.listFiles();
        if (files!=null) {
            for (File f: files) {
                if (f.isDirectory()) {
                    ForceDeleteDir(String.valueOf(f));
                } else {
                    f.delete();
                }
            }
        }

        boolean DirDeleted = TargetDir.delete();
        return DirDeleted;
    }
}
