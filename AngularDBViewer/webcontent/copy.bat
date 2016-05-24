echo on

REM set target="C:\prg\bytepocket\dbviewer\AngularDBViewer\webcontent"
set target="D:\prg\bytepocket\dbviewer\dbviewer\AngularDBViewer\webcontent"


copy *.* %target%

copy .\formTemplate\*.* %target%\formTemplate\ 

cd \
pause