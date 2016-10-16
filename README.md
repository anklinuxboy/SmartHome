# SmartHome

### Introduction:
-----------------
This project was undertaken under [Professor Thomas Little](https://www.bu.edu/eng/profile/thomas-little) as part of
my Masters of Science in Computer Engineering at Boston University.

I chose this project because I feel that **Internet of Things** is still a problem to be solved. We have so many
devices on the network and so many apps that it becomes cumbersome for the user to use them. So I was motivated to 
make things *easier* for the user.

In this project, I was able to control two smart lights and a smart thermostat from a single Android app.

### Project Parts:
------------------
#### Hardware
1. [Intel Edison](http://www.intel.com/content/www/us/en/do-it-yourself/edison.html) as a server and a router for websocket calls from the app.
2. Arduino Integrated Smart Lights designed by the [Smart Lighting Research Lab at Boston University](http://www.bu.edu/smartlighting)
3. [Nest Thermostat](https://nest.com/thermostat/meet-nest-thermostat)

#### Software
1. [Python Flask MicroFramework](http://flask.pocoo.org) as the backend server for the Android app.
2. [Socket IO](http://socket.io) Android and Python bindings for the Websocket calls.
2. Arduino C to control the smart lights.
3. Android/Java for the app.
4. [Firebase](https://firebase.google.com) to communicate to the Nest Cloud.

#### [Final Presentation](http://prezi.com/_wkjk0vll8ql/?utm_campaign=share&utm_medium=copy)
Click the above link to go to the Prezi presentation. It also includes a video demo.



