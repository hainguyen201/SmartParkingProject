import cv2
from license_detection import predict_license
from flask import Flask
from flask import send_file
from flask import request, jsonify
from flask_cors import CORS, cross_origin
import base64
from PIL import Image
from pathlib import Path
from io import BytesIO
from character_classification import get_character
from character_detection import char_detection
import os, shutil


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


@app.route('/license', methods=['POST'])
def get_license_code():
    clear_folder()
    base64_png = request.form['image']
    code = base64.b64decode(base64_png.split(',')[1])
    image_decoded = Image.open(BytesIO(code))
    image_decoded.save(Path('image.png'))
    predict_license(cv2.imread('image.png'))
    char_detection()
    char_detect = ''.join(get_character())
    with open("predict/license/license.jpg", "rb") as image_file:
        encoded_string = base64.b64encode(image_file.read()).decode('utf-8')
        return {
            "code": char_detect,
            "license": encoded_string
        }


app.run(host='localhost', port=8787)
