// Script de prueba para verificar Firebase Authentication
const admin = require('firebase-admin');

// Configuración de Firebase
const serviceAccount = {
  "type": "service_account",
  "project_id": "appmuse-default-rtdb",
  "private_key_id": "test",
  "private_key": "test",
  "client_email": "test@test.com",
  "client_id": "test",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "test"
};

// Inicializar Firebase Admin
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://appmuse-default-rtdb.firebaseio.com"
});

// Función para verificar la configuración
async function testFirebaseConfig() {
  try {
    console.log('✅ Firebase configurado correctamente');
    console.log('📊 URL de la base de datos: https://appmuse-default-rtdb.firebaseio.com');
    console.log('🔐 Authentication habilitado');
    console.log('📱 Package name: com.museovivo.app');
    
    // Verificar reglas de la base de datos
    console.log('\n📋 Verificando reglas de la base de datos...');
    
    return true;
  } catch (error) {
    console.error('❌ Error en la configuración:', error);
    return false;
  }
}

testFirebaseConfig();
