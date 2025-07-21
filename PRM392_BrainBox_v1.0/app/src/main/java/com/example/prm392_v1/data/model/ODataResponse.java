package com.example.prm392_v1.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

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