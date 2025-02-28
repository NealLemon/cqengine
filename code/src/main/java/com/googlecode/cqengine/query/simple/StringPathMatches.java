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

import static com.googlecode.cqengine.query.support.QueryValidation.checkQueryValueNotNull;

/**
 * Asserts than a {@link CharSequence}-based attribute starts with a given {@link CharSequence}-based prefix.

 * @author Niall Gallagher
 */
public class StringPathMatches<O, A extends CharSequence> extends SimpleQuery<O, A> {

    private final A value;

    /**
     * Creates a new {@link SimpleQuery} initialized to make assertions on values of the specified attribute
     *
     * @param attribute The attribute on which the assertion is to be made
     */
    public StringPathMatches(Attribute<O, A> attribute, A value) {
        super(attribute);
        this.value = checkQueryValueNotNull(value);
    }

    public A getValue() {
        return value;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        A attributeValue = attribute.getValue(object, queryOptions);
        return matchesValue(attributeValue, value, queryOptions);
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        for (A attributeValue : attribute.getValues(object, queryOptions)) {
            if (matchesValue(attributeValue, value, queryOptions)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    public static boolean matchesValue(CharSequence aValue, CharSequence bValue, QueryOptions queryOptions) {
        int charsMatched = 0;
        for (int i = 0, length = Math.min(aValue.length(), bValue.length()); i < length; i++) {
            if (aValue.charAt(i) != bValue.charAt(i)) {
                break;
            }
            charsMatched++;
        }
        return charsMatched <= bValue.length();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringPathMatches)) return false;

        StringPathMatches that = (StringPathMatches) o;

        if (!attribute.equals(that.attribute)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    protected int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StringPathMatches(" + asLiteral(super.getAttributeName()) +
                ", " + asLiteral(value) +
                ")";
    }
}
