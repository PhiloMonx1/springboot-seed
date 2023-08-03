package spring.boot.seed.restApi.Member.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.boot.seed.utils.BaseEntity;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;
	private String memberName;
	private String password;
}
