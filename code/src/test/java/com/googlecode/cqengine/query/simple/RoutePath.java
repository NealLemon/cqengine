package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.MultiValueNullableAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableMapAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.pattern.PathPattern;

import java.util.List;
import java.util.Set;

public class RoutePath {

    private List<PathPattern> requestPathPatterns;
    // 只保存key
    //保存key和value ex: Connection=keep-alive
    private Set<String> requestUrls;

    RoutePath(List<PathPattern> requestPathPatterns) {
        this.requestPathPatterns = requestPathPatterns;
    }

    RoutePath(Set<String> requestUrls) {
        this.requestUrls = requestUrls;
    }

    public static final Attribute<RoutePath, PathPattern> REQUEST_PATH = new MultiValueAttribute<RoutePath, PathPattern>("requestPathPatterns") {
        public Iterable<PathPattern> getValues(RoutePath routePath, QueryOptions queryOptions) { return routePath.requestPathPatterns; }
    };


    public static final Attribute<RoutePath, String> REQUEST_URLS = new MultiValueAttribute<RoutePath, String>("requestUrls") {
        public Iterable<String> getValues(RoutePath routePath, QueryOptions queryOptions) { return routePath.requestUrls; }
    };

    public List<PathPattern> getRequestPathPatterns() {
        return requestPathPatterns;
    }

    public Set<String> getRequestUrls() {
        return requestUrls;
    }
}
