# ZOMBIE2DGAME

Un proyecto de [libGDX](https://libgdx.com/) generado con [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

Este proyecto se generó con una plantilla que incluye launchers simples de aplicación y una extensión de `ApplicationAdapter` que dibuja el logo de libGDX.

## Plataformas

- `core`: Módulo principal con la lógica de la aplicación compartida por todas las plataformas.
- `lwjgl3`: Plataforma principal de escritorio usando LWJGL3; antes se llamaba 'desktop' en documentación antigua.

## Gradle

Este proyecto utiliza [Gradle](https://gradle.org/) para gestionar dependencias.  
Se incluyó el wrapper de Gradle, por lo que puedes ejecutar tareas de Gradle usando `gradlew.bat` o `./gradlew`.  

Tareas y flags útiles de Gradle:

- `--continue`: al usar este flag, los errores no detendrán la ejecución de las tareas.
- `--daemon`: gracias a este flag, se utilizará el daemon de Gradle para ejecutar las tareas seleccionadas.
- `--offline`: al usar este flag, se usarán los archivos de dependencias en caché.
- `--refresh-dependencies`: este flag fuerza la validación de todas las dependencias. Útil para versiones snapshot.
- `build`: compila el código fuente y genera los archivos compilados de todos los proyectos.
- `cleanEclipse`: elimina los datos de proyecto de Eclipse.
- `cleanIdea`: elimina los datos de proyecto de IntelliJ.
- `clean`: elimina las carpetas `build`, que almacenan las clases compiladas y los archivos generados.
- `eclipse`: genera los datos de proyecto para Eclipse.
- `idea`: genera los datos de proyecto para IntelliJ.
- `lwjgl3:jar`: genera el jar ejecutable de la aplicación, que se encuentra en `lwjgl3/build/libs`.
- `lwjgl3:run`: inicia la aplicación.
- `test`: ejecuta tests unitarios (si los hay).

Nota que la mayoría de las tareas que no son específicas de un proyecto pueden ejecutarse con el prefijo `name:`, donde `name` se reemplaza por el ID del proyecto específico.  
Por ejemplo, `core:clean` elimina la carpeta `build` solo del proyecto `core`.

## Descripción
Este proyecto implementa un videojuego de acción en 2D desarrollado con LibGDX en Java, donde el jugador debe enfrentarse a hordas de zombies a través de múltiples rondas. El juego incluye un sistema de animaciones detallado para el jugador y los enemigos, barra de vida, ataques con efectos de audio, y enemigos con comportamientos autónomos. Además, incorpora rondas progresivas, zombies con distintas velocidades y tipos, y gestión de puntuación, ofreciendo una experiencia dinámica y desafiante.

## Controles del juego

W, A, S, D para moverse.  
Espacio para atacar.
