package site.btrainbow.server.repo;

import org.springframework.data.repository.CrudRepository;
import site.btrainbow.server.model.SearchHistoryDo;

public interface SearchHistoryRepo extends CrudRepository<SearchHistoryDo, String> {
    <S extends SearchHistoryDo> S save(S searchHistory);
}
