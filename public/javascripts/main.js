function cpm(obj, title) {
    var html = "    <div id=\"updateSuccess\" class=\"modal fade modal-margin\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\" style=\"margin-top: 200px;\">\n" +
        "        <div class=\"modal-dialog\" style=\"width: 400px;\">\n" +
        "            <div class=\"modal-content\">\n" +
        "                <div class=\"modal-header bg-primary\">\n" +
        "                    <h4 class=\"modal-title\" align=\"center\" id=\"successTitle\">\n" +
        "                        <span id=\"deleteTitle\" style=\"font-weight: bold;\">" + title + "</span>\n" +
        "                    </h4>\n" +
        "                </div>\n" +
        "                <div class=\"row-fluid\" align=\"center\" >\n" +
        "                    <div id=\"successIcon\">\n" +
        "                        <img src=\"/assets/images/success.png\">\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "                <div class=\"modal-footer bg-info\">\n" +
        "                    <div align=\"center\">\n" +
        "                        <button type=\"button\" class=\"btn green\" onclick=\"sureSuccess(this)\" style=\"width: 100px;\" id=\"successBtn\" value='" + obj + "'>确定</button>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>";

    $("#model").empty();
    $("#model").html(html);
    $("#updateSuccess").modal("show");
}

function deleteShow(id, name) {
    let html = "\n" +
        "    <div id=\"deleteShow\" class=\"modal fade modal-margin\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\" style=\"margin-top: 200px;\">\n" +
        "        <div class=\"modal-dialog\" style=\"width: 400px;\">\n" +
        "            <div class=\"modal-content\">\n" +
        "                <div class=\"modal-header bg-primary\">\n" +
        "                    <h4 class=\"modal-title\" align=\"center\" id=\"title1\">\n" +
        "                        <span style=\"font-weight: bold\">请确认是否删除项目\"\n" +
        "                            <b id=\"dsample\">\n" + name +
        "\n" +
        "                            </b>\"?\n" +
        "                        </span>\n" +
        "                    </h4>\n" +
        "                    <h4 class=\"modal-title\" align=\"center\" id=\"title2\" style=\"display: none;\">\n" +
        "                        <span style=\"font-weight: bold;\">删除中...</span>\n" +
        "                    </h4>\n" +
        "                    <h4 class=\"modal-title\" align=\"center\" id=\"title3\" style=\"display: none;\">\n" +
        "                        <span style=\"font-weight: bold;\">删除成功</span>\n" +
        "                    </h4>\n" +
        "                </div>\n" +
        "                <div class=\"row-fluid\" align=\"center\" >\n" +
        "                    <div id=\"warn1\">\n" +
        "                        <img src=\"/assets/images/warning.png\">\n" +
        "                    </div>\n" +
        "                    <div id=\"warn2\" style=\"display: none;\">\n" +
        "                        <img src=\"/assets/images/timg2.gif\" style=\"height: 109px;\">\n" +
        "                    </div>\n" +
        "                    <div id=\"warn3\" style=\"display: none;\">\n" +
        "                        <img src=\"/assets/images/success.png\">\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "                <div class=\"modal-footer bg-info\">\n" +
        "                    <div align=\"center\">\n" +
        "                        <button type=\"button\" class=\"btn red\" onclick=\"deleteChoose('" + id + "')\" style=\"width: 100px;\" id=\"btn1\">\n" +
        "                            确定</button>\n" +
        "                        <button type=\"button\" class=\"btn green\" data-dismiss=\"modal\" style=\"width: 100px;\" id=\"btn2\">\n" +
        "                            取消</button>\n" +
        "                        <button type=\"button\" class=\"btn green\" onclick=\"updateDelete()\" style=\"width: 100px;\n" +
        "                            display: none;\" id=\"btn3\">确定</button>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n";
    deleteBefore();
    $("#model").empty();
    $("#model").html(html);
    $("#deleteShow").modal("show");
}

function sureSuccess(obj) {
    var state = obj.value;
    $("#updateSuccess").modal("hide");
    console.log(state)
    if (state == "table") {
        updateTable();
    } else {
        location.reload();
    }
}

function deleteBefore() {
    $("#title1").show();
    $("#title2").hide();
    $("#title3").hide();
    $("#warn1").show();
    $("#warn2").hide();
    $("#warn3").hide();
    $("#btn1").show();
    $("#btn2").show();
    $("#btn3").hide();
}

function deleting() {
    $("#title1").hide();
    $("#title3").hide();
    $("#title2").show();
    $("#warn1").hide();
    $("#warn3").hide();
    $("#warn2").show();
    $("#btn1").hide();
    $("#btn2").hide();
    $("#btn3").hide();
}

