# Gestor Personal de Tareas

## Nombre y descripción de la aplicación
* **Nombre:** Gestor Personal de Tareas (`com.example.gestorpersonaldetareas`). Alejandro Moreno Luna
* **Descripción:** Aplicación móvil para Android creada con **Kotlin** y **Jetpack Compose** que sirve para organizar tus tareas del día a día en el teléfono y consultar una lista de tareas de ejemplo desde Internet.

---

## Funcionalidades implementadas
* **Crear, ver, editar y borrar tareas:**
  * **Añadir tareas:** Pulsando el botón `+` se abre una ventana para escribir el título, una descripción opcional y elegir la prioridad (`BAJA`, `MEDIA` o `ALTA`).
  * **Ver el listado:** Todas las tareas aparecen ordenadas en una lista desplazable en la pantalla principal.
  * **Marcar como completada:** Cada tarea tiene una casilla (`Checkbox`); al marcarla, el título aparece tachado y el cambio queda guardado.
  * **Editar:** Pulsando el icono del lápiz se puede modificar cualquier dato de una tarea ya creada.
  * **Borrar con seguridad:** Pulsando la papelera aparece una ventana de confirmación para evitar borrar una tarea por error.
* **Comprobación de errores en el formulario:**
  * Si intentas guardar una tarea con el título vacío o con menos de 3 letras, aparece un aviso en rojo debajo del campo y no deja guardarla.
  * El botón de guardar se bloquea tras pulsarlo una vez para evitar crear tareas repetidas por error.
* **Guardado permanente en el teléfono (Room):**
  * Todas las tareas personales se guardan en una base de datos interna llamada `gestor_tareas_db`. Si cierras la aplicación y la vuelves a abrir, tus tareas siguen ahí.
* **Filtro que recuerda tu elección (DataStore):**
  * En la parte superior hay un interruptor llamado *«Mostrar solo pendientes»*. Si lo activas, oculta las tareas ya terminadas y guarda esa preferencia en el móvil para que siga igual la próxima vez que abras la app.
* **Consulta de tareas desde Internet (API REST con Retrofit):**
  * En la pestaña inferior *«Tareas de ejemplo»* hay un botón que se conecta a Internet (`JSONPlaceholder`) para descargar tareas de muestra sin mezclarlas con las tuyas.
  * La pantalla cambia según lo que ocurra: muestra un aviso inicial, un círculo girando mientras descarga los datos, la lista de tareas si todo va bien, o un mensaje de error con un botón **Reintentar** si no tienes conexión a Internet.
* **Compartir tareas con otras apps:**
  * Cada tarjeta tiene un icono de compartir que abre el menú de Android para enviar el texto de la tarea por WhatsApp, correo u otra aplicación instalada. Si no hubiera ninguna app compatible, muestra un aviso en pantalla en lugar de fallar.
* **Adaptación al girar la pantalla y a distintos tamaños:**
  * La barra inferior permite cambiar entre *«Mis Tareas»* y *«Tareas de ejemplo»*, conservando la pestaña abierta y todos los datos aunque gires el móvil o uses una tablet.

---

## Organización básica del proyecto
* **`apk/`**
  * `app-debug.apk`: Archivo instalable de la aplicación listo para probar en cualquier móvil o emulador Android.
* **`app/src/main/`**
  * `AndroidManifest.xml`: Archivo principal de configuración de la app donde se declara la pantalla de inicio y el permiso `INTERNET` para poder conectarse a la red.
  * **`res/drawable/`**
    * `ic_tareas_local.xml`: Icono local dibujado en la barra superior de la pantalla *«Mis Tareas»*.
  * **`java/com/example/gestorpersonaldetareas/`**
    * `MainActivity.kt`: Es el punto de entrada de la aplicación. Se encarga de arrancar la pantalla, crear el `ViewModel` y mostrar la barra inferior para cambiar entre las dos pestañas.
    * **`model/`**
      * `Tarea.kt`: Define cómo es una tarea por dentro (su identificador único, título, descripción, prioridad y si está completada) y crea la tabla `tareas` en la base de datos.
    * **`data/`**
      * `TareaDao.kt`: Contiene las órdenes para hablar con la base de datos: leer todas las tareas, guardar una nueva, actualizarla o borrarla.
      * `AppDatabase.kt`: Crea y abre la base de datos SQLite en el teléfono asegurándose de que solo exista una única conexión abierta a la vez.
    * **`preferencias/`**
      * `PreferenciasDataStore.kt`: Se encarga de guardar y leer en disco si el interruptor *«Mostrar solo pendientes»* está encendido o apagado.
    * **`comunicaciones/`**
      * `TareaEjemplo.kt`: Define los datos que tiene cada tarea descargada de Internet (`userId`, `id`, `title` y `completed`).
      * `TareasApiService.kt`: Indica la dirección web concreta (`GET /todos`) a la que se piden las tareas de ejemplo.
      * `RetrofitClient.kt`: Configura la conexión con el servidor de Internet y traduce los datos recibidos (formato JSON) a objetos que Kotlin entiende.
    * **`viewmodel/`**
      * `TareaViewModel.kt`: Es el «cerebro» intermedio entre los datos y la pantalla. Recibe las acciones del usuario, pide los datos a la base de datos o a Internet en segundo plano y avisa a la pantalla cuando hay cambios.
    * **`ui/`**
      * `TareasScreen.kt`: Dibuja la pantalla principal de *«Mis Tareas»*, el interruptor de filtro, la ventana de confirmación al borrar y la función de compartir.
      * `TareaItem.kt`: Dibuja cada tarjeta individual de una tarea con su casilla, textos y botones de acción.
      * `TareaDiaLog.kt`: Dibuja la ventana emergente para crear o editar una tarea y comprueba que el título sea válido.
      * `TareasEjemploScreen.kt`: Dibuja la segunda pestaña con el botón de descarga, el indicador de carga, los mensajes de error y la lista de tareas de Internet.
      * `theme/` (`Color.kt`, `Theme.kt`, `Type.kt`): Define los colores, estilos y tipos de letra de la aplicación.

