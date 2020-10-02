package space.kiritron.entery.ks_libs.pixel.genhash;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Класс с методом по созданию хеша алгоритмом SHA1.
 * @author Киритрон Стэйблкор
 * @version 1.1
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
