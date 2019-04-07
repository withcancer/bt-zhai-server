package site.btrainbow.server.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SuppressWarnings("SpellCheckingInspection")
@Configuration
@EnableElasticsearchRepositories(basePackages = "site.btrainbow.server.repo")
public class JpaConfig {
}
