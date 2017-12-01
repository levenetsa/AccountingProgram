package com.lev.accprog.ui;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.eclipse.swt.SWT.*;

class AccountingView {

    private final String LABEL_MAIN = "Collection program";

    private Display mDisplay = new Display();
    private QueueHolder mQueueHolder;
    private Table mTable;
    private Shell mMainShell;
    private Shell mErrorShell;
    private Shell mConfirmShell;
    private Shell mConfirmAllShell;
    private List<Food> mFoods;
    private Text mName;
    private Text mDate;
    private Text mTaste;
    private int mButtonWidth = 50;
    private int mButtonHeight = 50;

    AccountingView(QueueHolder queueHolder) {
        mQueueHolder = queueHolder;
    }

    private void initShells() {
        mMainShell = new Shell(mDisplay);
        mErrorShell = new Shell(mDisplay);
        mConfirmShell = new Shell(mDisplay);
        mConfirmAllShell = new Shell(mDisplay);
    }

    void initView() {
        initShells();
        initMainShell();
    }

    private void initMainShell() {
        RowLayout layout = new RowLayout();
        mMainShell.setText(LABEL_MAIN);
        mMainShell.setLayout(layout);
        Composite left = new Composite(mMainShell, SWT.NONE);
        left.setLayout(new RowLayout(SWT.VERTICAL));
        createTable(left);
        Composite sortButtons = new Composite(left, SWT.NONE);
        sortButtons.setLayout(new RowLayout());
        addRadios(sortButtons);
        Composite right = new Composite(mMainShell, SWT.NONE);
        right.setLayout(new RowLayout(SWT.VERTICAL));
        Composite fields = new Composite(right, SWT.NONE);
        fields.setLayout(new RowLayout());
        mName = new Text(fields, SWT.BORDER);
        mName.setLayoutData(rowData(90));
        mTaste = new Text(fields, SWT.BORDER);
        mTaste.setLayoutData(rowData(90));
        mDate = new Text(fields, SWT.BORDER);
        mDate.setLayoutData(rowData(90));
        Composite buttons = new Composite(right, SWT.NONE);
        buttons.setLayout(new RowLayout());
        Composite addButtons = new Composite(buttons, SWT.NONE);
        addButtons.setLayout(new RowLayout(SWT.VERTICAL));
        addCurrentButton(addButtons);
        addIfMaxButton(addButtons);
        Composite removeButtons = new Composite(buttons, SWT.NONE);
        removeButtons.setLayout(new RowLayout(SWT.VERTICAL));
        addRemoveButton(removeButtons);
        removeAllButton(removeButtons);
        removeGreaterButton(removeButtons);
        menuBar();
        Composite filters = new Composite(buttons, SWT.NONE);
        filters.setLayout(new RowLayout(SWT.VERTICAL));
        addFilters(filters);
        mMainShell.addListener(SWT.Close, event -> System.exit(0));
        mMainShell.open();
        mMainShell.setSize(670, 300);
        while (!mMainShell.isDisposed()) {
            if (!mDisplay.readAndDispatch())
                mDisplay.sleep();
        }
        mDisplay.dispose();
    }

    private RowData rowData() {
        RowData setSizer = new RowData();
        setSizer.width = 100;
        setSizer.height = 25;
        return setSizer;
    }

    private RowData rowData(int width) {
        RowData setSizer = new RowData();
        setSizer.width = width;
        setSizer.height = 20;
        return setSizer;
    }

    private RowData rowData(int width, int height) {
        RowData setSizer = new RowData();
        setSizer.width = width;
        setSizer.height = height;
        return setSizer;
    }

