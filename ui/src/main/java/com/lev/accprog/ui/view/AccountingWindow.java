package com.lev.accprog.ui.view;

import com.lev.accprog.ui.controller.QueueController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class AccountingWindow {

    private Display mDisplay = new Display();
    private QueueController mQueueController;
    private TablePanel mTablePanel;
    private Shell mMainShell = new Shell(mDisplay);
    public static ResourceBundle mMessages;
    private ControlPanel mControlPanel;

    public AccountingWindow(QueueController queueController) {
        mQueueController = queueController;
    }

    public void run(Locale locale) {
        RowLayout layout = new RowLayout();
        mMessages = ResourceBundle.getBundle("messages",locale);
        mMainShell.setText(mMessages.getString("main_label"));
        mMainShell.setLayout(layout);
        mTablePanel = new TablePanel(mMainShell, SWT.NONE, mQueueController);
        mControlPanel = new ControlPanel(mMainShell, SWT.NONE, mTablePanel);
        setUpMenuBar();
        mMainShell.addListener(SWT.Close, event -> System.exit(0));
        mMainShell.open();
        mMainShell.setSize(900, 300);
        while (!mMainShell.isDisposed()) {
            if (!mDisplay.readAndDispatch())
                mDisplay.sleep();
        }
    }

    private void setUpMenuBar() {
        Menu menuBar = new Menu(mMainShell, SWT.BAR);
        MenuItem menuHeader = new MenuItem(menuBar, SWT.CASCADE);
        menuHeader.setText(mMessages.getString("file"));
        Menu fileMenu = new Menu(mMainShell, SWT.DROP_DOWN);
        menuHeader.setMenu(fileMenu);
        MenuItem fileHelpItem = new MenuItem(fileMenu, SWT.PUSH);
        fileHelpItem.setText(mMessages.getString("help"));
        fileHelpItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                new InfoWindow("HELP",
                        "Tutorial how to use my programm ...PLS WRITE CORRECT 'DATA YYYY-MM-DD'").open();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        MenuItem ru = new MenuItem(fileMenu, SWT.PUSH);
        ru.setText("Russian");
        ru.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
               run(new Locale("ru", "RU"));
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        MenuItem tu = new MenuItem(fileMenu, SWT.PUSH);
        tu.setText("Turkish");
        tu.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                run(new Locale("tu", "TU"));
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        MenuItem ke = new MenuItem(fileMenu, SWT.PUSH);
        ke.setText("Vengerian");
        ke.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                run(new Locale("vn", "VN"));
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        }); MenuItem le = new MenuItem(fileMenu, SWT.PUSH);
        le.setText("English(Indian)");
        le.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                run(new Locale("en", "IN"));
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        MenuItem info = new MenuItem(fileMenu, SWT.PUSH);
        info.setText(mMessages.getString("info"));
        info.addSelectionListener(new SelectionListener() {
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

