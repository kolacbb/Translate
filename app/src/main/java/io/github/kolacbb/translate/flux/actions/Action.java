package io.github.kolacbb.translate.flux.actions;

import java.util.HashMap;

/**
 * 使用Builder模式设计Action类
 * 曾经见过使用Bundle作为Action的data值，经过比较，发现使用Map作为data类型可以使它的功能更为强大
 *
 * Created by Kola on 2016/6/4.
 */
public class Action {
    private final String type;
    private final HashMap<String, Object> data;

    Action(String type, HashMap<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    public static Builder type(String type) {
        return new Builder().with(type);
    }

    public String getType() {
        return type;
    }

    public HashMap getData() {
        return data;
    }

    public static class Builder {

        private String type;
        private HashMap<String, Object> data;

        Builder with(String type) {
            if (type == null) {
                throw new IllegalArgumentException("Type may not be null.");
            }
            this.type = type;
            this.data = new HashMap<>();
            return this;
        }

        public Builder bundle(String key, Object value) {
            if (key == null) {
                throw new IllegalArgumentException("Key may not be null.");
            }

            if (value == null) {
                throw new IllegalArgumentException("Value may not be null.");
            }
            data.put(key, value);
            return this;
        }

        public Action build() {
            if (type == null || type.isEmpty()) {
                throw new IllegalArgumentException("At least one key is required.");
            }
            return new Action(type, data);
        }
    }
}
