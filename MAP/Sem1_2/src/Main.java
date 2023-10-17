import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Problema 4
        MessageTask m1 = new MessageTask("1", "abc", "mesaj1", "A", "B", LocalDateTime.now());
        MessageTask m2 = new MessageTask("2", "abc", "mesaj2", "B", "C", LocalDateTime.now());
        MessageTask m3 = new MessageTask("3", "abc", "mesaj3", "C", "A", LocalDateTime.now());

        MessageTask[] tasks = new MessageTask[]{m1, m2, m3};

        for (MessageTask task : tasks){
            System.out.println(task);
        }

        // Problema 10

        StrategyTaskRunner strategyTaskRunner = new StrategyTaskRunner(Strategy.valueOf(args[0]));

        for (Task task : tasks){
            strategyTaskRunner.addTask(task);
        }

        strategyTaskRunner.executeAll();

        // Problema 13
        for (Task task : tasks){
            strategyTaskRunner.addTask(task);
        }

        PrinterTaskRunner printerTaskRunner = new PrinterTaskRunner(strategyTaskRunner);

        printerTaskRunner.executeAll();

        for (Task task : tasks){
            strategyTaskRunner.addTask(task);
        }

        PrinterTaskRunner printerTaskRunner1 = new PrinterTaskRunner(printerTaskRunner);

        printerTaskRunner1.executeAll();

        System.out.println("\n\n");

        // Laborator 2

        ArrayList<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(2);
        list.add(7);
        list.add(1);
        list.add(-3);
        list.add(12);
        list.add(6);
        list.add(9);

        ArrayList<Integer> copyList = new ArrayList<>(list);

        SortingTask sortingTask1 = new SortingTask("55", "st1", list, new BubbleSorter());
        SortingTask sortingTask2 = new SortingTask("56", "st2", copyList, new QuickSorter());

        sortingTask1.execute();
        System.out.println(list);

        sortingTask2.execute();
        System.out.println(copyList);
        System.out.println();

        StrategyTaskRunner strategyTaskRunner1 = new StrategyTaskRunner(Strategy.FIFO);

        for (Task task : tasks){
            strategyTaskRunner1.addTask(task);
        }

        strategyTaskRunner1.executeAll();

        // 7
        Container myContainer = TaskContainerFactory.getInstance().createContainer(Strategy.LIFO);

        // 14
        System.out.println();

        for (Task task : tasks){
            strategyTaskRunner1.addTask(task);
        }

        DelayTaskRunner delayTaskRunner = new DelayTaskRunner(strategyTaskRunner1);

        PrinterTaskRunner delayPrinterTaskRunner = new PrinterTaskRunner(delayTaskRunner);

        delayPrinterTaskRunner.executeAll();
    }
}