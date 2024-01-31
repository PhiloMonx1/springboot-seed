package spring.boot.seed.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.boot.seed.exception.AppException;
import spring.boot.seed.exception.ErrorCode;

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
		if(token.startsWith("Bearer ")){
			token = token.split(" ")[1];
		}
		try {
			return JWT.require(Algorithm.HMAC256(secretKey))
					.build()
					.verify(token);
		}catch (TokenExpiredException e){
			throw new AppException(ErrorCode.EXPIRED_TOKEN, ErrorCode.EXPIRED_TOKEN.getMessage());
		}catch (JWTDecodeException e){
			throw new AppException(ErrorCode.WRONG_TYPE_TOKEN, ErrorCode.WRONG_TYPE_TOKEN.getMessage());
		}
	}

	public static String getMemberName(String token){
		DecodedJWT decodedJWT = decodedToken(token);
		return decodedJWT.getClaim("memberName").asString();
	}

}
