var stompClient = null;
var username;

$(function () {
    username = prompt("your name:","");
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.send("/app/onConnect",
            {},
            JSON.stringify({username: username, changeType: 'JOIN'}));
        stompClient.subscribe('/topic/public', function (data) {
            console.log(JSON.parse(data.body));
        });
        stompClient.subscribe("/player/"+username, function (data) {
            $('#balance').html(JSON.parse(data.body));
            console.log("reply: " + JSON.parse(data.body));
        });
        $("#plus").on('click', function () {
            stompClient.send("/app/transfer",{},
                JSON.stringify({value: $('#inputValue').val()}));
        });
        $("#minus").on('click', function () {
            stompClient.send("/app/transfer",{},
                JSON.stringify({value: -($('#inputValue').val())}));
        });
    });
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
});
