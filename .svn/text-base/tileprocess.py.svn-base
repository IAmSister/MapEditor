import os
import sys
import struct

if __name__ == "__main__":
	sourcedir = sys.argv[len(sys.argv) - 3]
	targetdir = sys.argv[len(sys.argv) - 2]
	plat = sys.argv[len(sys.argv) - 1]
	files = os.listdir(sourcedir)
	for fi in files:
		fi_d = os.path.join(sourcedir, fi)
		if os.path.isdir(fi_d):
			continue
		else:
			file = fi_d
			if plat == "android":
				if not file[-4:] == ".ktx":
					continue
				else:
					img = open(file, "rb")
					data = img.read()
					img.close()
					img = open(targetdir + fi[:-4] + ".bin", "wb")
					img.write(data[100:])
					img.close()
			elif plat == "ios":
				if not file[-4:] == ".pvr":
					continue
				else:
					img = open(file, "rb")
					data = img.read()
					img.close()
					img = open(targetdir + fi[:-4] + ".bin", "wb")
					img.write(data[67:])
					img.close()

