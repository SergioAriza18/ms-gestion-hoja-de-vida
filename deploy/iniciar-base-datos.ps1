[CmdletBinding()]
param(
    [string]$EnvFile = (Join-Path $PSScriptRoot 'database.env')
)

$ErrorActionPreference = 'Stop'

$composeFile = Join-Path $PSScriptRoot 'compose.database.yaml'

if (-not (Test-Path -LiteralPath $EnvFile)) {
    throw "No se encontro el archivo de variables: $EnvFile"
}

$portDefinition = Get-Content -LiteralPath $EnvFile |
    Where-Object { $_ -match '^MYSQL_PORT=' } |
    Select-Object -First 1
$mysqlPort = if ($portDefinition) { ($portDefinition -split '=', 2)[1] } else { '3307' }

docker info | Out-Null
if ($LASTEXITCODE -ne 0) {
    throw 'Docker no esta disponible. Inicie Docker Desktop.'
}

docker compose `
    --env-file $EnvFile `
    --file $composeFile `
    up --detach --wait

if ($LASTEXITCODE -ne 0) {
    throw 'No fue posible iniciar la base de datos.'
}

Write-Host 'MySQL esta listo.'
Write-Host 'Contenedor: maestriacomputacion-hv-db'
Write-Host "Host desde Windows: 127.0.0.1:$mysqlPort"
Write-Host 'Host desde Docker: maestriacomputacion-hv-db:3306'
