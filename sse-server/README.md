# SSE


SSE Using Servlet : 

http://localhost:8080/sse-server/sse


SSE Using Jakarta Jax RS

Connect & Disconnect:

Connect : http://localhost:8080/sse-server/rest/events

Disconnect : http://localhost:8080/sse-server/rest/cdievents/{uuid} 

		UUID from log

Connect to broadcaster : http://localhost:8080/sse-server/rest/broadcastEvents

Connect : http://localhost:8080/sse-server/rest/cdievents

Post a message : [POST]  http://localhost:8080/sse-server/rest/messages

Post a broadcasting message : [POST] http://localhost:8080/sse-server/rest/broadcastEvents

