package com.tms.speeding.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.xml.bind.DatatypeConverter;

import com.tms.speeding.entity.Login;
import com.tms.speeding.repository.LoginRepository;
import com.tms.speeding.util.CustomException;
import com.tms.speeding.util.ResponseObject;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final LoginRepository repository;

    public LoginService (LoginRepository repository) {
        this.repository = repository;
    }

    private void setUser (String login, String password) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
        UserDetails loggedIn = new User(login, password, authorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(loggedIn, null, loggedIn.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String getHash(String input) {
        try {
            var md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException ex) {
            throw new CustomException("Something went wrong");
        }
    }

    public ResponseObject logIn(String login, String password) {
        String hash = getHash(password);
        Optional<Login> request = repository.checkLogin(login, hash);
        if (request.isEmpty()) {
            return new ResponseObject(false, "error", "Invalid login credentials");
        }
        var existing = request.get();
        existing.setLastVisit(new Date());
        repository.save(existing);
        setUser(login, password);

        return new ResponseObject();
    }

    public ResponseObject regIn(String login, String password) {
        Optional<Login> request = repository.findExisting(login);
        if (request.isPresent()) {
            return new ResponseObject(false, "error", "Such login already exists");
        }
        var user = new Login(login, getHash(password), new Date(), new Date());
        repository.save(user);
        setUser(login, password);
        return new ResponseObject();
    }

}





































