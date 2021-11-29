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


def gen_frames():
    camera = cv2.VideoCapture('http://192.168.1.2:4747/video')
    while True:
        success, frame=camera.read()
        if not success:
            break
        else:
            ret, buffer=cv2.imencode('.jpg', frame)
            frame=buffer.tobytes()
            yield (b'--frame\r\n'
                   b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')  # concat frame one by one and show result


@app.route('/video_feed')
def video_feed():
    return Response(gen_frames(), mimetype='multipart/x-mixed-replace; boundary=frame')
app.run(host='localhost', port=8787)
