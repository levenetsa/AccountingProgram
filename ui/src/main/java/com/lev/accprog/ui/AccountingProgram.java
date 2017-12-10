package com.lev.accprog.ui;

import com.lev.accprog.ui.controller.QueueController;
import com.lev.accprog.ui.view.AccountingWindow;

public class AccountingProgram {

    private static final String HELP = "You should pass file name as argument for correct program execution!";

    public static void main(String[] args) {
        QueueController queueController = new QueueController();
        new AccountingWindow(queueController).run();
    }
}
