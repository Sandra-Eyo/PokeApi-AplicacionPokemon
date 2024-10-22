# Tarea Aplicación Pokemon - Juego PokeApi

## Descripción

Realizar un juego ~~preferiblemente en php~~ que consulte la PokeAPI y tenga como resultado un juego como el del video que se muestra.
TIPS:
	- ~~Comprobación de resultado hecha con AJAX sin volver a recargar la página.~~
	- Para ello habrá un index.php y un ~~checkPokemon.php -> Javascript hará la llamada asíncrona desde index a checkPokemon para comprobar el resultado.~~
	- Se utiliza ~~BootStrap~~.
Crea un repo en GitHub.
Dockeriza la app.
Súbela a DockerHub.

## Tecnologías utilizadas

- **Java**: Lenguaje de programación principal utilizado para desarrollar la aplicación.
- **Spring Boot**: Framework utilizado para crear aplicaciones Java de manera sencilla y rápida.
- **Thymeleaf**: Motor de plantillas para generar contenido HTML dinámico en el lado del servidor.
- **Tailwind CSS**: Framework CSS utilizado para estilizar la aplicación y hacerla visualmente atractiva.

## Cómo usar

1. **Instalación:** Asegúrate de tener Java y Maven instalados en tu máquina. Clona este repositorio.

```bash
   git clone https://github.com/Sandra-Eyo/PokeApi-Tarea0-V.1-DAW.git
```

2. **Compilación:** Compila el proyecto utilizando Maven, asegúrate de usar el jdk 21, el cual puedes configurar en **File > Project Structure** .

```bash
   mvn clean install
```

3. **Ejecución:** Ejecuta la aplicación.

```bash
   mvn spring-boot:run
```

4. **Acceso a la aplicación:** Abre tu navegador y ve a http://localhost:8080 para comenzar usarlo
   
	![imagen](https://github.com/user-attachments/assets/37e5ea54-8213-4278-9c88-a70328b54344)



## Imagen en DockerHub

https://hub.docker.com/repository/docker/sandraeyo/pokeapi/general
