let stompClient = null;
let username = null;

window.addEventListener('load', function () {
  document.getElementById('connectBtn').addEventListener('click', connect);
  document.getElementById('sendBtn').addEventListener('click', sendMessage);
});

function connect() {
  username = document.getElementById('username').value.trim();
  if (!username) {
    alert('Please enter your name');
    return;
  }

  const socket = new SockJS(window.location.origin + '/ws');
  stompClient = Stomp.over(socket);

  stompClient.connect({}, function () {
    document.getElementById('loginScreen').style.display = 'none';
    document.getElementById('chat').style.display = 'block';

    stompClient.subscribe('/topic/public', function (message) {
      const msg = JSON.parse(message.body);
      showMessage(msg);
    });

    stompClient.send(
      '/app/chat.sendmessage',
      {},
      JSON.stringify({
        sender: username,
        messagetype: 'join',
        content: username + ' joined the chat',
      })
    );
  });
}

function sendMessage() {
  const content = document.getElementById('messageInput').value.trim();
  if (content && stompClient) {
    stompClient.send(
      '/app/chat.sendmessage',
      {},
      JSON.stringify({
        sender: username,
        content: content,
        messagetype: 'chat',
      })
    );
    document.getElementById('messageInput').value = '';
  }
}

function showMessage(message) {
  const messageArea = document.getElementById('messages');
  const div = document.createElement('div');
  div.textContent = message.sender + ': ' + message.content;
  messageArea.appendChild(div);
  messageArea.scrollTop = messageArea.scrollHeight; // auto scroll down
}
