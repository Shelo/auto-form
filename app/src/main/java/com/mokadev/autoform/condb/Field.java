package com.mokadev.autoform.condb;

import org.apache.http.NameValuePair;

public class Field
{
    private String name;
    private ValueCollector collector;
    private Validator validator;

    public Field(String name, ValueCollector collector, Validator validator)
    {
        this.name = name;
        this.collector = collector;
        this.validator = validator;
    }

    public String getName()
    {
        return name;
    }

    public Validator getValidator()
    {
        return validator;
    }

    public ValueCollector getCollector()
    {
        return collector;
    }

    public String getValue()
    {
        return getCollector().collectValue();
    }

    public NameValuePair getNameValuePair() throws InvalidValueException
    {
        NameValuePair param = new NameValuePair()
        {
            @Override
            public String getName()
            {
                return Field.this.getName();
            }

            @Override
            public String getValue()
            {
                return Field.this.getValue();
            }
        };

        String value = param.getValue();
        String response = getValidator().isValid(value);

        if (response != null)
        {
            throw new InvalidValueException(getName(), response);
        }

        return param;
    }
}
