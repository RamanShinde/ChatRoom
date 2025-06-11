let stompClient = null;
let username = null;

function sendMessage() {
	const content = document.getElementById('messageInput').value.trim();
	if (content && stompClient) {
		stompClient.send('/app/chat.sendmessage', {}, JSON.stringify({
			sender: username,
			content: content,
			messagetype: 'chat'
		}));
		document.getElementById('messageInput').value = '';
	}
}

function showMessage(message) {
	const messageArea = document.getElementById('messages');
	const div = document.createElement('div');
	div.textContent = message.sender + ': ' + message.content;

	// Only add 'self' class if message is from the current user
	if (message.sender === username) {
		div.classList.add('self');
	}

	messageArea.appendChild(div);
	messageArea.scrollTop = messageArea.scrollHeight; // Auto scroll to bottom
}

function connect() {
	username = document.getElementById('username').value.trim();
	if (!username) {
		alert('Please enter your name');
		return;
	}

	// Automatically use current host (localhost or deployed)
	const socket = new SockJS('https://chatrooma.onrender.com/ws');
	stompClient = Stomp.over(socket);

	stompClient.connect({}, function() {
		// Switch UI
		document.getElementById('loginScreen').style.display = 'none';
		document.getElementById('chat').style.display = 'block';

		// Subscribe to receive messages
		stompClient.subscribe('/topic/public', function(message) {
			const msg = JSON.parse(message.body);
			showMessage(msg);
		});

		// Register the user to session
		stompClient.send('/app/chat.adduser', {}, JSON.stringify({
			sender: username,
			messagetype: 'join',
			content: username + ' joined the chat'
		}));
	});
}

window.addEventListener('load', function() {
	document.getElementById('connectBtn').addEventListener('click', connect);
	document.getElementById('sendBtn').addEventListener('click', sendMessage);
});
