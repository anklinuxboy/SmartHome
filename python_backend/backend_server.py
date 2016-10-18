"""
@Author: Ankit Sharma
This program communicates with the Arduino connected to the Router through 
Ethernet cable. Flask is a server which listens to connections, and depending 
on the message sends the data to the arduino server using sockets.
"""

from flask import Flask, render_template
from flask_socketio import SocketIO, emit, send
from socket import *
import time

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)

# Connect to arduino
def connect_tile1(message):
  HOST = '192.000.0.000'
  PORT = 8080
  client_socket = socket(AF_INET, SOCK_STREAM)
  client_socket.connect((HOST, PORT))
  client_socket.send(message)
  client_socket.close()

# Connect to Tile2
def connect_tile2(message):
  HOST = '192.000.1.000'
  PORT = 8080
  client_socket = socket(AF_INET, SOCK_STREAM)
  client_socket.connect((HOST, PORT))
  client_socket.send(message)


#   define the template to use
@app.route('/')
def index():
  return render_template('index.html')

#  recieve client input
@socketio.on('my event')
def handle_message(message):
  if message == "1" or message == "3" or message == "4" or message == "5":
    print "Sending to arduino"
    connect_tile1(message)
  elif message == "2" or message == "6" or message == "7" or message == "8":
    connect_tile2(message)
  elif message == "9":
    connect_tile1(message)
    connect_tile2(message)

if __name__=='__main__':
  socketio.run(app,host='0.0.0.0',port=81)
