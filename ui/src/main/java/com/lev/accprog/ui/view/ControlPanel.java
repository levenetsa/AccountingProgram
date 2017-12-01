package com.lev.accprog.ui.view;

import com.lev.accprog.ui.core.Food;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Predicate;


class ControlPanel extends Composite {

    private Text mName;
    private Text mDate;
    private Text mTaste;
    private TablePanel mTablePanel;

    ControlPanel(Composite parent, int style, TablePanel tablePanel) {
        super(parent, style);
        mTablePanel = tablePanel;
        this.setLayout(new RowLayout(SWT.VERTICAL));
        addTextFields();
        addButtons();
    }

    private void addTextFields() {
        Composite parent = new Composite(this, SWT.NONE);
        parent.setLayout(new RowLayout());
        mName = new Text(parent, SWT.BORDER);
        mName.setLayoutData(new RowData(90, 20));
        mTaste = new Text(parent, SWT.BORDER);
        mTaste.setLayoutData(new RowData(90, 20));
        mDate = new Text(parent, SWT.BORDER);
        mDate.setLayoutData(new RowData(90, 20));
    }


    private void addButtons() {
        Composite parent = new Composite(this, SWT.NONE);
        parent.setLayout(new RowLayout());
        Composite createButtons = new Composite(parent, SWT.NONE);
        createButtons.setLayout(new RowLayout(SWT.VERTICAL));
        addCreateButton(createButtons);
        addIfMaxButton(createButtons);
        Composite removeButtons = new Composite(parent, SWT.NONE);
        removeButtons.setLayout(new RowLayout(SWT.VERTICAL));
        addRemoveButton(removeButtons);
        removeAllLikeButton(removeButtons);
        removeGreaterButton(removeButtons);
        addFilters(parent);
    }

    private void addRemoveButton(Composite parent) {
        new CommonButton(parent, SWT.PUSH, "REMOVE", () ->
                new ConfirmWindow(mTablePanel, ConfirmWindow.BEHAVIOR.DELETE_SINGLE).open());
    }

    private void addFilters(Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new RowLayout(SWT.VERTICAL));
        Button[] filters = new Button[3];
        new CommonButton(panel, SWT.PUSH, "Filter By:", () -> {
            try {
                Predicate<Food> filter = null;
                if (filters[0].getSelection()) {
                    filter = x -> x.getName().toLowerCase().contains(mName.getText().toLowerCase());
                }
                if (filters[1].getSelection()) {
                    filter = x -> x.getTaste().toString().contains(mTaste.getText().toUpperCase());
                }
                if (filters[2].getSelection()) {
                    final String dateText = mDate.getText();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date;
                    try {
                        date = sdf.parse(dateText);
                    } catch (ParseException exception) {
                        new InfoWindow(exception).open();
                        return;
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    GregorianCalendar dataParsed = new GregorianCalendar(cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH));
                    filter = x -> x.getExpirationDate().equals(dataParsed);
                }
                if (filter != null) {
                    mTablePanel.filter(filter);
                }
            } catch (Exception exception) {
                new InfoWindow(exception).open();
            }
        });
        filters[0] = new Button(panel, SWT.CHECK);
        filters[0].setText("Name");
        filters[1] = new Button(panel, SWT.CHECK);
        filters[1].setText("Taste");
        filters[1].setSelection(true);
        filters[2] = new Button(panel, SWT.CHECK);
        filters[2].setText("Date");
        new CommonButton(panel, SWT.PUSH, "CLEAR FILTER", () -> mTablePanel.reset());
    }

    private void addIfMaxButton(Composite parent) {
        new CommonButton(parent, SWT.PUSH, "ADD IF MAX", () -> {
            Food food;
            try {
                food = getFood();
            } catch (Exception exception) {
                new InfoWindow(exception).open();
                return;
            }
            mTablePanel.getQueueHolder().addIfMax(food);
            mTablePanel.reset();
        });
    }

    private void addCreateButton(Composite parent) {
        new CommonButton(parent, SWT.PUSH, "ADD", () -> {
            Food food;
            try {
                food = getFood();
            } catch (Exception ex) {
                new InfoWindow(ex).open();
                return;
            }
            mTablePanel.getQueueHolder().add(food);
            mTablePanel.reset();
        });
    }

    private void removeAllLikeButton(Composite parent) {
        new CommonButton(parent, SWT.PUSH, "REMOVE ALL", () -> {
            try {
                new ConfirmWindow(mTablePanel, ConfirmWindow.BEHAVIOR.DELETE_ALL_LIKE, getFood());
            } catch (Exception ex) {
                new InfoWindow(ex).open();
            }
        });
    }

    private void removeGreaterButton(Composite parent) {
        new CommonButton(parent, SWT.PUSH, "REM.GREATER", () -> {
            try {
                new ConfirmWindow(mTablePanel, ConfirmWindow.BEHAVIOR.REMOVE_GREATER, getFood()).open();
            } catch (Exception ex) {
                new InfoWindow(ex).open();
            }
        });
    }

    private Food getFood() throws ParseException {
        Food food = new Food();
        food.setTaste(Food.TASTE.valueOf(mTaste.getText()));
        food.setDate(mDate.getText());
        food.setName(mName.getText());
        return food;
    }
}
