package spring.boot.seed.restApi.Member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberJoinRequestDto {
	private String memberName;
	private String password;
}
