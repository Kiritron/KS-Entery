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

package space.kiritron.entery.ks_libs.duke;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Класс с методом получения и сравнения версий по SOCKET.
 * @author Киритрон Стэйблкор
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

    public static String checkVersion(String HOST, int PORT, String NAME_APP, String VER_APP, boolean checkMajor) {
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
            if (versionHandler.checkDifference(VER_APP, ServerAnswer)) {
                if (checkMajor) {
                    if (versionHandler.checkMajorMarker(ServerAnswer)) {
                        ServerVersion = "DIFFERENCE_FINDED. MAJOR.";
                    } else {
                        ServerVersion = "DIFFERENCE_FINDED. MINOR.";
                    }
                } else {
                    ServerVersion = "DIFFERENCE_FINDED";
                }
            } else {
                ServerVersion = "OK";
            }
        } catch (UnknownHostException ex) {
            ServerVersion = "UNKNOWN_HOST_ERROR";
        } catch (IOException ex) {
            ServerVersion = "IO_ERROR";
        }
        return ServerVersion;
    }
}
