package com.example.votewebback.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    @Value("${jwt.secretKey}")
    private String SECRET_KEY ;

    public String generateToken(UserDetails userDetails){
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = authorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList());

        Map<String, Object> extraCliams = new HashMap<>();
        extraCliams.put("roles", roles); // 권한 정보를 토큰의 claims에 추가

        return Jwts.builder()
                .setClaims(extraCliams)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))//생성시간
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) //만료시간
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact(); //서명
    }
    public boolean isTokenValid(String token,UserDetails userDetails){
        try {
            extractAllClaims(token);
            return true;
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {

            System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {

            System.out.println("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {

            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {

            System.out.println("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsresolver) {
        final Claims claims = extractAllClaims(token).getBody();
        return claimsresolver.apply(claims);
    }

    Jws<Claims> extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
    }
    //시크릿키 할당
    private Key getSignInKey(){
        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = extractAllClaims(token).getBody();

        List<String> roles = (List<String>) claims.get("roles");

        // 권한 정보를 SimpleGrantedAuthority 객체로 변환하여 Set에 담기
        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal,token,authorities);
    }
}
