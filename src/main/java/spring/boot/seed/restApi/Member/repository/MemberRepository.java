package spring.boot.seed.restApi.Member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.boot.seed.restApi.Member.model.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
