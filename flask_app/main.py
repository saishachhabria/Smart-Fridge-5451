from flask import Flask, request, jsonify
import requests
import cv2
import os
import numpy as np
import base64
from detecto import core, utils, visualize
from detecto.visualize import show_labeled_image, plot_prediction_grid

model = core.Model.load('outputs/model_weights.pth', 
                        ['Beer', 'Butter', 'Bread', 'Yoghurt', 'Egg(s)', 'Cheese', 'Milk', 'Banana', 'Cauliflower'])

app = Flask(__name__)

@app.route('/')
def base():
  return 'This is an API for retrieval of predictions from our Smart Fridge Project. Checkout more at https://github.com/saishachhabria.'

@app.route('/get_predictions', methods=['GET', 'POST'])
def process_image():
  if request.json:
    data = request.get_json()

    photo = data['image']
    image = base64.b64decode(photo)

    temp_file = 'temp.jpg'
    with open(temp_file, "wb") as file:
        file.write(image)

    image = utils.read_image(temp_file) 
    predictions = model.predict(image)
    labels, boxes, scores = predictions
    thresh = 0.6
    filtered_indices = np.where(scores>thresh)
    filtered_scores = scores[filtered_indices]
    filtered_boxes=boxes[filtered_indices]
    num_list = filtered_indices[0].tolist()
    filtered_labels = {}
    for i in range(len(num_list)):
      filtered_labels[i] = labels[num_list[i]] 

    img = cv2.imread(temp_file)
    for box in filtered_boxes:
      box = box.tolist()
      x = int(box[0])
      y = int(box[1])
      w = int(box[2])
      h = int(box[3])
      cv2.rectangle(img, (x, y), (x+w, y+h), (0, 0, 255), 2)
    cv2.imwrite('result.jpg', img) 
    if len(filtered_labels) == 0: return jsonify({0: "No data"})
    return jsonify(filtered_labels)

if __name__ == "__main__":
    app.run(debug=True, port=5000)
