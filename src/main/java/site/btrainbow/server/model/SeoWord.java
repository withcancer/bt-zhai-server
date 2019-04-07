package site.btrainbow.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@ToString
@Document(indexName = "seo_word", type = "common", shards = 1, replicas = 0)
public class SeoWord {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String word;
}
