# MazeHunter - El Templo Perdido 🏰 (Fase 2 - Beta)

**Maze Hunter** es una aventura de exploración de laberintos implementada en Java, diseñada bajo los más altos estándares de ingeniería de software, incluyendo el patrón **Modelo-Vista-Controlador (MVC)**, principios **SOLID** y patrones de diseño avanzados.

---

## 🎮 Guía de Juego: Cómo Sobrevivir al Templo

### 🏆 Objetivo de Victoria
Para escapar con éxito del Templo Perdido, debes seguir estos pasos:
1. **Explora**: Muévete por el laberinto usando **WASD**.
2. **Encuentra la Llave 🗝️**: Es indispensable para desbloquear el portal de salida.
3. **Escapa 🚪**: Una vez tengas la llave, busca la salida para ganar la partida.

### 🎒 Objetos y Elementos
| Objeto | Icono | Efecto |
| :--- | :---: | :--- |
| **Cristal** | 💎 | Aumenta tu puntuación en los Anales del Templo. |
| **Energía** | ⚡ | Restaura tu barra de energía para seguir moviéndote. |
| **Vida** | ❤️ | Recupera salud perdida por trampas. |
| **Bomba** | 💣 | Permite destruir **Muros Rojos (%)** pulsando la tecla **K**. |
| **Fósforo** | 🔥 | Recurso necesario para poder activar las bombas. |
| **Trampa** | 💀 | ¡Peligro! Reduce tu vida si pasas sobre ella. |
| **Muro Rojo**| % | Muros especiales que solo pueden ser destruidos con bombas. |

---

## 🌟 Características Principales (Fase 2)

### 🎨 Estética "Lost Temple"
- **Interfaz Premium**: Rediseño completo basado en CSS con una temática de templo antiguo (piedra y oro).
- **HUD Mejorado**: Barra de vida con porcentaje, contador de fósforos, cronómetro en tiempo real y estado de la llave.

### 🧠 Inteligencia y Lógica
- **Solubilidad Garantizada**: Algoritmo **BFS (Breadth-First Search)** que asegura que cada laberinto generado sea completable.
- **Patrón Strategy**: Sistema de dificultades que escala el tamaño y los desafíos del mapa.
- **Niebla de Guerra**: Visibilidad limitada que añade misterio y dificultad a la exploración.

---

## 🏗️ Arquitectura y Patrones
- **MVC**: Separación total entre Modelo, Vista y Controlador.
- **Strategy**: Encapsulamiento de algoritmos de generación de laberintos para permitir diferentes dificultades y estilos.
- **SOLID**: Código mantenible, escalable y desacoplado.
- **DTO**: Uso de objetos de transferencia para una persistencia JSON impecable.

---

## 🚀 Instalación y Ejecución
### Ejecución Rápida
```bash
./run.sh
```
### Compilación con Maven
```bash
mvn clean compile
mvn javafx:run
```

---

## 📁 Estructura Detallada del Proyecto

```
Proyecto-POO-MAZEHUNTER/
├── src/
│   ├── Main/
│   │   ├── controlador/           # Lógica de coordinación MVC
│   │   │   ├── ControladorAutenticacion.java  # Gestión de usuarios
│   │   │   └── ControladorJuego.java          # Gestión de la partida
│   │   ├── modelo/                # Modelo de datos y lógica de negocio
│   │   │   ├── Dominio/           # Entidades (Juego, Jugador, Laberinto, Celda)
│   │   │   ├── Transferencia/     # DTOs para persistencia y estadísticas
│   │   │   └── Constantes/        # Enums (TipoCelda, EstadoJuego, Direccion)
│   │   ├── estrategia/            # Patrones de comportamiento
│   │   │   └── generacion/        # Algoritmos (BFS + Solubilidad)
│   │   ├── servicio/              # Capa de servicios e infraestructura
│   │   │   ├── Interfaces/        # Abstracciones de servicios
│   │   │   └── Implementaciones/  # JSON, Cifrado AES, Lógica concreta
│   │   ├── ui/                    # Capa de presentación (Vistas)
│   │   │   ├── gui/               # Interfaz JavaFX (Lost Temple Theme)
│   │   │   ├── consola/           # Interfaz de texto legacy
│   │   │   └── util/              # Generador de Assets y estilos CSS
│   │   └── resources/             # Recursos estáticos
│   │       └── imagenes/          # Texturas (Piedra, Oro, Trampas)
│   ├── module-info.java           # Configuración de módulos Java
│   └── Main.java                  # Lanzador con reflexión (Fix warnings)
├── datos/                         # Almacenamiento persistente (JSON)
│   ├── usuarios.json              # Base de datos de usuarios
│   ├── juegos/                    # Partidas guardadas
│   └── estadisticas/              # Historial de los Anales
├── tools/                         # Maven local y dependencias
├── pom.xml                        # Configuración de Maven
└── run.sh                         # Script de ejecución rápida
```

---

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Java 22
- **Interfaz Gráfica**: JavaFX 22
- **Gestión de Dependencias**: Maven 3.9
- **Persistencia**: GSON (Google JSON)
- **Seguridad**: Java Cryptography Architecture (AES)
- **Control de Versiones**: Git & GitHub

---

## 📈 Evolución del Proyecto

1. **Fase 1 (Consola)**: Implementación de la lógica base, sistema de movimiento y persistencia inicial en archivos de texto.
2. **Fase 2 (GUI & Refactorización)**: 
   - Migración completa a **JavaFX**.
   - Implementación del patrón **MVC** para desacoplar la lógica de la interfaz.
   - Refactorización a **Maven** para una gestión profesional de dependencias.
   - Creación del tema visual **"Lost Temple"**.
   - Garantía de solubilidad mediante algoritmos de grafos (**BFS**).
   - Implementación del patrón **Strategy** para la generación de laberintos.
   - Implementación del patrón **DTO** para la persistencia de datos.

---




## 📝 Licencia y Versión
**Versión 2.0 (Beta)** - Diciembre 2025.
Desarrollado para la cátedra de Programación Orientada a Objetos.
