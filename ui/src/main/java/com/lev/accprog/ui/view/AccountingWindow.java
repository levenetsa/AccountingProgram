package com.lev.accprog.ui.view;

import com.lev.accprog.ui.controller.QueueController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

public class AccountingWindow {

    private final String LABEL_MAIN = "Collection program";

    private Display mDisplay = new Display();
    private QueueController mQueueController;
    private TablePanel mTablePanel;
    private Shell mMainShell = new Shell(mDisplay);
    private ControlPanel mControlPanel;

    public AccountingWindow(QueueController queueController) {
        mQueueController = queueController;
    }

    public void run() {
        RowLayout layout = new RowLayout();
        mMainShell.setText(LABEL_MAIN);
        mMainShell.setLayout(layout);
        mTablePanel = new TablePanel(mMainShell, SWT.NONE, mQueueController);
        mControlPanel = new ControlPanel(mMainShell, SWT.NONE, mTablePanel);
        setUpMenuBar();
        mMainShell.addListener(SWT.Close, event -> System.exit(0));
        mMainShell.open();
        mMainShell.setSize(670, 300);
        while (!mMainShell.isDisposed()) {
            if (!mDisplay.readAndDispatch())
                mDisplay.sleep();
        }
        //mDisplay.dispose();
    }


    private void setUpMenuBar() {
        Menu menuBar = new Menu(mMainShell, SWT.BAR);
        MenuItem menuHeader = new MenuItem(menuBar, SWT.CASCADE);
        menuHeader.setText("File");
        Menu fileMenu = new Menu(mMainShell, SWT.DROP_DOWN);
        menuHeader.setMenu(fileMenu);
        MenuItem fileHelpItem = new MenuItem(fileMenu, SWT.PUSH);
        fileHelpItem.setText("Help");
        fileHelpItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                new InfoWindow("HELP",
                        "Tutorial how to use my programm ...PLS WRITE CORRECT 'DATA YYYY-MM-DD'").open();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        MenuItem fileInfoItem = new MenuItem(fileMenu, SWT.PUSH);
        fileInfoItem.setText("Info");
        fileInfoItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                mQueueController.handleCommand("info", null, (info, newData)
                        -> new InfoWindow("Information", info).open());
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        mMainShell.setMenuBar(menuBar);
    }

}

