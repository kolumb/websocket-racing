var socket =  io.connect('http://localhost:9091');
let userName// = 'user' + Math.floor(Math.random()*1000)
console.log('my name is ' + userName)
let users = {}
let myId
socket.on('connect', () => {
    console.log('connected!')
});
socket.on('you', data => {
    console.log('you', data)
    myId = data
    if(users[myId]) {
        userName = users[myId]
        console.log('Now my name is ' + userName)
        rerender('you')
    }
});
socket.on('everybody', data => {
    users = data
    if(myId) {
        userName = users[myId]
        console.log('At least my name is ' + userName)
    }
    rerender('everybody')
});
socket.on('new user', data => {
    users[data[0]] = data[1]
    rerender('new user')
});
socket.on('down', data => {
    console.log(data.userName + ': down!')
});
socket.on('up', data => {
    console.log(data.userName + ': up!')
});
socket.on('disconnected', data => {
    console.log('disconnected: ' + data)
    rerender('disconnected')
})
addEventListener('keydown', e => {
    switch (e.code) {
    case 'ArrowDown':
    case 'KeyS':
        socket.emit('down', {userName:userName})
        break
    case 'ArrowUp':
    case 'KeyW':
        socket.emit('up',{userName:userName})
    }
})