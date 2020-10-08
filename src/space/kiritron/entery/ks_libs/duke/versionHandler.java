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

/**
 * Класс с методом получения и сравнения версий через HTTP/HTTPS.
 * @author Киритрон Стэйблкор
 */

public class versionHandler {
    protected static boolean checkDifference(String VER_APP, String VER_FROM_SERVER) {
        if (VER_FROM_SERVER.contains(":::minor") && VER_FROM_SERVER.contains(":::major")) {
            while (VER_FROM_SERVER.contains(":::minor") && VER_FROM_SERVER.contains(":::major")) {
                VER_FROM_SERVER = VER_FROM_SERVER.replace(":::minor", "");
                VER_FROM_SERVER = VER_FROM_SERVER.replace(":::major", "");
            }
        }

        // Вообще проверять наличие :::minor выше не прям уж обязательно, так как это не проверяется методом
        // checkMajorMarker, но вдруг кому-то вздумается вписать это в ответ от сервера?

        if (!VER_APP.equals(VER_FROM_SERVER)) {
            return true;
        } else {
            return false;
        }
    }

    protected static boolean checkMajorMarker(String VER_FROM_SERVER) {
        boolean MajorMarkerDetected = false;

        if (VER_FROM_SERVER.contains(":::major")) {
            MajorMarkerDetected = true;
        }

        return MajorMarkerDetected;
    }
}
