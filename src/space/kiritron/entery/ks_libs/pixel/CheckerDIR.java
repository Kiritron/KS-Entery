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

package space.kiritron.entery.ks_libs.pixel;

import space.kiritron.entery.ks_libs.pixel.filefunc.GetPathOfAPP;
import static space.kiritron.entery.ks_libs.pixel.filefunc.DirControls.CreateDir;
import static space.kiritron.entery.ks_libs.pixel.filefunc.DirControls.SearchDir;

/**
 * @author Киритрон Стэйблкор
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
