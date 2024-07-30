const socket = new WebSocket('ws://localhost:8080/ws');
let topic = null;
socket.addEventListener('open', event => {
    console.log("WebSocket connection opened to server");
})

socket.addEventListener('message', event => {
    console.log(event.data);
});

socket.addEventListener('close', event => {
    console.log("WebSocket connection closed");
});

socket.addEventListener('error', event => {
    console.error("WebSocket error observed:", event);
});


function sendMessage() {
    const messageInput = document.getElementById("messageInput");
    const message = messageInput.value;
    socket.send(topic + '||' + message);
}

function subscribeTopic() {
    const topicInput = document.getElementById("topic");
    topic = topicInput.value;
    console.log("Subscribing to topic: " + topic);
    socket.send('SUB:' + topic);
}