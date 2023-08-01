package spring.boot.seed.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.boot.seed.restApi.Member.service.MemberService;
import spring.boot.seed.utils.JwtUtil;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final MemberService memberService;
	private final String secretKey;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		if(authorization == null || !authorization.startsWith("Bearer ")){
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorization.split(" ")[1];

		if(JwtUtil.isExpiredToken(token, secretKey)){
			filterChain.doFilter(request, response);
			return;
		}

		String memberName = JwtUtil.getMemberName(token, secretKey);

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(memberName, null, List.of(new SimpleGrantedAuthority("USER")));

		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		filterChain.doFilter(request, response);
	}
}
