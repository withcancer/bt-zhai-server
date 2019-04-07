package site.btrainbow.server.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;
import site.btrainbow.server.model.TorrentInfoDo;

import java.util.List;

public interface TorrentInfoRepo extends CrudRepository<TorrentInfoDo, String> {

    long count();

// --Commented out by Inspection START (2018/9/3 21:00):
//    @Query("{\"multi_match\":" +
//            "{\"query\":\"?0\"," +
//            "\"type\":\"most_fields\"," +
//            "\"fields\":[\"name\",\"path\"]" +
//            "}}")
//    Page<TorrentInfoDo> findByNameAndPath(String keyword, Pageable pageable);
// --Commented out by Inspection STOP (2018/9/3 21:00)

    @Query("{\"function_score\":{" +
            "\"functions\":" +
            "[{\"random_score\":" +
            "{}}]}})")
    List<TorrentInfoDo> random(Pageable pageable);
}
