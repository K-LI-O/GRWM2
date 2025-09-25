package GRWM.backend.jwt;

import GRWM.backend.entity.user.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key; // JWT 서명에 사용할 키
    private final long accessTokenExpirationTime; // 액세스 토큰 만료 시간 (밀리초)

    // application.yml/properties에서 JWT Secret Key와 만료 시간을 주입받습니다.
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration-time}") long accessTokenExpirationTime) {

        // Base64 디코딩된 Secret Key를 HMAC SHA 알고리즘에 사용할 키로 변환합니다.
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationTime = accessTokenExpirationTime;
    }

    /**
     * Authentication 객체를 받아서 Access Token을 생성합니다.
     * @param authentication 인증된 Authentication 객체 (인증 후 UserDetails 정보 포함)
     * @return 생성된 JWT Access Token 문자열
     */
    public String generateToken(Authentication authentication) {
        // 1. 권한 정보 가져오기

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); // 콤마로 구분된 문자열로 변환 (예: "ROLE_USER,ROLE_ADMIN")

        // 2. 토큰 만료 시간 설정
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accessTokenExpirationTime);

        // 3. JWT 토큰 생성
        return Jwts.builder()
                .setSubject(authentication.getName()) // subject: 사용자 ID (username)
                .claim("userId", userDetails.getUserId()) // userId 추가
                .claim("communityUserId", userDetails.getCommunityUserId()) // communityUserId 추가
                .claim("auth", authorities) // "auth" 클레임에 권한 정보 저장
                .setIssuedAt(new Date()) // 토큰 발행 시간
                .setExpiration(validity) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 사용할 서명 키와 알고리즘 설정 (HS256 권장)
                .compact(); // JWT 문자열로 압축
    }

    /**
     * JWT 토큰으로부터 인증 정보를 가져옵니다.
     * //@param token JWT Access Token 문자열
     * //@return Authentication 객체
     */


    public Authentication getAuthentication(String token) {

        // 1. 토큰에서 클레임 추출
        Claims claims = parseClaims(token); // parseClaims 함수가 JWT를 파싱하여 Claims를 반환한다고 가정

        // 2. 클레임에서 정보 추출
        String username = claims.getSubject();
        Long userId = claims.get("userId", Long.class);
        Long communityUserId = claims.get("communityUserId", Long.class);


        String authClaim = claims.get("auth", String.class);
        Collection<? extends GrantedAuthority> authorities;

        if (StringUtils.hasText(authClaim)) {
            authorities = Arrays.stream(authClaim.split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else {
            // "auth" 클레임이 없거나 비어있는 경우, 기본 권한 (예: ROLE_USER) 부여
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // 3. CustomUserDetails 인스턴스 생성
        CustomUserDetails principal = new CustomUserDetails(userId, communityUserId, username, authorities);
        // 비밀번호는 이미 인증이 완료되었으므로 빈 문자열("") 또는 null을 전달

        // 4. Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(principal, null, authorities); }

    /**
     * JWT 토큰의 유효성을 검증합니다.
     * @param token JWT Access Token 문자열
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }

    /**
     * 토큰 파싱 시 발생할 수 있는 예외를 처리하고 클레임을 반환하는 헬퍼 메서드
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰의 경우에도 클레임은 추출 가능하므로, 이를 반환하여 만료된 토큰으로 인증 정보를 구성할 수 있도록 함
            // 실제 로직에서는 만료된 토큰은 보통 사용하지 않으므로 validateToken에서 먼저 걸러짐
            return e.getClaims();
        }
    }
}
