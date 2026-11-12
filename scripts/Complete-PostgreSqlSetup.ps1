#requires -RunAsAdministrator
[CmdletBinding()]
param()

$ErrorActionPreference = 'Stop'
$pgRoot = 'C:\Program Files\PostgreSQL\17'
$pgBin = Join-Path $pgRoot 'bin'
$dataDir = Join-Path $pgRoot 'data'
$serviceName = 'postgresql-x64-17'
$hbaPath = Join-Path $dataDir 'pg_hba.conf'
$projectRoot = Split-Path -Parent $PSScriptRoot
$backupPath = Join-Path $env:TEMP ('hiretrack-pg_hba-{0}.bak' -f [Guid]::NewGuid().ToString('N'))
$sqlPath = Join-Path $env:TEMP ('hiretrack-setup-{0}.sql' -f [Guid]::NewGuid().ToString('N'))

if (-not (Test-Path (Join-Path $pgBin 'psql.exe'))) {
    throw "PostgreSQL command-line tools were not found at $pgBin."
}

$passwordSecure = Read-Host 'Enter the password for the HireTrack database user hiretrack_app' -AsSecureString
$passwordPtr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($passwordSecure)
try {
    $password = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($passwordPtr)
}
finally {
    [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($passwordPtr)
}

if ([string]::IsNullOrWhiteSpace($password)) {
    throw 'A non-empty database password is required.'
}

$escapedPassword = $password.Replace("'", "''")
$sql = @"
DO `$role_setup`$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'hiretrack_app') THEN
        CREATE ROLE hiretrack_app LOGIN PASSWORD '$escapedPassword';
    ELSE
        ALTER ROLE hiretrack_app LOGIN PASSWORD '$escapedPassword';
    END IF;
END
`$role_setup`$;
"@

Copy-Item $hbaPath $backupPath -Force
try {
    $temporaryHba = Get-Content $hbaPath | ForEach-Object {
        if ($_ -match '^local\s+all\s+all\s+scram-sha-256$') {
            'local   all             all                                     trust'
        }
        elseif ($_ -match '^host\s+all\s+all\s+127\.0\.0\.1/32\s+scram-sha-256$') {
            'host    all             all             127.0.0.1/32            trust'
        }
        elseif ($_ -match '^host\s+all\s+all\s+::1/128\s+scram-sha-256$') {
            'host    all             all             ::1/128                 trust'
        }
        else {
            $_
        }
    }
    Set-Content $hbaPath $temporaryHba

    Restart-Service -Name $serviceName -Force
    & (Join-Path $pgBin 'psql.exe') -w -U postgres -d postgres -h 127.0.0.1 -p 5432 -v ON_ERROR_STOP=1 -c $sql
    if ($LASTEXITCODE -ne 0) {
        throw 'Could not configure the hiretrack_app role.'
    }

    $databaseExists = & (Join-Path $pgBin 'psql.exe') -w -U postgres -d postgres -h 127.0.0.1 -p 5432 -Atqc "SELECT 1 FROM pg_database WHERE datname = 'hiretrack_db';"
    if ($LASTEXITCODE -ne 0) {
        throw 'Could not inspect PostgreSQL databases.'
    }
    if ($databaseExists.Trim() -ne '1') {
        & (Join-Path $pgBin 'createdb.exe') -w -U postgres -h 127.0.0.1 -p 5432 -O hiretrack_app hiretrack_db
        if ($LASTEXITCODE -ne 0) {
            throw 'Could not create hiretrack_db.'
        }
    }
}
finally {
    Copy-Item $backupPath $hbaPath -Force
    Restart-Service -Name $serviceName -Force
    Remove-Item $backupPath -Force -ErrorAction SilentlyContinue
}

$env:PGPASSWORD = $password
try {
    $result = & (Join-Path $pgBin 'psql.exe') -w -U hiretrack_app -d hiretrack_db -h 127.0.0.1 -p 5432 -Atqc "SELECT current_database() || '|' || current_user;"
    if ($LASTEXITCODE -ne 0) {
        throw 'The password-based hiretrack_app connection failed.'
    }
    Write-Output "DATABASE_CONNECTION_OK $result"

    $env:DB_HOST = 'localhost'
    $env:DB_PORT = '5432'
    $env:DB_NAME = 'hiretrack_db'
    $env:DB_USERNAME = 'hiretrack_app'
    $env:DB_PASSWORD = $password
    Push-Location $projectRoot
    try {
        & mvn -q test
        if ($LASTEXITCODE -ne 0) {
            throw 'The Spring Boot Flyway/database integration test failed.'
        }
        Write-Output 'SPRING_BOOT_FLYWAY_TEST_OK'
    }
    finally {
        Pop-Location
    }
}
finally {
    Remove-Item Env:PGPASSWORD -ErrorAction SilentlyContinue
    Remove-Item Env:DB_PASSWORD -ErrorAction SilentlyContinue
    $password = $null
}
