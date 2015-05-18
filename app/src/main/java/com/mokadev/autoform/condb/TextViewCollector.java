package com.mokadev.autoform.condb;

import android.widget.TextView;

public class TextViewCollector implements ValueCollector
{
    TextView textView;

    public TextViewCollector(TextView textView)
    {
        this.textView = textView;
    }

    @Override
    public String collectValue()
    {
        return textView.getText().toString();
    }
}
