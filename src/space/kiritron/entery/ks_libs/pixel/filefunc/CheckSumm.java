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

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Киритрон Стэйблкор
 */

public class CheckSumm {
    /**
     * Генерация контрольной суммы.
     * @param Method Алгоритм, по которому нужно производить операцию хеширования. В библиотеке в классе space.kiritron.entery.ks_libs.pixel.genhash.Gen доступны: MD5, SHA1, SHA256, SHA384, SHA512. Можете использовать и свой параметр, если Вы понимаете, что делаете.
     * @param FilePath Путь до файла.
     * @return контрольную сумму в выбранном алгоритме.
     */

    public static String Gen(String Method, String FilePath) {
        MessageDigest md = null;
        StringBuilder result = new StringBuilder();

        try {
            md.getInstance(Method);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(FilePath), md)) {
            while (dis.read() != -1) ;
            md = dis.getMessageDigest();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }
}
