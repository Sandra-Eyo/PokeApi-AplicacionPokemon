# Usar una imagen base con JDK
FROM openjdk:21

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el JAR de la aplicación desde la máquina local al contenedor
COPY target/PokeApi-0.0.1-SNAPSHOT.jar /app/PokeApi-0.0.1-SNAPSHOT.jar

# Puerto que expone la aplicación (debe coincidir con el que usa Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/PokeApi-0.0.1-SNAPSHOT.jar"]
