package site.btrainbow.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * 种子文件的结构
 */
@Getter
@Setter
@ToString
@Document(indexName = "torrent_info", type = "common")
public class TorrentInfoDo {
    /**
     * info hash TODO:需不需要写@id
     */
    @Id
    private String id;
    /**
     * 文件名
     */
    private String name;
    /**
     * byte长度
     */
    private Long length;
    /**
     * 最近活跃度
     */
    private Integer popularity;
    /**
     * 创建时间(此处使用timestamp)
     */
    private Long create_time;
    /**
     * 文件列表
     */
    private List<FileList> files;
    /**
     * 种子名称Highlight
     */
    private String nameHighLight;
    /**
     * path Highlight
     */
    private List<FileList> pathHighlight;
}

