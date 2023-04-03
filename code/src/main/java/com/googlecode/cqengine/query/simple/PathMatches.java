/**
 * Copyright 2012-2015 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;

/**
 * Asserts uri {link spring cloud gateway PathRoutePredicateFactory}.
 * @author Neal
 * @param <O>
 * @param <A>
 */
public class PathMatches<O, A extends PathPattern> extends SimpleQuery<O, A> {

    private final String value;

    /**
     * Creates a new {@link PathMatches} initialized to make assertions on whether values of the specified
     * attribute match the given regular expression pattern.
     *
     * @param attribute The attribute on which the assertion is to be made
   //  * @param regexPattern The regular expression pattern with which values of the attribute should be tested
     */
    public PathMatches(Attribute<O, A> attribute, String value) {
        super(attribute);
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        PathContainer requestUri = PathContainer.parsePath(value);
        A attributeValue = attribute.getValue(object, queryOptions);
        return matchesPath(attributeValue, queryOptions, requestUri);
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        PathContainer requestUri = PathContainer.parsePath(value);
        for (A attributeValue : attribute.getValues(object, queryOptions)) {
            if (matchesPath(attributeValue, queryOptions,requestUri)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @SuppressWarnings("unused")
    public boolean matchesPath(A aValue, QueryOptions queryOptions, PathContainer requestUri){
        return aValue.matches(requestUri);
    }

}