package io.github.kolacbb.translate.data.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangd on 2017/9/24.
 */

public class BaiduResult implements Serializable {
    @SerializedName("query")
    private String from;

    @SerializedName("to")
    private String to;

    @SerializedName("trans_result")
    private List<Result> result;


    public Translate getTranslate() {
        Translate translate = new Translate();
        translate.setTranslation(result.get(0).getTranslation());
        return translate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result {
        private String query;

        private String translation;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getTranslation() {
            return translation;
        }

        public void setTranslation(String translation) {
            this.translation = translation;
        }
    }

}
