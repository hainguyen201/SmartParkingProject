import cv2
import numpy as np
from glob import glob

def char_detection(license, indexSave=0):
    net = cv2.dnn.readNet("weights/yolov4-tiny-obj_final_char_detect2.weights", "config/yolov4-tiny-custom.cfg")
    # license = cv2.imread('predict/license/license.jpg')
    layer_names = net.getLayerNames()
    output_layers = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]
    height, width, channels = license.shape
    blob = cv2.dnn.blobFromImage(license, 0.00392, (416, 416), (0, 0, 0), True, crop=False)
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
                print(confidence)
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
    # print(indexes)
    save_dr = '../../Dataset/CharDetectDatasetCarLong/'
    f = open(save_dr + 'carlong' + str(indexSave) + '.txt', 'x')
    for i in range(len(boxes)):
        if i in indexes:
            x, y, w, h = boxes[i]
            image_char = license[y:y + h, x: x + w]

            image_width = license.shape[1]
            image_height = license.shape[0]
            absolute_x = x + 0.5 * w
            absolute_y = y + 0.5 * h
            x = absolute_x / image_width
            y = absolute_y / image_height
            width = w / image_width
            height = h / image_height


            f.write('%d %.6f %.6f %.6f %.6f'% (0,x, y, width, height))
            f.write('\n')
    cv2.imwrite(save_dr + 'carlong'+str(indexSave) + '.jpg', license)
    f.close()
            # cv2.imwrite("predict/character/" + str(y) + '_' + str(x) + '.jpg', image_char)
list_license_path=glob("../../Dataset/Car_long_license/*.jpg")
indexSave=1
for license_path in list_license_path[1:]:
    license=cv2.imread(license_path)
    char_detection(license,indexSave)
    indexSave=indexSave+1