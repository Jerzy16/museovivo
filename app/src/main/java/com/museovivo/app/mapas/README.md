# Funcionalidad de Mapa - MuseoVivo

## Descripción
La funcionalidad de mapa permite a los usuarios explorar puntos culturales de Abancay, Perú, de manera interactiva usando Google Maps.

## Características Principales

### 🗺️ Mapa Interactivo
- Integración completa con Google Maps
- Vista centrada en Abancay, Perú
- Múltiples tipos de mapa (Normal, Satélite, Terreno, Híbrido)

### 📍 Puntos Culturales
- **Plaza de Armas de Abancay**: Plaza principal histórica
- **Iglesia de la Virgen del Rosario**: Templo colonial del siglo XVII
- **Puente Pachachaca**: Puente colonial histórico
- **Mirador de Taraccasa**: Vista panorámica de la ciudad
- **Casa Hacienda de Illanya**: Museo con patrimonio prehispánico
- **Mercado Central**: Centro comercial tradicional
- **Parque de la Libertad**: Espacio recreativo

### 🎯 Funcionalidades
- **Filtrado por categorías**: Plazas, Iglesias, Museos, Parques, etc.
- **Información detallada**: Descripción, categoría, distancia
- **Navegación**: Integración con Google Maps para direcciones
- **Ubicación del usuario**: Centrado automático en la ubicación actual
- **Controles de zoom**: Botones personalizados para acercar/alejar

### 🔐 Permisos Requeridos
- `ACCESS_FINE_LOCATION`: Para ubicación precisa
- `ACCESS_COARSE_LOCATION`: Para ubicación aproximada
- `INTERNET`: Para cargar mapas y datos

## Uso

### 1. Navegación Básica
- **Botón de regreso**: Regresa a la actividad anterior
- **Botón de ubicación**: Centra el mapa en tu ubicación actual
- **Botones de zoom**: Controlan el nivel de zoom del mapa

### 2. Exploración de Puntos Culturales
- **Tocar marcadores**: Muestra información del punto cultural
- **Panel de información**: Nombre, categoría, descripción y distancia
- **Botón "Ver Detalles"**: Muestra información completa (futuro)
- **Botón "Navegar"**: Abre Google Maps con direcciones

### 3. Filtrado
- **Chips de categoría**: Filtra puntos por tipo
- **Categorías disponibles**: Todos, Plazas, Iglesias, Museos, Parques, etc.

### 4. Configuración del Mapa
- **Botón de capas**: Cambia entre tipos de mapa
- **Tipos disponibles**: Normal, Satélite, Terreno, Híbrido

## Estructura del Código

### Clases Principales
- `MapActivity`: Actividad principal del mapa
- `CulturalPoint`: Modelo de datos para puntos culturales

### Layouts
- `activity_map.xml`: Layout principal de la actividad
- `bottom_sheet_point_info.xml`: Panel de información de puntos

### Recursos
- **Drawables**: Iconos y fondos personalizados
- **Colores**: Paleta de colores del tema
- **Strings**: Textos localizados

## Integración

### Con Google Maps
- API Key configurada en `AndroidManifest.xml`
- `SupportMapFragment` para renderizado del mapa
- `OnMapReadyCallback` para inicialización

### Con Ubicación
- `FusedLocationProviderClient` para ubicación precisa
- Manejo de permisos de ubicación
- Fallback a ubicación conocida

### Con Navegación
- Intent a Google Maps app
- Fallback a navegación web si la app no está disponible

## Personalización

### Agregar Nuevos Puntos
1. Editar método `getAllPoints()` en `CulturalPoint`
2. Agregar coordenadas, nombre, descripción y categoría
3. El mapa se actualiza automáticamente

### Cambiar Colores
1. Editar `colors.xml`
2. Modificar variables de color en el código
3. Actualizar drawables si es necesario

### Agregar Categorías
1. Agregar chip en `activity_map.xml`
2. Actualizar lógica de filtrado en `MapActivity`
3. Agregar strings correspondientes

## Dependencias

### Gradle
```gradle
implementation 'com.google.android.gms:play-services-maps:18.1.0'
implementation 'com.google.android.gms:play-services-location:21.0.1'
implementation 'com.google.android.material:material:1.9.0'
```

### Permisos
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />
```

## Notas de Desarrollo

### Manejo de Errores
- Verificación de nulos en vistas
- Manejo de excepciones de ubicación
- Fallbacks para funcionalidades críticas

### Performance
- Lazy loading de marcadores
- Filtrado eficiente por categorías
- Limpieza de recursos en `onDestroy`

### Accesibilidad
- Content descriptions en botones
- Textos descriptivos para funcionalidades
- Navegación por teclado compatible

## Futuras Mejoras

### Funcionalidades Planificadas
- [ ] Actividad de detalles completa para puntos culturales
- [ ] Búsqueda por nombre o categoría
- [ ] Favoritos y historial de visitas
- [ ] Notificaciones de puntos cercanos
- [ ] Modo offline con mapas descargados
- [ ] Integración con redes sociales
- [ ] Sistema de calificaciones y comentarios
- [ ] Realidad aumentada en puntos culturales
- [ ] Rutas culturales predefinidas
- [ ] Estadísticas de visitas y exploración
