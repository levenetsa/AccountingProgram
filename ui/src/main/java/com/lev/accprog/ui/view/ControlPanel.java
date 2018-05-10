
        package com.lev.accprog.ui.view;

        import com.lev.accprog.ui.Food;
        import org.eclipse.swt.SWT;
        import org.eclipse.swt.layout.RowData;
        import org.eclipse.swt.layout.RowLayout;
        import org.eclipse.swt.widgets.*;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.*;
        import java.util.function.Predicate;


class ControlPanel extends Composite {

    private Text mName;
    private Text mDate;
    private Combo mTaste;
    private Text mCreated;
    private TablePanel mTablePanel;
    private Shell mParent;
    private ResourceBundle mMessages;
    private String Items[]={"SWEET","SPICY","SALTY"};

    ControlPanel(Shell parent, int style, TablePanel tablePanel, ResourceBundle mMessages) {
        super(parent, style);
        mParent = parent;
        this.mMessages = mMessages;
        mTablePanel = tablePanel;
        this.setLayout(new RowLayout(SWT.VERTICAL));
        addTextFields();
        addButtons();
        resetText(mMessages);
    }

    private void addTextFields() {
        Composite parent = new Composite(this, SWT.NONE);
        parent.setLayout(new RowLayout());
        mName = new Text(parent, SWT.BORDER);
        mName.setText("apple");
        mName.setLayoutData(new RowData(90, 20));
        mTaste = new Combo(parent, SWT.BORDER);
        mTaste.setItems(Items);
        mTaste.select(1);
        mTaste.setLayoutData(new RowData(90, 20));
        mDate = new Text(parent, SWT.BORDER);
        mDate.setText("1911-11-11");
        mDate.setLayoutData(new RowData(90, 20));
        mCreated = new Text(parent,SWT.BORDER);
        mCreated.setLayoutData(new RowData(90, 20));
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
    Button[] mFilters;
    private void addFilters(Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new RowLayout(SWT.VERTICAL));
        mFilters = new Button[4];
        new CommonButton(panel, SWT.PUSH, "Filter_By", mMessages, () -> {
            try {
                Predicate<Food> filter = null;
                if (mFilters[0].getSelection()) {
                    filter = x -> x.getName().toLowerCase().contains(mName.getText().toLowerCase());
                }
                if (mFilters[1].getSelection()) {
                    filter = x -> x.getTaste().toString().contains(mTaste.getText().toUpperCase());
                }
                if (mFilters[2].getSelection()) {
                    final String dateText = mDate.getText();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date;
                    try {
                        date = sdf.parse(dateText);
                    } catch (ParseException exception) {
                        new InfoWindow(exception, mMessages).open();
                        return;
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    GregorianCalendar dataParsed = new GregorianCalendar(cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH));
                    filter = x -> x.getExpirationDate().equals(dataParsed);
                }
                if (mFilters[3].getSelection()) {
                    filter = x -> x.getCreated().toString().contains(mCreated.getText().toUpperCase());
                }
                if (filter != null) {
                    mTablePanel.filter(filter);
                }
            } catch (Exception exception) {
                new InfoWindow(exception, mMessages).open();
            }
        });
        mFilters[0] = new Button(panel, SWT.CHECK);
        mFilters[1] = new Button(panel, SWT.CHECK);;
        mFilters[1].setSelection(true);
        mFilters[2] = new Button(panel, SWT.CHECK);
        mFilters[3] = new Button(panel, SWT.CHECK);
        refreshButton = new CommonButton(panel, SWT.PUSH, "REFRESH",mMessages, () -> mTablePanel.load());
    }
    CommonButton refreshButton;
    CommonButton removeButton;
    CommonButton addIfmaxButton;
    CommonButton createButton;
    CommonButton removeAllLikeButton;
    CommonButton removeGreaterButton;

    private void addRemoveButton(Composite parent) {
        removeButton = new CommonButton(parent, SWT.PUSH, "REMOVE", mMessages, () ->
                new ConfirmWindow((s,d) -> mTablePanel.delete(), mMessages).open());
    }

    private void addIfMaxButton(Composite parent) {
        addIfmaxButton = new CommonButton(parent, SWT.PUSH, "ADD_IF_MAX", mMessages, () -> {
            Food food;
            try {

                food = getFood();

            } catch (Exception exception) {
                new InfoWindow(exception, mMessages).open();
                return;
            }
            mTablePanel.addIfMax(food);
        });
    }

    private void addCreateButton(Composite parent) {
        createButton = new CommonButton(parent, SWT.PUSH, mMessages.getString("ADD"), mMessages, () -> {

            Food food;
            try {
                food = getFood();
            } catch (Exception ex) {
                new InfoWindow(ex, mMessages).open();
                return;
            }
            mTablePanel.add(food);
        });
    }

    private void removeAllLikeButton(Composite parent) {
        removeAllLikeButton = new CommonButton(parent, SWT.PUSH, mMessages.getString("REMOVE_ALL"), mMessages, () -> new ConfirmWindow((s, d) -> {
            try {
                mTablePanel.deleteAllLike(getFood());
            } catch (ParseException e) {
                new InfoWindow(e, mMessages).open();
            }
        }, mMessages).open());
    }

    private void removeGreaterButton(Composite parent) {
        removeGreaterButton = new CommonButton(parent, SWT.PUSH, mMessages.getString("REM.GREATER"), mMessages, () -> new ConfirmWindow((s, d) -> {
            try {
                mTablePanel.removeGreater(getFood());
            } catch (ParseException ex) {
                new InfoWindow(ex, mMessages).open();
            }
        }, mMessages).open());
    }

    private Food getFood() throws ParseException {
        Food food = new Food();
        food.setTaste(Food.TASTE.valueOf(mTaste.getText()));
        food.setDate(mDate.getText());
        food.setName(mName.getText());
        return food;
    }

    public void resetText(ResourceBundle messages) {
        mMessages = messages;
        mFilters[3].setText(mMessages.getString("Time"));
        mFilters[2].setText(mMessages.getString("Date1"));
        mFilters[1].setText(mMessages.getString("Taste"));
        mFilters[0].setText(mMessages.getString("Name"));
        refreshButton.resetText(mMessages);
        removeButton.resetText(mMessages);
        addIfmaxButton.resetText(mMessages);
        createButton.resetText(mMessages);
        removeAllLikeButton.resetText(mMessages);
        removeGreaterButton.resetText(mMessages);
    }
}