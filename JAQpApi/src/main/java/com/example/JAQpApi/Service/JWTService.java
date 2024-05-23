package com.example.JAQpApi.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.JAQpApi.Config.SecurityConstants;

@Service
public class JWTService {
    //private static final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    //NO LOGIC TO UNITTEST
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
      }
    //NO LOGIC TO UNITTEST
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
    }
    //NO LOGIC TO UNITTEST
    public String generateToken(UserDetails userDetails) {
      return generateToken(new HashMap<>(), userDetails);
    }
    //NO LOGIC TO UNITTEST
    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ) {
      return buildToken(extraClaims, userDetails, SecurityConstants.JWT_EXPIRATION);
    }
    //NO LOGIC TO UNITTEST
    public String generateRefreshToken(
        UserDetails userDetails
    ) {
      return buildToken(new HashMap<>(), userDetails, SecurityConstants.JWT_REFRESH_EXPRIATION);
    }
    //NO LOGIC TO UNITTEST
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
      return Jwts
              .builder()
              .setClaims(extraClaims)
              .setSubject(userDetails.getUsername())
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis() + expiration))
              .signWith(key, SignatureAlgorithm.HS512)
              .compact();
    }
    //NO LOGIC TO UNITTEST
    public boolean validateToken(String token, UserDetails userDetails) {
      final String username = extractUsername(token);
      return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    //NO LOGIC TO UNITTEST
    private boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
    }
    //NO LOGIC TO UNITTEST
    private Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
    }
    //NO LOGIC TO UNITTEST
    private Claims extractAllClaims(String token) {
      return Jwts
          .parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();
    }
    //NO LOGIC TO UNITTEST
    public Optional<String> getJWTFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return Optional.ofNullable(null);
        }
        return Optional.of(authHeader.substring(7));
    }
}