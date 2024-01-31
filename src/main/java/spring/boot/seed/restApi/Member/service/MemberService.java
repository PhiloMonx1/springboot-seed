package spring.boot.seed.restApi.Member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.boot.seed.exception.AppException;
import spring.boot.seed.exception.ErrorCode;
import spring.boot.seed.restApi.Member.dto.MemberJoinRequestDto;
import spring.boot.seed.restApi.Member.dto.MemberLoginRequestDto;
import spring.boot.seed.restApi.Member.model.MemberEntity;
import spring.boot.seed.restApi.Member.repository.MemberRepository;
import spring.boot.seed.utils.JwtUtil;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder encoder;

	public String join(MemberJoinRequestDto dto){
		memberRepository.findByMemberName(dto.getMemberName())
				.ifPresent(memberEntity -> {throw new AppException(ErrorCode.MEMBER_NAME_DUPLICATED, ErrorCode.MEMBER_NAME_DUPLICATED.getMessage());});

		memberRepository.save(MemberEntity.builder()
				.memberName(dto.getMemberName())
				.password(encoder.encode(dto.getPassword()))
				.build());

		return  "SUCCESS";
	}

	public String login(MemberLoginRequestDto dto) {
		MemberEntity selectedMember = memberRepository.findByMemberName(dto.getMemberName())
				.orElseThrow(() ->new AppException(ErrorCode.MEMBER_NAME_NOT_FOUND, ErrorCode.MEMBER_NAME_NOT_FOUND.getMessage()));

		if(!encoder.matches(dto.getPassword(), selectedMember.getPassword())){
			throw new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
		}

		return JwtUtil.createToken(selectedMember.getMemberName());
	}
}
