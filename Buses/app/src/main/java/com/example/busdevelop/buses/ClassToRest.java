package com.example.busdevelop.buses;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

/**
 * Created by Manuel on 10/22/14.
 */
public interface ClassToRest {
    public StringEntity JsonAcumulator();
}
