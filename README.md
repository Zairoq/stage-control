# Stage Control

Mod Fabric dedicado al servidor para controlar manualmente el acceso al Nether y al End. No requiere datapack y no modifica el mod Zairoq's Game Enhancements.

## Requisitos

- Minecraft Java 26.3
- Java 25
- Fabric Loader 0.19.5
- Fabric API 0.163.0+26.3
- Gradle 9.6.0 para compilar

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
- La restricción se aplica en el servidor y no requiere instalar el mod en clientes.

## Compilar

Con JDK 25 y Gradle 9.6.0:

```sh
gradle --no-daemon build
```

El JAR se genera en `build/libs/`. GitHub Actions ejecuta el mismo build en cada push a `main` y pull request, y publica el JAR como artifact de la ejecución.

## Validación pendiente

La compilación CI debe confirmar las firmas exactas de Minecraft/Fabric 26.3. Después hay que probar en un servidor de pruebas: portales del Nether, portal del End, comandos y permisos, reinicio/persistencia, muerte/respawn, perlas de ender y teletransportes iniciados por otros mods. Un build correcto no sustituye estas pruebas funcionales.
