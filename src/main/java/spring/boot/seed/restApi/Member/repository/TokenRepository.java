package spring.boot.seed.restApi.Member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.boot.seed.restApi.Member.model.TokenEntity;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
}
