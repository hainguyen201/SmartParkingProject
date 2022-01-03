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
from character_classification import get_character, get_character2
from character_detection import char_detection
import os, shutil
import time
import numpy as np
from selenium import getLicenseFromAPI
import io
from sql_connect import addNewVehicle


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
    try:
        clear_folder()
        image = np.array(Image.open(request.files['image']))
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
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
    except:
        return {
            "code": "",
            "license": ""
        }


@app.route('/license', methods=['POST'])
def get_license_code2():
    try:
        clear_folder()
        code, box = getLicenseFromAPI(request.files['image'])
        image = np.array(Image.open(request.files['image']))
        license_image = image[box.get('ymin'):box.get('ymax'), box.get('xmin'):box.get('xmax')]
        retval, buffer = cv2.imencode('.jpg', license_image)
        encoded_string = base64.b64encode(buffer)
        return {
            "code": code.upper(),
            "license": encoded_string.decode('utf-8')
        }
    except:
        return {
            "code": "",
            "license": ""
        }


@app.route('/licenseBE', methods=['POST'])
def get_license_be():
    clear_folder()
    base64_png = request.files['images']
    code = base64.b64decode(base64_png.split(',')[1])
    image_decoded = Image.open(BytesIO(code))
    # image_decoded.save(Path('image.png'))
    image = np.array(image_decoded)
    predict_license(image)
    char_detection()
    char_detect = ''.join(get_character())
    with open("predict/license/license.jpg", "rb") as image_file:
        encoded_string = base64.b64encode(image_file.read()).decode('utf-8')
        return {
            "code": char_detect,
            "license": encoded_string
        }


@app.route('/vehicle_images', methods=['POST'])
def video_feed():
    json_request = request.get_json()
    camera_ip = json_request['camera_ip']
    cv2.imwrite('vehicle_images/license.jpg', gen_image(camera_ip))
    return 'ok'


@app.route('/entrances', methods=['POST'])
def vehicleEntrance():
    entrance_image = request.form['imageFile']
    print(type(entrance_image))


def gen_image(camera_ip):
    camera = cv2.VideoCapture(camera_ip)
    time.sleep(3)
    success, frame = camera.read()
    if not success:
        return gen_image(camera_ip)
    else:
        return frame


app.run(host='localhost', port=8787)
