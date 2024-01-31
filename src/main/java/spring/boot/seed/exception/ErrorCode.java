package spring.boot.seed.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	/**
	 *JWT 관련 에러코드
	 */
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료시간이 지난 토큰입니다."),
	DO_NOT_DECODE_TOKEN(HttpStatus.UNAUTHORIZED, "토큰을 decode 할 수 없습니다."),
	WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "알아볼 수 없는 토큰입니다."),

	/**
	 * 	MemberService 관련 에러코드
	 */
	MEMBER_NAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 memberName 입니다."),
	MEMBER_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 memberName 입니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

	;

	private HttpStatus httpStatus;
	private String message;
}


