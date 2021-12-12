import json

import requests
import base64
import datetime

baseUrl = "http://localhost:8089/vehicles"


class Vehicle:
    def __init__(self, vehicle_type, license_number, entrance_time, exit_time):
        self.type = vehicle_type
        self.licenseNumber = license_number
        self.entranceTime = entrance_time
        self.exitTime = exit_time


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


def addNewVehicle(vehicle_type, license_number, entrance_image):
    time_now = datetime.datetime.now()
    vehicle = Vehicle(vehicle_type=vehicle_type, license_number=license_number, entrance_time=time_now)
    file = {
        'entranceImage': entrance_image,
        'vehicle': (None, json.dumps(vehicle))
    }
    response = requests.post(url=baseUrl, files=file)
    print(response.text)
