package spring.boot.seed.restApi.Member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberLoginRequestDto {
	private String memberName;
	private String password;
}