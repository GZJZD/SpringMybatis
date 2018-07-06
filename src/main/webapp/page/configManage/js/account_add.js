
/*
* 平台下拉框
* */
function findPlatform() {
    $.ajax({
        url:url_+"/platform/getListPlatform.Action",
        type:'GET', //GET
        async:true,    //或false,是否异步
        data:{

        },
        timeout:5000,    //超时时间
        dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend:function(xhr){

        },
        success:function (data) {
            //

            var content = "";
            $.each(data,function (index,ele) {
                content += "<option value="+ele.id+">"+ele.name+"</option>"
            });
            $("#platform_id").append(content);

        },
        error:function(xhr,textStatus){

        },
        complete:function(){

        }
    })
}
/*
* 代理人下拉框
* */
function findAgent() {
    $.ajax({
        url:url_+"/agent/getListAgent.Action",
        type:'GET', //GET
        async:true,    //或false,是否异步
        data:{

        },
        timeout:5000,    //超时时间
        dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend:function(xhr){

        },
        success:function (data) {
            //

            var content = "";
            $.each(data,function (index,ele) {
                content += "<option value="+ele.id+">"+ele.name+"</option>"
            });
            $("#agent_id").append(content);

        },
        error:function(xhr,textStatus){

        },
        complete:function(){

        }
    })
}


layui.use(['form', 'layedit', 'laydate'], function () {
    var form = layui.form
        , layedit = layui.layedit
        , laydate = layui.laydate;

    //自定义验证规则
    form.verify({
        regPwd: function (value) {
            //获取密码
            var pwd = $("#pwd").val();
            var reg = $("#regPwd").val();
            if (pwd != reg) {
                return '两次输入的密码不一致';
            }
        }
    });
    //监听提交
    form.on('submit(save)', function (data) {
        var platformId = $("#platform_id option:selected").val();
        var agentId = $("#agent_id option:selected").val();
        var account = $("#account").val();
        var password = $("#pwd").val();
        $.ajax({
            url: url_ + "/account/createAccount.Action",
            type: 'post', //GET
            async: true,    //或false,是否异步
            data: {
                platformId: platformId,
                agentId: agentId,
                name: account,
                password: password
            },
            timeout: 5000,    //超时时间
            dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
            beforeSend: function (xhr) {

            },
            success: function (data) {
                parent.layer.closeAll();
                if(data.success){
                    parent.layer.msg(data.msg);
                }else{
                    parent.layer.msg(data.msg);
                }

            },
            error: function (xhr, textStatus) {

            },
            complete: function () {


            }
        })


    });


});

function cancel() {
    parent.layer.closeAll();
}

function accountAdd(){
        var url = "account-add.html";
        var title="账号管理";
        var w=1000;
        var h=600;
        if (title == null || title == '') {
            title=false;
        };
        if (url == null || url == '') {
            url="404.html";
        };
        if (w == null || w == '') {
            w=($(window).width()*0.9);
        };
        if (h == null || h == '') {
            h=($(window).height() - 50);
        };
        layer.open({
            type: 2,
            area: [w+'px', h +'px'],
            fix: false, //不固定
            maxmin: true,
            shadeClose: true,
            shade:0.4,
            title: title,
            content: url
        });
}
