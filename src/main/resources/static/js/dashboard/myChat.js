document.addEventListener("DOMContentLoaded", function(){
    connectStomp();
});


const myNickname = document.querySelector(".loginMemberNickname").value;
const yourNickname = window.location.pathname.replace("/dashboard/myChat/", "");


let sock = new SockJS(`http://${location.host}/api/chat/stomp-chating`);

let client = Stomp.over(sock);
ws = client;

function connectStomp(){
    ws.connect({}, function(){
        console.log("connection open");

        ws.subscribe("/api/chat/receive/message/"+myNickname, function(event){
            let data = JSON.parse(event.body);
            let senderNickname = data.senderNickname;
            let message = data.message;
            let time = data.sendTime;

            if(senderNickname != myNickname){   //받은 메시지가 내가 작성한 것인 경우 제외
                if(senderNickname == yourNickname){     //받은 메시지가 대화하고 있는 상대이어야함
                    receive(senderNickname, message, time);
                }
            }
            //스크롤 맨 아래로
            document.querySelector(".msg_history").scrollTop = document.querySelector(".msg_history").scrollHeight;
        });
    });

}


$("button.msg_send_btn").on("click", function(event){
    send();
});

$("input.write_msg").on("keyup", function(event){
    if(event.keyCode == 13){
        send();
    }
});

function send(){
    let message = $("input.write_msg").val();
    //ws.send(message);   //메시지 서버로 전송
    ws.send("/api/chat/send/message/"+yourNickname, {}, JSON.stringify({
        "senderNickname" : myNickname,
        "message" : message
    }));

    $(".msg_history").append(`
        <div class="outgoing_msg">
          <div class="sent_msg">
              <p>${message}</p>
              <span class="time_date"> ${nowTime()}</span>
          </div>
        </div>
    `);
}

function receive(senderNickname, message, time){
    $(".msg_history").append(`
        <div class="incoming_msg">
            <div class="incoming_msg_img"> <img src="https://ptetutorials.com/images/user-profile.png" alt="sunil"> </div>
            <div class="received_msg">
                <div class="receiver">${senderNickname}</div>
                <div class="received_withd_msg">
                    <p>${message}</p>
                    <span class="time_date">${time}</span></div>
            </div>
        </div>
    `);

}




function nowTime(){
    var today = new Date();

    var year = today.getFullYear();
    var month = ('0' + (today.getMonth() + 1)).slice(-2);
    var day = ('0' + today.getDate()).slice(-2);
    var hours = ('0' + today.getHours()).slice(-2);
    var minutes = ('0' + today.getMinutes()).slice(-2);

    return year + '-' + month  + '-' + day + " " + hours + ':' + minutes;
}