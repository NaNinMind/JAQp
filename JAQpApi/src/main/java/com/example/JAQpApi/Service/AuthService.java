package com.example.JAQpApi.Service;



import com.example.JAQpApi.DTO.AuthenticationResponse;
import com.example.JAQpApi.DTO.AuthenticationRequest;
import com.example.JAQpApi.Entity.Token.Token;
import com.example.JAQpApi.Entity.Token.TokenType;
import com.example.JAQpApi.Entity.User.*;
import com.example.JAQpApi.Exceptions.NotFoundException;
import com.example.JAQpApi.Repository.UserRepo;
import com.example.JAQpApi.Repository.TokenRepo;
import com.example.JAQpApi.DTO.RegistrationRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepository;
    private final TokenRepo tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    //NO LOGIC TO UNITTEST
    private String StripToken(String _token)
    {
        return _token.substring(7);
    }
    //NO LOGIC TO UNITTEST
    public User GetUserByToken(String _token) throws NotFoundException
    {
        return tokenRepository.findByToken(StripToken(_token)).orElseThrow(() -> new NotFoundException("User", "token" ,_token)).getUser();
    }
    //NO LOGIC TO UNITTEST
    public void register(RegistrationRequest request) throws Exception {
            Optional<User> user = userRepository.findByUsername(request.getUsername());
            if ( user.isPresent() ){
                throw new Exception("User with this username already exists");
            }
            else{
            User userToSave = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

                userRepository.save(userToSave);
            }
        
    }
    //NO LOGIC TO UNITTEST
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws NotFoundException, AuthenticationException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );
        var user = userRepository.findByUsername(((UserDetails)authentication.getPrincipal()).getUsername()).orElseThrow( ()-> new NotFoundException("User not found") );
        var jwtToken = jwtService.generateToken(user);
        
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
    //NO LOGIC TO UNITTEST
    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId());
        if ( validUserTokens.isEmpty() ){
            return;
        }
        //tokenRepository.deleteAll(validUserTokens);
        validUserTokens.forEach(t->{
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    //NO LOGIC TO UNITTEST
    void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
