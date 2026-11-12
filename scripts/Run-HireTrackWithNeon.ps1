$ErrorActionPreference = 'Stop'

$envFile = Join-Path (Split-Path -Parent $PSScriptRoot) '.env.local'
if (-not (Test-Path $envFile)) {
    throw '.env.local was not found. Run "neon deploy" first.'
}

$values = @{}
Get-Content $envFile | ForEach-Object {
    if ($_ -match '^\s*([^#=]+)=(.*)$') {
        $values[$matches[1].Trim()] = $matches[2].Trim().Trim('"')
    }
}

if (-not $values.ContainsKey('DATABASE_URL')) {
    throw 'DATABASE_URL is missing from .env.local.'
}

$databaseUri = [Uri]$values['DATABASE_URL']
$query = if ([string]::IsNullOrWhiteSpace($databaseUri.Query)) { 'sslmode=require' } else { $databaseUri.Query.TrimStart('?') }
$port = if ($databaseUri.Port -gt 0) { ':' + $databaseUri.Port } else { '' }
$env:DB_URL = "jdbc:postgresql://$($databaseUri.Host)$port$($databaseUri.AbsolutePath)?$query"
$env:DB_USERNAME = [Uri]::UnescapeDataString($databaseUri.UserInfo.Split(':')[0])
$env:DB_PASSWORD = [Uri]::UnescapeDataString($databaseUri.UserInfo.Substring($databaseUri.UserInfo.IndexOf(':') + 1))
$env:JWT_SECRET = if ($env:JWT_SECRET) { $env:JWT_SECRET } else { 'local-only-change-this-to-a-random-secret-at-least-32-chars' }

Set-Location (Split-Path -Parent $PSScriptRoot)
try {
    mvn spring-boot:run
}
finally {
    Remove-Item Env:DB_URL, Env:DB_USERNAME, Env:DB_PASSWORD -ErrorAction SilentlyContinue
}
