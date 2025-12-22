# Documentación Técnica - Maze Hunter (Fase 2)

Este documento detalla la arquitectura, patrones de diseño y principios de ingeniería de software aplicados en el desarrollo de **Maze Hunter**.

---

## 1. Arquitectura MVC (Modelo-Vista-Controlador)
El proyecto sigue estrictamente el patrón MVC para separar las responsabilidades y facilitar el mantenimiento:

- **Modelo (`Main.modelo`)**: Contiene la lógica de negocio y los datos. 
    - `Dominio`: Clases como `Juego`, `Jugador`, `Laberinto` y `Celda`.
    - `Constantes`: Enums como `TipoCelda` y `EstadoJuego`.
    - `Transferencia`: Objetos como `ResultadoJuego` y `EstadisticasJuego`.
- **Vista (`Main.ui`)**: Encargada de la representación visual.
    - `gui`: Implementación en JavaFX (`VentanaPrincipal`, `VistaJuego`, `VistaAnales`).
    - `consola`: Implementación original en texto para depuración y fallback.
- **Controlador (`Main.controlador`)**: Actúa como intermediario, gestionando la entrada del usuario y actualizando el modelo.
    - `ControladorJuego`: Orquestador de la partida.
    - `ControladorAutenticacion`: Gestión de usuarios y seguridad.

---

## 2. Patrón Strategy (Estrategia)
Se aplicó para la **generación de laberintos**. 

- **Interfaz `GeneradorLaberinto`**: Define el contrato para cualquier algoritmo de generación.
- **Implementación `GeneradorLaberintoDificultad`**: Implementa una estrategia específica que varía el tamaño, número de trampas, bombas y fósforos según la dificultad seleccionada.
- **Beneficio**: Permite cambiar o añadir nuevos algoritmos de generación (ej. Prim, Kruskal) sin modificar el resto del sistema.

---

## 3. Principios SOLID
Hemos aplicado los 5 principios para garantizar un código robusto:

1.  **S (Single Responsibility)**: Cada clase tiene una única razón para cambiar. Por ejemplo, `PersistenciaJASON` solo se encarga de leer/escribir archivos, no de la lógica del juego.
2.  **O (Open/Closed)**: El sistema está abierto a la extensión pero cerrado a la modificación. Gracias al patrón Strategy, podemos añadir nuevas dificultades o tipos de laberinto sin tocar el código existente.
3.  **L (Liskov Substitution)**: Las implementaciones como `ServicioJuegoImpl` pueden sustituir a su interfaz `ServicioJuego` en cualquier parte del código sin romper la funcionalidad.
4.  **I (Interface Segregation)**: Hemos creado interfaces específicas (`Cifrador`, `ServicioUsuario`) en lugar de una única interfaz gigante, evitando que las clases dependan de métodos que no usan.
5.  **D (Dependency Inversion)**: Los controladores dependen de abstracciones (interfaces), no de implementaciones concretas. Esto facilita las pruebas unitarias y el intercambio de componentes.

---

## 4. DTO (Data Transfer Objects)
Para la persistencia de datos, implementamos **DTOs** dentro de `PersistenciaJASON`:

- **Propósito**: Desvincular el modelo de dominio (`Juego`, `Jugador`) de la estructura de almacenamiento (JSON).
- **Implementación**: Clases como `JuegoDTO` y `JugadorDTO` capturan solo los datos necesarios para guardar. Esto evita problemas de recursividad circular al serializar y protege la integridad del modelo de dominio.

---

## 5. Interfaz Gráfica (JavaFX)
La transición de consola a GUI se realizó manteniendo la coherencia estética y técnica:

- **Componentes Personalizados**: `VistaJuego` utiliza un `Canvas` para renderizar el laberinto de forma eficiente, mientras que `VistaAnales` usa un `TableView` para los datos.
- **Aesthetic Overhaul**: Uso de CSS avanzado para crear el tema "Lost Temple" (piedra y oro) mediante gradientes, sombras y bordes dorados.
- **Solución Técnica (Reflection Launcher)**: Para evitar la advertencia de "unnamed module" en IntelliJ sin forzar una estructura modular compleja, implementamos un lanzador en `Main.java` que utiliza reflexión para cargar `MainApp`. Esto asegura un arranque limpio y profesional.

---

## 6. Refactorización y Balance
- **Fósforos vs Llaves de Explosión**: Se renombró el concepto para que fuera más intuitivo y temático.
- **Solubilidad (BFS)**: Se añadió un algoritmo de *Breadth-First Search* en el generador para garantizar que cada laberinto tenga al menos una ruta válida hacia la salida.
- **Timer en Tiempo Real**: Implementación de un `Timeline` que actualiza el HUD cada segundo, calculando la duración desde el inicio de la partida.

