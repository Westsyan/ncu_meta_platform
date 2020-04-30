function webScoket(proname) {
    let host = window.location.host;
    let wsurl = "ws://" + host + "/ws?proname=" + proname;
    let ws = new WebSocket(wsurl);

    ws.onopen = function (evt) {
        ws.send(JSON.stringify({"info": "start"}));
    };

    ws.onmessage = function (evt) {
        updateTable();
    };

    ws.onclose = function () {
        // 关闭 websocket
        console.log("连接已关闭,开始轮询...");
        window.setInterval(function () {
            $(".state").each(function (n, value) {
                var st = value.value;
                if (st == 0) {
                    updateTable();
                }
            })
        }, 3000);
    };

    ws.onerror = function () {
        window.setInterval(function () {
            $(".state").each(function (n, value) {
                var st = value.value;
                if (st == 0) {
                    updateTable();
                }
            })
        }, 3000);
    }
}