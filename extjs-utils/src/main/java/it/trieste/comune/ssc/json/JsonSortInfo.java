package it.trieste.comune.ssc.json;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

/**
 *
 * @author aleph
 */
public class JsonSortInfo {

    public static final Type SORT_INFO_LIST_TYPE = new TypeToken<JsonSortInfo[]>() {
    }.getType();
    public static final Direction ASC = Direction.ASC, DESC = Direction.DESC;
    private String property;
    private Direction direction = Direction.ASC;

    public JsonSortInfo() {
    }

    public JsonSortInfo(String property, Direction direction) {
        this.property = property;
        this.direction = direction;
    }
    
    public JsonSortInfo(String property) {
        this.property = property;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public static enum Direction {

        ASC, DESC
    };
}
