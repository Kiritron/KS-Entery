package space.kiritron.entery.ks_libs.pixel.genhash;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Класс с методами для генерации хеша.
 * @author Киритрон Стэйблкор и MR.REX
 * @version 1.0
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
