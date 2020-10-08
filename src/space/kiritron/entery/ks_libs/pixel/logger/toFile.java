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

package space.kiritron.entery.ks_libs.pixel.logger;

import space.kiritron.entery.ks_libs.pixel.GDate;
import space.kiritron.entery.ks_libs.pixel.filefunc.GetPathOfAPP;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Киритрон Стэйблкор
 */

public class toFile {
    private static GDate TADClass = new GDate();

    private static boolean LogFileWriter(String prefixLogFile, String log_messageFile) {
        File logFile;
        if (prefixLogFile.equals("")) {
            logFile = new File(GetPathOfAPP.GetPathWithSep() + "logs" + GetPathOfAPP.GetSep() + TADClass.GetDate + ".log");
        } else {
            logFile = new File(GetPathOfAPP.GetPathWithSep() + "logs" + GetPathOfAPP.GetSep() + prefixLogFile + "-" + TADClass.GetDate + ".log");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            String lineSeparator = System.getProperty("line.separator");
            writer.write(log_messageFile + lineSeparator);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Запись лог файла.
     * @param log_message - Сообщение, которое нужно записать в лог файл.
     * @param prefixLogFile - Префикс лог файла.
     * @return результат выполнения. Если равно TRUE, то всё в порядке, а если FALSE, то что-то пошло не так.
     */
    public static boolean WriteLog(String log_message, String prefixLogFile) { return LogFileWriter(prefixLogFile, log_message); }

    /**
     * Запись лог файла, но уже без префикса в названии лог файла.
     * @param log_message - Сообщение, которое нужно записать в лог файл.
     * @return результат выполнения. Если равно TRUE, то всё в порядке, а если FALSE, то что-то пошло не так.
     */
    public static boolean WriteLogNoPrefix(String log_message) { return LogFileWriter("", log_message); }
}
