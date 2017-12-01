package com.lev.accprog.ui.view;

import com.lev.accprog.ui.core.Food;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Shell;

class ConfirmWindow {

    private final TablePanel mTablePanel;
    private final Shell mConfirmShell;
    private final BEHAVIOR mBehavior;
    private final Food mFood;

    enum BEHAVIOR {
        DELETE_ALL_LIKE,
        DELETE_SINGLE,
        REMOVE_GREATER
    }

    ConfirmWindow(final TablePanel tablePanel, BEHAVIOR behavior) {
        mConfirmShell = new Shell();
        mTablePanel = tablePanel;
        mBehavior = behavior;
        mFood = null;
    }

    ConfirmWindow(final TablePanel tablePanel, BEHAVIOR behavior, Food food) {
        mConfirmShell = new Shell();
        mTablePanel = tablePanel;
        mBehavior = behavior;
        mFood = food;
    }

    void open() {
        RowLayout layout = new RowLayout();
        layout.justify = true;
        layout.marginTop = 17;
        mConfirmShell.setLayout(layout);
        mConfirmShell.setText("Are you sure?");
        addOk(mConfirmShell);
        addCancel(mConfirmShell);
        mConfirmShell.setSize(300, 100);
        mConfirmShell.open();
    }

    private void addOk(Shell shell) {
        new CommonButton(shell, SWT.PUSH, "Ok", () -> {
            switch (mBehavior) {
                case DELETE_ALL_LIKE:
                    mTablePanel.deleteAllLike(mFood);
                case REMOVE_GREATER:
                    mTablePanel.removeGreater(mFood);
                case DELETE_SINGLE:
                    mTablePanel.delete();
                default:
                    shell.dispose();
            }
        });
    }

    private void addCancel(Shell shell) {
        new CommonButton(shell, SWT.PUSH, "Cancel", shell::close);
    }
}