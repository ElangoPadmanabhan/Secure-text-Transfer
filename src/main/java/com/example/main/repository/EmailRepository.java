package com.example.main.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.example.main.dao.EmailDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmailRepository {

    Logger logger = LoggerFactory.getLogger(EmailRepository.class);

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public void storeData (EmailDao emailDao) {
        logger.info("storeData");
        dynamoDBMapper.save(emailDao);
    }


   public List<EmailDao> getById(EmailDao emailDao) {

        logger.info("getById");
        DynamoDBQueryExpression<EmailDao> queryExpression = new DynamoDBQueryExpression<EmailDao>()
               .withHashKeyValues(emailDao)
               .withIndexName("receiverEmail-index")
               .withConsistentRead(false);

        return dynamoDBMapper.queryPage(EmailDao.class, queryExpression).getResults();
   }
      
}
