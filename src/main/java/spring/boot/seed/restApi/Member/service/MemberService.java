package spring.boot.seed.restApi.Member.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.boot.seed.exception.AppException;
import spring.boot.seed.exception.ErrorCode;
import spring.boot.seed.restApi.Member.dto.request.MemberJoinRequestDto;
import spring.boot.seed.restApi.Member.dto.request.MemberLoginRequestDto;
import spring.boot.seed.restApi.Member.dto.response.MemberResponseDto;
import spring.boot.seed.restApi.Member.dto.response.MemberTokenResponseDto;
import spring.boot.seed.restApi.Member.model.MemberEntity;
import spring.boot.seed.restApi.Member.repository.MemberRepository;
import spring.boot.seed.utils.JwtUtil;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder encoder;

	public MemberTokenResponseDto join(MemberJoinRequestDto dto, HttpServletResponse response) {
		memberRepository.findByMemberName(dto.getMemberName())
				.ifPresent(memberEntity -> {
					throw new AppException(ErrorCode.MEMBER_NAME_DUPLICATED, ErrorCode.MEMBER_NAME_DUPLICATED.getMessage());
				});

		MemberEntity member = MemberEntity.builder()
				.memberName(dto.getMemberName())
				.password(encoder.encode(dto.getPassword()))
				.build();

		memberRepository.save(member);

		MemberResponseDto memberResponseDto = MemberResponseDto.builder()
				.memberId(member.getMemberId())
				.memberName(member.getMemberName())
				.createdAt(member.getCreatedAt())
				.modifiedAt(member.getModifiedAt())
				.build();

		String accessToken = JwtUtil.createToken(member.getMemberName());

		response.setHeader("accessToken", accessToken);

		return MemberTokenResponseDto.builder()
				.memberInfo(memberResponseDto)
				.accessToken(accessToken)
				.build();
	}

	public MemberTokenResponseDto login(MemberLoginRequestDto dto, HttpServletResponse response) {
		MemberEntity selectedMember = memberRepository.findByMemberName(dto.getMemberName())
				.orElseThrow(() -> new AppException(ErrorCode.MEMBER_NAME_NOT_FOUND, ErrorCode.MEMBER_NAME_NOT_FOUND.getMessage()));

		if (!encoder.matches(dto.getPassword(), selectedMember.getPassword())) {
			throw new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
		}

		MemberResponseDto memberResponseDto = MemberResponseDto.builder()
				.memberId(selectedMember.getMemberId())
				.memberName(selectedMember.getMemberName())
				.createdAt(selectedMember.getCreatedAt())
				.modifiedAt(selectedMember.getModifiedAt())
				.build();

		String accessToken = JwtUtil.createToken(selectedMember.getMemberName());

		response.setHeader("accessToken", accessToken);

		return MemberTokenResponseDto.builder()
				.memberInfo(memberResponseDto)
				.accessToken(accessToken)
				.build();
	}
}
