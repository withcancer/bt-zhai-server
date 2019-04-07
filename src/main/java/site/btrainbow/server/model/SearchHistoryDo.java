package site.btrainbow.server.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(indexName = "search_history", type = "common", shards = 1, replicas = 0)
public class SearchHistoryDo {
    @Id
    private String id;
    @Field(type = FieldType.Keyword)
    private String keyword;
    @Field(type = FieldType.Date)
    private Long add_time;
    @Field(type = FieldType.Keyword)
    private String ip;
}
