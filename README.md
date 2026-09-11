# Módulo de Gestión Académica e Información Básica de la Hoja de Vida del Estudiante

Microservicio backend del **Módulo de Gestión Académica e Información Básica de la Hoja de Vida del Estudiante para el Prototipo del Sistema Académico-Administrativo de la Maestría en Computación**.

Permite consultar estudiantes, consolidar su trayectoria académica y administrar distinciones con sus resoluciones en PDF.

## Funcionalidades

- Consulta, búsqueda y filtrado de estudiantes.
- Generación de la historia académica consolidada.
- Consulta de asignaturas, créditos, promedio e información académica adicional.
- Registro, consulta, edición y eliminación de distinciones académicas.
- Validación y descarga de resoluciones en formato PDF.
- Autorización por roles y acceso del estudiante únicamente a su información.
- Documentación interactiva con OpenAPI y Swagger UI.

## Tecnologías principales

- Java 17
- Spring Boot 2.7.12
- Spring Web, Validation y Data JPA
- Spring Security y OAuth2 Resource Server
- MySQL
- Apache PDFBox
- springdoc-openapi
- Maven

## Requisitos

- JDK 17
- MySQL 8 o una versión compatible
- Microservicio de autenticación en ejecución para obtener tokens mediante el flujo real de inicio de sesión

El proyecto incluye Maven Wrapper, por lo que no es necesario instalar Maven globalmente.

> La aplicación valida la estructura de la base de datos mediante Hibernate, pero no crea ni actualiza las tablas. El esquema debe existir antes de iniciar el microservicio.

## Configuración local

1. Cree el archivo `.env` a partir de la plantilla:

```powershell
Copy-Item .env.example .env
```

2. Configure la conexión a MySQL y la clave JWT compartida con autenticación:

```dotenv
HOJA_VIDA_SERVER_PORT=8080
HOJA_VIDA_CORS_ALLOWED_ORIGINS=http://localhost:4200
HOJA_VIDA_RESOLUTION_MAX_SIZE=5MB
HOJA_VIDA_MAX_REQUEST_SIZE=6MB

HOJA_VIDA_JWT_SECRET=clave_base64_compartida_con_autenticacion

HOJA_VIDA_DB_URL=jdbc:mysql://localhost:3306/nombre_base_datos?useSSL=false&serverTimezone=America/Bogota
HOJA_VIDA_DB_USERNAME=usuario_local
HOJA_VIDA_DB_PASSWORD=contrasena_local
```

La clave `HOJA_VIDA_JWT_SECRET` debe ser la misma que utiliza el microservicio de autenticación para firmar los tokens HS512. Debe estar codificada en Base64 y representar al menos 64 bytes.

Las variables opcionales para producción y sus valores predeterminados se encuentran documentados en `.env.example`.

## Perfiles

| Perfil | Uso | Base de datos | Swagger |
| --- | --- | --- | --- |
| `dev` | Desarrollo local y perfil predeterminado | MySQL | Habilitado |
| `test` | Pruebas automatizadas | H2 compatible con MySQL | Uso interno |
| `prod` | Despliegue productivo | MySQL | Deshabilitado |

## Ejecución

### Desarrollo local

```powershell
.\mvnw.cmd spring-boot:run
```

Por defecto, el servicio queda disponible en `http://localhost:8080`.

### Construcción y verificación

```powershell
.\mvnw.cmd clean verify
```

El artefacto se genera en:

```text
target/ms-gestion-hoja-de-vida-0.0.1-SNAPSHOT.jar
```

Para ejecutarlo con el perfil de producción:

```powershell
java -jar target/ms-gestion-hoja-de-vida-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Imagen Docker

La imagen se construye en dos etapas y ejecuta el microservicio con Java 17, el perfil `prod` y un usuario sin privilegios. Incluye comprobación de salud y cierre controlado.

```powershell
docker build `
  --build-arg APP_VERSION=1.0.0 `
  --build-arg VCS_REF=<hash-del-commit> `
  -t ms-gestion-hoja-de-vida:1.0.0 .
```

Para ejecutarla utilizando las variables del archivo `.env`:

```powershell
docker run --rm --name ms-gestion-hoja-de-vida `
  --env-file .env `
  -e SPRING_PROFILES_ACTIVE=prod `
  -p 8080:8080 `
  --stop-timeout 35 `
  ms-gestion-hoja-de-vida:1.0.0
