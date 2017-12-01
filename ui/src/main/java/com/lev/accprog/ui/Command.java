package com.lev.accprog.ui;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.json.JSONObject;

class Command {

    private Food mArgumentObject;
    private String mNameAndArgs;
    private String mCommandText;
    private int mSpaceIndex;

    Command(String nameAndArgs) {
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

    void prepareArgument() throws InvalidArgumentException {
        mArgumentObject = castToFood(mNameAndArgs.substring(mSpaceIndex + 1, mNameAndArgs.length()).trim());
    }

    private Food castToFood(String s) throws InvalidArgumentException {
        JSONObject parser = new JSONObject(s);
        mArgumentObject = new Food();
        mArgumentObject.setDate(parser.getString("expirationDate"));
        mArgumentObject.setName(parser.getString("name"));
        mArgumentObject.setTaste(parser.getString("taste"));
        return mArgumentObject;
    }

    public Food getArgumentObject() throws InvalidArgumentException {
        prepareArgument();
        return mArgumentObject;
    }

    public String getmCommandText() {
        return mCommandText;
    }
    String getNameAndArgs() {
        return mNameAndArgs;
    }
}
