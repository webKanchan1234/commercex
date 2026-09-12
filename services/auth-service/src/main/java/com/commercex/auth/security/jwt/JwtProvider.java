package com.commercex.auth.security.jwt;

import com.commercex.auth.security.model.CustomUserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties properties;

    private SecretKey getKey() {

        return Keys.hmacShaKeyFor(
                properties.getSecret().getBytes()
        );

    }

    public String generateAccessToken(CustomUserPrincipal principal) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() +
                        properties.getAccessTokenExpiration()
        );

        return Jwts.builder()
                .subject(principal.getUsername())
                .issuedAt(now)
                .expiration(expiry)
                .claim(
                        "roles",
                        principal.getAuthorities()
                                .stream()
                                .map(a -> a.getAuthority())
                                .toList()
                )
                // ====================================================
                // Session Versioning
                // Every JWT carries the user's token version.
                // If password changes or admin invalidates sessions,
                // increment tokenVersion in DB.
                // Old JWTs become invalid immediately.
                // ====================================================
                .claim(
                        "version",
                        principal.getUser().getTokenVersion()
                )
                .signWith(getKey())
                .compact();

    }

    public String extractUsername(String token){

        return Jwts

                .parser()

                .verifyWith(getKey())

                .build()

                .parseSignedClaims(token)

                .getPayload()

                .getSubject();

    }


    public boolean validateToken(String token){

        try{

            Jwts

                    .parser()

                    .verifyWith(getKey())

                    .build()

                    .parseSignedClaims(token);

            return true;

        }

        catch (ExpiredJwtException ex){
            return false;
        }
        catch (MalformedJwtException ex){
            return false;
        }
        catch (SecurityException ex){
            return false;
        }
        catch (IllegalArgumentException ex){
            return false;
        }

    }


    public String generateRefreshToken(CustomUserPrincipal principal){

        Date now = new Date();

        Date expiry = new Date(

                now.getTime()

                        +

                        properties.getRefreshTokenExpiration()

        );

        return Jwts.builder()

                .subject(principal.getUsername())

                .issuedAt(now)

                .expiration(expiry)

                .claim("type","REFRESH")

                .signWith(getKey())

                .compact();

    }

    public Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public Date extractExpiration(String token) {

        return extractClaims(token).getExpiration();

    }

    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());

    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {

        return (List<String>) extractClaims(token)
                .get("roles");

    }

    public Integer extractTokenVersion(String token) {

        return extractClaims(token)
                .get("version", Integer.class);

    }


    public boolean isRefreshToken(String token){

        String type = extractClaims(token)
                .get("type", String.class);

        return "REFRESH".equals(type);

    }




}