---

## Bibliotecas utilizadas
* **Jetpack Compose y Material 3:** Sirven para construir toda la parte visual de la aplicación (botones, listas, tarjetas y ventanas) de forma moderna.
* **Room:** Sirve para guardar las tareas en una base de datos SQLite dentro del móvil sin tener que escribir código complicado de gestión de archivos.
* **Preferences DataStore:** Sirve para guardar ajustes sencillos del usuario (en este caso, el estado del interruptor de filtro) de forma permanente.
* **Retrofit y Gson:** Retrofit sirve para conectarse a servidores de Internet y pedir datos; Gson traduce automáticamente la respuesta del servidor (JSON) a variables de Kotlin.
* **Corrutinas y ViewModel:** Permiten hacer tareas pesadas (como leer la base de datos o descargar datos de Internet) en segundo plano para que la pantalla nunca se quede bloqueada, y mantienen los datos vivos al girar el dispositivo.

---

## Instrucciones para ejecutar la aplicación
* **Opción 1 — Abrir el proyecto en Android Studio:**
  1. Descargar o clonar este repositorio (`https://github.com/alejandromoreno25/Gestor-Personal-de-Tareas.git`) y abrir la carpeta **`GestorPersonaldeTareas`** desde Android Studio.
  2. Esperar unos segundos a que Gradle descargue las bibliotecas necesarias (hace falta tener Internet).
  3. Elegir un emulador o móvil con **Android 7.0 (API 24) o superior** y pulsar el botón verde **Run (`▶`)**.
* **Opción 2 — Instalar el archivo APK directamente:**
  1. Descargar el archivo `apk/app-debug.apk` de este repositorio.
  2. Arrastrarlo encima de un emulador Android abierto o instalarlo en un teléfono Android compatible.

---

## Configuraciones de los dos emuladores utilizados
* **Emulador 1 — Pantalla pequeña (Teléfono):**
  * **Modelo:** Medium Phone (6.4", 1080 × 2400 px, 420 dpi).
  * **Sistema:** Android 15 (API 35) — `x86_64`.
* **Emulador 2 — Pantalla grande (Tablet):**
  * **Modelo:** Medium Tablet (10.05", 2560 × 1600 px, hdpi).
  * **Sistema:** Android 15 (API 35) — `x86_64`.

---

## Tabla de pruebas realizadas y resultados obtenidos
| Nº | Prueba | Resultado esperado | Resultado de la prueba |
| :-: | :--- | :--- | :---: |
| 1 | Crear una tarea válida | Se guarda y aparece en la lista. | Correcto (Ambos emuladores) |
| 2 | Intentar guardar un título vacío | Se muestra un error y no se crea la tarea. | Correcto (Ambos emuladores) |
| 3 | Editar una tarea | Los cambios se muestran correctamente. | Correcto (Ambos emuladores) |
| 4 | Marcar una tarea como completada | Su estado cambia y queda guardado. | Correcto (Ambos emuladores) |
| 5 | Cancelar una eliminación | La tarea no desaparece. | Correcto (Ambos emuladores) |
| 6 | Eliminar una tarea confirmando | La tarea desaparece de la base de datos. | Correcto (Ambos emuladores) |
| 7 | Cerrar y volver a abrir la aplicación | Se recuperan las tareas guardadas. | Correcto (Ambos emuladores) |
| 8 | Activar el filtro y reiniciar | Se conserva la preferencia seleccionada. | Correcto (Ambos emuladores) |
| 9 | Consultar la API con Internet | Se muestran las tareas recibidas. | Correcto (Ambos emuladores) |
| 10 | Consultar la API sin conexión | Se muestra un error y se permite reintentar. | Correcto (Ambos emuladores) |
| 11 | Compartir una tarea | Se abre el selector de aplicaciones compatibles. | Correcto (Ambos emuladores) |
| 12 | Cambiar la orientación del dispositivo | La aplicación sigue funcionando y conserva sus datos persistidos. | Correcto (Ambos emuladores) |

---

## Enlace al chat de IA usado durante el desarrollo
* **Enlace a la conversación:**
https://gemini.google.com/share/d/1ithLlOnM8Uim7LZVcKYXTq3RFmBIXG72?usp=sharing
---

## Incidencias conocidas o funcionalidades que no se hayan podido completar
* Ninguna incidencia conocida. Todas las funciones pedidas en la práctica y las 12 pruebas obligatorias funcionan correctamente en ambos emuladores.

---

## Capturas de pantalla
- **Listado principal (`Mis Tareas`):**
  ![Listado principal](capturas/listado_principal.png)
- **Formulario de creación (`Nueva Tarea`):**
  ![Formulario de creación](capturas/formulario_creacion.png)
- **Consulta a la API (`Tareas de ejemplo`):**
  ![Consulta a la API](capturas/consulta_api.png)
- **Interacción relevante (Confirmación de borrado / Compartir / Tablet):**
  ![Interacción relevante](capturas/interaccion_relevante.png)
