let socket;
window.socket = window.MozWebSocket || window.WebSocket;
if (window.WebSocket) {
    socket = new WebSocket("ws://localhost:8888/websocket");
    socket.onmessage = function (event) {
        handleNewMsg(event.data);
    };
    socket.onopen = function (event) {
        send("loginReq:admin");
    };
} else {
    alert("你的浏览器不支持 WebSocket！");
}

function send(message) {
    if (!window.WebSocket) {
        return;
    }
    if (socket.readyState == WebSocket.OPEN) {
        socket.send(message);
    } else {
        alert("连接没有开启.");
    }
}