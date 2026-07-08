param(
    [string]$MySqlExe = "mysql",
    [string]$DbHost = "127.0.0.1",
    [int]$Port = 3309,
    [string]$User = "root",
    [string]$Password = "123456",
    [string]$Database = "travel_platform"
)

$ErrorActionPreference = "Stop"
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
try { [Console]::InputEncoding = [System.Text.UTF8Encoding]::new($false) } catch {}
try { chcp 65001 | Out-Null } catch {}

function Invoke-MySqlFile {
    param(
        [Parameter(Mandatory = $true)][string]$Path
    )

    if (-not (Test-Path $Path)) {
        throw "Missing SQL file: $Path"
    }

    $fileName = Split-Path $Path -Leaf
    Write-Host "Importing $fileName ..."

    $args = @(
        "--default-character-set=utf8mb4",
        "-h", $DbHost,
        "-P", $Port,
        "-u", $User,
        "-p$Password",
        $Database
    )

    $content = Get-Content -Path $Path -Raw -Encoding UTF8
    $content | & $MySqlExe @args
    if ($LASTEXITCODE -ne 0) {
        throw "Import failed for $fileName"
    }
}

Invoke-MySqlFile -Path (Join-Path $PSScriptRoot "00_create_tables.sql")
$segmentDir = Join-Path $PSScriptRoot "segments"
Invoke-MySqlFile -Path (Join-Path $segmentDir "01_user.sql")
Invoke-MySqlFile -Path (Join-Path $segmentDir "02_attraction.sql")
Invoke-MySqlFile -Path (Join-Path $segmentDir "03_hotel.sql")
Invoke-MySqlFile -Path (Join-Path $segmentDir "04_route.sql")
Invoke-MySqlFile -Path (Join-Path $segmentDir "05_route_day.sql")
Invoke-MySqlFile -Path (Join-Path $segmentDir "06_favorite.sql")

Write-Host "All SQL files imported successfully."
