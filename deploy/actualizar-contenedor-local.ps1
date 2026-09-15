[CmdletBinding()]
param(
    [string]$EnvFile,
    [string]$DatabaseUrl = 'jdbc:mysql://maestriacomputacion-hv-db:3306/maestriacomputacion_HV?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota',
    [string]$SolicitudesUrl = 'http://ms-gestion-solicitudes-local:8095/msmaestriac',
    [string]$DockerNetwork = 'maestriacomputacion-hv-network'
)

$ErrorActionPreference = 'Stop'

$repositoryRoot = Split-Path -Parent $PSScriptRoot
$image = 'ms-gestion-hoja-de-vida:local'
$container = 'ms-gestion-hoja-de-vida-local'

if (-not $EnvFile) {
    $EnvFile = Join-Path $repositoryRoot '.env'
}

if (-not (Test-Path -LiteralPath $EnvFile)) {
    throw "No se encontró el archivo de variables: $EnvFile"
}

docker info | Out-Null
if ($LASTEXITCODE -ne 0) {
    throw 'Docker no está disponible. Inicie Docker Desktop.'
}

$networkExists = docker network inspect $DockerNetwork 2>$null
if ($LASTEXITCODE -ne 0 -or -not $networkExists) {
    throw "No existe la red $DockerNetwork. Inicie primero la base de datos con deploy/iniciar-base-datos.ps1."
}

$revision = git -C $repositoryRoot rev-parse --short=12 HEAD
if ($LASTEXITCODE -ne 0) {
    throw 'No fue posible obtener el commit actual.'
}

if (git -C $repositoryRoot status --porcelain) {
    $revision = "${revision}-dirty"
}

Write-Host "Construyendo $image..."
docker buildx build `
    --platform linux/amd64 `
    --build-arg APP_VERSION=local `
    --build-arg "VCS_REF=$revision" `
    --tag $image `
    --load `
    $repositoryRoot
if ($LASTEXITCODE -ne 0) {
    throw 'La construcción de la imagen falló.'
}

$existingContainer = docker container inspect $container 2>$null
if ($LASTEXITCODE -eq 0 -and $existingContainer) {
    Write-Host "Reemplazando el contenedor $container..."
    docker container rm --force $container | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw 'No fue posible reemplazar el contenedor anterior.'
    }
}

docker run -d `
    --name $container `
    --network $DockerNetwork `
    --env-file $EnvFile `
    --env SPRING_PROFILES_ACTIVE=prod `
    --env "HOJA_VIDA_DB_URL=$DatabaseUrl" `
    --env "HOJA_VIDA_SOLICITUDES_URL=$SolicitudesUrl" `
    --publish 8080:8080 `
    --restart unless-stopped `
    --stop-timeout 35 `
    $image
if ($LASTEXITCODE -ne 0) {
    throw 'No fue posible iniciar el contenedor.'
}

Write-Host "Contenedor iniciado en http://localhost:8080"
Write-Host "Consulte su estado con: docker ps --filter name=$container"
