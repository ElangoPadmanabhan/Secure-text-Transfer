package com.example.main.service;

import com.example.main.dao.EmailDao;
import com.example.main.dao.UserDao;
import com.example.main.model.request.CreateUserRequest;
import com.example.main.model.request.EmailRequest;
import com.example.main.model.response.EmailResponse;
import com.example.main.model.response.UserResponse;
import com.example.main.repository.EmailRepository;
import com.example.main.repository.UserRepository;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailRepository emailRepository;

    public UserResponse userResponse(String emailId){
        logger.info("UserDao method");
        UserDao userDao = userRepository.findById(emailId);
        logger.info("userDetail Dao " + userDao);
        if (userDao == null) {
            UserResponse userResponse = new UserResponse();
            userResponse.setEmailId(emailId);
            userResponse.setRole(1);
            return userResponse;
        } else {
            UserResponse userResponse = new UserResponse();
            userResponse.setEmailId(userDao.getEmailId());
            userResponse.setName(userDao.getName());
            userResponse.setRole(0);
            return userResponse;
        }
    }

    public UserResponse createANewUser(CreateUserRequest createUserRequest, HashMap<String, String> hashMap) {
        logger.info("createANewUser method");
        UserResponse userResponse = new UserResponse();
        UserDao userDao = userRepository.findById(hashMap.get("email"));
        logger.info("userDetail Dao " + userDao);
        if (userDao == null) {
            userDao = new UserDao();
            userDao.setEmailId(hashMap.get("email"));
            userDao.setName(createUserRequest.getName());
            userDao.setGender(createUserRequest.getGender());
            userDao.setDob(createUserRequest.getDob());
            userDao.setRole(0);
            userDao.setPhoneNumber(createUserRequest.getPhoneNumber());
            generateRSAKey(userDao);
            userRepository.createANewRegistration(userDao);
            userResponse.setName(userDao.getName());
            userResponse.setRole(0);
        } else {
            logger.error("Data Already present");
            userResponse.setErrorReason("Data Already Present");
            userResponse.setErrorMessage("The email Id provided is already present");
        }
        return userResponse;
    }

    private void generateRSAKey(UserDao userDao) {

        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();

        userDao.setEncryption(Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()));
        userDao.setDecryption(Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded()));
    }

    public String sendEmail(EmailRequest emailRequest, HashMap<String, String> hashMap) throws Exception {
        logger.info("inside sendEmail method");

        UserDao userDao = userRepository.findById(emailRequest.getReceiverEmail());
        if (userDao != null) {
            EmailDao emailDao = new EmailDao();
            emailDao.setId(String.valueOf(LocalTime.now()));
            emailDao.setSubject(getEncryptedMessage(emailRequest.getSubject(), userDao.getEncryption()));
            emailDao.setText(getEncryptedMessage(emailRequest.getText(), userDao.getEncryption()));
            emailDao.setReceiverEmail(emailRequest.getReceiverEmail());
            emailDao.setSenderEmail(hashMap.get("email"));
            emailDao.setDate(String.valueOf(LocalDate.now()));

            emailRepository.storeData(emailDao);
        } else {
            logger.info("user not found");
            return "User not found";
        }
        return "Email sent successfully!";
    }

    public List<UserResponse> getEmailList() {
        logger.info("Inside getEmailList method");
        List<UserDao> userDaoList = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();

        for (UserDao userDao : userDaoList) {
            UserResponse userResponse = new UserResponse();
            userResponse.setEmailId(userDao.getEmailId());
            userResponse.setName(userDao.getName());

            userResponseList.add(userResponse);
        }

        return userResponseList;
    }

    public List<EmailResponse> getEmailById(String emailId) {
        logger.info("inside getEmailById method");
        List<EmailResponse> emailResponseList = new ArrayList<>();
        EmailDao emailDao = new EmailDao();
        emailDao.setReceiverEmail(emailId);
        List<EmailDao> emailDaos = emailRepository.getById(emailDao);

        if (!emailDaos.isEmpty()) {
            UserDao userDao = userRepository.findById(emailId);
            emailDaos.forEach((emailDao1 -> {
                EmailResponse emailResponse = new EmailResponse();
                emailResponse.setDate(emailDao1.getDate());
                emailResponse.setId(emailDao1.getId());
                emailResponse.setSenderEmail(emailDao1.getSenderEmail());
                try {
                    emailResponse.setSubject(decryptString(emailDao1.getSubject(), userDao.getDecryption()));
                    emailResponse.setText(decryptString(emailDao1.getText(), userDao.getDecryption()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                emailResponseList.add(emailResponse);
            }));
        }
        return emailResponseList;
    }

    private String getEncryptedMessage(String message, String publicKey) throws Exception {

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey1 = keyFactory.generatePublic(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey1);
            return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes("UTF-8")));
        }catch (Exception exception) {
            throw new Exception("Error in encryption");
        }
    }

    private String decryptString(String content, String decryptionKey) throws Exception {

        logger.info("Inside decryptString method");
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(decryptionKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(content.getBytes())));
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }
}
