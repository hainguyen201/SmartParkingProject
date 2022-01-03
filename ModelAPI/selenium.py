import requests
from pprint import pprint
from glob import glob
from license_detection import predict_license
import cv2
# from main import clear_folder
from character_classification import sort_character
from character_detection import char_detection
import os
import shutil


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


# list_file =glob("F:/OneDrive - Hanoi University of Science and Technology/20211/DATN/Process/Dataset/Car_long/*.jpg")
# regions = ['vn'] # Change to your country
# newDir= "F:/OneDrive - Hanoi University of Science and Technology/20211/DATN/Process/Dataset/Car_long_character_classification/"
# index = 0
#
# for file in list_file:
#     clear_folder()
#     with open(file, 'rb') as fp:
#         response = requests.post(
#             'https://api.platerecognizer.com/v1/plate-reader/',
#             data=dict(regions=regions),  # Optional
#             files=dict(upload=fp),
#             headers={'Authorization': 'Token d2fef091cb3849cd48558bb1fe9c61874f8dfd69'})
#         result=response.json().get('results')
#         image= cv2.imread(file)
#         image=cv2.cvtColor(image, cv2.COLOR_BGR2RGB);
#
#         if result!= None and len(result)>0:
#             plate=response.json().get('results')[0].get('plate')
#             pprint(plate)
#             license = predict_license(image)
#             try:
#                 char_detection(license)
#                 image_paths = sort_character(origin_path='predict/character/')
#                 for i in range(len(image_paths)):
#                     if not os.path.exists(newDir+plate[i].upper()):
#                         os.mkdir(newDir+plate[i].upper())
#                     shutil.move(image_paths[i], newDir+plate[i].upper()+'/'+str(index)+'.jpg')
#                     index=index+1
#             except:
#                 print("Error: "+file)
def getLicenseFromAPI(license):
    response = requests.post(
        'https://api.platerecognizer.com/v1/plate-reader/',
        data=dict(regions=['vn']),  # Optional
        files=dict(upload=license),
        headers={'Authorization': 'Token 3e9eb962924ef8850df5244723a23dad90f18d3d'})
    result = response.json().get('results')[0].get('plate')
    box = response.json().get('results')[0].get('box')
    return result, box
# with open('../../Dataset/GreenParking/0000_02187_b.jpg', "rb") as f:
#     print(getLicenseFromAPI(f))
