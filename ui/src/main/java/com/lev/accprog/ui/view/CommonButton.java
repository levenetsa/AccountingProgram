package com.lev.accprog.ui.view;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import java.util.ResourceBundle;

class CommonButton{
    ResourceBundle mMessages;
    String mText;
    Button mButton;
    CommonButton(Composite parent, int flags, String text, ResourceBundle mMessages, ClickListener clickListener) {
        this.mMessages = mMessages;
        mText = text;
         mButton = new Button(parent, flags);
        RowData layout = new RowData();
        layout.width = 100;
        layout.height = 20;
        mButton.setLayoutData(layout);
        mButton.setText(mMessages.getString(mText));
        mButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                clickListener.onClick();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });
    }

    public void resetText(ResourceBundle mMessages) {
        mButton.setText(mMessages.getString(mText));
    }

    public interface ClickListener{
        void onClick();
    }
}
