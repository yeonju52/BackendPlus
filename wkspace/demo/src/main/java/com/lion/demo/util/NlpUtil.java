package com.lion.demo.util;

import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;

public class NlpUtil {

    public static Query buildMatchQuery(String field, String keyword) {
        if (keyword.isEmpty()) {
            return new StringQuery("{\"match_all\": {}}");
        }
//        String queryString = String.format(
//                "{\"match\": {\"%s\": {\"query\": \"%s\", \"fuzziness\": \"AUTO\"}}}",
        String queryString = String.format("""
                        {
                            "match": {
                                "%s": {
                                    "query": "%s",
                                    "fuzziness": "AUTO"
                                }
                            }
                        }        
                """,
                field, keyword
        );
        return new StringQuery(queryString);
    }

    public static Query buildMultiMatchQuery(String field, String keyword) {
        String queryString = null;
        if (keyword.isEmpty()) {
            return new StringQuery("{\"match_all\": {}}");
        }
        if (field.equals("info")) {
            queryString = String.format("""
                        {
                            "multi_match": {
                                "query": "%s",
                                "fields": [
                                    "info.*"
                                ],
                                "fuzziness": "AUTO"
                            }
                        }        
                    """, keyword
            );
        } else if (field.equals("reviews")) {
            queryString = String.format("""
                        {
                            "nested": {
                                "path": "reviews",
                                "query": {
                                    "match": {
                                        "reviews.review": {
                                            "query": "%s",
                                            "fuzziness": "AUTO"
                                        }
                                    }
                                }
                            }
                        }        
                    """, keyword
            );
        } else {
            queryString = String.format("""
                    {
                        "match": {
                            "%s": {
                                "query": "%s",
                                "fuzziness": "AUTO"
                            }
                        }
                    }        
                """, field, keyword
            );
        }
        return new StringQuery(queryString);
    }
}
