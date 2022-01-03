import cv2
import numpy as np
import os
from glob import glob
import shutil
def predict_license(img):
    net = cv2.dnn.readNet("weights/yolov4-tiny-obj_3000.weights", "config/yolov4-tiny-custom.cfg")
    layer_names = net.getLayerNames()
    output_layers = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]
    scaleRate = img.shape[1] / 500
    width = int(img.shape[1] / scaleRate)
    height = int(img.shape[0] / scaleRate)
    dim = (width, height)
    img = cv2.resize(img, dim, cv2.INTER_AREA)
    height, width, channels = img.shape
    # Detecting objects
    blob = cv2.dnn.blobFromImage(img, 0.00392, (416, 416), (0, 0, 0), True, crop=False)
    net.setInput(blob)
    outs = net.forward(output_layers)
    class_ids = []
    confidences = []
    boxes = []
    for out in outs:
        for detection in out:
            scores = detection[5:]
            class_id = np.argmax(scores)
            confidence = scores[class_id]
            if confidence > 0.3:
                # Object detected
                center_x = int(detection[0] * width)
                center_y = int(detection[1] * height)
                w = int(detection[2] * width)
                h = int(detection[3] * height)

                # Rectangle coordinates
                x = int(center_x - w / 2)
                y = int(center_y - h / 2)
                boxes.append([x, y, w, h])
                confidences.append(float(confidence))
                class_ids.append(class_id)
    indexes = cv2.dnn.NMSBoxes(boxes, confidences, 0.5, 0.4)
    for i in range(len(boxes)):
        if i in indexes:
            x, y, w, h = boxes[i]
            image_license = img[y:y+h, x: x+w]
            # print(boxes[i])
            image_width=img.shape[1]
            image_height=img.shape[0]
            absolute_x = x + 0.5 * w
            absolute_y = y + 0.5 * h
            x = absolute_x / image_width
            y = absolute_y / image_height
            width = w / image_width
            height = h / image_height
            return x, y, width, height
            # return image_license
            # cv2.imwrite(r"predict/license/license.jpg", image_license)
base_dir='../../Dataset/Car_long/'
save_dr='../../Dataset/LicenseDetectDatasetTmp/'
list_images=glob(base_dir+'*')
index=0
list_errors=[]
print(list_images[251])
for image_path in list_images[:500]:
    image=cv2.imread(base_dir+image_path)

    box=predict_license(image)
    if box!=None:
        f=open(save_dr +'carlong'+str(index)+'.txt', 'x')
        f.write('%d %.6f %.6f %.6f %.6f'% (0,box[0], box[1], box[2], box[3]))
        cv2.imwrite(save_dr + 'carlong'+str(index) + '.jpg', image)
        index+=1
    else:
        list_errors.append(image_path)
print(list_errors)
# f=open('errrors.txt', 'r')
# save_dr='../../Dataset/LicenseDetectDataset3/'
# file_errors= f.readlines()
# index=438
# # print(file_errors[0][0:-1])
# for file_error in file_errors[1:]:
#     shutil.copy(file_error[0:-1], save_dr+str(index)+'.jpg')
#     index+=1