/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.trieste.comune.ssc.beans;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * @author Gabri
 */
@Deprecated
public class Order {

    public static final String DIR_ASC = "ASC", DIR_DESC = "DESC";
    private String property;
    private String direction;
    private static final Type listType = new TypeToken<List<Order>>() {
    }.getType();

    public Order() {
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public static Order deserialize(String orderString) {
        return deserializeList(orderString).get(0);
    }

    public static List<Order> deserializeList(String orderString) {
        return new Gson().fromJson(orderString, listType);
    }
}
