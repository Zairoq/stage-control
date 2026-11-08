# Stage Control

Mod Fabric para controlar manualmente el acceso al Nether y al End, y ampliar la capacidad de los bundles. No requiere datapack y no modifica el mod Zairoq's Game Enhancements.

## Requisitos

- Minecraft Java 26.3
- Java 25
- Fabric Loader 0.19.5
- Fabric API 0.163.0+26.3
- Gradle 9.6.0 para compilar

## Instalación

Instala el mismo JAR en el servidor y en cada cliente que vaya a usar bundles ampliados. El control de dimensiones se ejecuta en el servidor, pero la inserción de objetos en bundles también se procesa en el cliente; por eso el mixin de capacidad debe estar cargado en ambos lados.

## Comandos

- `/stage lock nether`
- `/stage unlock nether`
- `/stage lock end`
- `/stage unlock end`
- `/stage status`

Los comandos requieren permiso de moderador. Los jugadores sin permiso reciben exactamente:

`tengan paciencia :)`

## Comportamiento

- Nether y End empiezan bloqueados por defecto.
- El bloqueo impide que un jugador entre en la dimensión; no expulsa a quienes ya estén dentro cuando se activa.
- El estado se guarda en `stage-control.properties` dentro de la carpeta del mundo y sobrevive reinicios.
- Overworld nunca se bloquea.
- La restricción de dimensiones se aplica en el servidor; los clientes no necesitan el mod para esa función.
- Los bundles admiten hasta 3 veces su capacidad vanilla (192 unidades de peso en lugar de 64), incluyendo objetos no apilables. Por ejemplo, se pueden guardar tres libros encantados, siempre que cada uno tenga el peso vanilla de un objeto no apilable.
- La ampliación de bundles requiere el mod tanto en el servidor como en los clientes que interactúen con ellos.

## Compilar

Con JDK 25 y Gradle 9.6.0:

```sh
gradle --no-daemon build
```

El JAR se genera en `build/libs/`. GitHub Actions ejecuta el mismo build en cada push a `main` y pull request, y publica el JAR como artifact de la ejecución.

## Validación pendiente

Después de compilar, hay que probar en un servidor de pruebas con el mod instalado en ambos lados:
- Tres libros encantados u otros objetos no apilables en un bundle.
- Inserción de objetos apilables, mezcla de pesos y rechazo al superar la capacidad.
- Extracción, intercambio entre inventario y bundle, y persistencia en cofres/inventarios.
- Portales del Nether y del End, comandos, permisos, reinicio/persistencia, muerte/respawn, perlas de ender y teletransportes iniciados por otros mods.

Un build correcto no sustituye estas pruebas funcionales. La interfaz vanilla del bundle puede seguir mostrando la barra de capacidad llena antes de llegar al nuevo límite, aunque la inserción ampliada funcione.
