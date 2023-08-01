package spring.boot.seed.restApi.Member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
	@GetMapping("/hello")
	public ResponseEntity<String> login(){
		return ResponseEntity.ok().body("hello");
	}
}