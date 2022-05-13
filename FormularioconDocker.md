## Web app Formulario
Normalmente cuando iniciamos en el desarrollo web, se requiere de un servidor para poder ejecutar nuestro código. Así se ve lo que vamos a desarrollar.

<img src="https://user-images.githubusercontent.com/9124597/168213834-18e039cc-1bc1-432b-9eff-cc11e1ac5b23.png" width="400"/>

* Issue to solve:  Si desarrollamos de la forma tradicional, al querer aumentar la cantidad de desarrolladores en un proyecto, surgen problemas de operabilidad, compatibilidad, versiones, dependencias y se torna complicado que cada vez más personas trabajen en el mismo proyecto.

  * <img src="https://user-images.githubusercontent.com/9124597/168214383-cd735bb8-0dd6-41a4-a91a-6b48ac2d7a10.png" width="400"/>

* Solución: Empaquetar nuestra aplicación para trabajar independiente de plataformas, sistemas operativos, versiones, drivers, etc.

  * <img src="https://user-images.githubusercontent.com/9124597/168214416-3b844e0b-5590-409b-a235-a9719b6dfa1d.png" width="400"/>

### Creando una base de datos en un contenedor

* Creamos una imagen desde el DockerHub
  * ```docker pull mysql```
* Creamos un contenedor de la imagen
  * Asignamos los siguientes datos:
    * --name: Nombre del contenedor
    * -d: Modo Detach (Los logs no aparecen en la terminal)
    * -e: Variable de entorno
    * MYSQL_ROOT_PASSWORD: Contraseña para la base de datos
    * MYSQL_DATABASE: Nombre de la base de datos
  * ```docker run --name app-db -d -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=myDB mysql```
  * Ejecutamos el contenedor.  
    * <img src="https://user-images.githubusercontent.com/9124597/168214586-8580cbe4-9bc3-4626-802a-797b296d0160.png" width="400"/>
  * Verificamos con el comando ```docker ps```
  * Revisamos los logs de nuestro contenedor con el comando ```docker logs app-db```
  
**Nota:** Actualmente existe un debate si debemos contener las bases de datos para las aplicaciones que se ejecutan en producción. Hay muchos argumentos válidos en ambos lados
  que querrás considerar cuando estés liste para que tu aplicación entre en producción. Sin embargo para entornos de desarrollo donde la integridad de los datos no es 
  una preocupación, la creación de contenedores de tu base de datos puede ser beneficiosa debido a lo fácil y rápido que es configurar una base de datos con Docker.

Otro punto es qque con esta configuración actual, mis datos se borrarán cada vez que vuelva a crear mi contenedor. Para mi entorno de desarrollo, está bien. Pero si estás
pensando en usar bases de datos en producción y te preguntas cómo funciona, Docker te permite almacenar datos a largo plazo utilizando volúmenes que te permiten almacenar
tus datos en el sistema de archivos del host.


### Preparando nuestra aplicación para contenerizar

Ahora que tenemos nuestro contenedor de base de datos en funcionamiento, creamos nuestro segundo contenedor que tendrá nuestra aplicación web.
Comenzaremos revisando nuestro template de aplicación:

