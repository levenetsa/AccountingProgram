package com.lev.accprog.ui.view;

import com.lev.accprog.ui.Food;
import com.lev.accprog.ui.controller.QueueController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class TablePanel extends Composite {

    private List<Food> mFoods;
    private Table mTable;
    private QueueController mQueueController;

    TablePanel(Composite parent, int style, QueueController queueController) {
        super(parent, style);
        mFoods = new ArrayList<>();
        mQueueController = queueController;
        this.setLayout(new RowLayout(SWT.VERTICAL));
        createTable();
        Composite sortButtons = new Composite(this, SWT.NONE);
        sortButtons.setLayout(new RowLayout());
        addRadios(sortButtons);
    }

    private void addRadios(Composite parent) {
        Button[] radios = new Button[3];
        Button button2 = new Button(parent, SWT.PUSH);
        button2.setText("SORT BY");
        button2.setLayoutData(rowData());
        button2.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                Comparator<Food> comparator;
                if (radios[0].getSelection()) {
                    comparator = Comparator.comparing(Food::getName);
                } else if (radios[1].getSelection()) {
                    comparator = Comparator.comparing(Food::getTaste);
                } else {
                    comparator = Comparator.comparing(Food::getExpirationDate);
                }
                mFoods = mFoods.stream().sorted(comparator).collect(Collectors.toList());
                mTable.removeAll();
                addTableContents(mFoods);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        radios[0] = new Button(parent, SWT.RADIO);
        radios[0].setText("Name");
        radios[1] = new Button(parent, SWT.RADIO);
        radios[1].setText("Taste");
        radios[1].setSelection(true);
        radios[2] = new Button(parent, SWT.RADIO);
        radios[2].setText("Date");
    }

    private void createTable() {
        mTable = new Table(this, SWT.FULL_SELECTION | SWT.V_SCROLL);
        mTable.setHeaderVisible(true);
        mTable.setLinesVisible(true);
        RowData rowData = new RowData();
        rowData.height = 150;
        mTable.setLayoutData(rowData);
        createTableColumn(mTable, SWT.LEFT, "Name", 86);
        createTableColumn(mTable, SWT.CENTER, "Taste", 85);
        createTableColumn(mTable, SWT.RIGHT, "Date", 100);
        load();
    }

    private void createTableColumn
            (Table table, int style, String title, int width) {
        TableColumn tc = new TableColumn(table, style);
        tc.setText(title);
        tc.setResizable(true);
        tc.setWidth(width);
    }

    private void addTableContents(List<Food> queue) {
        mFoods = queue;
        mTable.removeAll();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        for (Food food : queue) {
            String[] row = new String[]{
                    food.getName(),
                    String.valueOf(food.getTaste()),
                    formatter.format(food.getExpirationDate().getTime())
            };
            TableItem ti = new TableItem(mTable, SWT.NONE);
            ti.setText(row);
        }
    }

    public void load(){
        mQueueController.handleCommand("import", (s, d) -> addTableContents(d));
    }

    void delete() {
        if (mTable.getSelectionIndex() == -1) return;
        mQueueController.handleCommand("remove " + mFoods.get(mTable.getSelectionIndex()).toString(),
                (s, d) -> reset(d));
    }

    void deleteAllLike(Food food) {
        mQueueController.handleCommand("remove_all " + food.toString(),
                (s, d) ->  reset(d));
    }

    void addIfMax(Food food){
        mQueueController.handleCommand("add_if_max " + food.toString(), (s, d) ->  reset(d));
    }

    void add(Food food){
        mQueueController.handleCommand("add " + food.toString(), (s, d) ->  reset(d));
    }

    void filter(Predicate<Food> filter) {
        mFoods = mFoods.stream().filter(filter).collect(Collectors.toList());
        mTable.removeAll();
        addTableContents(mFoods);
    }

    void reset(List<Food> d) {
        mTable.removeAll();
        addTableContents(d);
    }

    private RowData rowData() {
        RowData setSizer = new RowData();
        setSizer.width = 100;
        setSizer.height = 20;
        return setSizer;
    }

    void removeGreater(Food food) {
        mQueueController.handleCommand("remove_greater " + food.toString(),
                (s, d) ->  reset(d));
    }
}
