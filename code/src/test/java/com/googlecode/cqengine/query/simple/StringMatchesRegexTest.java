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

import com.googlecode.concurrenttrees.common.PrettyPrinter;
import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.index.standingquery.StandingQueryIndex;
import com.googlecode.cqengine.index.support.KeyStatisticsIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class StringMatchesRegexTest {

    private PathPatternParser pathPatternParser = new PathPatternParser();
    @Test
    public void testPathMatchesRegex() {
        Query<RoutePath> query = matchesPath(RoutePath.REQUEST_PATH, "/api/v1/a");
        IndexedCollection<RoutePath> indexedCollection = new ConcurrentIndexedCollection<RoutePath>();
        indexedCollection.addIndex(HashIndex.onAttribute(RoutePath.REQUEST_PATH));
        List<PathPattern> patternList1 = new ArrayList<>();
        List<PathPattern> patternList2 = new ArrayList<>();
        PathPattern pathPattern1 = this.pathPatternParser.parse("/api/v1/a/*");
        PathPattern pathPattern2 = this.pathPatternParser.parse("/api/v1/{name}");
        patternList1.add(pathPattern1);
        patternList2.add(pathPattern2);
        RoutePath routePath1 = new RoutePath(patternList1);
        RoutePath routePath2 = new RoutePath(patternList2);
        indexedCollection.add(routePath1);
        indexedCollection.add(routePath2);
      //  indexedCollection.addIndex(HashIndex.onAttribute(RoutePath.REQUEST_PATH));
      //  indexedCollection.addAll(asList("/api/v1/a/*/b","/api/v1/a/[a-zA-Z0-9]+","/api/v1/a/*"));
        IndexedCollection<RoutePath> collection = indexedCollection;
        ResultSet<RoutePath> results = collection.retrieve(query);
        for (RoutePath match : results) {
            System.out.println(match); // TOAST, TEST, TT
        }
    }


    @Test
    public void testStrPathMatchesRegex() {
        Query<RoutePath> query = strMatchesPath(RoutePath.REQUEST_URLS, "/api/full/v1000/test");
        IndexedCollection<RoutePath> indexedCollection = new ConcurrentIndexedCollection<RoutePath>();
        indexedCollection.addIndex(RadixTreeIndex.onAttribute(RoutePath.REQUEST_URLS));
        Set<String> patternList1 = new HashSet<>();
        Set<String> patternList2 = new HashSet<>();
        patternList1.add("/api/full/v1000/**");
        patternList2.add("/api/v1/a/*/test");
        RoutePath routePath1 = new RoutePath(patternList1);
        RoutePath routePath2 = new RoutePath(patternList2);
        indexedCollection.add(routePath1);
        indexedCollection.add(routePath2);
        //  indexedCollection.addIndex(HashIndex.onAttribute(RoutePath.REQUEST_PATH));
        //  indexedCollection.addAll(asList("/api/v1/a/*/b","/api/v1/a/[a-zA-Z0-9]+","/api/v1/a/*"));
        IndexedCollection<RoutePath> collection = indexedCollection;
        ResultSet<RoutePath> results = collection.retrieve(query);
        for (RoutePath match : results) {
            System.out.println(match.getRequestUrls().toString());
        }
    }

    @Test
    public void testStringMatchesRegex() {
        Query<String> query = matchesRegex(selfAttribute(String.class), "f.*");
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        indexedCollection.addAll(asList("foo1", "foo2", "bar", "baz", "car"));
        IndexedCollection<String> collection = indexedCollection;
        ResultSet<String> results = collection.retrieve(query);
        assertEquals(2, results.size());
        assertTrue(results.iterator().hasNext());
    }

    @Test
    public void testStringMatchesRegexWithIndex() {
        Query<String> query = matchesRegex(selfAttribute(String.class), "f.*");
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        indexedCollection.addAll(asList("foo1", "foo2", "bar", "baz", "car"));
        IndexedCollection<String> collection = indexedCollection;
        collection.addIndex(StandingQueryIndex.onQuery(query));
        ResultSet<String> results = collection.retrieve(query);
        assertEquals(2, results.size());
        assertTrue(results.iterator().hasNext());
    }

    @Test
    public void testStringMatchesRegexNegatedWithIndex() {
        Query<String> query = not(matchesRegex(selfAttribute(String.class), "[fb].*"));
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        indexedCollection.addAll(asList("foo1", "foo2", "bar", "baz", "car"));
        IndexedCollection<String> collection = indexedCollection;
        collection.addIndex(StandingQueryIndex.onQuery(query));
        ResultSet<String> results = collection.retrieve(query);
        assertEquals(1, results.size());
        assertTrue(results.iterator().hasNext());
    }

    @Test
    public void testWildcard() {
        ConcurrentRadixTree<Integer> tree = new ConcurrentRadixTree<Integer>(new DefaultCharArrayNodeFactory());
//        tree.put("/api/v1/*/c", 1);
//        tree.put("/api/v1/b/*/d", 2);
//        tree.put("/api/v1/*/c", 3);

        tree.put("/api/v1/a/*/test", 4);
        tree.put("/api/full/*/test",5 );
//        tree.putWildcard("/api/v1/b/e/d",6 );
//        tree.putWildcard("/api/v1/*/c/*/f", 6);

//        tree.put("/api/v1/b/*/d/*/g", 6);
        // tree.put("/api/v1/b/c", 3);

        //    ○
        //    └── ○ FOO (1)
        //        ├── ○ BAR (2)
        //        └── ○ D (3)

        String expected, actual;
//        expected =
//                "○\n" +
//                "└── ○ FOO (1)\n" +
//                "    ├── ○ BAR (2)\n" +
//                "    └── ○ D (3)\n";
        actual = PrettyPrinter.prettyPrint(tree);

        System.out.println(tree.getValueForWildcardKey("/api/full/v1000/test"));
//        assertEquals(Integer.valueOf(4), tree.getValueForWildcardKey("/api/full/v1000/test"));
//        assertEquals(Integer.valueOf(5), tree.getValueForWildcardKey("/api/v1/b/c/d"));
    }
}
