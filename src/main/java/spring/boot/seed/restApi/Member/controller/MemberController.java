package spring.boot.seed.restApi.Member.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.seed.restApi.Member.dto.request.MemberJoinRequestDto;
import spring.boot.seed.restApi.Member.dto.request.MemberLoginRequestDto;
import spring.boot.seed.restApi.Member.dto.response.MemberTokenResponseDto;
import spring.boot.seed.restApi.Member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/join")
	public ResponseEntity<MemberTokenResponseDto> join(@RequestBody MemberJoinRequestDto dto, HttpServletResponse response){
		return ResponseEntity.ok().body(memberService.join(dto, response));
	}

	@PostMapping("/login")
	public ResponseEntity<MemberTokenResponseDto> login(@RequestBody MemberLoginRequestDto dto,  HttpServletResponse response){
		return ResponseEntity.ok().body(memberService.login(dto, response));
	}

	@GetMapping("/hello")
	public ResponseEntity<String> login(){
		return ResponseEntity.ok().body("hello");
	}
}