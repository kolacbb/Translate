package io.github.kolacbb.translate.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kola on 2016/6/4.
 */
public class YouDaoResult {

    @SerializedName("errorCode")
    private Integer errorCode;
    @SerializedName("query")
    private String query;
    @SerializedName("translation")
    private List<String> translation = new ArrayList<String>();
    @SerializedName("basic")
    private Basic basic;
    @SerializedName("web")
    private List<Web> web = new ArrayList<Web>();

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public List<Web> getWeb() {
        return web;
    }

    public void setWeb(List<Web> web) {
        this.web = web;
    }

    public class Basic {

        @SerializedName("phonetic")
        private String phonetic;
        @SerializedName("uk-phonetic")
        private String ukPhonetic;
        @SerializedName("us-phonetic")
        private String usPhonetic;
        @SerializedName("explains")
        private List<String> explains = new ArrayList<String>();

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public String getUkPhonetic() {
            return ukPhonetic;
        }

        public void setUkPhonetic(String ukPhonetic) {
            this.ukPhonetic = ukPhonetic;
        }

        public String getUsPhonetic() {
            return usPhonetic;
        }

        public void setUsPhonetic(String usPhonetic) {
            this.usPhonetic = usPhonetic;
        }

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }

    }

    public class Web {

        @SerializedName("key")
        private String key;
        @SerializedName("value")
        private List<String> value = new ArrayList<String>();

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }

    }


}
