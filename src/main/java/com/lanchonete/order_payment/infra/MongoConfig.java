package com.lanchonete.order_payment.infra;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
public class MongoConfig {

//    @Bean
//    public MongoClient mongoClient() {
//        try {
//            // Definir o caminho do TrustStore
//            System.setProperty("javax.net.ssl.trustStore", "/opt/java/openjdk/lib/security/cacerts");
//            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
//
//            // Configurar conexão MongoDB com TLS
//            String connectionString = "mongodb://root:password@lanchonete-instance.c56iugywe6gv.us-east-1.docdb.amazonaws.com:27017/order_payment?" +
//                    "tls=true&retryWrites=false&replicaSet=rs0&readPreference=secondaryPreferred";
//            MongoClientSettings settings = MongoClientSettings.builder()
//                    .applyConnectionString(new ConnectionString(connectionString))
//                    .build();
//
//            return MongoClients.create(settings);
//        } catch (Exception e) {
//            throw new RuntimeException("Erro ao configurar conexão MongoDB com TLS", e);
//        }
//    }
}
