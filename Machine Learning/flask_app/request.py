import requests
import base64

url = 'http://127.0.0.1:5000/get_predictions'
# my_img = {'image_path': open('data/test/test_3.jpeg', 'rb')}

# filename = 'test1.jpeg'
filename = 'data/test/test_4.jpeg'
with open(filename, "rb") as img:
    string = base64.b64encode(img.read()).decode('utf-8')

response = requests.post(url=url, json={'image':string})
# r = requests.post(url, files=my_img)

# convert server response into JSON format.
print(response.json())
