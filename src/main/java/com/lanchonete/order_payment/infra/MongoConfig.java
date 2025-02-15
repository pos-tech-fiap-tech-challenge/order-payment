package com.lanchonete.order_payment.infra;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
public class MongoConfig {

    @Bean
    public com.mongodb.client.MongoClient mongoClient() {
        try {
            String certPath = "/certs/global-bundle.pem";
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            FileInputStream certInputStream = new FileInputStream(certPath);
            X509Certificate caCert = (X509Certificate) factory.generateCertificate(certInputStream);
            certInputStream.close();

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("caCert", caCert);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString("mongodb://lanchonete-instance.c56iugywe6gv.us-east-1.docdb.amazonaws.com:27017/order_payment?tls=true&retryWrites=false"))
                    .applyToSslSettings(builder -> builder.enabled(true).context(sslContext))
                    .build();

            return MongoClients.create(settings);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar conexão MongoDB com TLS", e);
        }
    }
}
