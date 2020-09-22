// Copyright (c) 2020 Киритрон Стэйблкор.

package space.kiritron.entery.core.handler;

import space.kiritron.entery.ks_libs.pixel.filefunc.FileControls;
import space.kiritron.entery.ks_libs.pixel.filefunc.GetPathOfAPP;
import space.kiritron.entery.ks_libs.pixel.logger.genLogMessage;
import space.kiritron.entery.ks_libs.pixel.logger.toConsole;

import java.io.IOException;

public class BookmarkHandler {
    final static private String filepath = GetPathOfAPP.GetPathWithSep() + "cfg" + GetPathOfAPP.GetSep() + "bookmarks.data";

    public static void AddBookmarkToDataFile(String name, String link) {
        boolean FileReady = false;
        if (!checkBookmarkFile()) {
            try {
                FileControls.CreateFile(filepath);
                FileReady = true;
            } catch (IOException e) {
                toConsole.print(genLogMessage.gen((byte) 3, false, "Добавить закладку невозможно. Файл для хранения не создаётся."));
                FileReady = false;
            }
        } else {
            FileReady = true;
        }

        if (FileReady) {
            try {
                String cache = FileControls.ReadFile(filepath);
                if (cache.contains(";")) {
                    FileControls.writeToFile(filepath, cache.substring(0, cache.indexOf(";") + 1) + "R00R00R" + name + ":00:00:" + link + "?0?0?0?;");
                } else {
                    FileControls.writeToFile(filepath, "R00R00R" + name + ":00:00:" + link + "?0?0?0?;");
                }
            } catch (Exception e) {
                toConsole.print(genLogMessage.gen((byte) 3, false, "Добавить закладку невозможно. Не удаётся записать данные в файл."));
                e.printStackTrace();
            }
        }
    }

    public static void RemoveBookmarkInDataFile(String name, String link) {
        if (checkBookmarkFile()) {
            try {
                String data = FileControls.ReadFile(filepath);
                data = data.replace("R00R00R" + name + ":00:00:" + link + "?0?0?0?;", "");
                FileControls.writeToFile(filepath, data);
            } catch (Exception e) {
                toConsole.print(genLogMessage.gen((byte) 3, false, "Удалить закладку невозможно."));
            }
        } else {
            toConsole.print(genLogMessage.gen((byte) 3, false, "Удалить закладку невозможно. Файла нет."));
        }
    }

    public static String[] ReadBookmarksFromDataFile() {
        String[] bookmarks = null;

        try {
            String data = FileControls.ReadFile(filepath);
            if (data != null || !data.isEmpty()) {
                int lines = 1;
                int pos = 0;
                while ((pos = data.indexOf("\n", pos) + 1) != 0) {
                    lines++;
                }

                bookmarks = new String[lines];
                int deleteLenght;

                for (int count = 0; count < lines;) {
                    String cache;
                    cache = data.substring(0, data.indexOf(";"));
                    bookmarks[count] = cache;
                    data = data.replace(cache + ";", "");

                    count++;
                }
            }
        } catch (Exception e) {
            toConsole.print(genLogMessage.gen((byte) 3, false, "Не удаётся прочитать файл с закладками."));
        }

        return bookmarks;
    }

    private static boolean checkBookmarkFile() {
        return FileControls.SearchFile(filepath);
    }
}
