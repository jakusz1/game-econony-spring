var stompClient = null;
var username;

function addUser(key,username) {
    $('#playerSelect').append("<a id='p-" + username + "' class='dropdown-item player-id' href='#'>" + username + "</a>");
}
function removeUser(username) {
    $('#p-' + username).remove();
}

$(function () {
    username = prompt("your name:","").toLowerCase();
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({username: username}, function (frame) {
        console.log('Connected: ' + frame);

        // stompClient.send("/app/onConnect",
        //     {},
        //     JSON.stringify({username: username, changeType: 'JOIN'}));

        stompClient.subscribe('/topic/public', function (d) {
            //var activeUsers = JSON.parse();
            //console.log(data.body);
            data = JSON.parse(d.body);
            console.log(data);
            console.log(data.changeType);
            console.log(data.username);
            if(data.activeUsers){(data.activeUsers).forEach(addUser);}
            else {
                if (data.changeType === 'JOIN') {
                    addUser(null,data.username);
                }
                else if (data.changeType === 'LEAVE') {
                    removeUser(data.username);
                }
            }
            //console.log(JSON.parse(data.body));
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
        $(".player-id").on('click', function () {
            stompClient.send("/app/transfer",{},
                JSON.stringify({receiverUsername: $(this).text() ,value: $('#inputValue').val()}));
        });
    });
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
});
