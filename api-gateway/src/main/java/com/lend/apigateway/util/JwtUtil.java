//package com.lender.apigateway.util;
//
//import com.lender.baseservice.exception.APIException;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.UnsupportedJwtException;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.security.SignatureException;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//
//@Component
//public class JwtUtil {
//
//    public static final String SECRET_KEY = "";
//
//    //validate JWT token
//    public boolean validateToken(String token) {
//        try {
////            Jwts.parser().setSigningKey(SECRET_KEY)
////                    .parseClaimsJws(token);
//            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
//            return true;
//        } catch (SignatureException e) {
//            throw new APIException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
//        } catch (MalformedJwtException e) {
//            throw new APIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
//        } catch (ExpiredJwtException e) {
//            throw new APIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
//        } catch (UnsupportedJwtException e) {
//            throw new APIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
//        } catch (IllegalArgumentException e) {
//            throw new APIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
//        }
//    }
//
//    private Key getSignKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//}
