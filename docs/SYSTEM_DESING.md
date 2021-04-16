# Diseño del sistema

## Definición

Las personas tendemos a poseer cientos o miles de objetos en nuestras casas. Unos son artículos personales y otros son compartidos con otros miembros de la familia.  
Encontrar algunas de ellas cuando se necesitan puede ser una tarea costosa en términos del tiempo requerido. En este contexto, un sistema computacional nos podría ayudar a encontrar en menos tiempo un objeto a partir de información sobre el mismo, como su descripción textual, su función, una foto del mismo, el sonido que emite, quienes comparten el objeto, el uso de los espacios en la casa y las últimas ubicaciones conocidas del mismo, en entre otros.  

## Objetivo del sistema y requerimientos

### Objetivo

Desarrollar un sistema que permita realizar la gestión de usuarios, espacios comunes y de los objetos que posee así como las relaciones que tiene el mismo objeto con otros usuarios. Además, facilitar la consulta de los objetos a través de diferentes medios como textos, imágenes, sonidos, quienes comparten el objeto y espacios compartidos.  

Este sistema se encuentra acotado para funcionar en una aplicación móvil y una aplicación web.

## Condiciones

El sistema debe cumplir con las siguientes condiciones:

* Web Back-end
* Web Front-end
* Programación
* Aplicaciones móviles nativas
* Base de datos estructuradas
* Base de datos no estructuradas
* Uso de Git

### Requerimientos

El sistema debe cumplir con los siguientes requerimientos:

#### Aplicación móvil  

Requerimiento | Descripción
----------|---------
AMRF-001 | Los usuarios podrán registrarse al sistema.
AMRF-002 | Los usuarios podrán ingresar al sistema.
AMRF-003 | Los usuarios podrán cambiar su contraseña con el correo registrado en caso de olvidarla.
AMRF-004 | El sistema enviará correos de validación de cuenta.
AMRF-005 | Los usuarios podrán agregar/visualizar/editar/eliminar espacios comunes.
AMRF-006 | Los usuarios podrán agregar/visualizar/editar/eliminar objetos.
AMRF-007 | Los usuarios podrán visualizar los objetos agregados en un mapa.
AMRF-008 | Los usuarios podrán encontrar y listar el subconjunto de objetos tal que la suma de sus valores económicos se aproxime más a un valor introducido.
AMRF-009 | La sesión de los usuarios perdurará hasta que se cierre la sesión.
AMRF-010 | Los usuarios podrán cerrar sesión.
AMRNF-011 | La aplicación móvil debe ser fácil, intuitiva y agradable a la vista.

#### Aplicación web

Requerimiento | Descripción
----------|---------
AWRF-001 | Los usuarios podrán registrarse al sistema.
AWRF-002 | Los usuarios podrán ingresar al sistema.
AWRF-003 | Los usuarios podrán agregar/visualizar/editar/eliminar espacios comunes.
AWRF-004 | Los usuarios podrán agregar/visualizar/editar/eliminar objetos.
AWRF-005 | Los usuarios podrán filtrar los objetos agregados.
AWRF-006 | Los usuarios podrán visualizar los objetos agregados en un mapa.
AWRF-007 | Los usuarios podrán visualizar los objetos que tiene cada miembro del grupo.
AWRF-008 | Los usuarios podrán visualizar estadísticas de los objetos como la cantidad de objetos por tipo y las veces que se ha movido por mes.
AWRF-009 | Los usuarios podrán visualizar gráficas de las estadísticas generadas.
AWRF-010 | Los usuarios podrán exportar los resultados de las estadísticas a un PDF.
AWRF-011 | Los usuarios podrán hacer búsquedas de objetos por palabras clave, función, valor económico, persona que comparte el objeto y la ubicación.
AWRNF-012 | La aplicación web debe ser fácil, intuitiva y agradable a la vista.

## Arquitectura planteada

Tomando en cuenta las condiciones del sistema se plantea la siguiente arquitectura:

![Arquitectura planteada](../extras/DIAGRAMA_ARQUITECTURA.png)

### Componentes

#### Dispositivo móvil

Tomando en cuenta las condiciones, la aplicación móvil debe ser desarrollada de manera nativa y exclusivamente para dispositivos Android o iOS.  
Para definir la tecnología se exploran las siguientes alternativas:

