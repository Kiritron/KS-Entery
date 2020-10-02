package space.kiritron.entery.ks_libs.pixel.filefunc;

import java.io.*;

/**
 * Класс с методами для управления файлами.
 * @author Киритрон Стэйблкор
 * @version 1.1
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
    public static boolean addDataToFile(String filename, String data) throws IOException {
        boolean Status = false;
        String SecondData = ReadFile(filename) + data;
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
}
