package com.lev.accprog.server;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class QueueHolder {

private static final String HELP = "You should pass file name as argument for correct program execution!";

    public PriorityQueue<Food> getQueue() {
        return mQueue;
    }

    private PriorityQueue<Food> mQueue;

    public QueueHolder() {
        mQueue = new PriorityQueue<>();
    }

    public void handleCommand(Command command) {
        try {
            execute(command);
        } catch (NoSuchElementException e) {
            System.out.println("Bye!");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Неверное количество аргументов");
        } catch (NoSuchMethodException e) {
            System.out.println("Нет такой команды");
        } catch (ParseException e) {
            System.out.println("так не делается, брат");
        } catch (IllegalArgumentException e) {
            System.out.println("ало , карыто");
        }
    }

    private void execute(Command command) throws NoSuchMethodException, ParseException {
        switch (command.getCommandText()) {
            case "help":
                show(HELP);
                break;

            case "info":
                printInfo();
                break;

            case "import":
                if (mQueue != null){
                    mQueue.retainAll(Collections.emptyList());
                }
                loadDataFrom("data.txt");
                break;

            case "put":
                putDataTo(command.getArgumentAsText());
                break;

            case "remove":
                mQueue.remove(command.getArgumentObject());
                break;

            case "remove_all":
                try {
                    removeAll(command.getArgumentObject());
                } catch (JSONException e) {
                    System.out.println("Не правильный формат команды");
                }
                break;

            case "add":
                try {
                    mQueue.add(command.getArgumentObject());
                } catch (JSONException e) {
                    System.out.println("не правильный формат команды");
                }
                break;

            case "add_if_max":
                try {
                    addIfMax(command.getArgumentObject());
                } catch (JSONException e) {
                    System.out.println("не правильный формат команды");
                }
                break;

            case "remove_greater":
                try {
                    removeGreater(command.getArgumentObject());
                } catch (JSONException e) {
                    System.out.println("Не правильный формат команды");
                }
                break;

            default:
                throw new NoSuchMethodException();

        }
    }

    /**
     * @author Levenets Denis
     * Печает Тип колекции + его название + количество элементов
     * Выдаёт информацию о коллекции
     * @exception  NoSuchMethodException может вылезти ошибка с неверным вводом названия команды (NoSuchMethodException)
     * @return getState (информация о коллекции)
     **/
    public String printInfo() {
        System.out.printf("Тип коллекции:" + Food.class.toString() + "%nКоличество элементов: " + mQueue.size() + "%n");
        return "Тип коллекции:" + Food.class.toString() +" "+ "Количество элементов: " + mQueue.size();
    }

    /**
     *
     * @param s строка , которую мы записываем в коллецию
     */
    private void loadDataFrom(String s) {
        FileIO writter = new FileIO();
        try {
            FoodDAO dao = new FoodDAO();
            mQueue.addAll(dao.getFood());
            System.out.println("Data loaded from " + s);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param fileName аргумент в строковом формате
     */
    private void putDataTo(String fileName) {
        FoodDAO dao = new FoodDAO();
        try {
            dao.saveFood(new ArrayList<>(mQueue));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //FileIO writer = new FileIO();
        //writer.writeQueue(mQueue, fileName);
    }

    /**
     * @author Denis Levenets
     * Пока очередь не пустая , берется первый элемент коллекции ( его дату )  и сравнивается с датой заданного элементаи
     * если дата элемента с очереди больше , чем дата заданного , то элемент очереди удаляется
     * удаление будет происходить до тех пор , пок дата верхнего элемента очереди будет больше , чем у mArgument заданного
     * @return void
     * @exception NoSuchMethodException ошибка в вводе команды
     * @exception ArrayIndexOutOfBoundsException не правильное количество аргументов
     * @exception JSONException не правильный не правильнвй формат команды
     * удаляют из коллекции все элементы , превышающий заданный
     */

    public void removeGreater(Food food) {
        while (!mQueue.isEmpty() && mQueue.peek().getExpirationDate().compareTo(food.getExpirationDate()) > 0) {
            mQueue.poll();
        }

    }

    /**
     *@author Levenets Denis
     *@exception JSONException, Может попасть такая ошибка , как JSONException , если не правильно будет задан формат команды, а также InvalidArgumentException , если не верный аргумент
     *@exception NoSuchMethodException описание ниже
     * @exception ArrayIndexOutOfBoundsException описание ниже
     * может также возникнуть ошибка с нверным вводом аргумента и неправильнным вводом названия команды!((NoSuchMethodException,ArrayIndexOutOfBoundsException)
     * добавляет новый элемент  в коллекцию , если  его значение превышает значение наибольшего элемента
     *Команда add_if_max Добавляет заданный элемент  в очередь , если он больше максимально заданного объекта  который уже находится в очереди
     * Берёт верхний элемент коллекции и сравнивает с заданным элементом
     *Подаётся элемент типа Food
     *Food Класс с полями
     *Когда прописуется команда , подаётся агумент , который распарсивается  сначало как строка , а потом создаётся json объект , после у json берутся поля и записываются в Food
     *  @return void
     */

    public void addIfMax(Food food) {
        if (!mQueue.isEmpty() && mQueue.peek().compareTo(food) > 0)
            mQueue.add(food);
        System.out.println("Done");
        //add_if_max {"taste":"SWEET","expirationDate":"2017-05-21","name":"Apple"}
    }

    /**
     * @author Denis Levenets
     * @exception JSONException возникнуть  такая ошибка , как JSONException , если не правильно будет задан формат команды
     * @exception NoSuchMethodException не правильно введено название команды
     *@exception ArrayIndexOutOfBoundsException неправильное кольчество аргументов
     * Команда удаляет все элементы , эквивалентные заданному
     * Задаётся аргумент типа Food после чего стравнивает его со всеми элементами очереди, после чего удаляются все элементы
     * , которые эквивалентны заданному.
     * @return void
     */
    public  void removeAll(Food food) {
        mQueue.removeIf(x -> x.equals(food));
        System.out.println("Done");
        //remove_all {"taste":"SWEET","expirationDate":"2017-05-15","name":"Apple"}
    }

    public void initShutdownHook() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String name = "data";
            String ext = ".txt";
            File file = new File(name + ext);
            String newName = "";
            String putCommand = "";
            if (!file.canWrite()) {
                int i = 1;
                boolean written = false;
                while (!written) {
                    newName = name + "_" + i;
                    file = new File(newName + ext);
                    if (!file.exists() || file.isDirectory()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            System.out.println("Can't create file " + newName + ext);
                        }
                    }
                    if (file.canWrite()) {
                        putCommand = "put " + newName + ext;
                        written = true;
                    } else {
                        System.out.println("ipprosible to write in " + name + "will be written in new_data.txt");
                        i++;
                    }
                }
            } else {
                putCommand = "put " + name + ext;
            }
            this.handleCommand(new Command(putCommand));
        }, "Shutdown-thread"));
    }

    public void initListening() {
        System.out.println("Enter a command!");
        Scanner scanner = new Scanner(System.in);
        String line;
        while (true) {
            line = scanner.nextLine();
            if (line.equals("quit")) {
                break;
            }
                this.handleCommand(new Command(line));
        }
    }

    private void show(String str) {
        System.out.println(str);
    }

    public void add(Food food){
        mQueue.add(food);
    }
}
