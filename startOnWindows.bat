@echo off

SET s=false
SET l=false

:initial
if "%1"=="" goto done
echo              %1
set aux=%1
if "%aux:~0,1%"=="-" (
   set nome=%aux:~1,250%
) else (
   set "%nome%=%1"
   set nome=
)
shift
goto initial
:done

if %s% == true (
    if %l% == true (
        echo "Running with SSL and Login"
        call java -jar -Dspring.profiles.active=ssl gaps.jar
    ) else (
        echo "Running with SSL and without Login"
        call java -jar -Dspring.profiles.active=ssl-no-login gaps.jar
    )
) else (
    if %l% == true (
        echo "Running without SSL and with Login"
        call java -jar -Dspring.profiles.active=no-ssl gaps.jar
    ) else (
        echo "Running without SSL and without Login"
        call java -jar -Dspring.profiles.active=no-ssl-no-login gaps.jar
    )
)