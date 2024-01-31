package spring.boot.seed.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
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
	private static String refreshKey;
	private static Long expireTimeMs = 1000 * 60 * 60 * 24L;

	@Value("${jwt.secret}")
	public void setSecretKey(String secret) {
		JwtUtil.secretKey = secret;
	}
	@Value("${refreshKey.secret}")
	public void setRefreshKey(String refreshKey) {
		JwtUtil.refreshKey = refreshKey;
	}
	public static String createAccessToken(String memberName) {
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

	public static String createRefreshToken(String memberName, Long memberId) {
		String issuer = "JWT";
		Algorithm hashKey = Algorithm.HMAC256(refreshKey);
		Date issuedTime = new Date();
		Long refreshExpireTimeMs = 1000 * 60 * 60 * 24 * 7L;
		Date expirationTime = new Date(issuedTime.getTime() + refreshExpireTimeMs);

		return JWT.create()
				.withIssuer(issuer)
				.withClaim("memberName", memberName.toLowerCase())
				.withClaim("memberId", memberId)
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
			throw new AppException(ErrorCode.DO_NOT_DECODE_TOKEN, ErrorCode.DO_NOT_DECODE_TOKEN.getMessage());
		}catch (SignatureVerificationException e){
			throw new AppException(ErrorCode.WRONG_TYPE_TOKEN, ErrorCode.WRONG_TYPE_TOKEN.getMessage());
		}
	}

	public static DecodedJWT verifyRefreshToken(String refreshToken) {
		if(refreshToken.startsWith("Bearer ")) refreshToken = refreshToken.split(" ")[1];

		try {
			return JWT.require(Algorithm.HMAC256(refreshKey))
					.build()
					.verify(refreshToken);

		} catch (TokenExpiredException e) {
			throw new AppException(ErrorCode.EXPIRED_TOKEN, ErrorCode.EXPIRED_TOKEN.getMessage());
		} catch (JWTDecodeException e) {
			throw new AppException(ErrorCode.DO_NOT_DECODE_TOKEN, ErrorCode.DO_NOT_DECODE_TOKEN.getMessage());
		} catch (SignatureVerificationException e) {
			throw new AppException(ErrorCode.WRONG_TYPE_TOKEN, ErrorCode.WRONG_TYPE_TOKEN.getMessage());
		}
	}

	public static String getMemberName(String token){
		try {
			DecodedJWT decodedJWT = decodedToken(token);
			return decodedJWT.getClaim("memberName").asString();
		}catch (Exception e){
			if(!e.getMessage().equals("만료시간이 지난 토큰입니다.")){
				DecodedJWT decodedJWT = verifyRefreshToken(token);
				return decodedJWT.getClaim("memberName").asString();
			}
			throw new AppException(ErrorCode.EXPIRED_TOKEN, ErrorCode.EXPIRED_TOKEN.getMessage());
		}
	}

}
