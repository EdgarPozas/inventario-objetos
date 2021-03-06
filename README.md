# inventario-objetos

![badge](https://img.shields.io/badge/inventario-v1.0.0-blue)

Aplicación móvil y web enfocada a gestionar un inventario de objetos que poseen
y comparten los miembros de una familia.

## Diseño del sistema

El proyecto cumple con las siguientes características:

### Aplicación móvil

1. Manejo de múltiples usuarios.
2. Manejo de las sesiones de los usuarios.
3. CRUD de los espacios de la casa y sus funciones.
4. CRUD de los objetos de la casa.
5. Visualización de los objetos de la casa por filtros.
6. Visualización de las últimas ubicaciones de los objetos en un mapa.
7. Funcionalidad de encontrar y listar el subconjunto de objetos tal que la suma de sus valores económicos se aproxime más a un valor introducido.

### Aplicación web

1. Visualización de los objetos que tiene cada usuario.
2. Visualización de información de cada objeto además de sus estadisticas.
3. Generación de reportes PDF.
4. Búsquedas de objetos por palabras clave, función, valor económico, usuario que comparte y ubicación.
5. Visualizar en un mapa la ubicación de estos objetos.

Para mayor información sobre la toma de desiciones en la elaboración de estos producto
consultar el [diseño del sistema](https://github.com/EdgarPozas/inventario-objetos/blob/master/docs/SYSTEM_DESING.md)

## Arquitectura

La siguiente imagen muestra la arquitectura planteada

![Arquitectura planteada](./extras/DIAGRAMA_ARQUITECTURA.png)

## Tecnologías utilizadas

Lugar |Tecnologías
-----|------
Aplicación móvil | Android con Kotlin
Servidor web | Node JS con express JS
Página web | Pug JS, Boostrap, Vue JS, JQuery, axios y Chart JS
Base de datos online | MongoDB através del servicio MongoDB Atlas
Base de datos offline | SQLite
Base de datos de archivos | Servicio S3 de AWS
Gestor de correos | Servicio de SendGrid
Algoritmo subconjuntos | Android con Kotlin
Búsqueda de objeto | Paquete JIMP

## Estructura de las carpetas

Carpeta | Descripción
------|------
[/mobile](https://github.com/EdgarPozas/inventario-objetos/tree/master/mobile)| Proyecto móvil en Android utilizando Kotlin
[/server](https://github.com/EdgarPozas/inventario-objetos/tree/master/server)| Servidor web en NodeJS
[/docs](https://github.com/EdgarPozas/inventario-objetos/tree/master/docs)| Documentación
[/algoritm](https://github.com/EdgarPozas/inventario-objetos/tree/master/algorithm)| Prueba del algoritmo para listar subconjuntos
[/compare](https://github.com/EdgarPozas/inventario-objetos/tree/master/docs)| Prueba del algoritmo para comparar imágenes


Desarrollado por Edgar Pozas.
