package com.example.main.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.main.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    Logger logger = LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public UserDao findById(String emailID) {
        logger.info("Find my emailID");
        return dynamoDBMapper.load(UserDao.class, emailID);
    }

    public void createANewRegistration(UserDao userDao) {
        logger.info("createANewRegistration");
        dynamoDBMapper.save(userDao);
    }

    public List<UserDao> findAll() {
        logger.info("Inside findAll method");
        return dynamoDBMapper.scan(UserDao.class, new DynamoDBScanExpression());
    }
}
