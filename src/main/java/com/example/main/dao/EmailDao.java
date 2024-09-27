package com.example.main.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName ="MessageContent")
public class EmailDao {

    @DynamoDBHashKey
    private String id;

    @DynamoDBAttribute
    private String subject;

    @DynamoDBAttribute
    private String text;

    @DynamoDBIndexHashKey(attributeName = "receiverEmail", globalSecondaryIndexName = "receiverEmail-index")
    private String receiverEmail;

    @DynamoDBAttribute
    private String senderEmail;

    @DynamoDBAttribute
    private String date;

}
