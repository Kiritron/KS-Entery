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

package space.kiritron.entery.ks_libs.pixel.genhash;

/**
 * @author Киритрон Стэйблкор
 */

public class SHA1 {
    /**
     * Создание хеша алгоритмом SHA1.
     * @param Message - Сообщение, которое нужно пропустить через алгоритм SHA1.
     * @return возвращает хеш SHA1.
     * @deprecated есть аналогичное и более компактное решение в классе Gen.
     */

    //Киритрон: Метод сохранён и переделан, чтобы избежать проблем с совместимостью.
    public static String GenHash(String Message) {
        return Gen.Hash(Gen.SHA1, Message);
    }
}
