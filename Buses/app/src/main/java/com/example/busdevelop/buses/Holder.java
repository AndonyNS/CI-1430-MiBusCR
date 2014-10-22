package com.example.busdevelop.buses;

import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Andony on 22/10/2014.
 */
class Holder
{
    TextView textViewTitle;
    TextView textViewSubtitle;
    CheckBox checkBox;

    public TextView getTextViewTitle()
    {
        return textViewTitle;
    }

    public void setTextViewTitle(TextView textViewTitle)
    {
        this.textViewTitle = textViewTitle;
    }

    public TextView getTextViewSubtitle()
    {
        return textViewSubtitle;
    }

    public void setTextViewSubtitle(TextView textViewSubtitle)
    {
        this.textViewSubtitle = textViewSubtitle;
    }

    public CheckBox getCheckBox()
    {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox)
    {
        this.checkBox = checkBox;
    }

}
