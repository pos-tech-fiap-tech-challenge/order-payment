//package com.lanchonete.order_payment.infra;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoClient;
//
//@Configuration
//public class MongoConfig {
//
//    @Value("${SPRING_DATA_MONGODB_URI}")
//    private String mongoUri;
//
//    @Value("${SPRING_DATA_MONGODB_USERNAME}")
//    private String username;
//
//    @Value("${SPRING_DATA_MONGODB_PASSWORD}")
//    private String password;
//
//    @Bean
//    public MongoClient mongoClient() {
//        String connectionString = String.format("mongodb+srv://%s:%s@%s",
//                username, password, mongoUri.replace("mongodb+srv://", ""));
//
//        System.out.println("Conectando ao MongoDB com URI: " + connectionString);
//
//        return MongoClients.create(new ConnectionString(connectionString));
//    }
//}
