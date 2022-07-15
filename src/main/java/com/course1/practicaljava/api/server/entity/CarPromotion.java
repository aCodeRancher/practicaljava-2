package com.course1.practicaljava.api.server.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName="practical-java-2")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarPromotion {

    private String type;

    private String description;

    @Id
    private String id;
}
