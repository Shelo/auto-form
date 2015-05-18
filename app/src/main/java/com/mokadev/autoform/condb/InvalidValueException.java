package com.mokadev.autoform.condb;

public class InvalidValueException extends Exception
{
    String field;

    public InvalidValueException(String field, String message)
    {
        super(message);

        this.field = field;
    }

    public String getField()
    {
        return field;
    }
}
