package com.lev.accprog.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Shell;

class ConfirmWindow {

    private final Shell mConfirmShell;
    private final ConfirmCallback mCallback;

    ConfirmWindow(ConfirmCallback callback) {
        mConfirmShell = new Shell();
        mCallback = callback;
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
            mCallback.onConfirm(null, null);
            shell.dispose();
        });
    }

    private void addCancel(Shell shell) {
        new CommonButton(shell, SWT.PUSH, "Cancel", shell::close);
    }
}