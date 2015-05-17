package com.zeroone_creative.basicapplication.model.parseobject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by shunhosaka on 15/05/16.
 */
@ParseClassName("Comment")
public class CommentParseObject extends ParseObject {

    public String getBody() {
        return getString("body");
    }

    public String getImageId() {
        return getString("imageId");
    }

    public String getUserId() {
        return getString("userId");
    }
}
