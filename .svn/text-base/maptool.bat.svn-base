@echo off

set /p SOURCE=地图名称（all表示全部）：
set /p PLAT_NUM=平台（1:安卓，2:iOS）

if "%PLAT_NUM%"=="1" (
	set PLAT="android"
) else (
	if "%PLAT_NUM%"=="2" (
		set PLAT="ios"
	)
)

echo PLAT=%PLAT%

if "%SOURCE%"=="all" (
	for /R "map\" %%i in (.) do (
		call:makeonemap %%i %PLAT%
	)
) else (
	call:makeonemap %SOURCE% %PLAT%
)

goto:eof  

:makeonemap
	set MAPNAME=%~1
	
	set PLATFORM=%~2
	set TARGET_DIR=%PLATFORM%

	if exist %TARGET_DIR% (
		rd /s /q %TARGET_DIR%
	)
	mkdir %TARGET_DIR%
	
	for %%i in (map\%MAPNAME%\map_*_*.jpg) do (
		if "%PLATFORM%"=="android" (
			PVRTexTool.exe  -i "%%i" -o %TARGET_DIR%\%%~ni.ktx -flip y -f ETC1 -q etcfastperceptual
		) else (
			PVRTexTool.exe  -i "%%i" -o %TARGET_DIR%\%%~ni.pvr -flip y -f PVRTC1_4_RGB -q pvrtcnormal
		)
	)
	
	set BIN_SOURCE_DIR=D:\work\MapEditor\%TARGET_DIR%
	set BIN_TARGET_DIR=D:\rpg02\client\trunk\u3u2\Assets\StreamingAssets\ZoneScenes\%MAPNAME%\
	if exist %BIN_TARGET_DIR% (
		rd /s /q %BIN_TARGET_DIR%
	)
	mkdir %BIN_TARGET_DIR%
	
	python tileprocess.py %BIN_SOURCE_DIR% %BIN_TARGET_DIR% %PLATFORM%
	rd /s /q %BIN_SOURCE_DIR%
	
goto:eof 
