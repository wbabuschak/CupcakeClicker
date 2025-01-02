@echo off
setlocal enabledelayedexpansion

REM Directory containing the subdirectory "target"
set base_folder=%~dp0

REM Log file location
set log_file=%base_folder%log.txt

REM Clear the log file if it exists
echo. > "%log_file%"

REM Find the first .jar file in the "target" subdirectory
for /r "%base_folder%" %%f in (target\*.jar) do (
    echo Found JAR file: %%~nxf
    echo Running the JAR file...
    cscript //nologo "%base_folder%run_silent.vbs" "%%f"
    exit
)

echo No JAR file found in any "target" subdirectory. >> "%log_file%"
pause
