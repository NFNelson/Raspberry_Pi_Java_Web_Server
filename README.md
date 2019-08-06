# Raspberry_Pi_Java_Web_Server
A simple web server designed in java to run on a raspberry pi

To Execute this software:
ant execute.server will execute the server
ant execute.client will execute to the client(if not using a normal browser)

To connect the client to the server type http://ipaddressofrpi:8080/index.html and the server will send the information to the client.

To host a webpage publicly you must:
1. Port forward in your router settings (make sure you have robust security set up).
2. If using a purchased domain redirect to http://(your public ip):(port number you chose in step 1)/index.html
3. Edit build.xml to point to your webpage file "index.html"
