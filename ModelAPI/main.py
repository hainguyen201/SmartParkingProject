import io

import cv2
from license_detection import predict_license
from flask import Flask
from flask import send_file
from flask import request, jsonify, Response
from flask_cors import CORS, cross_origin
import base64
from PIL import Image
from pathlib import Path
from io import BytesIO
from character_classification import get_character2
from character_detection import char_detection
import os, shutil
import numpy as np


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


app = Flask(__name__)
CORS(app)


@app.route('/license2', methods=['POST'])
def get_license_code():
    # try:
        clear_folder()
        image = np.array(Image.open(request.files['image']))
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        # cv2.imshow("", image)
        # cv2.waitKey()
        # cv2.destroyAllWindows()
        license_image = predict_license(image)
        license_image = np.array(license_image)
        char_detection(license_image)
        char_detect = ''.join(get_character2())

        retval, buffer = cv2.imencode('.jpg', license_image)
        encoded_string = base64.b64encode(buffer)
        return {
            "code": char_detect,
            "license": encoded_string.decode('utf-8')
        }
app.run(host='localhost', port=8787)
    # except BaseException as err:
    #     print(err)
    #     return {
    #         "code": "",
    #         "license": ""
    #     }


# @app.route('/license', methods=['POST'])
# def get_license_code2():
#     try:
#         clear_folder()
#         code, box = getLicenseFromAPI(request.files['image'])
#         image = np.array(Image.open(request.files['image']))
#         license_image = image[box.get('ymin'):box.get('ymax'), box.get('xmin'):box.get('xmax')]
#         retval, buffer = cv2.imencode('.jpg', license_image)
#         encoded_string = base64.b64encode(buffer)
#         return {
#             "code": code.upper(),
#             "license": encoded_string.decode('utf-8')
#         }
#     except:
#         return {
#             "code": "",
#             "license": ""
#         }


# img_list=glob("../../Dataset/TestAll/*.jpg")
# f=open("../../Dataset/TestAll/result.txt", 'a')
# for image_path in img_list:
#     image=cv2.imread(image_path)
#     license_image = predict_license(image)
#     license_image = np.array(license_image)
#     char_detection(license_image)
#     char_detect = ''.join(get_character2())
#
#     # retval, buffer = cv2.imencode('.jpg', license_image)
#     # encoded_string = base64.b64encode(buffer)
#     # f.write(image_path+' '+char_detect+'\n')
#     print(char_detect)
#     break
#     # plt.imshow(image)
#     # plt.show()
# f.close()