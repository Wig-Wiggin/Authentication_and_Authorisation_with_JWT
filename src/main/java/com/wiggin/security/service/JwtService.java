package com.wiggin.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {


    private final String SECRET_KEY="404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";


    private Key getSignInKey() {

        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    private Claims extractClaimsAll(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private <T> T extractClaim(String token, Function<Claims, T>claimResolver){
        Claims claims = extractClaimsAll(token);
        return claimResolver.apply(claims);
    }

    public String extractUserName(String token) {

        return extractClaim(token,Claims::getSubject);
    }


    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration );
    }


     public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }


    public boolean isTokenValid(String token,UserDetails userDetails){
        String name = extractUserName(token);
        return (name.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
