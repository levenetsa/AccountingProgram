package com.lev.accprog.ui.view;

import com.lev.accprog.ui.core.Food;
import com.lev.accprog.ui.core.QueueHolder;
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
    private QueueHolder mQueueHolder;

    TablePanel(Composite parent, int style, QueueHolder queueHolder) {
        super(parent, style);
        mFoods = new ArrayList<>();
        mQueueHolder = queueHolder;
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
        addTableContents(mQueueHolder.getQueue());
    }

    private void createTableColumn
            (Table table, int style, String title, int width) {
        TableColumn tc = new TableColumn(table, style);
        tc.setText(title);
        tc.setResizable(true);
        tc.setWidth(width);
    }

    private void addTableContents(PriorityQueue<Food> queue) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        mFoods = new ArrayList<>();
        for (Food food : queue) {
            mFoods.add(food);
            String[] row = new String[]{
                    food.getName(),
                    String.valueOf(food.getTaste()),
                    formatter.format(food.getExpirationDate().getTime())
            };
            TableItem ti = new TableItem(mTable, SWT.NONE);
            ti.setText(row);
        }
    }

    private void addTableContents(List<Food> queue) {
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

    void delete() {
        if (mTable.getSelectionIndex() == -1) return;
        mQueueHolder.getQueue().remove(mFoods.get(mTable.getSelectionIndex()));
        reset();
    }

    void deleteAllLike(Food food){
        mQueueHolder.removeAll(food);
        reset();
    }

    void filter(Predicate<Food> filter) {
        mFoods = mFoods.stream().filter(filter).collect(Collectors.toList());
        mTable.removeAll();
        addTableContents(mFoods);
    }

    void reset() {
        mTable.removeAll();
        addTableContents(mQueueHolder.getQueue());
    }

    private RowData rowData() {
        RowData setSizer = new RowData();
        setSizer.width = 100;
        setSizer.height = 20;
        return setSizer;
    }

    QueueHolder getQueueHolder() {
        return mQueueHolder;
    }

    void removeGreater(Food food) {
        mQueueHolder.removeGreater(food);
        reset();
    }
}
