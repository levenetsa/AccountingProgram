package com.lev.accprog.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

class InfoWindow {

    private final Shell mShell;

    /** PROBABLY USEFULL FOR SHELLS
     *while (!mErrorShell.isDisposed()) {
     *  if (!mDisplay.readAndDispatch())
     *      mDisplay.sleep();
     *}
     * */

    InfoWindow(final Exception e) {
        mShell = new Shell();
        RowLayout layout = new RowLayout();
        layout.justify = true;
        layout.marginTop = 17;
        mShell.setLayout(layout);
        mShell.setText("Error");
        new Text(mShell, SWT.READ_ONLY).setText(e.getMessage());
        mShell.setSize(500, 100);
    }

    InfoWindow(String title, String content){
        mShell = new Shell();
        RowLayout layout = new RowLayout();
        layout.justify = true;
        layout.marginTop = 7;
        mShell.setLayout(layout);
        mShell.setText(title);
        Text text = new Text(mShell, SWT.READ_ONLY);
        text.setText(content);
        text.setLayoutData(new RowData(500,50));
        new CommonButton(mShell, SWT.PUSH,"Cancel", mShell::close);
    }

    void open() {
        mShell.open();
    }
}

