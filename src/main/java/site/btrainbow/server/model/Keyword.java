package site.btrainbow.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@ToString
public class Keyword {
    /**
     * keyword的值
     */
    @Field(type = FieldType.Keyword)
    private String value;
    /**
     * 搜索次数
     */
    @Field(type = FieldType.Long)
    private Long search_times;
}
