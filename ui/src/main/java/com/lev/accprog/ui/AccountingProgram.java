package com.lev.accprog.ui;

import com.lev.accprog.ui.controller.QueueController;
import com.lev.accprog.ui.view.AccountingWindow;

import java.util.Locale;

public class AccountingProgram {

    public static void main(String[] args) {
        QueueController queueController = new QueueController();
        new AccountingWindow(queueController).run(new Locale("ru", "RU"));
    }
}
