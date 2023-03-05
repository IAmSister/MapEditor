
RPG02_ROOT=/Users/zhangzhe/Developer/rpg02

function makeonemap()
{
	MAPNAME=$1
	PLATFORM=$2
	targetDir=map/$MAPNAME/$PLATFORM

	if [ -d "$targetDir" ]; then
		rm -rf $targetDir
	fi
	mkdir $targetDir

	for img in `ls map/$MAPNAME/map_*_*.jpg`
	do
	    targetImg=${img##*/}
		targetImg=${targetImg%%.*}
		if [[ "$PLATFORM" = "android" ]]; then
			./PVRTexTool -i $img -o $targetDir/$targetImg.ktx -flip y -f ETC1 -q etcfastperceptual
		elif [[ "$PLATFORM" = "ios" ]]; then
			./PVRTexTool -i $img -o $targetDir/$targetImg.pvr -flip y -f PVRTC1_4_RGB -q pvrtcnormal
		fi
	done

	sourceDir=$RPG02_ROOT/shared/MapEditor/map/$MAPNAME/$PLATFORM/
	targetDir=$RPG02_ROOT/client/u3u2/Assets/StreamingAssets/ZoneScenes/$MAPNAME/
	if [ -d "$targetDir" ]; then
		rm -rf $targetDir
	fi
	mkdir $targetDir
	
	python tileprocess.py $sourceDir $targetDir $PLATFORM
	rm -rf $sourceDir
}

function tilername()
{
	MAPNAME=$1
	for i in `ls map/$MAPNAME/map,*,*.jpg`; do mv "$i" `echo "$i" |awk -F ',' '{print $1 "_" $2 "_" $3}'`; done
}

echo 地图名称（all表示全部）：
read SOURCE
echo 平台（1:安卓，2:iOS）
read PLAT_NUM

if [[ $PLAT_NUM -eq 1 ]]; then
	PLAT="android"
elif [[ $PLAT_NUM -eq 2 ]]; then
	PLAT="ios"
fi

if [[ "$SOURCE" = "all" ]]; then
	for map in `ls map`
	do
		makeonemap $map $PLAT
		#tilername $map
	done
else
	makeonemap $SOURCE $PLAT
fi