package com.mokadev.autoform.condb;

import android.view.View;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Form
{
    private List<Field> fields = new ArrayList<>();

    private String targetCodeUrl;

    public JsonResponse response = new JsonResponse()
    {
        @Override
        public void onResponse(JSONObject jsonObject)
        {
            onSuccess(jsonObject);
        }
    };

    public Form(String targetCodeUrl)
    {
        if (targetCodeUrl == null)
        {
            throw new IllegalArgumentException("Target Code Url cannot be null.");
        }

        this.targetCodeUrl = targetCodeUrl;
    }

    /**
     * Adds a new field with validation to the form. Using this with a null validator is the same
     * as calling the overload with one argument.
     *
     * @param collector the value collector for the fields.
     * @param field     the name of the field to be validated.
     * @param validator the validator for the field value.
     */
    public void addField(String field, ValueCollector collector, Validator validator)
    {
        fields.add(new Field(field, collector, validator));
    }

    /**
     * Add a new field that doesn't need validation.
     *
     * @param collector the field collector.
     * @param field     the field to be added.
     */
    public void addField(String field, ValueCollector collector)
    {
        addField(field, collector, null);
    }

    public void submit()
    {
        List<NameValuePair> data = new ArrayList<>();

        for (Field field : fields)
        {
            try
            {
                data.add(field.getNameValuePair());
            } catch (InvalidValueException e)
            {
                onFailure(e.getField(), e.getMessage());
            }
        }

        DatabaseManager.getInstance().requestData(getTargetCodeUrl(), data, response);
    }

    public View.OnClickListener getOnClickListener()
    {
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                submit();
            }
        };

        return listener;
    }

    public String getTargetCodeUrl()
    {
        return targetCodeUrl;
    }

    public abstract void onFailure(String field, String response);
    public abstract void onSuccess(JSONObject object);
}
