package ru.clevertec.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.security.entity.Role;
import ru.clevertec.security.entity.User;

import java.util.Date;
import java.util.HashMap;

class JwtServiceTest {

    private final String secretKey = "secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey";

    @Test
    void extractUsernameTest_shouldReturnJWTTokenSubject() {
        //given
        User user = new User(
                1L,
                "andrei",
                "yurueu",
                "dobrowydka",
                "12345",
                Role.ADMIN);

        String token = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), SignatureAlgorithm.HS256)
                .compact();

        //when
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        String actual = claims.getSubject();

        //then
        Assertions.assertEquals(user.getUsername(), actual);
    }

    @Test
    void generateTokenTest_shouldGenerateJWTToken() {
        //given
        User user = new User(
                1L,
                "andrei",
                "yurueu",
                "dobrowydka",
                "12345",
                Role.ADMIN);

        //when
        String actual = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10000000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), SignatureAlgorithm.HS256)
                .compact();

        //then
        Assertions.assertNotNull(actual);
    }

    @Nested
    class IsTokenValidTest {

        @Test
        void isTokenValidTest_shouldReturnTrueIfUsernameIsCorrectAndTokenNotExpired() {
            //given
            String correctToken = Jwts
                    .builder()
                    .setClaims(new HashMap<>())
                    .setSubject("username")
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000000))
                    .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), SignatureAlgorithm.HS256)
                    .compact();

            //when
            boolean result = "username".equals(extractUsername(correctToken)) &&
                    !extractExpiration(correctToken).before(new Date());

            //then
            Assertions.assertTrue(result);
        }

        private Date extractExpiration(String token) {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getExpiration();
        }

        private String extractUsername(String token) {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject();
        }

        @Test
        void isTokenValidTest_shouldReturnFalseIfUsernameIsIncorrectAndTokenNotExpired() {
            //given
            String incorrectToken = Jwts
                    .builder()
                    .setClaims(new HashMap<>())
                    .setSubject("username123")
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000000))
                    .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), SignatureAlgorithm.HS256)
                    .compact();

            //when
            boolean result = "username".equals(extractUsername(incorrectToken)) &&
                    !extractExpiration(incorrectToken).before(new Date());

            //then
            Assertions.assertFalse(result);
        }
    }
}