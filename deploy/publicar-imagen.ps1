[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [ValidateNotNullOrEmpty()]
    [string]$DockerHubUser,

    [Parameter(Mandatory = $true)]
    [ValidatePattern('^\d+\.\d+\.\d+(?:-[0-9A-Za-z.-]+)?$')]
    [string]$Version,

    [string]$Repository = 'ms-gestion-hoja-de-vida',

    [ValidateSet('linux/amd64', 'linux/arm64')]
    [string]$Platform = 'linux/amd64',

    [switch]$Push,

    [switch]$TagLatest
)

$ErrorActionPreference = 'Stop'

$repositoryRoot = Split-Path -Parent $PSScriptRoot
$currentChanges = git -C $repositoryRoot status --porcelain
if ($LASTEXITCODE -ne 0) {
    throw 'No fue posible consultar el estado del repositorio Git.'
}

if ($currentChanges) {
    throw 'El repositorio tiene cambios sin confirmar. Haga commit antes de construir una imagen de entrega.'
}

$commit = git -C $repositoryRoot rev-parse --short=12 HEAD
if ($LASTEXITCODE -ne 0) {
    throw 'No fue posible obtener el commit que identifica la imagen.'
}

docker info | Out-Null
if ($LASTEXITCODE -ne 0) {
    throw 'Docker no está disponible. Inicie Docker Desktop o el daemon de Docker.'
}

$image = "${DockerHubUser}/${Repository}"
$tags = @('--tag', "${image}:${Version}")
if ($TagLatest) {
    $tags += @('--tag', "${image}:latest")
}

$buildArguments = @(
    'buildx', 'build',
    '--platform', $Platform,
    '--build-arg', "APP_VERSION=${Version}",
    '--build-arg', "VCS_REF=${commit}"
) + $tags

if ($Push) {
    $buildArguments += '--push'
} else {
    $buildArguments += '--load'
}

$buildArguments += $repositoryRoot

Write-Host "Construyendo ${image}:${Version} desde el commit ${commit} para ${Platform}..."
& docker @buildArguments
if ($LASTEXITCODE -ne 0) {
    throw 'La construcción de la imagen falló.'
}

if ($Push) {
    Write-Host "Imagen publicada: ${image}:${Version}"
    docker buildx imagetools inspect "${image}:${Version}"
} else {
    Write-Host "Imagen local creada: ${image}:${Version}"
    Write-Host 'Ejecute nuevamente con -Push después de validarla.'
}
