package com.example.main.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName ="PersonalInformation")
public class UserDao {

    @DynamoDBHashKey
    private String emailId;

    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    private int role;

    @DynamoDBAttribute
    private String encryption;

    @DynamoDBAttribute
    public String decryption;

    @DynamoDBAttribute
    public String gender;

    @DynamoDBAttribute
    public String dob;

    @DynamoDBAttribute
    public String phoneNumber;

}