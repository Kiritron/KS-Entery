package space.kiritron.entery.ks_libs.duke;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Класс с методом получения и сравнения версий по SOCKET.
 * @author Киритрон Стэйблкор
 * @version 1.0
 */

public class srvsockconn {
    /**
     * Проверка версии с помощью подключения к серверу КС Дьюк.
     * @param HOST Адрес сервера. Может быть IP адресом или доменом.
     * @param PORT Порт сервера.
     * @param NAME_APP Имя приложения. Должно соотвествовать имени профиля приложния на сервере КС Дьюк.
     * @param VER_APP Версия этого приложения(Не та, что на сервере).
     * @return возвращает результат проверки. OK - Всё в порядке. DIFFERENCE_FINDED - Есть различия. Другой ответ - признак ошибки.
     */

    public static String checkVersion(String HOST, int PORT, String NAME_APP, String VER_APP) {
        String ServerVersion = null;
        try (Socket socket = new Socket(HOST, PORT)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            String sendMessage = "Какой сейчас актуальный код версии у " + NAME_APP + "?";
            writer.println(sendMessage);
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String ServerAnswer = reader.readLine();
            socket.close();
            if (VER_APP.equals(ServerAnswer)) {
                ServerVersion = "OK";
            } else {
                ServerVersion = "DIFFERENCE_FINDED";
            }
        } catch (UnknownHostException ex) {
            ServerVersion = "UNKNOWN_HOST_ERROR";
        } catch (IOException ex) {
            ServerVersion = "IO_ERROR";
        }
        return ServerVersion;
    }
}