    private void addFilters(Composite parent) {
        Button radioButton = new Button(parent, SWT.CHECK);
        radioButton.setText("Name");
        Button radioButton1 = new Button(parent, SWT.CHECK);
        radioButton1.setText("Taste");
        radioButton1.setSelection(true);
        Button radioButton2 = new Button(parent, SWT.CHECK);
        radioButton2.setText("Date");
        Button[] filters = new Button[]{radioButton, radioButton1, radioButton2};
        Button button2 = new Button(parent, SWT.PUSH);
        button2.setText("Filter By:");
        button2.setLayoutData(rowData());
        button2.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    if (filters[0].getSelection() || filters[1].getSelection() || filters[2].getSelection()) {
                        if (filters[0].getSelection()) {
                            final String name = mName.getText().toLowerCase();
                            mFoods = mFoods.stream().filter(x -> x.getName().toLowerCase().contains(name)).collect(Collectors.toList());
                            System.out.println("Pressed");
                        }
                        if (filters[1].getSelection()) {
                            final String taste = mTaste.getText().toUpperCase();
                            mFoods = mFoods.stream().filter(
                                    x -> x.getTaste().toString().contains(taste)).collect(Collectors.toList());
                            System.out.println("Pressed");
                        }
                        if (filters[2].getSelection()) {
                            final String dateText = mDate.getText();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date;
                            try {
                                date = sdf.parse(dateText);
                            } catch (ParseException exception) {
                                shellOpenerError(exception);
                                return;
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            GregorianCalendar dataParsed = new GregorianCalendar(cal.get(Calendar.YEAR),
                                    cal.get(Calendar.MONTH),
                                    cal.get(Calendar.DAY_OF_MONTH));
                            mFoods = mFoods.stream().filter(x -> x.getExpirationDate().equals(dataParsed)).collect(Collectors.toList());
                            System.out.println("Pressed");
                        }
                        mTable.removeAll();
                        addTableContents(mFoods);
                    } else {
                        System.out.println("Pressed");
                    }
                } catch (Exception exception) {
                    shellOpenerError(exception);
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        Button button3 = new Button(parent, SWT.PUSH);
        button3.setLayoutData(rowData());
        button3.setText("CLEAR FILTER");
        button3.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                mTable.removeAll();
                addTableContents(mQueueHolder.getQueue());
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void createTable(Composite parent) {
        mTable = new Table(parent, SWT.FULL_SELECTION | SWT.V_SCROLL);
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

    private void addRemoveButton(Composite parent) {
        Button button1 = new Button(parent, SWT.PUSH);
        button1.setLayoutData(rowData());
        System.out.println("getSize: " + button1.getSize());
        button1.setText("REMOVE");
        button1.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                shellOpener();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void addRadios(Composite parent) {
        Button radioButton = new Button(parent, SWT.RADIO);
        radioButton.setText("Name");
        Button radioButton1 = new Button(parent, SWT.RADIO);
        radioButton1.setText("Taste");
        radioButton1.setSelection(true);///!!!vazhno
        Button radioButton2 = new Button(parent, SWT.RADIO);
        radioButton2.setText("Date");
        Button[] radios = new Button[]{radioButton, radioButton1, radioButton2};
        Button button2 = new Button(parent, SWT.PUSH);
        button2.setText("SORT BY");
        button2.setLayoutData(rowData());
        button2.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                if (radios[0].getSelection()) {
                    mFoods = mFoods.stream().sorted(Comparator.comparing(Food::getName)).collect(Collectors.toList());
                    mTable.removeAll();
                    addTableContents(mFoods);
                    System.out.println("Pressed");
                    return;
                }
                if (radios[1].getSelection()) {
                    mFoods = mFoods.stream().sorted(Comparator.comparing(Food::getTaste)).collect(Collectors.toList());
                    mTable.removeAll();
                    addTableContents(mFoods);
                    System.out.println("Pressed");
                    return;
                }
                mFoods = mFoods.stream().sorted(Comparator.comparing(Food::getExpirationDate)).collect(Collectors.toList());
                mTable.removeAll();
                addTableContents(mFoods);
                System.out.println("Pressed");

            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void addIfMaxButton(Composite parent) {
        Button button = new Button(parent, SWT.PUSH);
        button.setLayoutData(rowData());
        System.out.println("getSize: " + button.getSize());
        button.setText("ADD IF MAX");
        button.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                Food food = new Food();
                try {
                    food = getFood();
                } catch (Exception exception) {
                    shellOpenerError(exception);
                    return;
                }
                mQueueHolder.addIfMax(food);
                mTable.removeAll();
                addTableContents(mQueueHolder.getQueue());
                System.out.println("Pressed");
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

    }

    private void addCurrentButton(Composite parent) {

        Button button = new Button(parent, SWT.PUSH);
        button.setLayoutData(rowData());
        button.setText("ADD");
        button.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                Food food = new Food();
                try {
                    food = getFood();
                } catch (Exception exception) {
                    shellOpenerError(exception);
                    return;
                }
                mQueueHolder.getQueue().add(food);
                mTable.removeAll();
                addTableContents(mQueueHolder.getQueue());
                System.out.println("Pressed");
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    /*
     * Удаление элементов главная кнопка на основной панеле mMainShell в ней 2 кнопки
     * которые будут указывать каким способом удалить
     */
    private void removeAllButton(Composite parent) {
        Button button2 = new Button(parent, SWT.PUSH);
        button2.setLayoutData(rowData());
        button2.setText("REMOVE ALL");
        button2.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                shellOpenerForAll();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void removeGreaterButton(Composite parent) {
        Button button2 = new Button(parent, SWT.PUSH);
        button2.setLayoutData(rowData());
        button2.setText("REM.GREATER");
        button2.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                shellOpenerForGreater();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void shellOpener() {
        RowLayout layout = new RowLayout();
        layout.justify = true;
        layout.marginTop = 17;
        //////////////
        if (mConfirmShell.isDisposed()) {
            mConfirmShell = new Shell();
        }
        ///////////////////
        mConfirmShell.setLayout(layout);
        mConfirmShell.setText("Are you sure?");
        addOk(mConfirmShell);
        addCancel(mConfirmShell);
        mConfirmShell.setSize(300, 100);
        mConfirmShell.open();

        while (!mConfirmShell.isDisposed()) {
            if (!mDisplay.readAndDispatch())
                mDisplay.sleep();
        }
    }

    private void addOk(Shell shell) {
        Button b = new Button(shell, SWT.PUSH);
        b.setLayoutData(rowData());
        b.setText("Ok");
        b.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                if (mTable.getSelectionIndex() == -1) return;
                mQueueHolder.getQueue().remove(mFoods.get(mTable.getSelectionIndex()));
                mTable.removeAll();
                addTableContents(mQueueHolder.getQueue());
                System.out.println("Pressed");
                shell.dispose();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void addCancel(Shell shell) {
        Button button1 = new Button(shell, SWT.PUSH);
        button1.setLayoutData(rowData());
        button1.setText("Cancel");
        button1.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void shellOpenerForAll() {
        RowLayout layout = new RowLayout();
        layout.justify = true;
        layout.marginTop = 17;
        if (mConfirmAllShell.isDisposed())
            mConfirmAllShell = new Shell();
        mConfirmAllShell.setLayout(layout);
        mConfirmAllShell.setText("Delete all?");
        addOkAll(mConfirmAllShell);
        addCancelAll(mConfirmAllShell);
        mConfirmAllShell.setSize(300, 100);
        mConfirmAllShell.open();

        while (!mConfirmAllShell.isDisposed()) {
            if (!mDisplay.readAndDispatch())
                mDisplay.sleep();
        }

    }

    private void addOkAll(Shell shell) {
        Button b = new Button(shell, SWT.PUSH);
        b.setLayoutData(rowData());
        b.setText("Ok");
        b.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                Food food = new Food();
                try {
                    food = getFood();
                } catch (Exception exception) {
                    shellOpenerError(exception);
                    return;
                }
                mQueueHolder.removeAll(food);
                mTable.removeAll();
                addTableContents(mQueueHolder.getQueue());
                System.out.println("Pressed");
                shell.dispose();

            }


            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void addCancelAll(Shell shell) {
        Button button1 = new Button(shell, SWT.PUSH);
        button1.setLayoutData(rowData());
        button1.setText("Cancel");
        button1.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void shellOpenerForGreater() {
        RowLayout layout = new RowLayout();
        layout.justify = true;
        layout.marginTop = 17;
        if (mConfirmAllShell.isDisposed())
            mConfirmAllShell = new Shell();
        mConfirmAllShell.setLayout(layout);
        mConfirmAllShell.setText("Delete Greater?");
        addOkGreater(mConfirmAllShell);
        addCancelGreater(mConfirmAllShell);
        mConfirmAllShell.setSize(300, 100);
        mConfirmAllShell.open();

        while (!mConfirmAllShell.isDisposed()) {
            if (!mDisplay.readAndDispatch())
                mDisplay.sleep();
        }

    }

    private void addOkGreater(Shell shell) {
        Button b = new Button(shell, SWT.PUSH);
        b.setLayoutData(rowData());
        b.setText("Ok");
        b.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                Food food = new Food();
                try {
                    food = getFood();
                } catch (Exception exception) {
                    shellOpenerError(exception);
                    return;
                }
                mQueueHolder.removeGreater(food);
                mTable.removeAll();
                addTableContents(mQueueHolder.getQueue());
                System.out.println("Pressed");
                shell.dispose();

            }


            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void addCancelGreater(Shell shell) {
        Button button1 = new Button(shell, SWT.PUSH);
        button1.setLayoutData(rowData());
        button1.setText("Cancel");
        button1.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void shellOpenerError(Exception e) {
        RowLayout layout = new RowLayout();
        layout.justify = true;
        layout.marginTop = 17;
        if (mErrorShell.isDisposed())
            mErrorShell = new Shell();
        mErrorShell.setLayout(layout);
        mErrorShell.setText("Error");
        new Text(mErrorShell, SWT.READ_ONLY).setText(e.getMessage());
        mErrorShell.setSize(300, 100);
        mErrorShell.open();

        while (!mErrorShell.isDisposed()) {
            if (!mDisplay.readAndDispatch())
                mDisplay.sleep();
        }

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

    public Food getFood() throws InvalidArgumentException {
        Food food = new Food();
        food.setTaste(Food.TASTE.valueOf(mTaste.getText()));
        food.setDate(mDate.getText());
        food.setName(mName.getText());
        return food;
    }

    private void menuBar() {
        Menu menuBar = new Menu(mMainShell, SWT.BAR);
        MenuItem fileMenuheader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuheader.setText("File");
        Menu fileMenu = new Menu(mMainShell, SWT.DROP_DOWN);
        fileMenuheader.setMenu(fileMenu);
        MenuItem fileHelpItem = new MenuItem(fileMenu, SWT.PUSH);
        fileHelpItem.setText("Help");
        fileHelpItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {

                shellOpenerForHelper();

            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        MenuItem fileInfoItem = new MenuItem(fileMenu, SWT.PUSH);
        fileInfoItem.setText("Info");
        fileInfoItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {

                shellOpenerForInformer();

            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        mMainShell.setMenuBar(menuBar);
    }

    private void shellOpenerForHelper() {
        RowLayout layout = new RowLayout();
        layout.justify = true;
        layout.marginTop = 7;
        int SHELL_TRIM=CLOSE | TITLE | MIN | MAX |RESIZE;
        if (mConfirmAllShell.isDisposed())
            mConfirmAllShell = new Shell(mDisplay,SHELL_TRIM);
        mConfirmAllShell.setLayout(layout);
        mConfirmAllShell.setText("HELP");
        Text text=new Text(mConfirmAllShell,SWT.READ_ONLY);
        String Help="Tutorial how to use my programm ...PLS WRITE CORRECT 'DATA YYYY-MM-DD'";
        text.setText(mQueueHolder.show(Help));
        RowData rowData=new RowData();
        rowData.height=50;
        rowData.width=500;
        Button button1 = new Button(mConfirmAllShell, SWT.PUSH);
        button1.setText("Cancel");
        mConfirmAllShell.setSize(550, 120);
        mConfirmAllShell.open();
        //public static final
        button1.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                mConfirmAllShell.close();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        while (!mConfirmAllShell.isDisposed()) {
            if (!mDisplay.readAndDispatch())
                mDisplay.sleep();
        }

    }

    private void shellOpenerForInformer() {
        RowLayout layout = new RowLayout();
        layout.justify = true;
        layout.marginTop = 17;
        if (mConfirmAllShell.isDisposed())
            mConfirmAllShell = new Shell();
        mConfirmAllShell.setLayout(layout);
        mConfirmAllShell.setText("Delete Greater?");
        Text text=new Text(mConfirmAllShell,SWT.READ_ONLY);
        text.setText(mQueueHolder.printInfo());
        RowData rowData=new RowData();
        rowData.height=50;
        rowData.width=500;
        text.setLayoutData(rowData);
        Button button1 = new Button(mConfirmAllShell, SWT.PUSH);
        button1.setText("Cancel");
        mConfirmAllShell.setSize(400, 160);
        mConfirmAllShell.open();
        button1.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                mConfirmAllShell.close();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        while (!mConfirmAllShell.isDisposed()) {
            if (!mDisplay.readAndDispatch())
                mDisplay.sleep();
        }

    }
}

