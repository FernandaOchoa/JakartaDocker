# WebApp con Jakarta y Docker

Plantilla de aplicación web que usa Docker y Docker Compose.

## Tutoriales previos

* Puedes consultar el vídeo de la sesión: [Aquí](https://web.microsoftstream.com/video/43463030-c7be-4b72-b8f7-f4231e42bc3d?list=studio)
```
  00:08:23 Inicio del Live
  00:15:40 Explicación del proyecto a realizar
  00:22:40 Hola Mundo con Jakarta EE y Tomcat
  Explicación de Java Web : JSP y Servlets
  00:47:10 Contenerizar el Proyecto
  - Contenerizar una bd
  - Contenerizar una app de java web
  - Trabajando con puertos
  - Docker Networking
  01:40:30 Docker Compose
  01:51:50 Desplegar una app desde un repositorio
```

* Para acceder al tutorial de la creación del Hola Mundo con Jakarta y Tomcat [clic aquí](https://web.microsoftstream.com/video/43463030-c7be-4b72-b8f7-f4231e42bc3d?list=studio)
* Para acceder al tutorial de la creación del Docker y Docker Compose [clic aquí](https://github.com/FernandaOchoa/JakartaDocker/blob/master/FormularioconDocker.md)

## Instrucciones para levantar y ejecutar la aplicación:

0. Asegúrate de tener Docker instalado y corriendo.
1. Clona este repositorio.
  * Abre la terminal y ejecuta los siguientes comandos:
      ``` 
          cd IdeaProjects
          mkdir LaunchDemo
          cd LaunchDemo
      ```
    * ![image](https://user-images.githubusercontent.com/9124597/168210122-81d164fc-199c-4995-a375-d767add6bdce.png)
    * **cd:** change directory o cambiar de directorio/folder
    * **mkdir:** make directory o crear un directorio/folder 
    * ![image](https://user-images.githubusercontent.com/9124597/168210403-7f2eb1e4-f9df-4290-a04d-55cc60f3b336.png)
    * **cd:** change directory o cambiar directorio/folder
    * ![image](https://user-images.githubusercontent.com/9124597/168210473-df3cb3f9-a1cd-4ed5-a27e-fef2735b9503.png)
  * Clonamos el repositorio copiando la liga
    * ![image](https://user-images.githubusercontent.com/9124597/168210854-3bae6102-42b6-4475-87e0-20f45d4545af.png)
    * En la **terminal** escribimos el comando `git clone ` seguido de lo que copiamos de GitHub
      * ![image](https://user-images.githubusercontent.com/9124597/168210969-120aa83d-7950-4737-ac06-eb1b03fa8009.png)
      * ![image](https://user-images.githubusercontent.com/9124597/168211007-fd9adf12-2c5e-4bf5-8873-1ee0d83bae2b.png)
2. Abre tu IDE **IntelliJ Ultimate** seleccionamos la opción **Open**
      * ![image](https://user-images.githubusercontent.com/9124597/168210686-5c9cc873-a1b7-41e9-89b7-40031aea0a85.png)
   * Seleccionamos el proyecto **JakartaDocker** y damos clic en **OK**
      * ![image](https://user-images.githubusercontent.com/9124597/168211071-c83de48b-0664-431a-8558-8e6afc95b41a.png)
   * Ahora seleccionamos la opción **Trust Project**
      * ![image](https://user-images.githubusercontent.com/9124597/168211140-ad8092a4-838d-4944-a021-41239562eb08.png)
   * Esperamos a que termine de cargar unos segundos.
3. Construimos el archivo war haciendo uso de IntelliJ IDEA, seleccionamos **Build Artifacts**
    * ![image](https://user-images.githubusercontent.com/9124597/168211254-1de7c94d-db53-4336-b4dc-99070a399e8b.png)
    * ![image](https://user-images.githubusercontent.com/9124597/168211305-8c8be5e5-ea9e-4bb8-9a6a-387c06e9a370.png)
  * Ya construido el proyecto, nos aparecerá la carpeta **target** y podremos visualizar el archivo **MyWebApp.war**
    * ![image](https://user-images.githubusercontent.com/9124597/168211442-3ec155ce-2c63-40b9-96f0-fa9dc1166d78.png)
4. Ejecuta el comando `docker-compose up` Esperamos unos segundos en lo que termina su proceso.
    * ![image](https://user-images.githubusercontent.com/9124597/168211636-b0e8685a-19d9-4500-a5b0-54fcd453cc5c.png)
    * ![image](https://user-images.githubusercontent.com/9124597/168211743-175f8ea5-46b1-40c0-bef4-dba5a871bccb.png)
5. Accede a la aplicación desde la dirección http://localhost:8080/MyWebApp/
  * ![image](https://user-images.githubusercontent.com/9124597/168211816-56082fa2-dfe1-410a-b74d-46092e1c92f0.png)
  * Al ingresar tus datos podrás ver que tu aplicación funciona correctamente
    * ![image](https://user-images.githubusercontent.com/9124597/168211891-50faba69-aa85-4a4c-b63e-bd1d4f879031.png)


## Explorando la aplicación


### Base de Datos

  * En la terminal con el comando ```docker exec -it jakartadocker_app-db_1 mysql -uroot -p``` podremos ingresar al contenedor de la base de datos con la contraseña **pasword** 
    * ![image](https://user-images.githubusercontent.com/9124597/168212171-dad04728-f04f-4c43-9750-ecd4499e4ace.png)
  * Mostramos las bases de datos existentes en el contenedor
    * ![image](https://user-images.githubusercontent.com/9124597/168212298-d56b0857-6f0e-445a-bd45-024111d58ba0.png)
  * Especificamos la base de datos que queremos usar
    * ![image](https://user-images.githubusercontent.com/9124597/168212338-df032f1c-10d3-4512-9f05-6b8282deccfe.png)
  * Mostramos las columnas que tiene tabla **Person** de la base de datos que creamos.
    * ![image](https://user-images.githubusercontent.com/9124597/168212453-4ecd591e-ebb4-461c-abfd-d601115e6d09.png)
  * Podemos comparar la tabla con nuestra clase **Person** en Java
    * ![image](https://user-images.githubusercontent.com/9124597/168212693-d4f7fc6d-a276-4292-8bcd-4d43c187b93d.png)
    * Son idénticas, ya que al ejecutar nuestra app, se crea la base de datos tomando como referencia nuestra clase **Persona** (Gracias JPA's)  
  * Realizamos una consulta para saber el contenido de nuestra tabla persona
    * ![image](https://user-images.githubusercontent.com/9124597/168212566-72862121-f256-4f1c-ac0d-952ae03c0c4b.png)

### Contenedores

Tenemos 2 contenedores:

* App
* Base de Datos

![image](https://user-images.githubusercontent.com/9124597/168212960-4c4e7656-70a4-4acf-a375-b55a0fc831bb.png)

 
---

Curso Backend Java Developer para Launch X - Innovacción Virtual.

Material desarrollado por: Fernanda Ochoa - Learning Producer de LaunchX & Mission Commander de LaunchX.

Redes:
* GitHub: [FernandaOchoa](https://github.com/FernandaOchoa)
* Twitter: [@imonsh](https://twitter.com/imonsh)
* Instagram: [fherz8a](https://www.instagram.com/fherz8a/)
 

 