function deleteAfter() {
    $("#title1").hide();
    $("#title2").hide();
    $("#title3").show();
    $("#warn1").hide();
    $("#warn2").hide();
    $("#warn3").show();
    $("#btn1").hide();
    $("#btn2").hide();
    $("#btn3").show();
}

function deleteFailed(message) {
    $("#deleteShow").modal("hide");
    let html = "\n" +
        "    <div id=\"deleteShow\" class=\"modal fade modal-margin\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\" style=\"margin-top: 200px;\">\n" +
        "        <div class=\"modal-dialog\" style=\"width: 400px;\">\n" +
        "            <div class=\"modal-content\">\n" +
        "                <div class=\"modal-header bg-primary\">\n" +
        "                    <h4 class=\"modal-title\" align=\"center\" id=\"title1\">\n" +
        "                        <span style=\"font-weight: bold\">发生错误</span>\n" +
        "                    </h4>\n" +
        "                </div>\n" +
        "                <div class=\"row-fluid\" align=\"center\" >\n" +
        "                    <h4>\n" + message +
        "                    </h4>\n" +
        "                </div>\n" +
        "                <div class=\"modal-footer bg-info\">\n" +
        "                    <div align=\"center\">\n" +
        "                        <button type=\"button\" class=\"btn green\" onclick=\"updateDelete()\" style=\"width: 100px;\n" +
        "                         \">确定</button>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n";
    $("#model").empty();
    $("#model").html(html);
    $("#deleteShow").modal("show");
}

function updateDelete() {
    $("#deleteShow").modal("hide");
    updateTable();
}

function createTable() {
    $("#table").bootstrapTable({
        columns: [{
            field: "name",
            title: " 任务名",
            sortable: "true",
        }, {
            field: "state",
            title: "运行状态",
            sortable: "true",
            formatter: function (value, row) {
                if (row.state === 0) {
                    return "正在运行 <img src='/assets/images/timg.gif'  style='width: 20px; height: 20px;'><input class='state' value='" + row.state + "'>";
                } else if (row.state === 1) {
                    return "<span class='success-span'>成功</span><input class='state' value='" + row.state + "'>";
                } else {
                    return "<span class='faild-span'>失败</span><input class='state' value='" + row.state + "'>";
                }
            }
        }, {
            field: "date",
            title: "创建时间",
            sortable: "true",
        }, {
            field: "operation",
            title: "操作",

            formatter: function (value, row) {
                let result = "<button class=\"update\" onclick=\"getResult('" + row.id + "','" + row.name + "')\" ><i class=\"fa fa-eye\"></i> 查看结果</button>";
                let log = "<button class=\"update\" onclick=\"OpenLog('" + row.id + "')\" ><i class=\"fa fa-file-text\"></i> 查看日志</button>";
                let de = "<button class='delete' onclick=\"deleteShow('" + row.id + "','" + row.name + "')\"><i class='fa fa-trash'></i> 删除任务</button>";
                if (row.state === 0) {
                    return de;
                } else if (row.state === 1) {
                    return result + log + de;
                } else {
                    return log + de;
                }
            }
        }]
    });
}

function getTaskName(form) {
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let h = date.getHours();
    let m = date.getMinutes();
    let s = date.getSeconds();
    let name = "" + year + month + day + h + m + s + "_Task";
    $("#name").val(name);
    $(form).formValidation("revalidateField", "name");
}

function logShow(log) {
    let html = "    <div id=\"logShow\" class=\"modal fade modal-margin\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">\n" +
        "        <div class=\"modal-dialog\" style=\"width: 1000px;\">\n" +
        "            <div class=\"modal-content\">\n" +
        "                <div class=\"modal-header bg-primary\">\n" +
        "                    <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\"></button>\n" +
        "                    <h4 class=\"modal-title\">\n" +
        "                        <span id=\"lblAddTitle\" style=\"font-weight: bold\">日志信息：</span>\n" +
        "                        <button type=\"button\" class=\"btn-remove\" data-dismiss=\"modal\" ><i class=\"fa fa-remove\"></i></button>\n" +
        "                    </h4>\n" +
        "                </div>\n" +
        "                    <div class=\"modal-body\">\n" +
        "                        <div class=\"row-fluid\" id=\"logInfo\">\n" + log +
        "\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>";
    $("#model").empty();
    $("#model").html(html);
    $("#logShow").modal("show")
}

function openHelp() {
    $("#help").modal("show")
}
