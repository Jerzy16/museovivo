# Instrucciones para Debuggear el Problema del Mapa

## Problema Identificado
Cuando se presiona el botón del mapa en la aplicación, se abre la `MapActivity` pero solo se muestra "fragment" en lugar del mapa real de Google Maps.

## Soluciones Implementadas

### 1. Logs de Debug
Se han agregado logs extensivos en:
- `onCreate()` - Para verificar que la actividad se inicie correctamente
- `setupMap()` - Para verificar la inicialización del fragment del mapa
- `onMapReady()` - Para verificar que el callback del mapa se ejecute
- `checkGoogleMapsAPIKey()` - Para verificar la API key

### 2. Manejo Robusto de Errores
- Verificación de que el fragment del mapa exista
- Creación programática del fragment si falla la búsqueda
- Manejo de excepciones en todos los métodos críticos

### 3. Botón de Prueba Temporal
Se ha agregado un botón "Test" en el toolbar para probar la funcionalidad del mapa una vez que esté cargado.

### 4. Reorganización del Layout
Se ha reorganizado el `activity_map.xml` para asegurar que el fragment del mapa esté correctamente posicionado.

## Pasos para Diagnosticar

### 1. Ejecutar la Aplicación
```bash
# Compilar
gradlew.bat assembleDebug

# Instalar en dispositivo/emulador
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 2. Navegar al Mapa
1. Abrir la aplicación
2. Presionar el botón del mapa en la navegación inferior
3. Observar los logs en Android Studio (Logcat)

### 3. Verificar Logs
Buscar en Logcat con el tag "MapActivity":
```
MapActivity: onCreate: Actividad iniciada correctamente
MapActivity: setupMap: Iniciando configuración del mapa
MapActivity: setupMap: Fragment encontrado, configurando mapa asíncrono
MapActivity: onMapReady: Mapa listo, configurando...
```

### 4. Usar el Botón de Prueba
Una vez en la `MapActivity`, presionar el botón "Test" para verificar que el mapa esté funcionando.

## Posibles Causas del Problema

### 1. API Key de Google Maps
- Verificar que la API key en `AndroidManifest.xml` sea válida
- Verificar que la API key tenga habilitados los servicios de Maps
- Verificar que no haya restricciones de IP o aplicación

### 2. Dependencias
- Verificar que `play-services-maps:18.2.0` esté correctamente incluido
- Verificar que no haya conflictos de versiones

### 3. Permisos
- Verificar que los permisos de ubicación estén concedidos
- Verificar que no haya problemas con los permisos del sistema

### 4. Fragment del Mapa
- Verificar que el `SupportMapFragment` se inicialice correctamente
- Verificar que el callback `onMapReady` se ejecute

## Comandos de Debug

### Ver Logs en Tiempo Real
```bash
adb logcat | grep "MapActivity"
```

### Verificar Permisos
```bash
adb shell dumpsys package com.example.museoenvivo | grep permission
```

### Verificar API Key
```bash
adb shell dumpsys package com.example.museoenvivo | grep "com.google.android.geo.API_KEY"
```

## Próximos Pasos

1. **Ejecutar la aplicación** y revisar los logs
2. **Identificar el punto exacto** donde falla la inicialización
3. **Verificar la API key** en Google Cloud Console
4. **Probar en diferentes dispositivos/emuladores**
5. **Verificar la conectividad a internet** del dispositivo

## Contacto para Soporte

Si el problema persiste después de seguir estas instrucciones, proporcionar:
- Logs completos de Logcat
- Captura de pantalla del error
- Información del dispositivo/emulador
- Versión de Android
