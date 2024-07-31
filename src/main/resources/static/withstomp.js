const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws',
});

stompClient.onConnect = frame => {
    console.log("Connected to the server");
    stompClient.subscribe('/topic/greetings', message => {
        console.log(JSON.parse(message.body).message);
    });
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
}

const sendMessage = () => {
    const messageInput = document.getElementById("messageInput");
    const message = messageInput.value;
    stompClient.publish({
        destination: '/app/hello',
        body: JSON.stringify({message: message})});
}