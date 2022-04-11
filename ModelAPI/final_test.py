import os
import shutil
from glob import glob

import cv2
import numpy as np

from character_classification import get_character2
from character_detection import char_detection
from license_detection import predict_license


def clear_folder():
    folders = ['predict/character/', 'predict/license/']
    for folder in folders:
        for filename in os.listdir(folder):
            file_path = os.path.join(folder, filename)
            try:
                if os.path.isfile(file_path) or os.path.islink(file_path):
                    os.unlink(file_path)
                elif os.path.isdir(file_path):
                    shutil.rmtree(file_path)
            except Exception as e:
                print('Failed to delete %s. Reason: %s' % (file_path, e))

f = open("../../Dataset/TestAll/result2.txt", 'r')
lines = f.readlines()
countC = 0
for line in lines:
    clear_folder()
    image = cv2.imread(line.split()[0])
    license_image = predict_license(image)
    license_image = np.array(license_image)
    char_detection(license_image)
    char_detect = ''.join(get_character2())
    # cv2.imshow("", image)
    # cv2.waitKey()
    # cv2.destroyAllWindows()
    if char_detect == line.split()[1]:
        countC += 1
print(countC)


