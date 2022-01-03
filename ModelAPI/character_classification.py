from tensorflow.keras.layers import Dense, Conv2D, MaxPooling2D, Dropout, Flatten, AveragePooling2D
from tensorflow.keras.models import Sequential
from tensorflow.keras import optimizers
from tensorflow.keras.preprocessing import image
import cv2
import numpy as np
from glob import glob
import matplotlib.pyplot as plt
import tensorflow as tf
import os


def sort_character(origin_path):
    image_paths = os.listdir(origin_path)
    # image_paths.sort(key=lambda x: int(x[0:-4].split('_')[0]))
    image_paths.sort(key=lambda x: int(x[0:-4].split('_')[1]) + 3 * int(x[0:-4].split('_')[0]))
    path1 = image_paths[0:4]
    path2 = image_paths[4:-1]
    path1.sort(key=lambda x: int(x[0:-4].split('_')[1]))
    path2.sort(key=lambda x: int(x[0:-4].split('_')[1]))
    new_paths = path1 + path2
    return [origin_path + path for path in image_paths]


def image_segmentation(character_image, k):
    k = 2
    criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 100, 0.2)
    pixel_values = character_image.reshape((-1, 3))
    pixel_values = np.float32(pixel_values)
    _, labels, (centers) = cv2.kmeans(pixel_values, k, None, criteria, 10, cv2.KMEANS_RANDOM_CENTERS)
    # convert back to 8 bit values
    centers = np.uint8(centers)

    # flatten the labels array
    labels = labels.flatten()
    max_labels = np.argmax(centers)
    min_labels = np.argmax(centers)
    # convert all pixels to the color of the centroids
    segmented_image = centers[labels.flatten()]
    # reshape back to the original image dimension
    segmented_image = segmented_image.reshape(character_image.shape)
    return segmented_image


def get_character():
    inv_map = {0: '0', 1: '1', 2: '2', 3: '3', 4: '4', 5: '5', 6: '6', 7: '7', 8: '8', 9: '9', 10: 'A',
               11: 'B', 12: 'C', 13: 'D', 14: 'E', 15: 'F', 16: 'G', 17: 'H', 18: 'I', 19: 'J', 20: 'K',
               21: 'L', 22: 'M', 23: 'N', 24: 'P', 25: 'Q', 26: 'R', 27: 'S', 28: 'T', 29: 'U', 30: 'V',
               31: 'W', 32: 'X', 33: 'Y', 34: 'Z'}
    model = Sequential()
    model.add(Conv2D(32, (3, 3), padding='same', activation='relu', input_shape=(28, 28, 3)))
    model.add(Conv2D(32, (3, 3), activation='relu'))
    model.add(MaxPooling2D(pool_size=(2, 2)))
    model.add(Dropout(0.25))

    model.add(Conv2D(64, (3, 3), padding='same', activation='relu'))
    model.add(Conv2D(64, (3, 3), activation='relu'))
    model.add(MaxPooling2D(pool_size=(2, 2)))
    model.add(Dropout(0.25))

    model.add(Conv2D(64, (3, 3), padding='same', activation='relu'))
    model.add(Conv2D(64, (3, 3), activation='relu'))
    model.add(MaxPooling2D(pool_size=(2, 2)))
    model.add(Dropout(0.25))

    model.add(Flatten())
    model.add(Dense(512, activation='relu'))
    model.add(Dropout(0.5))
    model.add(Dense(35, activation='sigmoid'))
    model.summary()
    model.compile(loss="categorical_crossentropy", optimizer=optimizers.Adam(1e-3), metrics=['acc'])
    model.load_weights(r"classification_model/modelCharacter6.h5")
    image_paths = sort_character(origin_path='predict/character/')
    char_detect = []
    for path in image_paths:
        img = cv2.imread(path)
        print(path)
        img = cv2.resize(img, (28, 28))
        img = image_segmentation(img, 8)

        X = image.img_to_array(img)
        X = np.expand_dims(X, axis=0)
        images = np.vstack([X])
        value = model.predict(images)
        char_detect.append(inv_map[np.argmax(value)])
    return char_detect


def get_character2(ActivationFunc='tanh', input_shape=(28,28,3), Nclasses=35):
    inv_map = {0: '0', 1: '1', 2: '2', 3: '3', 4: '4', 5: '5', 6: '6', 7: '7', 8: '8', 9: '9', 10: 'A',
               11: 'B', 12: 'C', 13: 'D', 14: 'E', 15: 'F', 16: 'G', 17: 'H', 18: 'I', 19: 'J', 20: 'K',
               21: 'L', 22: 'M', 23: 'N', 24: 'P', 25: 'Q', 26: 'R', 27: 'S', 28: 'T', 29: 'U', 30: 'V',
               31: 'W', 32: 'X', 33: 'Y', 34: 'Z'}
    model = Sequential()
    model.add(Conv2D(filters=6, kernel_size=(5, 5), strides=(1, 1), activation=ActivationFunc, input_shape=input_shape,
                      padding="same"))

    model.add(AveragePooling2D(pool_size=(2, 2), strides=(2, 2), padding='valid'))
    model.add(Conv2D(filters=16, kernel_size=(5, 5), strides=(1, 1), activation=ActivationFunc, padding='valid'))
    model.add(AveragePooling2D(pool_size=(2, 2), strides=(2, 2), padding='valid'))
    model.add(Flatten())
    model.add(Dense(units=120, activation=ActivationFunc))
    model.add(Dense(units=84, activation=ActivationFunc))
    model.add(Dense(units=Nclasses, activation='softmax'))
    model.compile(optimizer='adam', loss=tf.keras.losses.categorical_crossentropy, metrics=['accuracy'])
    model.load_weights(r"classification_model/modelCharacter8.h5")
    image_paths = sort_character(origin_path='predict/character/')
    char_detect = []
    for path in image_paths:
        img = cv2.imread(path, 0)
        print(path)

        # img = image_segmentation(img, 8)
        img=cv2.threshold(img, 0,255, cv2.THRESH_OTSU)[1]
        img=cv2.cvtColor(img, cv2.COLOR_GRAY2RGB)
        img = cv2.resize(img, (28, 28))
        # plt.imshow(img)
        # plt.show()
        X = image.img_to_array(img)
        X = np.expand_dims(X, axis=0)
        images = np.vstack([X])
        value = model.predict(images)
        char_detect.append(inv_map[np.argmax(value)])
    return char_detect


get_character2()

