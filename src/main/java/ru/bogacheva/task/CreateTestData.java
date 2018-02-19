package ru.bogacheva.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Пользователь on 20.02.2018.
 */
//класс для создания тестовых данных
public class CreateTestData {
    public static void main(String[] args) throws IOException {
        cretaeStructure();
        try {
            createLogs();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //метод для создания структуры директорий
    private static void cretaeStructure() {
        createDir(new File("C://logs"));
        createDir(new File("C://logs/logs1"));
        createDir(new File("C://logs/logs2"));
        createDir(new File("C://logs/logs3"));
    }

    //метод для создания файлов логов
    private static void createLogs() throws IOException {
        createLog(new File("C://logs/logs1/log1.log"),100);
        createLog(new File("C://logs/logs1/log01.log"),200);
        createLog(new File("C://logs/logs2/log2.log"),300);
        createLog(new File("C://logs/logs2/log02.log"),400);
        createLog(new File("C://logs/log1.log"),100);
        createLog(new File("C://logs/log01.log"),500);
    }

    //создание одной директории
    private static void createDir(File dir) {
        if (!dir.exists()) {dir.mkdir();}
    }

    //создание одного файла логов и его заполнение
    private static void createLog (File file,int id) throws IOException {
        if (!file.exists()) {file.createNewFile();}
        FileWriter writer = new FileWriter(file, true);
        for (int i = 0; i < 1000; i++){
            Date date = new Date();
            SimpleDateFormat formatForDate = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
            if (((i+1) % 200) == 0){
                id = id + i/200;
                String text = formatForDate.format(date) +" id " + id + " this is test log" + System.getProperty("line.separator");
                writer.write(text);
            }
            else {
                String text = formatForDate.format(date) + " this is test log" + System.getProperty("line.separator");
                writer.write(text);
            }
        }
        writer.flush();
    }
}
