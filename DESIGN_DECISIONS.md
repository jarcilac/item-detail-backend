# Decisiones de Diseño y Desafíos

## Decisiones de Diseño del Frontend

### 1. Estructura de Componentes
- Se implementó un diseño modular utilizando componentes de React para mejorar la mantenibilidad y reutilización del código.
- Se utilizó Material-UI (MUI) como biblioteca de componentes para garantizar una interfaz de usuario consistente y profesional.

### 2. Manejo de Tipos
- Se implementó TypeScript para proporcionar un sistema de tipos robusto.
- Se crearon interfaces específicas (como `Product`) para garantizar la seguridad de tipos y mejorar la documentación del código.

### 3. Diseño Responsivo
- Se utilizó el sistema de Grid de Material-UI para crear un diseño responsivo que se adapta a diferentes tamaños de pantalla.
- La disposición de elementos cambia de una columna en dispositivos móviles a múltiples columnas en pantallas más grandes.

### 4. Experiencia de Usuario
- Se implementó una galería de imágenes interactiva con una imagen principal y miniaturas seleccionables.
- Se agregaron indicadores visuales claros para el estado de carga y errores.
- Se incluyeron elementos visuales como chips y ratings para mejorar la comprensión de la información.

## Desafíos y Soluciones del Frontend

### 1. Manejo de Datos Nulos
**Desafío:** El manejo de datos opcionales, específicamente en el campo `reviews` que podría ser `null`.

**Solución:** 
- Se modificó la interfaz `Product` para hacer que `reviews` sea opcional usando el tipo unión con `null`.
- Se implementó el operador de encadenamiento opcional (`?.`) en el renderizado de las reseñas para manejar de forma segura los casos donde `reviews` es `null`.

### 2. Gestión de Imágenes
**Desafío:** Implementar una galería de imágenes interactiva con una imagen principal y miniaturas.

**Solución:**
- Se creó un estado separado para la imagen principal.
- Se implementó un sistema de selección de imágenes que permite cambiar la imagen principal al hacer clic en las miniaturas.
- Se utilizaron estilos de Material-UI para resaltar la miniatura seleccionada.

### 3. Diseño Responsivo
**Desafío:** Crear una interfaz que funcione bien en diferentes tamaños de pantalla.

**Solución:**
- Se utilizó el sistema de Grid de Material-UI con breakpoints.
- Se ajustaron los tamaños y disposiciones de los elementos según el tamaño de la pantalla.
- Se implementaron estilos flexibles para garantizar una buena visualización en cualquier dispositivo.

## Decisiones de Diseño del Backend

### 1. Arquitectura
- Se implementó una arquitectura en capas (Controlador, Servicio, Modelo) para una mejor separación de responsabilidades.
- Se utilizó Spring Boot para un desarrollo rápido y una configuración estandarizada.
- Se eligió el almacenamiento en archivos JSON en lugar de una base de datos para mayor simplicidad y portabilidad.

### 2. Persistencia de Datos
- Se implementó un sistema de almacenamiento basado en archivos JSON.
- Se utilizó Jackson para la serialización/deserialización de JSON.
- Se mantuvo la integridad de los datos mediante operaciones atómicas de archivos.

### 3. Diseño de API
- Se implementó un diseño RESTful siguiendo las mejores prácticas.
- Se implementaron códigos de estado HTTP apropiados para diferentes escenarios.
- Se utilizaron DTOs para separar las representaciones de datos internas y externas.

### 4. Validación
- Se implementó una validación de entrada utilizando Jakarta Validation.
- Se proporcionaron mensajes de error detallados para una mejor retroalimentación al cliente.

## Desafíos y Soluciones del Backend

### 1. Validación de Datos
**Desafío:** Asegurar la integridad de datos y la validación adecuada de objetos anidados.

**Solución:**
- Se implementaron anotaciones de validación a nivel de DTOs.

### 3. Gestión del Sistema de Archivos
**Desafío:** Manejar operaciones del sistema de archivos de manera confiable en diferentes entornos.

**Solución:**
- Se utilizó la abstracción Resource de Spring para operaciones de archivo.
- Se implementó un manejo adecuado de errores para operaciones del sistema de archivos.
- Se agregó la creación automática de archivos y directorios si no existen.

### 4. Pruebas
**Desafío:** Probar operaciones basadas en archivos y asegurar el aislamiento adecuado de las pruebas.

**Solución:**
- Se implementaron mocks para operaciones del sistema de archivos.
- Se crearon recursos de prueba separados para las pruebas.
- Se utilizó JUnit 5 y Mockito para los unit tests.

### 5. Rendimiento
**Desafío:** Optimizar el rendimiento con almacenamiento basado en archivos.

**Solución:**
- Se implementó caché en memoria de datos de productos.