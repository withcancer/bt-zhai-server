package site.btrainbow.server.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import site.btrainbow.server.model.SeoWord;

public interface SeoWordRepo extends CrudRepository<SeoWord, String> {
    Page<SeoWord> findAll();
}
