package com.example.prm392_v1.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * A generic class to handle OData JSON responses that wrap the main data
 * array in a "value" field.
 * @param <T> The type of the objects in the list.
 */
public class ODataResponse<T> {

    @SerializedName("value")
    public List<T> value;

    public List<T> getValue() {
        return value;
    }

    public void setValue(List<T> value) {
        this.value = value;
    }
}