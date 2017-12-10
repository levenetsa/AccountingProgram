package com.lev.accprog.ui.view;

import com.lev.accprog.ui.Food;

import java.util.List;

public interface ConfirmCallback {

    void onConfirm(String info, List<Food> newData);
}
