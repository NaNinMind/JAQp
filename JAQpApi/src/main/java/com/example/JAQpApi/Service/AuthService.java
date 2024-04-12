package com.example.JAQpApi.Service;


import com.example.JAQpApi.DTO.AuthenticationResponse;
import com.example.JAQpApi.DTO.AuthenticationRequest;
import com.example.JAQpApi.Entity.Token.Token;
import com.example.JAQpApi.Entity.Token.TokenType;
import com.example.JAQpApi.Entity.User.*;
import com.example.JAQpApi.Exeptions.UserAlreadyExists;
import com.example.JAQpApi.Exeptions.UserNotFoundExeption;
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

    private String StripToken(String _token)
    {
        return _token.substring(7);
    }
    public User GetUserByToken(String _token) throws UserNotFoundExeption
    {
        return tokenRepository.findByToken(StripToken(_token)).orElseThrow(() -> new UserNotFoundExeption("")).getUser();
    }

    public void register(RegistrationRequest request) throws UserAlreadyExists {
            Optional<User> user = userRepository.findByUsername(request.getUsername());
            if ( user.isPresent() ){
                throw new UserAlreadyExists("User with this username already exists");
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

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws UserNotFoundExeption, AuthenticationException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );
        var user = userRepository.findByUsername( ((UserDetails)authentication.getPrincipal()).getUsername() ).orElseThrow( ()-> new UserNotFoundExeption("User not found") );

        var jwtToken = jwtService.generateToken(user);
        
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .id(user.getId())
                .build();
    }

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
