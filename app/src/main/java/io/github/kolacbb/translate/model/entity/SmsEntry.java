package io.github.kolacbb.translate.model.entity;

/**
 * Created by Kola on 2016/6/26.
 */
public class SmsEntry {
    private String address;
    private String date;
    private String content;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
