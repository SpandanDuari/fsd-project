package com.hiretrack.service;
import com.hiretrack.model.User; import com.hiretrack.repo.UserRepository; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service;
@Service public class AuthService { private final UserRepository users; private final PasswordEncoder encoder; public AuthService(UserRepository u,PasswordEncoder e){users=u;encoder=e;} public boolean emailExists(String email){return users.findByEmail(email).isPresent();} public String hash(String password){return encoder.encode(password);} }
