# Stage Control

Mod Fabric dedicado al servidor para controlar manualmente el acceso al Nether y al End, y ampliar la capacidad de los bundles. No requiere datapack y no modifica el mod Zairoq's Game Enhancements.

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
- Los bundles admiten hasta 3 veces su capacidad vanilla (192 unidades de peso en lugar de 64), conservando el cálculo vanilla de peso y las operaciones normales de inserción/extracción.

## Compilar

Con JDK 25 y Gradle 9.6.0:

```sh
gradle --no-daemon build
```

El JAR se genera en `build/libs/`. GitHub Actions ejecuta el mismo build en cada push a `main` y pull request, y publica el JAR como artifact de la ejecución.

## Validación pendiente

La compilación CI debe confirmar las firmas exactas de Minecraft/Fabric 26.3. Después hay que probar en un servidor de pruebas:
- Portales del Nether y del End, comandos, permisos, reinicio/persistencia, muerte/respawn, perlas de ender y teletransportes iniciados por otros mods.
- Bundles hasta el 100 %, inserción parcial al superar la capacidad, extracción, objetos con distintos tamaños máximos de stack y persistencia en cofres/inventarios.

Un build correcto no sustituye estas pruebas funcionales. El cliente vanilla conserva la interfaz estándar del bundle; su barra de progreso puede marcarlo como lleno al alcanzar el peso vanilla, aunque el servidor todavía permita insertar más objetos.
