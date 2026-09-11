# Entrega y despliegue del microservicio de hoja de vida

Esta guía define la primera versión del proceso para construir, publicar y entregar a TICS la imagen del microservicio. No contiene secretos ni presupone que la base de datos se ejecute dentro de Docker.

## 1. Datos que identifican una entrega

Cada entrega debe asociar estos tres elementos:

- una versión semántica, por ejemplo `1.0.0`;
- el commit exacto de Git desde el cual se construyó;
- el digest `sha256` generado por Docker Hub.

Una etiqueta publicada no debe reutilizarse con contenido diferente. `latest` puede mantenerse como referencia informativa, pero TICS debe desplegar una versión fija o, preferiblemente, su digest.

## 2. Prerrequisitos

- rama de entrega estable, con pruebas aprobadas y sin cambios locales;
- Docker Desktop o Docker Engine con soporte para `buildx`;
- repositorio creado en Docker Hub;
- acceso mediante `docker login` si el repositorio es privado;
- esquema MySQL preparado y accesible desde el servidor de TICS;
- clave JWT compartida con el microservicio de autenticación;
- URL HTTPS definitiva del frontend para configurar CORS.

## 3. Construcción local

Desde la raíz del repositorio, en PowerShell:

```powershell
.\deploy\publicar-imagen.ps1 `
  -DockerHubUser USUARIO_DOCKERHUB `
  -Version 1.0.0
```

El script se detiene si existen cambios sin commit, obtiene el hash actual, construye para `linux/amd64` y crea la etiqueta indicada. Para agregar también `latest`:

```powershell
.\deploy\publicar-imagen.ps1 `
  -DockerHubUser USUARIO_DOCKERHUB `
  -Version 1.0.0 `
  -TagLatest
```

## 4. Validación de la imagen

Copie `deploy/hoja-vida.env.example` fuera del repositorio como `hoja-vida.env`, reemplace sus valores y ejecute:

```powershell
docker run -d `
  --name hoja-vida-validacion `
  --env-file .\hoja-vida.env `
  -p 8080:8080 `
  --stop-timeout 35 `
  USUARIO_DOCKERHUB/ms-gestion-hoja-de-vida:1.0.0
```

Comprobaciones mínimas:

```powershell
docker inspect --format "{{.State.Health.Status}}" hoja-vida-validacion
docker logs hoja-vida-validacion
```

- `GET /actuator/health/liveness` responde `200`;
- `GET /actuator/health/readiness` responde `200` y confirma indirectamente la conexión a la base de datos;
- los endpoints protegidos aceptan un JWT emitido por autenticación;
- la consulta y generación de una hoja de vida funcionan;
- el registro y la descarga de una resolución PDF conservan el archivo;
- Swagger no está disponible con el perfil `prod`.

Al terminar:

```powershell
docker stop hoja-vida-validacion
docker rm hoja-vida-validacion
```

## 5. Publicación en Docker Hub

Después de validar la imagen y autenticar Docker con `docker login`:

```powershell
.\deploy\publicar-imagen.ps1 `
  -DockerHubUser USUARIO_DOCKERHUB `
  -Version 1.0.0 `
  -TagLatest `
  -Push
```

El comando final muestra el manifiesto publicado. El digest se debe copiar al acta o ficha de entrega.

## 6. Preparación de la base de datos

La imagen no contiene MySQL ni scripts ejecutados automáticamente. El perfil `prod` utiliza `spring.jpa.hibernate.ddl-auto=validate`: valida la estructura al iniciar, pero nunca crea o modifica tablas.

La entrega de base de datos debe componerse de:

1. un esquema base limpio y compatible con la versión desplegada del sistema;
2. migraciones incrementales, numeradas y ejecutadas en orden;
3. catálogos institucionales necesarios, sin estudiantes ni información de prueba;
4. una consulta de verificación posterior a la migración;
5. instrucciones de respaldo y recuperación.

Como la base es compartida por varios microservicios, el esquema base debe provenir de la fuente institucional consolidada. No se debe reconstruir únicamente desde las entidades de hoja de vida.

Antes de migrar una base existente, TICS debe generar un respaldo. Un ejemplo para MySQL es:

```bash
mysqldump --single-transaction --routines --triggers --hex-blob \
  -u USUARIO -p NOMBRE_SCHEMA > respaldo_previo.sql
```

