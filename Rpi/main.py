import os
import time
import board
import pyrebase
import requests
import base64
import RPi.GPIO as GPIO
from datetime import datetime
from picamera import PiCamera 
from time import sleep
from adafruit_bme280 import basic as adafruit_bme280

GPIO.setwarnings(False)

i2c = board.I2C()  
bme280 = adafruit_bme280.Adafruit_BME280_I2C(i2c)

config={
    "apiKey": "gQny9AOO0rElTcAFRQIqHAW3ifHSDDQgtvB934iv",
    "authDomain": "smartfridge5451-cd0f4.firebaseapp.com",
    "databaseURL": "https://smartfridge5451-cd0f4-default-rtdb.firebaseio.com/",
    "storageBucket": "smartfridge5451-cd0f4.appspot.com",
}

url = 'https://5d44-103-6-150-60.ngrok.io/get_predictions'


firebase = pyrebase.initialize_app(config)
db = firebase.database()

camera = PiCamera() 
camera.resolution = (1024, 1000)
camera.rotation= 180
camera.awb_mode = 'incandescent'
camera.contrast = 5

def ping():
    """Get reading from HC-SR04"""
    GPIO.setmode(GPIO.BCM)
     
    TRIG = 23 
    ECHO = 18
     
    GPIO.setup(TRIG,GPIO.OUT)
    GPIO.setup(ECHO,GPIO.IN)
     
    GPIO.output(TRIG, False)
    time.sleep(5)
     
    GPIO.output(TRIG, True)
    time.sleep(0.00001)
    GPIO.output(TRIG, False)
     
    while GPIO.input(ECHO)==0:
      pulse_start = time.time()
     
    while GPIO.input(ECHO)==1:
      pulse_end = time.time()
     
    pulse_duration = pulse_end - pulse_start
     
    distance = pulse_duration * 17150
     
    distance = round(distance, 2)
    
    now = datetime.now()
    dt = now.strftime("%d%m%Y%H:%M:%S")
    name = dt+".jpg"
    camera.capture(name)
    
    with open(name, "rb") as img:
        string = base64.b64encode(img.read()).decode('utf-8')
    response = requests.post(url=url, json={'image':string})
    print(response.json())
    
    print ("Distance:",distance,"cm")
    
    print("Temperature: %0.1f C" % bme280.temperature)
    
    print("Humidity: %0.1f %%" % bme280.relative_humidity)
    
    #os.remove(name)
    
    
    data = {
            "datetime": now.strftime("%d%m%Y%H:%M:%S"),
            "temperature": bme280.temperature,
            "humidity": bme280.relative_humidity,
            "doorswing": distance,
            "item(s)": response.json()
            
        }
    
    db.child("Most Recent").set(data)
    db.child("Log").push(data)
     
    GPIO.cleanup()


while True:
    ping()