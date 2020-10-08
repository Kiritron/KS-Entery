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

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author Киритрон Стэйблкор и MR.REX
 */

public class Gen {
    //Киритрон: В библиотеке от Рекса, был метод для генерации хеша в MD2.
    //Я не стал его добавлять в код, так как данный алгоритм признали
    //взломанным и им уже не пользуются.
    public final static String MD5 = "MD5",
                               SHA1 = "SHA-1",
                               SHA256 = "SHA-256",
                               SHA384 = "SHA-384",
                               SHA512 = "SHA-512";

    /**
     * Создание хеша.
     * @param Method Алгоритм, по которому нужно производить операцию хеширования. В библиотеке доступны: MD5, SHA1, SHA256, SHA384, SHA512. Можете использовать и свой параметр, если Вы понимаете, что делаете.
     * @param Message Сообщение, которое нужно пропустить через алгоритм выбранный алгоритм.
     * @return возвращает хеш в выбранном алгоритме.
     */
    public static String Hash(String Method, String Message) {
        String Hash = null;
        try {
            MessageDigest MD = MessageDigest.getInstance(Method);
            MD.reset();
            MD.update(Message.getBytes());
            byte[] Digest = MD.digest();
            BigInteger Big = new BigInteger(1, Digest);
            Hash = Big.toString(16);
            while (Hash.length() < 32) Hash += "0" + Hash;
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
        return Hash;
    }
}
