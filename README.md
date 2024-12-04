
# BravquezRentCar

**Rent Car App** es una aplicación móvil desarrollada en **Kotlin** utilizando **Jetpack Compose** para facilitar la gestión de renta de vehículos. Este proyecto está diseñado como una solución completa que incluye autenticación, persistencia local, comunicación remota y un diseño moderno.

## 🚀 Características

- **Autenticación Segura:**
  - Login con Google mediante Firebase Authentication.
  - Manejo de usuarios administradores y clientes regulares.

- **Gestión de Datos:**
  - **Clientes:** Registro y consulta de datos de clientes.
  - **Marcas y Modelos:** Administración de vehículos con marcas y modelos asociados.
  - **Vehículos:** Gestión de vehículos, incluyendo tipo de combustible, tipo de vehículo y estado de renta.
  - **Rentas:** Seguimiento de la información de rentas, fechas y montos.
  - **Proveedores:** Mantenimiento de datos de proveedores.

- **Interfaz Gráfica Moderna:**
  - Diseñada con **Jetpack Compose** para un rendimiento fluido y atractivo.
  - Componentes personalizados como botones de filtro, cuadros de diálogo y selección de imágenes.

- **Persistencia Local:**
  - **Room Database:** Almacenamiento y consultas optimizadas.

- **Integración API Remota:**
  - Conexión a servicios externos utilizando **Retrofit** y **Moshi**.

- **Inyección de Dependencias:**
  - Uso de **Dagger Hilt** para mantener el código modular y fácil de probar.

## 📂 Estructura del Proyecto

```plaintext
├── app/
│   ├── src/main/
│   │   ├── java/ucne/edu/proyectofinalaplicada2/
│   │   │   ├── data/        # Gestión de datos locales y remotos
│   │   │   ├── presentation/ # Componentes de UI y ViewModels
│   │   │   ├── repository/  # Repositorios para encapsular lógica de datos
│   │   │   ├── di/          # Módulos de inyección de dependencias
│   │   │   └── utils/       # Utilidades comunes
│   │   ├── res/
│   │   │   ├── drawable/    # Recursos gráficos
│   │   │   ├── layout/      # Diseños XML
│   │   │   └── values/      # Colores, strings, temas
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── .github/
│   ├── workflows/
│   │   ├── android.yml      # CI para construcción en Android
│   │   ├── build.yml        # CI para integración continua
│   │   └── sonarcloud.yml   # Análisis de calidad de código
└── README.md
```
🛠️ Tecnologías y Librerías

Kotlin: Lenguaje principal del desarrollo.
Jetpack Compose: Framework moderno para diseño de interfaces gráficas.
Room: Base de datos local.
Retrofit: Llamadas HTTP eficientes.
Dagger Hilt: Inyección de dependencias.
Firebase: Autenticación y almacenamiento en la nube.
Coil: Manejo de imágenes.
JUnit & Mockk: Pruebas unitarias.

🚀 Instalación y Ejecución
Clona el repositorio:

git clone https://github.com/tuusuario/tu-repo.git
cd tu-repo
./gradlew build



<div align="center">

## 📸 **Capturas de Pantalla**

</div>

---

### Pantallas de Inicio de Sesión y Registro
<table>
  <tr>
    <td align="center" style="padding: 10px;">
      <img src="https://github.com/user-attachments/assets/ed7b1c31-f74e-4ae9-b2dc-de29324fd237" alt="Login" width="300"><br>
      <b>Pantalla de Inicio de Sesión</b>
    </td>
    <td align="center" style="padding: 10px;">
      <img src="https://github.com/user-attachments/assets/34f62c94-ae44-40e8-b3ae-d010e3175862" alt="Signup" width="300"><br>
      <b>Pantalla de Registro de Usuario</b>
    </td>
  </tr>
</table>

---

### Gestión de Vehículos y Modelos
<table>
  <tr>
    <td align="center" style="padding: 10px;">
      <img src="https://github.com/user-attachments/assets/d6edeea5-81b8-4006-8b67-ee09ffd12069" alt="Filtrar Vehículos" width="300"><br>
      <b>Filtrar Vehículos</b>
    </td>
    <td align="center" style="padding: 10px;">
      <img src="https://github.com/user-attachments/assets/5c2326fa-09eb-4b59-987c-a494f7ac44d2" alt="Tipos de Modelos" width="300"><br>
      <b>Tipos de Modelos</b>
    </td>
  </tr>
</table>

---

### Registro y Gestión de Rentas
<table>
  <tr>
    <td align="center" style="padding: 10px;">
      <img src="https://github.com/user-attachments/assets/439bc1c7-01ab-474c-b457-723c35c07938" alt="Registro Vehículo" width="300"><br>
      <b>Registro de Vehículo</b>
    </td>
    <td align="center" style="padding: 10px;">
      <img src="https://github.com/user-attachments/assets/4921d374-0a03-45ec-8ec9-d98694ba7ac1" alt="Rentas" width="300"><br>
      <b>Gestión de Rentas</b>
    </td>
  </tr>
</table>

---

<div align="center">

✨ **Visualiza tus registros y gestiona tu información fácilmente** ✨

</div>


