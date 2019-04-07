package site.btrainbow.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Getter
@Setter
@ToString
@Document(indexName = "init_cache", type = "common", shards = 1, replicas = 0)
public class InitInfoDo {

    @Id
    @Field(type = FieldType.Auto)
    private String id;
    /**
     * 总数
     */
    @Field(type = FieldType.Long)
    private Long count_total;

    /**
     * 今日新增
     */
    @Field(type = FieldType.Long)
    private Long count_today;

    /**
     * 搜索热词
     */
    private List<Keyword> popular_keyword;

    /**
     * 随机种子
     */
    private List<TorrentInfoDo> random_torrent;
}
