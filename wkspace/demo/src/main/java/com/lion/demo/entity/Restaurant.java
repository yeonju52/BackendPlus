package com.lion.demo.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Map;

@Document(indexName = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id
    private String id;

    private String name;
    private String intro;
    private String imgSrc;

    @Field(type = FieldType.Object)
    private Map<String, Object> info;

    @Field(type = FieldType.Nested)
    private List<Map<String, Object>> reviews;
}
