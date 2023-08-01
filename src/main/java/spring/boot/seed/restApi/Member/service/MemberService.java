package spring.boot.seed.restApi.Member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.boot.seed.restApi.Member.dto.MemberJoinRequestDto;
import spring.boot.seed.restApi.Member.dto.MemberLoginRequestDto;
import spring.boot.seed.restApi.Member.model.MemberEntity;
import spring.boot.seed.restApi.Member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public String join(MemberJoinRequestDto dto){
		memberRepository.findByMemberName(dto.getMemberName())
				.ifPresent(memberEntity -> {throw new RuntimeException("이미 사용중인 사용자명 입니다.");});

		memberRepository.save(MemberEntity.builder()
				.memberName(dto.getMemberName())
				.password(dto.getPassword())
				.build());

		return  "SUCCESS";
	}

	public String login(MemberLoginRequestDto dto) {
		MemberEntity selectedMember = memberRepository.findByMemberName(dto.getMemberName())
				.orElseThrow(() ->new RuntimeException("해당 유저를 찾을 수 없습니다."));

		if(!dto.getPassword().equals(selectedMember.getPassword())){
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}

		return "Token";
	}
}
