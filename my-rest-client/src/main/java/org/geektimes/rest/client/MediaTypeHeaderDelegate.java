package org.geektimes.rest.client;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.commons.lang.StringUtils;

/**
 * 这里不知道怎么搞，怎么又是泛型，又是具体的操作
 * 
 * @author xubin
 * @date 2021/3/30 19:33
 */
public class MediaTypeHeaderDelegate<T> implements RuntimeDelegate.HeaderDelegate<T> {

    @Override
    public T fromString(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("value is not blank");
        }
        String[] split = value.split("/");
        if (split.length < 2) {
            throw new IllegalArgumentException("value must / split");
        }

        return (T)new MediaType(split[0], split[1]);
    }

    @Override
    public String toString(T value) {
        MediaType mediaType = (MediaType)value;
        if (StringUtils.isBlank(mediaType.getType()) || StringUtils.isBlank(mediaType.getSubtype())) {
            return "";
        }
        String contextType = mediaType.getType() + "/" + mediaType.getSubtype();
        String charSet = mediaType.getParameters().get(MediaType.CHARSET_PARAMETER);
        if (StringUtils.isNotBlank(charSet)) {
            contextType = contextType + ";" + charSet;
        }
        return contextType;
    }
}
