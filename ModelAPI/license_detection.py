import cv2
import numpy as np
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
            imageLicense = img[y:y+h, x: x+w]
            cv2.imwrite(r"predict/license/license.jpg", imageLicense)

