import json

import requests
import base64

baseUrl = "http://localhost:8089/vehicles"


def updateData(files, url):
    response = requests.post(url=url, files=files)
    print(response.text)
    if response.status_code == 200:
        return response.json()
    else:
        return None;


def postData(data, url):
    response = requests.post(url, data=data)
    return response.json()


vehicle = {
    "type": 1,
    "licenseNumber": "24234142",
}
file = {'entrance_image': base64.encodebytes(open(r'./vehicle_images/license.jpg', 'rb').read())}
responses = requests.post(url=baseUrl, data=vehicle, files=file)
print(responses.text)
