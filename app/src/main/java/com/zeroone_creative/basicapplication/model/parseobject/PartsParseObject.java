package com.zeroone_creative.basicapplication.model.parseobject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shunhosaka on 15/05/16.
 */
@ParseClassName("Parts")
public class PartsParseObject extends ParseObject {
    public String id;
    public String url;
    public List<String> tag = new ArrayList<>();

    public String getId() {
        return getString("id");
    }

    public String getUrl() {
        return getString("url");
    }

    public List<String> getTag() {
        return getList("tag");
    }

}
