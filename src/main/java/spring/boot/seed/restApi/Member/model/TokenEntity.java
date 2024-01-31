package spring.boot.seed.restApi.Member.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.boot.seed.utils.BaseEntity;

@Builder
@Entity
@Table(name="REFRESH_TOKEN")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenEntity extends BaseEntity {
	@Id
	@Column(name = "MEMBER_ID", nullable = false)
	private Long memberId;

	@Column(name = "TOKEN", nullable = false)
	private String  token;
}