En la estructura actual del módulo, `estudiantes_distinciones_academicas.resolucion_pdf` debe ser `MEDIUMTEXT`, porque el PDF se almacena codificado en Base64. Esta condición debe verificarse en el esquema que se entregue.

El usuario de ejecución de la aplicación no debe tener permisos de `DROP`, `ALTER` ni creación de esquemas. Las migraciones deben ejecutarse con una cuenta administrativa separada.

## 7. Creación del contenedor por TICS

TICS debe copiar `deploy/hoja-vida.env.example` a una ubicación protegida del servidor y reemplazar los valores. No debe almacenar la clave JWT ni la contraseña de MySQL dentro de la imagen.

Ejemplo de ejecución en Linux:

```bash
docker pull USUARIO_DOCKERHUB/ms-gestion-hoja-de-vida:1.0.0

docker run -d \
  --name ms-gestion-hoja-de-vida \
  --restart unless-stopped \
  --env-file /opt/maestria/hoja-vida.env \
  --stop-timeout 35 \
  --read-only \
  --tmpfs /tmp:rw,noexec,nosuid,size=128m \
  --security-opt no-new-privileges:true \
  --cap-drop ALL \
  -p 127.0.0.1:8080:8080 \
  USUARIO_DOCKERHUB/ms-gestion-hoja-de-vida:1.0.0
```

El servicio debe publicarse mediante el proxy inverso institucional con HTTPS. Si MySQL u otros componentes están en contenedores, se debe usar una red Docker privada y sus nombres DNS; `localhost` dentro del contenedor siempre apunta al propio microservicio.

## 8. Variables que administra TICS

| Variable | Obligatoria | Descripción |
| --- | :---: | --- |
| `SPRING_PROFILES_ACTIVE` | Sí | Debe ser `prod`. |
| `HOJA_VIDA_SERVER_PORT` | Sí | Puerto interno; valor esperado `8080`. |
| `HOJA_VIDA_CORS_ALLOWED_ORIGINS` | Sí | URL HTTPS exacta del frontend. |
| `HOJA_VIDA_JWT_SECRET` | Sí | Misma clave Base64 usada por autenticación para HS512. |
| `HOJA_VIDA_DB_URL` | Sí | URL JDBC hacia el MySQL privado. |
| `HOJA_VIDA_DB_USERNAME` | Sí | Usuario de ejecución con privilegios mínimos. |
| `HOJA_VIDA_DB_PASSWORD` | Sí | Contraseña administrada como secreto. |
| `HOJA_VIDA_RESOLUTION_MAX_SIZE` | No | Tamaño máximo del PDF; predeterminado `5MB`. |
| `HOJA_VIDA_MAX_REQUEST_SIZE` | No | Tamaño máximo de la petición; predeterminado `6MB`. |
| `HOJA_VIDA_DB_POOL_MAX_SIZE` | No | Máximo de conexiones; predeterminado `10`. |
| `HOJA_VIDA_DB_POOL_MIN_IDLE` | No | Conexiones mínimas inactivas; predeterminado `2`. |
| `HOJA_VIDA_DB_CONNECTION_TIMEOUT` | No | Espera de conexión en milisegundos; predeterminado `30000`. |
| `HOJA_VIDA_DB_VALIDATION_TIMEOUT` | No | Validación de conexión en milisegundos; predeterminado `5000`. |
| `HOJA_VIDA_SHUTDOWN_TIMEOUT` | No | Tiempo de cierre controlado; predeterminado `30s`. |

## 9. Criterios de aceptación y reversión

La entrega se acepta cuando el contenedor está saludable, se conecta al esquema esperado, valida JWT reales, permite las operaciones principales y no expone Swagger en producción.

Para revertir el servicio, TICS debe conservar y volver a ejecutar la etiqueta estable anterior. Las migraciones de base de datos deben diseñarse, cuando sea posible, de forma compatible con la versión anterior; revertir solamente la imagen no revierte automáticamente los cambios de datos.

## 10. Información que acompaña la entrega

- nombre completo de la imagen y versión;
- digest `sha256`;
- commit y etiqueta Git;
- fecha y responsable de la construcción;
- resultado de las pruebas y validación funcional;
- scripts de base de datos aplicables y su orden;
- plantilla de variables, sin valores secretos;
- versión anterior disponible para reversión;
- incidencias o limitaciones conocidas.
