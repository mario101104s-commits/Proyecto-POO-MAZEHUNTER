# MazeHunter - El Templo Perdido 🏰

Juego de laberinto implementado en Java con patrón **Modelo-Vista-Controlador (MVC)**.

## 📋 Requisitos

- Java JDK 8 o superior
- Librería GSON 2.10.1 (incluida en `lib/`)

## 🏗️ Arquitectura MVC

### Modelo
- **Paquete**: `Main.modelo`
- **Responsabilidad**: Entidades de dominio (Usuario, Juego, Jugador, Laberinto)
- **Características**: POJOs puros sin lógica de negocio

### Vista
- **Paquete**: `Main.ui.consola`
- **Responsabilidad**: Presentación e interacción con el usuario
- **Clases principales**:
  - `AutenticacionConsola` - UI de login/registro
  - `MenuPrincipal` - UI del menú principal
  - `ConsolaLaberinto` - UI del juego

### Controlador
- **Paquete**: `Main.controlador`
- **Responsabilidad**: Lógica de negocio y coordinación
- **Clases principales**:
  - `ControladorAutenticacion` - Lógica de autenticación
  - `ControladorJuego` - Lógica del juego

### Servicios
- **Paquete**: `Main.servicio`
- **Responsabilidad**: Operaciones de negocio y persistencia
- **Interfaces**: Cifrador, ServicioUsuario, ServicioJuego, Persistencia

## 🚀 Compilación y Ejecución

### Opción 1: Usar el script (Recomendado)
```bash
./run.sh
```

### Opción 2: Comandos manuales
```bash
# Compilar
javac -cp "lib/gson-2.10.1.jar" -d out -sourcepath src $(find src -name "*.java")

# Ejecutar
java -cp "out:lib/gson-2.10.1.jar" Main.Main
```

### Opción 3: Desde IntelliJ IDEA
1. Abrir el proyecto en IntelliJ IDEA
2. Asegurarse de que GSON esté en las librerías del proyecto
3. Ejecutar `Main.Main`

## 📁 Estructura del Proyecto

```
Proyecto-POO-MAZEHUNTER/
├── src/Main/
│   ├── controlador/           # 🆕 Controladores MVC
│   │   ├── ControladorAutenticacion.java
│   │   └── ControladorJuego.java
│   ├── modelo/                # Modelo de dominio
│   │   ├── Dominio/
│   │   ├── Transferencia/
│   │   └── Constantes/
│   ├── servicio/              # Capa de servicios
│   │   ├── Interfaces/
│   │   └── Implementaciones/
│   ├── ui/                    # Vistas
│   │   ├── consola/
│   │   └── util/
│   └── Main.java              # Punto de entrada
├── lib/
│   └── gson-2.10.1.jar        # Dependencia GSON
├── datos/                     # Archivos de persistencia
├── out/                       # Clases compiladas
└── run.sh                     # Script de ejecución
```

## 🎮 Cómo Jugar

1. **Registro/Login**: Crea una cuenta o inicia sesión
2. **Nueva Aventura**: Configura el tamaño del laberinto (5x5 a 20x20)
3. **Controles**:
   - `W` - Mover arriba
   - `A` - Mover izquierda
   - `S` - Mover abajo
   - `D` - Mover derecha
   - `M` - Ver mapa completo
   - `G` - Guardar y salir
   - `Q` - Salir sin guardar
4. **Objetivo**: Encuentra la llave 🗝️ y llega a la salida 🚪

## 🎯 Características

- ✅ Patrón MVC correctamente implementado
- ✅ Sistema de autenticación con cifrado AES
- ✅ Generación procedural de laberintos
- ✅ Sistema de guardado/carga de partidas
- ✅ Estadísticas de jugador
- ✅ Múltiples elementos: cristales, trampas, energía, vida
- ✅ Persistencia en JSON

## 📊 Cambios del Patrón MVC

El proyecto fue refactorizado para seguir correctamente el patrón MVC:

- **Antes**: Main.java con 658 líneas mezclando Vista + Controlador
- **Después**: Main.java con 75 líneas como punto de entrada limpio
- **Nuevos**: 2 controladores, 3 vistas implementadas
- **Refactorizados**: 6 servicios corregidos

Ver `walkthrough.md` para detalles completos de la refactorización.

## 👥 Autores

- Mario Sanchez
- Jose Berroteran
- Niyerlin Muñoz

## 📝 Versión

1.0 - Implementación con Patrón MVC (Diciembre 2025)
