<div align="center">
  <h1>🗡️ MAZE HUNTER 🛡️</h1>
  <p><strong>¡Sobrevive, explora y escapa de las profundidades del laberinto!</strong></p>
</div>

<br>

**Maze Hunter** es una emocionante aventura de exploración y supervivencia donde asumes el rol de un valiente cazador atrapado en un laberinto. Empleando tu astucia y diversos recursos del entorno como bombas, llaves y el ansiado teletransporte, tu objetivo será encontrar la salida antes de que tu energía se agote o caigas presa de las trampas mortales que acechan en las sombras. Cada partida te ofrece una experiencia desafiante y única, invitándote a romper tus propios récords de tiempo y recolección de cristales mientras te sumerges por completo en la niebla de guerra.

Bajo el capó, el código de este proyecto fue diseñado pensando en una arquitectura limpia y escalable, haciendo uso de fuertes fundamentos de programación orientada a objetos y patrones de diseño. Para lograr niveles siempre impredecibles y variados, utilizamos el patrón **Strategy**, el cual nos permite alterar dinámicamente el algoritmo de generación del laberinto en tiempo de ejecución. Del mismo modo, el apartado sonoro está optimizado gracias a la implementación del patrón **Singleton** para la gestión centralizada del audio, evitando fugas de memoria y cruce de pistas. Finalmente, toda la transferencia de información y persistencia de las partidas está respaldada de forma segura por objetos **DTO** (Data Transfer Object), garantizando que tu progreso e inventario se guarden permanentemente mediante la serialización estructurada en archivos **JSON**.

---

## GAMEPLAY COMPLETO

<div align="center">
  <video src="src/resources/video/Gameplay.mov" autoplay loop muted playsinline width="100%"></video>
</div>

---

## MENÚ PRINCIPAL
![CapturaMenu](src/resources/imagenes/CapturaMenu.png)

## INSTRUCCIONES
![CapturaInstrucciones](src/resources/imagenes/CapturaInstrucciones.png)

## JUEGO EN CURSO
![CapturaJuego](src/resources/imagenes/CapturaJuego.png)

## ANALES DEL TEMPLO
![CapturaAnales](src/resources/imagenes/CapturaAnales.png)

<br>

<div align="center">
  <i>Desarrollado con ❤️ usando JavaFX</i>
</div>
