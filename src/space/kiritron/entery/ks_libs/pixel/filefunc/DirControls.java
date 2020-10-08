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

package space.kiritron.entery.ks_libs.pixel.filefunc;

import java.io.File;

/**
 * @author Киритрон Стэйблкор
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
