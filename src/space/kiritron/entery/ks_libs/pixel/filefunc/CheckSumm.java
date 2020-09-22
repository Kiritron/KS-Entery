package space.kiritron.entery.ks_libs.pixel.filefunc;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
