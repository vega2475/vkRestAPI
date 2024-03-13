package com.vkRestAPI.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Класс для создания токенов
@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    // Метод, который позволяет из юзера сделать токен
    // Мы не будем генерировать токен опмраясь на все данные, нам хватит юзернейм и список ролей
    public String generateToken(UserDetails userDetails){
        // Формируем payload часть jwt
        Map<String, Object> claims = new HashMap<>();
        // Подшиваем список ролей
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", roles);

        // Теперь нужна инфа о том когда токен создан и когда истечет его время жизни
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        // Собирем токен
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                //Подписываем токен
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // Разбор приходящего токена на части
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Кастомный метод для получения username
    public String getUsername(String token){
        return getAllClaimsFromToken(token).getSubject();
    }

    // Метод для получения списка ролей
    public List<String> getRoles(String token){
        return getAllClaimsFromToken(token).get("roles", List.class);
    }
}