```

Si MySQL se ejecuta directamente en Windows, `HOJA_VIDA_DB_URL` debe utilizar `host.docker.internal` en lugar de `localhost`. Si MySQL se encuentra en otro contenedor, ambos deben compartir una red Docker y la URL debe usar el nombre de ese servicio.

En producción, las credenciales y la clave JWT deben configurarse como secretos de la plataforma; la aplicación también admite secretos montados como archivos en `/run/secrets/`. El esquema y sus migraciones deben ejecutarse antes de iniciar esta imagen, porque Hibernate solo valida las tablas existentes.

El estado del contenedor se consulta internamente mediante `/actuator/health/readiness`. El endpoint no revela detalles sensibles.

El procedimiento de versionado, validación, publicación en Docker Hub, preparación de base de datos y entrega a TICS se encuentra en [DEPLOYMENT.md](DEPLOYMENT.md).

## Autenticación y roles

Los endpoints de negocio requieren un JWT válido en el encabezado:

```http
Authorization: Bearer <token>
```

El token debe contener el claim `rol`. Para los estudiantes también debe incluir `codigoAcademico`, utilizado para restringir el acceso a su propia hoja de vida.

| Operación | Coordinador | Estudiante |
| --- | :---: | :---: |
| Listar, buscar y filtrar estudiantes | Sí | No |
| Consultar cualquier historia académica | Sí | No |
| Consultar su propia historia académica | Sí | Sí |
| Administrar distinciones | Sí | No |
| Descargar una resolución propia | Sí | Sí |

Para obtener un token, inicie sesión mediante el microservicio de autenticación o el frontend. Si utiliza el frontend, puede copiarlo desde la clave `token` del almacenamiento local del navegador.

## Swagger y OpenAPI

Con el perfil `dev` y el backend en ejecución:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- OpenAPI YAML: `http://localhost:8080/v3/api-docs.yaml`

Para probar un endpoint protegido, presione **Authorize** en Swagger UI y pegue el JWT sin escribir el prefijo `Bearer`.

## Endpoints principales

URL base: `/api/hoja-vida`

| Método | Ruta | Acceso | Descripción |
| --- | --- | --- | --- |
| `GET` | `/estudiantes` | Coordinador | Lista los estudiantes. |
| `GET` | `/estudiantes/buscar?valor={valor}` | Coordinador | Busca por código, identificación o nombre. |
| `GET` | `/estudiantes/filtrar` | Coordinador | Filtra por suficiencia de idioma y/o semestre. |
| `GET` | `/estudiantes/{codigo}/historia-academica` | Coordinador o propietario | Consulta la historia académica consolidada. |
| `POST` | `/estudiantes/{codigo}/distinciones` | Coordinador | Registra una distinción y su resolución. |
| `GET` | `/estudiantes/{codigo}/distinciones/{tipo}` | Coordinador | Consulta una distinción. |
| `GET` | `/estudiantes/{codigo}/distinciones/{tipo}/resolucion` | Coordinador o propietario | Obtiene la resolución PDF. |
| `PUT` | `/estudiantes/{codigo}/distinciones/{tipo}` | Coordinador | Actualiza una distinción. |
| `DELETE` | `/estudiantes/{codigo}/distinciones/{tipo}` | Coordinador | Elimina una distinción. |

Los parámetros, formatos multipart, validaciones y respuestas de cada operación se encuentran detallados en Swagger UI.

## Pruebas

Ejecutar únicamente las pruebas unitarias:

```powershell
.\mvnw.cmd test
```

Ejecutar las pruebas unitarias y de integración:

```powershell
.\mvnw.cmd verify
```

Las pruebas de integración utilizan H2 en modo compatible con MySQL y validan los controladores, seguridad, persistencia, archivos PDF y contrato OpenAPI.

## Problemas frecuentes

- **El backend no inicia:** compruebe que MySQL esté disponible, el esquema exista y las credenciales de `.env` sean correctas.
- **Respuesta `401`:** verifique el encabezado, la vigencia del token y que ambos microservicios utilicen la misma clave JWT.
- **Respuesta `403`:** el token es válido, pero el rol o el código académico no permiten realizar la operación.
- **Error de CORS:** agregue el origen exacto del frontend a `HOJA_VIDA_CORS_ALLOWED_ORIGINS`.
- **Swagger UI no aparece:** compruebe que la aplicación no se esté ejecutando con el perfil `prod`.
