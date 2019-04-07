package site.btrainbow.server.repo;

import org.springframework.data.repository.CrudRepository;
import site.btrainbow.server.model.InitInfoDo;

import java.util.Optional;

public interface InitRepo extends CrudRepository<InitInfoDo, String> {
    long count();

    <T extends InitInfoDo> T save(T initDo);

    Optional<InitInfoDo> findById(String id);
}
