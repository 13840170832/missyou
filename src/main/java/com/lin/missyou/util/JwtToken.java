package com.lin.missyou.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtToken {

    private static String jwtKey;
    private static Integer expiredTimeIn;
    private static Integer defaultScope = 8;

    @Value("${missyou.security.jwt-key}")
    public void setJwtKey(String jwtKey) {
        JwtToken.jwtKey = jwtKey;
    }

    @Value("${missyou.security.token-expired-in}")
    public void setExpiredTimeIn(Integer expiredTimeIn) {
        JwtToken.expiredTimeIn = expiredTimeIn;
    }

    public static String makeToken(Long uid, Integer scope) {
        return JwtToken.getToken(uid, scope);
    }

    public static Optional<Map<String,Claim>> getClaims(String token){
        DecodedJWT decodedJWT ;
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try{
            decodedJWT = jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            return Optional.empty();
        }
        return Optional.of(decodedJWT.getClaims());
    }

    public static Boolean verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try{
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            return false;
        }
        return true;
    }

    public static String makeToken(Long uid) {
        return JwtToken.getToken(uid, JwtToken.defaultScope);
    }

    private static String getToken(Long uid, Integer scope) {
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
        Map<String,Date> map = JwtToken.calculateExpiredIssues();


        return JWT.create()
                .withClaim("uid", uid)
                .withClaim("scope", scope)
                .withExpiresAt(map.get("expiredTime"))
                .withIssuedAt(map.get("now"))
                .sign(algorithm);
    }

    private static Map<String, Date> calculateExpiredIssues() {
        Map<String, Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.SECOND, JwtToken.expiredTimeIn);
        map.put("now", now);
        map.put("expiredTime", calendar.getTime());
        return map;
    }
}
