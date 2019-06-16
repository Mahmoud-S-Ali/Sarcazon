package com.joyapeak.sarcazon.data.network.model.server;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud Ali on 4/19/2018.
 */

public class ActionRequest {

    @Expose
    @SerializedName("action")
    protected String action;

    public ActionRequest(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public int hashCode() {
        return action != null ? action.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "action='" + action + '\'';
    }
}
