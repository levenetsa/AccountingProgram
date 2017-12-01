package com.lev.accprog.ui.view;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

class CommonButton{
    CommonButton(Composite parent, int flags, String text, ClickListener clickListener) {
        Button button = new Button(parent, flags);
        RowData layout = new RowData();
        layout.width = 100;
        layout.height = 20;
        button.setLayoutData(layout);
        button.setText(text);
        button.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                clickListener.onClick();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });
    }

    public interface ClickListener{
        void onClick();
    }
}
