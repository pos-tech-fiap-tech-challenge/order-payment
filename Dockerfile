# 🔹 Fase 1: Build da aplicação
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

# Copia os arquivos necessários para resolver as dependências primeiro
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código da aplicação
COPY . .

# Define um nome fixo para o JAR e empacota a aplicação
RUN mvn clean package -DskipTests

# 🔹 Fase 2: Runtime - Criar container leve apenas com o JDK
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Criar diretório de certificados
RUN mkdir -p /etc/certs

# Copiar o certificado do DocumentDB
COPY global-bundle.pem /etc/certs/global-bundle.pem

# Importar o certificado no TrustStore do Java
RUN keytool -import -trustcacerts \
    -keystore /opt/java/openjdk/lib/security/cacerts \
    -storepass changeit -noprompt \
    -alias documentdb-cert \
    -file /etc/certs/global-bundle.pem

# Definir explicitamente o trustStore e trustStorePassword no Java no ENTRYPOINT
ENTRYPOINT ["java", "-Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts", "-Djavax.net.ssl.trustStorePassword=changeit", "-jar", "order-payment-app.jar"]

# Copia o JAR gerado na fase de build
COPY --from=build /app/target/order-payment-*.jar order-payment-app.jar

# Expor a porta 8080
EXPOSE 8080
