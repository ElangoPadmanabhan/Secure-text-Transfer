package com.example.main.controller;

import com.example.main.model.request.CreateUserRequest;
import com.example.main.model.request.EmailRequest;
import com.example.main.service.GoogleAuthenticationService;
import com.example.main.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public GoogleAuthenticationService googleAuthenticationService;

    @Autowired
    private UserService userService;

    @GET
    @RequestMapping("/login")
    ResponseEntity<Object> login(@RequestHeader Map<String, String> requestHeaders, HttpServletResponse response) {

        logger.info("inside login method");
        try {
            HashMap<String, String> payload = googleAuthenticationService.validateGoogleToken(requestHeaders.get("authentication"));
            Cookie cookie = new Cookie("authentication", requestHeaders.get("authentication"));
            response.addCookie(cookie);
            return new ResponseEntity<>(userService.userResponse(payload.get("email")), HttpStatus.OK);
        }catch (Exception exception) {
            return new ResponseEntity<>("Error while processing", HttpStatus.BAD_REQUEST);
        }
    }

    @POST
    @RequestMapping("/createUser")
    ResponseEntity<Object> createAUser(@CookieValue(value = "authentication") String authentication, @ModelAttribute CreateUserRequest createUserRequest) {

        logger.info("inside createAUser method");
        try {
            HashMap<String, String> hashMap = googleAuthenticationService.validateGoogleToken(authentication);
            return new ResponseEntity<>(userService.createANewUser(createUserRequest, hashMap), HttpStatus.OK);
        }catch (Exception exception) {
            return new ResponseEntity<>("Error while processing", HttpStatus.BAD_REQUEST);
        }
    }

    @GET
    @RequestMapping("/getAllEmailId")
    ResponseEntity<Object> getEmailList(@CookieValue(value="authentication") String authentication) {
        logger.info("inside getEmailList method");
        try {
            googleAuthenticationService.validateGoogleToken(authentication);
            return new ResponseEntity<>(userService.getEmailList(), HttpStatus.OK);
        }catch (Exception exception) {
            return new ResponseEntity<>("Error while processing", HttpStatus.BAD_REQUEST);
        }
    }

    @POST
    @RequestMapping("/sendingEmail")
    ResponseEntity<Object> sendingEmail(@CookieValue(value="authentication") String authentication, @ModelAttribute EmailRequest emailRequest) {
        logger.info("inside sendingEmail method");
        try {
            HashMap<String, String> hashMap = googleAuthenticationService.validateGoogleToken(authentication);
            return new ResponseEntity<>(userService.sendEmail(emailRequest, hashMap), HttpStatus.OK);
        }catch (Exception exception) {
            return new ResponseEntity<>("Error while processing", HttpStatus.BAD_REQUEST);
        }
    }

    @GET
    @RequestMapping("/getMailByUser")
    ResponseEntity<Object> getMailByUser(@CookieValue(value = "authentication") String authentication) {
        logger.info("inside getMailByUser method");
        try {
            HashMap<String, String> hashMap = googleAuthenticationService.validateGoogleToken(authentication);
            return new ResponseEntity<>(userService.getEmailById(hashMap.get("email")), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>("Error while processing", HttpStatus.BAD_REQUEST);
        }
    }
}
