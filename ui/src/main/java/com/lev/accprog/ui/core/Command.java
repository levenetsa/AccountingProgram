package com.lev.accprog.ui.core;

import org.json.JSONObject;

import java.text.ParseException;

public class Command {

    private Food mArgumentObject;
    private String mNameAndArgs;
    private String mCommandText;
    private int mSpaceIndex;

    public Command(String nameAndArgs) {
        this.mNameAndArgs = nameAndArgs.trim();
        mSpaceIndex = mNameAndArgs.indexOf(" ");
        if (mSpaceIndex == -1) {
            mCommandText = mNameAndArgs;
        } else {
            mCommandText = nameAndArgs.substring(0, mSpaceIndex);
        }
    }

    String getArgumentAsText() {
        return mNameAndArgs.substring(mSpaceIndex + 1, mNameAndArgs.length()).trim();
    }

    private void prepareArgument() throws ParseException {
        mArgumentObject = castToFood(mNameAndArgs.substring(mSpaceIndex + 1, mNameAndArgs.length()).trim());
    }

    private Food castToFood(String s) throws ParseException {
        JSONObject parser = new JSONObject(s);
        mArgumentObject = new Food();
        mArgumentObject.setDate(parser.getString("expirationDate"));
        mArgumentObject.setName(parser.getString("name"));
        mArgumentObject.setTaste(parser.getString("taste"));
        return mArgumentObject;
    }

    Food getArgumentObject() throws ParseException {
        prepareArgument();
        return mArgumentObject;
    }

    String getCommandText() {
        return mCommandText;
    }
}
