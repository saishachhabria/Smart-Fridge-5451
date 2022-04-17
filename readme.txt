Group 2 Members:
1. Saisha Ajay Chhabria |saisha.chhabria@u.nus.edu| A0244449X
2. Mahmoud F M Younis	| e0833581@u.nus.edu | A0243734E
3. Wong Chung Ho Ryan	| e0792452@u.nus.edu | A0242113Y
4. Liang Youfan	| e0724777@u.nus.edu | A0232978N
5. Vijaya Laxmi Tripathi | vijaya.t@u.nus.edu| A0236465Y



Youtube Link: https://youtu.be/sT8glhaPa-0


Computer (server):

1. Setting up the machine learning model:
	
	Ensure the model.pth is placed in the directory path: outputs/model.pth

2. Run the flask application
	
	python3 main.py

3. Host the port 5000 or access it on the same network server as: https://<IPaddress>:5000/get_predictions



Raspberry Pi:

1. For checking if connections are correct:

 	sudo i2cdetect -y 1 (For the bme280)
 	vcgencmd get_camera (For raspberry pi camera)

2. Add the configuration details for Firebase access to main.py in the placeholder.

3. Add the URL to the flask server in the placeholder.

3. For running the main code:

	python3 main.py 
	

	


