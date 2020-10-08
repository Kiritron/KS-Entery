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

import java.io.*;

/**
 * @author Киритрон Стэйблкор
 */

public class FileControls {
    /**
     * Поиск файла.
     * @param filename - Путь до файла.
     * @return возвращает результат поиска. TRUE - файл есть, FALSE - файла нет.
     */

    public static boolean SearchFile(String filename) {
        File TargetFile = new File(filename); 
        boolean FileFounded = TargetFile.exists();
        return FileFounded;
    }

    /**
     * Удаление файла.
     * @param filename - Путь до файла.
     * @return возвращает результат удаления. TRUE - файл удалён, FALSE - файл удалить не удалось.
     */
    public static boolean DeleteFile(String filename) {
        File TargetFile = new File(filename);
        boolean FileDeleted = TargetFile.delete();
        return FileDeleted;
    }

    /**
     * Создание файла.
     * @param filename - Путь до файла.
     * @return возвращает результат создания. TRUE - файл создан, FALSE - файл создать не удалось.
     */
    public static boolean CreateFile(String filename) throws IOException {
        File TargetFile = new File(filename);
        boolean FileCreated = TargetFile.createNewFile();
        return FileCreated;
    }

    /**
     * Прочитать файл.
     * @param filename - Путь до файла.
     * @return возвращает данные, которые удалось вытянуть из файла.
     */
    public static String ReadFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader (filename));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        if (stringBuilder.length() != 0) {
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }

        return stringBuilder.toString();
    }

    /**
     * Записать в файл. Обратите внимание, данный метод перезаписывает данные, а не дополняет их.
     * @param filename - Путь до файла.
     * @param data - Данные, которые нужно записать в файл.
     * @return возвращает результат записи. TRUE - данные записаны в файл, FALSE - данные в файл записать не удалось.
     */
    public static boolean writeToFile(String filename, String data) {
        boolean Status = false;
        File TargetFile = new File(filename);
        FileWriter fr = null;
        try {
            fr = new FileWriter(TargetFile);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                Status = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Status;
    }

    /**
     * Добавить данные в файл. Работает как и writeToFile, но дополняет данные, а не перезаписывает.
     * @param filename - Путь до файла.
     * @param data - Данные, которые нужно записать в файл.
     * @return возвращает результат перезаписи. TRUE - данные перезаписаны в файл, FALSE - данные в файл записать не удалось.
     */
    public static boolean addDataToFile(String filename, String data) {
        boolean Status = false;
        String SecondData;
        try {
            SecondData = ReadFile(filename) + data;
        } catch (IOException ee) {
            return Status;
        }

        File TargetFile = new File(filename);
        FileWriter fr = null;
        try {
            fr = new FileWriter(TargetFile);
            fr.write(SecondData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                Status = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Status;
    }

    /**
     * Переместить файл в другую папку.
     * @param filename Путь до файла.
     * @param destpath Директория, в которую необходимо перенести файл.
     * @return возвращает результат переноса. TRUE - файл перенесён, FALSE - перенос не удался.
     */
    public static boolean movingFile(String filename, String destpath) {
        File file = new File(filename);

        if (!destpath.substring(destpath.length() - 1).contains(GetPathOfAPP.GetSep())) {
            destpath = destpath + GetPathOfAPP.GetSep();
        }

        File destFolder = new File(destpath + file.getName());

        return file.renameTo(destFolder);
    }

    /**
     * Переименовать файл.
     * @param filename Путь до файла.
     * @param newfilename Новое имя файла.
     * @return возвращает результат изменения имени файла. TRUE - файл переименован, FALSE - переименовать файл не удалось.
     */
    public static boolean renameFile(String filename, String newfilename) {
        File file = new File(filename);
        File destFolder = new File(filename.replace(file.getName(), "") + newfilename);

        return file.renameTo(destFolder);
    }

    /**
     * Скопировать файл.
     * @param in_filename Путь до файла, который необходимо скопировать.
     * @param out_dir Путь, в который файл необходимо скопировать.
     * @return возвращает результат копирования. TRUE - файл скопирован, FALSE - копирование не удалось.
     */
    public static boolean copyFile(String in_filename, String out_dir) {
        File in_f = new File(in_filename);

        if (!out_dir.substring(out_dir.length() - 1).contains(GetPathOfAPP.GetSep())) {
            out_dir = out_dir + GetPathOfAPP.GetSep();
        }

        File out_f = new File(out_dir + in_f.getName());

        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(in_f);
            os = new FileOutputStream(out_f);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) { os.write(buffer, 0, length); }
            return true;
        } catch (IOException EeE) {
            return false;
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                return false;
            }
        }
    }
}
