package com.zeroone_creative.basicapplication.model.parseobject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by shunhosaka on 15/05/16.
 */
@ParseClassName("Image")
public class ImageParseObject extends ParseObject {

    public String getBody() {
        return getString("body");
    }

    public List<String> getTag() {
        return getList("tag");
    }

    public String getUserId() {
        return getString("userId");
    }

    public String getSentenceId() {
        return getString("sentenceId");
    }

    public void setBody(String body) {
        put("body", body);
    }

    public void setTag(List<String> tag) {
        addAllUnique("tag", tag);
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }

    public void setSentenceId(String sentenceId) {
        put("sentenceId", sentenceId);
    }

}
