let socket;
let userId, recipientId, chattingStatus;

function connect() {
	userId = document.getElementById('userId').value;
    chattingStatus = document.getElementById('chattingStatus').value;
	socket = new WebSocket('ws://localhost:8090/chat?userId=' + userId + '&status=' + chattingStatus);

	socket.onopen = () => {
		console.log('Connected as ' + userId);
		$('#statusIcon').css({color: 'green', fontWeight: 'bold'});
	}
	socket.onmessage = async(event) => {
		console.log('Message from server: ' + event.data);
		setTimeout(async () => {
		    await fetchChatterList();
		}, 200);
	}
	socket.onclose = () => {
		console.log('Disconnected from the server');
        $('#statusIcon').css({color: 'red', fontWeight: 'bold'});
	}
}

async function fetchChatterList() {
    const userId = $('#userId').val();
    try {
        const response = await fetch(`/chatting/getChatterList?userId=${userId}`);
        if (response.ok) {
            const chatterList = await response.json();
            updateChatterList(chatterList);
        }
    } catch (error) {
        console.error('Failed to fetch chatter list:', error);
    }
}

function updateChatterList(chatterList) {
    const tableBody = document.querySelector('.table > tbody');
    tableBody.innerHTML = ''; // 기존 내용을 초기화

    chatterList.forEach(chatter => {
        const row = document.createElement('tr');

        row.innerHTML = `
            <td style="text-align: center;">
                <img src="${chatter.friendProfileUrl}" alt="${chatter.friendUname}" width="40" style="border-radius: 50%">
            </td>
            <td>
                <a href="/chatting/chat/${chatter.friendUid}">
                    <span style="font-weight: bold; font-size: 0.8rem">${chatter.friendUname}</span>
                </a><br>
                <span style="font-size: 0.8rem;">${chatter.message}</span>
            </td>
            <td style="text-align: center;">
                <span style="font-size: 0.8rem;">
                    ${chatter.timeStr}
                    ${chatter.newCount !== 0 ? `<br><span class="new-count">${chatter.newCount}</span>` : ''}
                </span>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function disconnect() {
	if (socket && socket.readyState === socket.OPEN) {
		socket.close();
		console.log('Disconnected from the server by ' + recipientId);
	} else {
		alert('WebSocket is not connected.');
	}
}
