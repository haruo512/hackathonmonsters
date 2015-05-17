package com.zeroone_creative.basicapplication.model.enumerate;

/**
 * Created by shunhosaka on 15/05/16.
 */
public enum Language {
    English("en", "English"),
    Japan("ja", "日本語"),
    ;
    public String code;
    public String name;
    Language(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
