$param1 = $args[0] # Nume fisier exe
$param2 = $args[1] # No of threads
$param3 = $args[2] # No of runs
$param4 = $args[3] # Tip matrice

# Executare exe in cmd mode

$suma = 0

for ($i = 0; $i -lt $param3; $i++){
    Write-Host "Rulare" ($i+1)
    $a = (cmd /c .\$param1 $param2 $param4 2`>`&1)
    Write-Host $a
    $suma += $a
    Write-Host ""
}
$media = $suma / $i
#Write-Host $suma
Write-Host "Timp de executie mediu:" $media

# Creare fisier .csv
if (!(Test-Path outC.csv)){
    New-Item outC.csv -ItemType File
    #Scrie date in csv
    Set-Content outC.csv 'Tip Matrice,Tip alocare,Nr threads,Timp executie'
}

# Append
Add-Content outC.csv "$($param4),dynamic,$($args[1]),$($media)"