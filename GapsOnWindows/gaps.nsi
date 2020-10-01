;NSIS Gaps Installer
;Written by Jason House

NAME "Gaps"
Caption "Gaps Installer"
Icon "..\GapsWeb\src\main\resources\static\images\gaps.ico"

# define installer name
OutFile "installer.exe"
 
# set desktop as install directory
InstallDir $PROGRAMFILES\Gaps

RequestExecutionLevel admin

# default section start
Section

# define output path
SetOutPath $INSTDIR

# specify file to go in output path
File gaps.jar
File start.bat

# define uninstaller name
WriteUninstaller $INSTDIR\uninstaller.exe

# default section end
SectionEnd

Section
CreateDirectory $INSTDIR\data
SetOutPath $INSTDIR\data
File ..\movieIds.json
SectionEnd
 
# create a section to define what the uninstaller does.
# the section will always be named "Uninstall"
Section "Uninstall"

# Always delete uninstaller first
Delete $INSTDIR\uninstaller.exe

# Delete the directory
RMDIR /r $INSTDIR\data
RMDIR /r $INSTDIR
SectionEnd

# name the installer
OutFile "gaps-0.8.2-installer.exe"