package com.hiretrack.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import java.nio.charset.StandardCharsets; import java.util.Date;
@Service public class JwtService {
 private final byte[] key; private final long expiration;
 public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.expiration-ms}") long expiration){key=secret.getBytes(StandardCharsets.UTF_8);this.expiration=expiration;}
 public String token(String email,String role){Date now=new Date();return Jwts.builder().subject(email).claim("role",role).issuedAt(now).expiration(new Date(now.getTime()+expiration)).signWith(Keys.hmacShaKeyFor(key)).compact();}
 public String email(String token){return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(key)).build().parseSignedClaims(token).getPayload().getSubject();}
}
