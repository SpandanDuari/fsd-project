$ErrorActionPreference = 'Stop'
$password = Read-Host 'Enter the local hiretrack_app password' -AsSecureString
$ptr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($password)
try {
    $env:DB_PASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($ptr)
}
finally {
    [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($ptr)
}
$env:DB_HOST = 'localhost'
$env:DB_PORT = '5432'
$env:DB_NAME = 'hiretrack_db'
$env:DB_USERNAME = 'hiretrack_app'
$env:JWT_SECRET = 'local-only-change-this-to-a-random-secret-at-least-32-chars'
Set-Location (Split-Path -Parent $PSScriptRoot)
try {
    mvn spring-boot:run
}
finally {
    Remove-Item Env:DB_PASSWORD -ErrorAction SilentlyContinue
}
