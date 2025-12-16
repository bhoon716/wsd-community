package wsd.community.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import wsd.community.domain.user.entity.UserRole;
import wsd.community.security.config.SecurityConstant;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateAccess(Long userId, String email, UserRole role) {
        String jti = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiredAt = new Date(now + accessTokenExpiration);

        return Jwts.builder()
                .id(jti)
                .subject(email)
                .claim(SecurityConstant.CLAIM_USER_ID, userId)
                .claim(SecurityConstant.CLAIM_ROLE, role.name())
                .claim(SecurityConstant.CLAIM_TYPE, SecurityConstant.ACCESS_TOKEN_TYPE)
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefresh(Long userId) {
        String jti = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiredAt = new Date(now + refreshTokenExpiration);

        return Jwts.builder()
                .id(jti)
                .subject(String.valueOf(userId))
                .claim(SecurityConstant.CLAIM_USER_ID, userId)
                .claim(SecurityConstant.CLAIM_TYPE, SecurityConstant.REFRESH_TOKEN_TYPE)
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰: {}", e.getMessage());
        } catch (Exception e) {
            log.info("유효하지 않은 토큰: {}", e.getMessage());
        }
        return false;
    }

    public String getEmail(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.get(SecurityConstant.CLAIM_USER_ID, Long.class);
    }

    public long getExpiration(String token) {
        Date expiration = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    public long getRefreshExpiration() {
        return refreshTokenExpiration;
    }
}
