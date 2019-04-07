package site.btrainbow.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Torrent Meta里的文件列表
 */
@Getter
@Setter
@ToString
public class FileList {
    /**
     * 文件名
     */
    private String path;
    /**
     * byte长度
     */
    private Long length;
}
