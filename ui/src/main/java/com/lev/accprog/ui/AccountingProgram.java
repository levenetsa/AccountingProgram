package com.lev.accprog.ui;

import com.lev.accprog.ui.core.Command;
import com.lev.accprog.ui.core.QueueHolder;
import com.lev.accprog.ui.view.AccountingWindow;

import java.util.PriorityQueue;

public class AccountingProgram {

    private static final boolean CONSOLE_MODE = false;
    private static final String HELP = "You should pass file name as argument for correct program execution!";


    public static void main(String[] args) {
        QueueHolder queueHolder = new QueueHolder(new PriorityQueue<>());
        if (args.length == 0){
            System.out.println("Please print file name!");
            show(HELP);
            return;
        }

        queueHolder.handleCommand(new Command("import " + args[0]));

        queueHolder.initShutdownHook();

        if (CONSOLE_MODE){
            queueHolder.initListening();
        } else {
            new AccountingWindow(queueHolder).run();
        }
    }

    private static void show(String str) {
        System.out.println(str);
    }
}
