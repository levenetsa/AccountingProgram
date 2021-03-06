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
    private ResourceBundle mMessages;

    TablePanel(Composite parent, int style, QueueController queueController, ResourceBundle mMessages) {
        super(parent, style);
        this.mMessages = mMessages;
        mFoods = new ArrayList<>();
        mQueueController = queueController;
        this.setLayout(new RowLayout(SWT.VERTICAL));
        createTable();
        Composite sortButtons = new Composite(this, SWT.NONE);
        sortButtons.setLayout(new RowLayout());
        addRadios(sortButtons);
        resetText(mMessages);
    }

    Button[] radios;
    Button button2;
    private void addRadios(Composite parent) {
        radios = new Button[4];
        button2 = new Button(parent, SWT.PUSH);
        button2.setLayoutData(rowData());
        button2.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                Comparator<Food> comparator=null;
                if (radios[0].getSelection()) {
                    comparator = Comparator.comparing(Food::getName);
                } else if (radios[1].getSelection()) {
                    comparator = Comparator.comparing(Food::getTaste);
                } else if (radios[2].getSelection()) {
                    comparator = Comparator.comparing(Food::getExpirationDate);
                } else if (radios[3].getSelection()) {
                    comparator = Comparator.comparing(Food::getCreated);
                }
                mFoods = mFoods.stream().sorted(comparator).collect(Collectors.toList());
                mTable.removeAll();
                addTableContents(mFoods);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        radios[0] = new Button(parent, SWT.RADIO);
        radios[1] = new Button(parent, SWT.RADIO);
        radios[1].setSelection(true);
        radios[2] = new Button(parent, SWT.RADIO);
        radios[3] = new Button(parent, SWT.RADIO);
    }

    public void resetText(ResourceBundle messages) {
        mMessages = messages;
        button2.setText("SORT BY");
        radios[0].setText(mMessages.getString("Name"));
        radios[1].setText(mMessages.getString("taste"));
        radios[2].setText(mMessages.getString("Date"));
        radios[3].setText(mMessages.getString("Create"));
        column1.setText(mMessages.getString("Name"));
        column2.setText(mMessages.getString("taste"));
        column3.setText(mMessages.getString("Date"));
        column4.setText(mMessages.getString("Create"));
    }

    TableColumn column2;
    TableColumn column1;
    TableColumn column3;
    TableColumn column4;

    private void createTable() {
        mTable = new Table(this, SWT.FULL_SELECTION | SWT.V_SCROLL);
        mTable.setHeaderVisible(true);
        mTable.setLinesVisible(true);
        RowData rowData = new RowData();
        rowData.height = 150;
        mTable.setLayoutData(rowData);
        column1 = createTableColumn(mTable, SWT.LEFT, mMessages.getString("Name"), 86);
        column2 = createTableColumn(mTable, SWT.CENTER, mMessages.getString("taste"), 85);
        column3 = createTableColumn(mTable, SWT.RIGHT, mMessages.getString("Date"), 100);
        column4 = createTableColumn(mTable,SWT.RIGHT,mMessages.getString("Create"),100);
        load();
    }

    private TableColumn createTableColumn
            (Table table, int style, String title, int width) {
        TableColumn tc = new TableColumn(table, style);
        tc.setText(title);
        tc.setResizable(true);
        tc.setWidth(width);
        return tc;
    }

    private void addTableContents(List<Food> queue) {

        mFoods = queue;
        mTable.removeAll();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        for (Food food : queue) {
            String[] row = new String[]{
                    food.getName(),
                    String.valueOf(food.getTaste()),
                    formatter.format(food.getExpirationDate().getTime()),
                    String.valueOf(food.getCreated())
            };
            TableItem ti = new TableItem(mTable, SWT.NONE);
            ti.setText(row);
        }
    }

    public void load(){
        mQueueController.handleCommand("import",null, (s, d) -> addTableContents(d));
    }

    void delete() {
        if (mTable.getSelectionIndex() == -1) return;
        mQueueController.handleCommand("remove", mFoods.get(mTable.getSelectionIndex()),
                (s, d) -> reset(d));
    }

    void deleteAllLike(Food food) {
        mQueueController.handleCommand("remove_all", food,
                (s, d) ->  reset(d));
    }

    void addIfMax(Food food){
        mQueueController.handleCommand("add_if_max", food, (s, d) ->  reset(d));
    }

    void add(Food food){
        mQueueController.handleCommand("add", food , (s, d) ->  reset(d));
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
        mQueueController.handleCommand("remove_greater", food,
                (s, d) ->  reset(d));
    }

}
