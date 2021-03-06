package com.lev.accprog.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Shell;

import java.util.ResourceBundle;

import static org.eclipse.swt.SWT.APPLICATION_MODAL;
import static org.eclipse.swt.SWT.BORDER;
import static org.eclipse.swt.SWT.CLOSE;

class ConfirmWindow {

    private Shell mConfirmShell;
    private final ConfirmCallback mCallback;
    private ResourceBundle mMessages;

    ConfirmWindow(ConfirmCallback callback, ResourceBundle properties) {
        mMessages = properties;
        mConfirmShell = new Shell(APPLICATION_MODAL | BORDER | CLOSE);
        mCallback = callback;
    }

    void open() {
        RowLayout layout = new RowLayout();
        layout.justify = true;
        layout.marginTop = 17;
        if (mConfirmShell.isDisposed()){
            mConfirmShell = new Shell();
        }
        mConfirmShell.setLayout(layout);
        mConfirmShell.setText(mMessages.getString("Are_you_sure?"));
        addOk(mConfirmShell);
        addCancel(mConfirmShell);
        mConfirmShell.setSize(300, 100);
        mConfirmShell.open();
    }

    private void addOk(Shell shell) {
        new CommonButton(shell, SWT.PUSH, "ok", mMessages, () -> {
            mCallback.onConfirm(null, null);
            shell.dispose();
        });
    }

    private void addCancel(Shell shell) {
        new CommonButton(shell, SWT.PUSH, "Cancel", mMessages, shell::close);
    }
}