Lenguaje | Plataforma | Ventajas | Desventajas
----|----|-------|-----
Java | Android | Código común en el desarrollo, lenguaje robusto, tipado nativo, nuevas implementaciones de programación funcional con java 8, amplia comunidad, entre otras | Aumento en el tiempo de desarrollo, generación de más código del necesario, se está optando por cambiar el desarrollo de Java a nuevas tecnologías.
Kotlin | Android | Curva de aprendizaje corta, interoperabilidad con java, soporta tipado dinámico, validación de datos nulos, manejo de programación funcional, clases dedicadas a datos, entre otras | Lenguaje con una comunidad no tan grande como la de Java, las aplicaciones pueden pesar más, de inicio la legibilidad en el código puede resultar confusa
Objective-C | iOS | Desarrollo eficiente, manejo de recursos a bajo nivel, entre otras | Algunas implementaciones pueden resultar confusas de leer por el desarrollador, se necesita tener un control de punteros para evitar huecos de seguridad
Swift | iOS | Lenguaje de alto nivel, soporta tipado dinámico, se está convirtiendo en el lenguaje standard de desarrollo en iOS, entre otras | Aumenta el tiempo de compilación, es un lenguaje nuevo, no cuenta con soporte para versiones anteriores de iOS

##### Links
[Java vs Kotlin](https://code.tutsplus.com/es/articles/java-vs-kotlin-should-you-be-using-kotlin-for-android-development--cms-2784)
[Swift vs Objective C](https://applecoding.com/analisis/swift-objectivec-analizamos-cual-es-mejor)
[Swift](https://www.altexsoft.com/blog/engineering/the-good-and-the-bad-of-swift-programming-language/)

**Consideraciones extra:** El desarrollo de aplicaciones iOS requiere contar con un sistema operativo macOS.  

#### Conclusión
Se desarrollará la aplicación para la plataforma de Android utilizando [Kotlin](https://developer.android.com/kotlin?hl=es).

#### Servidor web

Con el objetivo de centralizar los recursos y establecer un canal de comunicación bidireccional entre dispositivos se hará uso de un servidor web.
Para definir la tecnología se exploran las siguientes alternativas:

Servidor| Lenguaje | Ventajas | Desventajas
----|-------|-----|---------
Apache | PHP | Muy sencillo de aprender y maquetar aplicaciones, poner en producción un servidor es muy sencillo, es un lenguaje con mucho tiempo en el mercado | La legibilidad del código, necesita una buena planeación para mantener una estructura en los archivos
Jsp | Java | Integridad con módulos de java, herramienta robusta, en su implementación es similar a PHP | Complejidad en el aprendizaje
Node JS | JavaScript | Alta velocidad , amplia comunidad, es una de las herramientas con más crecimiento y uso en la industria, cuenta con un gestor de paquetes, fácil aprendizaje, funciona tanto para proyectos robustos como sencillos, facilita la implementación de APIs | Nativamente no cuenta con tipado de datos, se debe controlar el alcanze de las variables y las funciones callback, se necesita una planeación para estructurar los archivos
Flask | Python | Facil desarrollo, servidor minimalista, escalable, facil puesta en producción, manejo intuitivo de las rutas | Aún no se encuentra en una versión muy estable, no cuenta con librerias integradas
RubyOnRails | Ruby | Trabaja con una estructura de archivos definida, cuenta con una herramienta generadora de archivos, trabaja mediante migraciones, cuenta con su gestor de paquetes | Velocidad, falta de flexibilidad, se debe tener planeado el proceso de desarrollo, mantenibilidad en el largo plazo
Django | Python | Fácil desarrollo, estructura definida, incorpora librerias que facilitan el desarrollo, cuenta con una gran comunidad, es el entorno más popular de desarrollo en python | Cuenta ya con su estilo definido para realizar implementaciones
ASP.NET | C# | Orientado a objetos, veloz, cuenta con soporte de Microsoft | Consumo de recursos, es costosa su implementación en producción

##### Links
[Flask vs Django](https://www.ilimit.com/blog/flask-vs-django/#:~:text=Desventajas%20de%20Flask&text=Se%20trata%20de%20un%20entorno,conectar%20con%20bases%20de%20datos)
[Pros - Cons RubyOnRails](https://www.netguru.com/blog/pros-cons-ruby-on-rails)
[Lenguajes de programación del servidor](https://yosoy.dev/lenguajes-de-programacion-del-lado-servidor/#:~:text=Los%20lenguajes%20de%20lado%20servidor,entre%20otras%20funciones%2C%20sitios%20web)
[Node js pros and cons](https://www.voidcanvas.com/describing-node-js/)

#### Conclusión
Tomando en cuenta el tiempo disponible de desarrollo, la necesidad de generar APIs REST para conectar servicios y el uso de paquetes para elementos especificos se decide usar [Node JS](https://nodejs.org/es/) y dentro de este el paquete para manejar el direccionamiento de rutas será [Express JS](https://expressjs.com/es/).

#### Página web

Partiendo en que el desarrollo back-end del proyecto que será Node JS, se decide utilizar una tecnología que permita la rápida maquetación y alta creabilidad de componentes como lo es [Pug JS](https://pugjs.org/api/getting-started.html).  
Para manejar los estilos de la aplicación se utilizará la librería de [Boostrap](https://getbootstrap.com/), también se hace uso de CSS3 para complementar y adecuar la página web.  
Para manejar toda la parte dinámica de las vistas se utilizará: [VueJS](https://vuejs.org/), [JQuery](https://jquery.com/), [axios](https://github.com/axios/axios) y [chartJS](https://www.chartjs.org/).

#### Bases de datos

Con el objetivo de perdurar la información se hará uso de una base de datos centralizada.  
Dadas las especificaciones del sistema es necesario hacer uso de 3 tipos de bases de datos, una online, otra offline y una online que almacene imágenes. Pudiera existir una 4 base de datos "offline de imágenes" pero esta queda fuera de los alcances definidos del proyecto.  

El flujo de la información indica lo siguiente:
* Al descargar la aplicación la base de datos local se encontrará vacia.
* Una vez el usuario ingrese sesión o mediante una interacción de sincronización se buscará sincronizar la base de datos local con la que se encuentre en la nube.
* Si el usuario quiere realizar algún cambio y tiene internet se actualizará en su base de datos local y en la de la nube.
* Si el usuario quiere realizar algún cambio y **NO** tiene internet **NO** se actualizará en su base de datos local ni en la de la nube.

Dado el flujo anterior y las condiciones del proyecto que indican que se debe usar una base de datos SQL y una NoSQL se analiza lo siguiente:

Tipo de base de datos| Ventajas | Desventajas
----|-------|-----
NoSQL | Escalabilidad, flexibilidad, optimización de consultas, esquemas flexibles, entre otras.  | No hay una linea definida para implementarse en móvil, falta de estandarización, etc.
SQL | Simplicidad de representación de información, puesta en producción sencilla, atomicidad, entre otras. | Poco escalables y flexibles, se debe tener una planeación para el manejo masivo de datos.

#### Links
[SQL vs NoSQL](https://pandorafms.com/blog/es/nosql-vs-sql-diferencias-y-cuando-elegir-cada-una/)

#### Conclusión

La base de datos a utilizar de modo online es la **NoSQL** y como en el servidor se utilizará Node JS, una de las bases de datos que más se adapta a este flujo de datos es [MongoDB](https://www.mongodb.com/es) una base de datos basada en colecciones y documentos.  
La base de datos a utilizar de modo offline es la **SQL** la base de datos que se encuentra presente de manera nativa en los sistemas operativos móviles es [SQLite](https://www.sqlite.org/index.html).
La base de datos a utilizar en la nube es es [**S3 de AWS**](https://aws.amazon.com/es/s3/?c=s&sec=srv)

#### Búsqueda de objeto

Dada una petición del sistema y si el usuario se encuentra conectado a internet se realizará una busqueda del objeto que se pide.
Esta búsqueda se realizará con la librería de [JIMP](https://www.npmjs.com/package/jimp) un paquete para node js que permite realizar la manipulación y comparación de imágenes a través de sus pixeles.


#### Gestor de correos

El sistema implementará un módulo para enviar correos a los usuarios basados en acciones realizadas en el mismo sistema. Este estará gestionado a través de [SendGrid](https://sendgrid.com/).

#### Algoritmo subconjuntos

La aplicación móvil tendrá la funcionalidad de encontrar y listar el subconjunto de objetos tal que la suma de sus valores económicos se aproxime más a un valor introducido.
Si existe más de un subconjunto que cumple con lo anterior entonces mostrar adicionalmente el número de subconjuntos de objetos que mejor aproxima con la misma suma de valores.

Desarrollado por Edgar Pozas
