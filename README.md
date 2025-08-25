# 🏛️ Museo Vivo - Aplicación Android

Una aplicación móvil innovadora que permite a los usuarios explorar patrimonio cultural a través de mapas interactivos y contenido multimedia inmersivo.

## 📋 Características Principales

### 🔐 Autenticación Completa
- Login y registro con Firebase
- Recuperación de contraseñas
- Gestión segura de sesiones

### 🗺️ Mapa Cultural Interactivo
- Puntos de interés cultural geolocalizados
- Navegación GPS integrada
- Rutas culturales predefinidas
- Búsqueda de lugares cercanos

### 📱 Experiencia multimedia
- Visualización de contenido multimedia
- Reconstrucciones históricas (cuando estén disponibles)

### 🎯 Sistema de Gamificación
- Logros y recompensas digitales
- Tabla de clasificación local
- Puntos por exploración

### 💾 Modo Offline
- Descarga previa de contenido
- Funciona sin conexión a internet
- Sincronización automática

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Java
- **IDE**: Android Studio
- **Base de datos**: Firebase Realtime Database
- **Autenticación**: Firebase Auth
- **Mapas**: Google Maps API
-- **AR**: eliminado (no usado)
- **UI**: Material Design 3
- **Arquitectura**: Clean Architecture + MVVM

## 🚀 Configuración del Proyecto

### Prerrequisitos
- Android Studio Arctic Fox o superior
- JDK 8 o superior
- SDK Android 24+ (Android 7.0)
- Google Play Services

### Instalación

1. **Clonar el proyecto**:
   ```bash
   git clone <repository-url>
   cd MuseoVivo
   ```

2. **Abrir en Android Studio**:
   - File → Open → Seleccionar carpeta del proyecto

3. **Configurar Firebase**:
   - El archivo `google-services.json` ya está incluido
   - API Key configurada: `AIzaSyCkuare8bmXIK4bqX6wC2bLQ_lgxdZHQEQ`
   - Database URL: `https://appmuse-default-rtdb.firebaseio.com/`

4. **Sincronizar Gradle**:
   - Android Studio lo hará automáticamente
   - O manualmente: Build → Clean Project → Rebuild Project

## 📁 Estructura del Proyecto

```
app/src/main/java/com/museovivo/app/
├── data/
│   ├── auth/
│   │   └── AuthRepository.java          # Autenticación Firebase
│   ├── repository/                      # Repositorios de datos
│   ├── local/                          # Base de datos local (Room)
│   └── remote/                         # APIs remotas
├── domain/
│   ├── model/
│   │   ├── User.java                   # Modelo de usuario
│   │   ├── CulturalPoint.java          # Puntos culturales
│   │   ├── CulturalRoute.java          # Rutas culturales
│   │   └── Achievement.java            # Logros/recompensas
│   ├── repository/                     # Interfaces de repositorios
│   └── usecase/                        # Casos de uso
├── ui/
│   ├── MainActivity.java               # Actividad principal
│   ├── auth/
│   │   └── AuthActivity.java           # Pantalla de login/registro
│   ├── main/
│   │   └── HomeFragment.java           # Pantalla de inicio
│   ├── map/
│   │   └── MapFragment.java            # Mapa interactivo
│   ├── ar/                             # Realidad aumentada
│   ├── content/                        # Contenido cultural
│   ├── profile/                        # Perfil de usuario
│   └── scanner/                        # Escáner QR
├── utils/                              # Utilidades
└── MuseoVivoApplication.java           # Clase Application
```

## 🎨 Recursos y Assets

```
app/src/main/res/
├── layout/
│   ├── activity_main.xml               # Layout principal
│   ├── activity_auth.xml               # Layout de autenticación
│   ├── fragment_home.xml               # Layout de inicio
│   └── fragment_map.xml                # Layout del mapa
├── values/
│   ├── strings.xml                     # Textos en español
│   ├── colors.xml                      # Colores temáticos culturales
│   └── themes.xml                      # Tema Material Design
├── drawable/                           # Iconos vectoriales
├── menu/
│   └── bottom_navigation_menu.xml      # Menú de navegación
└── mipmap-*/                          # Iconos de la app
```

## 🔧 Configuración de APIs

### Google Maps
- API Key ya configurada en `AndroidManifest.xml`
- Permisos de ubicación incluidos

### Firebase
- Proyecto configurado: `appmuse-default-rtdb`
- Autenticación habilitada
- Realtime Database configurada
- Storage para archivos multimedia

## 📱 Funcionalidades Implementadas

### ✅ Completadas
- [x] Estructura del proyecto Android
- [x] Configuración Firebase completa
- [x] Sistema de autenticación (login/registro)
- [x] Navegación principal con Bottom Navigation
- [x] Pantalla de inicio con acciones rápidas
- [x] Integración Google Maps con marcadores
- [x] Gestión de permisos (ubicación, cámara)
- [x] Modelos de datos completos
- [x] Tema Material Design cultural
- [x] Layouts responsivos

### 🚧 En Desarrollo
<!-- Realidad Aumentada eliminada de la versión actual -->
- [ ] Sistema de logros y recompensas
- [ ] Carga de contenido cultural desde Firebase
- [ ] Funcionalidad offline
- [ ] Upload de contenido por usuarios
- [ ] Reproductor multimedia
- [ ] Escáner QR

## 🎮 Cómo Usar la App

1. **Registro/Login**: Crear cuenta o iniciar sesión
2. **Explorar**: Ver puntos culturales en el mapa
3. **Navegar**: Usar GPS para llegar a destinos
4. **Descubrir**: Escanear códigos QR en sitios
5. **AR**: Visualizar reconstrucciones 3D
6. **Aprender**: Leer contenido cultural
7. **Ganar**: Obtener puntos y logros
8. **Compartir**: Subir contenido propio

## 🔒 Permisos Requeridos

- **INTERNET**: Conectividad de red
- **ACCESS_FINE_LOCATION**: GPS para mapas
- **ACCESS_COARSE_LOCATION**: Ubicación aproximada
- **CAMERA**: Realidad aumentada y QR
- **RECORD_AUDIO**: Grabación de audio
- **READ/WRITE_EXTERNAL_STORAGE**: Modo offline

## 🏗️ Próximos Pasos

1. **(AR eliminado)**
2. **Conectar Firebase**: Cargar datos reales de puntos culturales
3. **Sistema de Logros**: Gamificación completa
4. **Modo Offline**: Room Database + sincronización
5. **CultureFragment**: Visualizar tradiciones y leyendas
6. **ProfileFragment**: Gestión de usuario y estadísticas
7. **Upload System**: Permitir contribuciones de usuarios
8. **Optimizaciones**: Performance y escalabilidad

## 📞 Soporte

Para problemas o sugerencias:
- Crear issue en el repositorio
- Documentación en `/docs`
- Wiki del proyecto

---

**Museo Vivo** - Conectando el pasado con el presente a través de la tecnología 🏛️📱✨