* Jsp: Contiene el formulario que el usuario ve cuando navega a la aplicación.
  * ![image](https://user-images.githubusercontent.com/9124597/168389857-d306a7db-2b43-4c8a-9cf4-39ecc7734632.png)

* Servlet: Cuando el usuario envía el formulario, se manda llamar el servlet que crea un objeto que contiene la informació pasada por el usuario y la conserva en la base de datos.
* Persistence.xml: Especifica la url que utiliza la aplicación para conectarse a la base de datos. Esto será importante más adelante.

Preparemos la aplicación para agregarla a un contenedor. Para hacer eso, seguiremos los pasos:

* Crear una imagen
  * Dockerfile: Archivo con la especificación de como crear una imagen que tendrá nuestro código de aplicaciones, servidor y JVM.
  * Añadir el server tomcat en el file ``` FROM tomcat:10-jdk11 ```
  * Rebuild mi archivo war
    * ``` mvn clean install ```
      * Para obtener esto: 
        * ![image](https://user-images.githubusercontent.com/9124597/167516679-af8764de-24c0-4bd1-bd8a-de1542cd47ce.png)
  * Añado el target de mi proyecto como primer argumento y como segundo argumento agrego el target directory donde el war file estará guardado en la imagen, es decir en donde tomcat necesita que estén para poder funcionar. ``` ADD target/MyWebApp.war /usr/local/tomcat/webapps/MyWebApp.war ```
  * Desde CMD ejecuto el script ```catalina.sh``` para iniciar el server de tomcat. ``` CMD ["catalina.sh", "run"] ```
  
 Básicamente mi archivo dice: 
 * Construye mi imagen tomando como base una imagen que incluye Java 17 y Tomcat 10.
 * Mueve mi archivo war a un directorio de tomcat apropiado.
 * Inicia Tomcat.

```
FROM tomcat:10-jdk17
ADD target/MyWebApp.war /usr/local/tomcat/webapps/MyWebApp.war
CMD ["catalina.sh", "run"]
```

### Construyamos la imagen 

* En la terminal ejecutamos el siguiente comando: ```docker build -t my-web-app:1.0 .```
* Verificamos la ejecución: ``` docker images ```
* Ahora estamos listos para crear nuestro contenedor de aplicaciones.

### Creamos el contenedor de la app

* En la terminal ejecutamos lo siguiente:
 * ``` docker run --name app -d my-web-app:1.0 ```
 * <img src="https://user-images.githubusercontent.com/9124597/168214699-8893c79c-dc1a-4d63-bd48-5fb0c558a1ca.png" width="400"/>
* Buscamos las imagenes activas: ```docker images ```
* Verificamos con el comando ```docker ps```
* Revisamos los logs de nuestro contenedor con el comando ```docker logs app```
* Si intentamos acceder a nuestra app desde el puerto 8080 en localhost obtendremos un error.
 * ![image](https://user-images.githubusercontent.com/9124597/167518292-3f28a715-f128-4a2e-a2a6-5d9867f0c640.png)
 
### Trabajando con puertos

En mi computadora tengo un número de puertos en el que procesos y servicios se ejecutan y reciben peticiones. En mi entorno original, el servidor tomcat tenía directamente un puerto asignado de manera automática el 8080, el cuál es el puerto default de tomcat. Esto me permite probar mi aplicación haciendo una petición http al servidor local (localhost) en el puerto 8080.

<img src="https://user-images.githubusercontent.com/9124597/168214858-2001c081-8a71-4489-9544-47112ac3ef3b.png" width="400"/>

Ahora en mi nuevo entorno que utiliza Docker, existe una capa extra de aislamiento con mis contenedores, lo que significa que mientras nuestro contenedor de aplicaciones se inicia y corre en el puerto 8080 en este contenedor, docker no sabe que está pasando en ese contenedor. Para solucionarlo, debemos proveerlo de información extra de los contenedores. Así que tenemos un par de detalles extra que debemos proporcionarle a docker para tener el contenido de nuestro contenedor visible fuera del mismo. 

<img src="https://user-images.githubusercontent.com/9124597/168215001-75611a92-e5bf-4c15-bace-c313b4ed92fb.png" width="400"/>


Primero, debemos decirle a docker que hay un proceso en el contenedor que escucha en un puerto específico en tiempo de ejecución. Lo hacemos a través de la exposición del puerto. Por ejemplo, para nuestro contenedor de la app debemos exponer el puerto 8080 ya que tomcat estará escuchando en ese puerto. Cuando exponemos el puerto, exponemos el puerto a docker que nos permite comunicarnos entre contenedores de docker, sin embargo, eso no hace que sean visibles fuera de docker.

Para poder hacer que los puertos estén disponibles fuera de docker, necesitas publicar el puerto y vincularlo a un puerto en la máquina host.

<img src="https://user-images.githubusercontent.com/9124597/168215067-7909fbdb-1bd6-4cd7-8c62-b45a08d3fb8d.png" width="400"/>

### Exponiendo puertos en docker

En nuestro Dockerfile hacemos los siguientes cambios:
Añadimos el comando expose y reconstruimos nuestra imagen de docker.
```
FROM tomcat:10-jdk17
ADD target/MyWebApp.war /usr/local/tomcat/webapps/MyWebApp.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
```
En la terminal reconstruimos la imagen de docker.

``` docker build -t my-web-app:1.0 . ```


Ahora docker sabe, que este contenedor escucha un proceso en el puerto 8080 y podemos vincularlo a un puerto disponible en la computadora host.
Para ello eliminaremos el contenedor anterior y crearemos uno que nos funcione para este objetivo.

```docker rm -f app```

Para vincular puertos agregamos el tag -p para indicar que tenemos un puerto expuesto, damos el puerto del host y el puerto del contenedor.

``` docker run --name app -d -p 8080:8080 my-web-app:1.0 ```

Intentemos ahora cargar nuestra app en el navegador:

``` localhost:8080/MyWebApp/```

Genial, ahora tenemos nuestra aplicación corriendo en un contenedor desde un puerto local 8080. (Podemos escoger cualquier puerto en nuestra computadora)

<img src="https://user-images.githubusercontent.com/9124597/168215159-d905eaba-0249-453b-bdea-1e9bebaf8b28.png" width="400"/>

``` docker run --name app -d -p 8080:8081 my-web-app:1.0 ```

Puedo borrar mi segunda instancia del contenedor.
``` docker rm -rf 4caracteres_del_id ```

``` docker ps ```

### Comunicandonos entre contenedores 

#### Creación de una red dedicada

En docker hay muchos tipos de redes que nos permiten crear redes seguras para comunicarnos entre contenedores. **Bridge Networks** son unas de las más comunes.
De hecho, si creas una red y no le pasas un tipo, el **bridge network** lo hará por default.

 * <img src="https://user-images.githubusercontent.com/9124597/168215223-60c2aae6-53ec-4719-818a-600c45343d61.png" width="400"/>

Si queremos configurar una red bridge para manejar la comunicación entre mis dos contenedores debemos ejecutar el siguiente comando:

 * ``` docker network create app-network ```

Ahora tenemos una red creada. Revisemos las redes que tenemos ahora:

 * ``` docker network ls ```

Veremos nuestra red, sin embargo veremos también otras 3 redes que docker creo por default:

* Red Host: Elimina el aislamiento de red que existe entre un contenedor y una máquina de host.
* Red none: Deshabilita todas las redes.
* Red bridge: A la que los contenedores están conectados de forma predeterminada.

La recomendación es crear una red dedicada para los contenedores que están destinados a estar conectados entre sí en lugar de usar la red bridge por default que se comparte entre todos los contenedores en el mismo deamon de Docker.

#### Conectar la BD a la red
Ahora que tenemos nuestra **app-network**, debemos conectar nuestros dos contenedores a esta red.

Comencemos con el contenedor app-db:

Ejecutamos el comando, docker network y especificaremos el nombre de la red y luego el contenedor que queremos conectar a la red.

``` docker network connect  app-network app-db ```

<img src="https://user-images.githubusercontent.com/9124597/168215308-9e9b2514-ba93-43df-b72d-0b984d76421a.png" width="400"/>


Ahora nuestro contenedor app-db está conectado a nuestra red de aplicaciones.

#### Conectar la app a la red

A continuación, debemos conectar el contenedor de la aplicación a la red. Sin embargo, antes de hacer eso, debemos cambiar la forma en que la aplicación se conecta a la base de datos cambiando la URL.

Para ello seguiremos los pasos:

* En el archivo de la aplicación que especifica la URL de la base de datos, debemos modificarlo:
 *  ![image](https://user-images.githubusercontent.com/9124597/167974920-9c6dcb16-2cf3-4adb-9b82-b7d0ab8c1b78.png)
 *  En nuestra configuración original sin Docker, nuestra aplicación usaba localhost para acceder a la base de datos MySQL instalada directamente en la máquina. En su lugar, queremos conectar la aplicación al contenedor de la base de datos.
   * ![image](https://user-images.githubusercontent.com/9124597/168389536-2ca75fbd-956e-462f-a4dc-2c7549df9ef8.png)
 * Modificaremos ese parámetro por el nombre de nuestra aplicación, funcionando de la siguiente manera
   * ![image](https://user-images.githubusercontent.com/9124597/168389653-050123e3-8dfa-4ac1-93b6-ebd6e249d262.png)
   * Esto funciona por que ambos contenedores se encuentran conectados a su propia red dedicada que resuelve la dirección correctamente.
* Ahora que hemos realizado un cambio en el código de nuestra aplicación, debemos actualizar nuestra imagen con el nuevo código. 
* Por lo que, reconstruiremos el archivo de nuestra aplicación que está incluido en nuestra imagen de docker. 
  * ``` mvn clean install ```

* Ahora reconstruimos la imagen
  * ``` docker build -t my-web-app:1.0 . ```
* Eliminamos el antiguo contenedor
  * ``` docker rm -f app ```
* Mientras recreamos el contenedor de la aplicación puedo pasar la red que quiero que use mi contenedor junto con el comando de ejecución, en lugar de tener que llamar a la conexión de red por separado. 
  * ``` docker run --name app -d -p 8080:8080 --network=app-network my-web-app:1.0 ```
  * ![image](https://user-images.githubusercontent.com/9124597/167978097-13e89fa3-3f6d-48c4-9a49-7221389df2ce.png)
* Ahora que el contenedor de la aplicación está conectado a la red de aplicaciones, ambos contenedores se comunican entre sí.
  * <img src="https://user-images.githubusercontent.com/9124597/168215566-5e4a11f7-13bc-435a-a55e-5d73d6ec9a7f.png" width="400"/>
* Veamos si nuestra aplicación funciona completamente y puede llegar a la base de datos
  * <img src="https://user-images.githubusercontent.com/9124597/168215642-84fe2067-6116-4134-895c-9ec074dcf3cd.png" width="400"/>
* El contenedor de mi aplicación ahora está correctamente conectado al contenedor de la base de datos y puedo persistir mis datos correctamente.

Hasta ahora es súper tortuoso recordar todos los comandos que se necesitan para lograr que mis contenedores estén funcionando. Por ello existe algo llamado Docker Compose.

### Docker Compose

Docker compose es una herramienta que te permite definir tus servicios de aplicación y básicamente codificar los comandos a ejecutar.

Veamos como podemos usar Docker Compose para nuestra aplicación.

* Creamos un nuevo archivo llamado: **docker-compose.yml** (Yaml es un lenguaje que se usa comúnmente para la configuración y se compone de pares clave-valor, siendo una alternativa a XML o JSON)
* El primer par clave-valor que debemos especificar es la versión que utilizará el archivo de docker compose.
* Definimos los servicios que tiene nuestra aplicación, en nuestro caso el servicio de la base de datos y el otro de la aplicación.
  * Asignamos la imagen de la base de datos
    * Agregamos nuestras variables de entorno
* Definimos los requerimientos de nuestra app
  * Le pedimos a docker que construya la imagen por nosotros si no existe a través de **build: .** que buscará el Dockerfile y creara la imagen antes de iniciar el contenedor.
  * Listamos los puertos a vincular
  * Declaramos una dependencia en el servicio de la base de datos, de esa manera, el servicio de mi aplicación no aparecerá hasta que se haya iniciado el servicio de mi base de datos.

Debe quedar de la siguiente manera:

```
version: "3"
services:
  app-db:
    image: mysql
    environment:
      - MYSQL_ROOT_PASSWORD=password
      - MYSQL_DATABASE=myDB
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - app-db
```
Ahora, estamos listos para usar nuestro archivo docker-compose para abrir nuestra app. Antes de eso, hagamos un poco de limpieza y eliminemos los contenedores que creamos anteriormente usando los comandos de docker. Esto nos ayudará a evitar errores de vinculación de puertos cuando docker compose intente asignar el puerto 8080 en la máquina host.

```
docker rm -f app
docker rm -f app-db
docker ps
```

![image](https://user-images.githubusercontent.com/9124597/168390125-d23cdd82-bf08-4223-bea1-464854346586.png)

Ahora ejecutamos el comando ``` docker compose up -d``` que abrirá nuestra aplicación creando e iniciando nuestros dos contenedores.

Si ejecutamos ``` docker ps ``` veremos los dos contenedores que docker compose creó para nosotros.

Al hacer uso de docker compose, no necesitamos crear una conexión de tipo bridge ya que docker compose se encarga de definirlas de manera autómatica para los servicios de aplicaciones que se encuentren en el ```docker-compose.yml```

### Docker Up and Running

* Abrimos la terminal y buscamos el directorio: **IdeaProjects** con el comando **cd IdeaProjects** 
* Clonamos el repositorio con el comando: git clone [https://github.com/FernandaOchoa/JakartaDocker](https://github.com/FernandaOchoa/JakartaDocker)
* Abrimos intelliJ y seleccionamos **build**, **artifacts** y **MyWebApp** 
* Esperamos a que termine de construirlo
* Ahora ejecutamos el comando **docker-compose up**
* Esperamos unos segundos a que termine las configuraciones
* Accedemos a la aplicación en la liga http://localhost:8080/MyWebApp/



  




