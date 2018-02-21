package ru.bogacheva.task;


import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyAnswer {

    public static void main(String[] args) throws IOException {
        String directory = null;
        String mask = null;
        int id = 0;
        Scanner in = new Scanner(System.in);
        //массив с отобранными файлами
        ArrayList <File> files = new ArrayList();
        //массив строк в файле
        ArrayList <String> lines = new ArrayList();
        //массив позиций(номер строки) искомого идентификатора в файле
        ArrayList <Integer> idPos = new ArrayList();


        //получение директории
        File dir = new File(getDir(directory, in));

        while (mask == null) {
            //ввод маски
            System.out.println("Введите маску файла");
            mask = in.nextLine();
            //преобразование маски в регулярное выражение
            mask = conversationMask(mask);
            System.out.println(mask);
            //получение выборки файлов по маске
            files = (ArrayList) find(dir, mask);
            if (files.size() == 0) {
                mask = null;
                System.out.println("Не найдено ни одного файла!");
            }
        }

        //получение искомого идентификатора
        try {
            id = getId(in,id);
        } catch (InputMismatchException e) {
            System.out.println("Введеное значение не соответствует ожидаемому");
            id = getId(in,id);
        }

        //перебор всех подходящих файлов
        for (File f: files) {
            lines.clear();
            idPos.clear();
            //чтение содержимого файла
            readFile(id, lines, idPos, f);
            //вывод логов
            if (idPos.size()>0){
                System.out.println(f.getPath());
            }
            prinLogs(lines, idPos);
        }


    }

    private static void prinLogs(ArrayList<String> lines, ArrayList<Integer> idPos) {
        //для каждой позиции выводим по 100
        for (int i=0; i<idPos.size();i++){
            //позиция с искомым id
            int n=idPos.get(i);
            int idP=idPos.get(i);
            //ищем с какой строки начинать выводить
            for (int j=0;j<100;j++){
                if(n>0){n=n-1;}
            }
            //вывод строк
            for (int j=n;(j <=n+200) & (j != lines.size()-1); j++) {
                if (j==idP) {
                    String ANSI_RED = "\u001B[31m";
                    String ANSI_RESET = "\u001B[0m";
                    System.out.println(ANSI_RED + lines.get(j)+ ANSI_RESET);
                }
                else {
                    System.out.println(lines.get(j));
                }

            }
        }
    }

    private static void readFile(int id, ArrayList<String> lines, ArrayList<Integer> idPos, File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f.getPath()));
        String line;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
            if (line.contains(" " + id + " ")) {
                //если находим id, то запоминаем номер строки
                idPos.add(i);
            }
            i++;
        }
    }

    private static int getId(Scanner in, int id) {
        System.out.println("Введите числовой идентификатор");
        id = in.nextInt();
        return id;
    }

    //преобразование маски в регулярное выражение
    private static String conversationMask(String mask) {
        mask = mask.replace(".", "\\.");
        mask = mask.replace("*", ".*");
        mask = mask.replace("?", ".");
        return "^" + mask + "$";
    }

    private static String getDir(String directory, Scanner in) {
        //запрос входных данных
        while (directory == null) {
            System.out.println("Введите директорию поиска в формате 'С:/dir1/dir2'");
            directory = in.nextLine();
            File dir = new File(directory);
            if (!dir.exists()) {
                directory = null;
                System.out.println("Директория не существует!");
            }
            if (!dir.isDirectory()) {
                directory = null;
                System.out.println("Это не директория!");
            }
        }
        return directory;
    }


    private static List find(File dir, String mask) {
        ArrayList res = new ArrayList();
        searchFiles(dir, mask, res);
        return res;
    }

    //поиск подходящих под маску файлов и заполнение ими массива
    private static void searchFiles(File dir, String mask, ArrayList res) {
        File[] list = dir.listFiles();
        for (int i = 0; i < list.length; i++) {
            if (list[i].isDirectory()) {
                searchFiles(list[i], mask, res);
            } else {
                Pattern p = Pattern.compile(mask);
                Matcher m = p.matcher(list[i].getName());
                if (m.matches()) {
                    res.add(list[i]);
                }
            }
        }
    }


}