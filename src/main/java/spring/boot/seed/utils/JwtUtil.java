package spring.boot.seed.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
	private static String secretKey;
	private static Long expireTimeMs = 1000 * 60 * 60 * 24L;

	@Value("${jwt.secret}")
	public void setSecretKey(String secret) {
		JwtUtil.secretKey = secret;
	}
	public static String createToken(String memberName) {
		String issuer = "JWT";
		Algorithm hashKey = Algorithm.HMAC256(secretKey);
		Date issuedTime = new Date();
		Date expirationTime = new Date(issuedTime.getTime() + expireTimeMs);

		return JWT.create()
				.withIssuer(issuer)
				.withClaim("memberName", memberName)
				.withIssuedAt(issuedTime)
				.withExpiresAt(expirationTime)
				.sign(hashKey);

	}

	public static DecodedJWT decodedToken(String token){
		DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secretKey))
				.build()
				.verify(token);

		return jwt;
	}

	public static Boolean isExpiredToken(String token) {
		DecodedJWT decodedJWT = decodedToken(token);
		return decodedJWT.getExpiresAt().before(new Date());
	}

	public static String getMemberName(String token){
		DecodedJWT decodedJWT = decodedToken(token);
		return decodedJWT.getClaim("memberName").asString();
	}

}
