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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Asserts that an attribute's value matches a regular expression.
 * <p/>
 * To accelerate {@code matchesRegex(...)} queries, add a Standing Query Index on {@code matchesRegex(...)}.
 *
 * @author Niall Gallagher, Silvano Riz
 */
public class PathMatches<O, A extends String> extends SimpleQuery<O, A> {

    private final String path;

    /**
     * Creates a new {@link PathMatches} initialized to make assertions on whether values of the specified
     * attribute match the given regular expression pattern.
     *
     * @param attribute The attribute on which the assertion is to be made
   //  * @param regexPattern The regular expression pattern with which values of the attribute should be tested
     */
    public PathMatches(Attribute<O, A> attribute, String path) {
        super(attribute);
        this.path = path;
    }


    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        A attributeValue = attribute.getValue(object, queryOptions);
        return matchesValue(attributeValue, queryOptions);
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        for (A attributeValue : attribute.getValues(object, queryOptions)) {
            if (matchesValue(attributeValue, queryOptions)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }

    @SuppressWarnings("unused")
    public boolean matchesValue(A aValue, QueryOptions queryOptions){
        Pattern pattern = Pattern.compile(aValue);
        Matcher matcher = pattern.matcher(path);
        return matcher.find(0);
    }

}