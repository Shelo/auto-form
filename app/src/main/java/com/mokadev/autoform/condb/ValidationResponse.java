package com.mokadev.autoform.condb;

public final class ValidationResponse
{
    private String field;
    private boolean valid;
    private String message;

    public ValidationResponse(boolean valid, String field, String message)
    {
        this.valid = valid;
        this.field = field;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public String getField()
    {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
