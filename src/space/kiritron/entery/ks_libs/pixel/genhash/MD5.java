package space.kiritron.entery.ks_libs.pixel.genhash;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Класс с методом по созданию хеша алгоритмом MD5.
 * @author Киритрон Стэйблкор
 * @version 1.1
 */

public class MD5 {
    /**
     * Создание хеша алгоритмом MD5.
     * @param Message - Сообщение, которое нужно пропустить через алгоритм MD5.
     * @return возвращает хеш MD5.
     * @deprecated есть аналогичное и более компактное решение в классе Gen.
     */

    //Киритрон: Метод сохранён и переделан, чтобы избежать проблем с совместимостью.
    public static String GenHash(String Message) {
        return Gen.Hash(Gen.MD5, Message);
    }
}
