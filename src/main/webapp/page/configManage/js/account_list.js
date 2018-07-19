var method = "get";
var tb_account = $("#accountTable");
var url_account = url_ + "/account/getListAccount.Action";
var unique_Id = "id";
var sortOrder = "asc";
var columns = [{
    title: '全选',
    field: 'select',
    //复选框
    checkbox: true,

    align: 'center',
    valign: 'middle'
}, {
    field: 'account.platform.name',
    title: '平台'
}, {
    field: 'account.account',
    title: '账号'
}, {
    field: 'account.agent.name',
    title: '对应代理人'

}, {
    field: 'status',
    title: '跟单状态',
    formatter: function (value, row, index) {
        if (value == "未跟单") {
            return "<span style='color: #45b6af'>未跟单</span>";
        } else {
            return "<span style='color: #c6c6c6'>跟单中</span>";

        }
    }
}, {
    field: 'allTotal',
    title: '跟单次数'

}, {
    field: 'profitAndLoss',
    title: '跟单收益'
}, {
    field: '',
    title: '操作',
    formatter: function (value, row, index) {
        var account = JSON.stringify(row.account);
        var accountInfo =  JSON.stringify(row)
        return "<a href='javascript:;' onclick='lo()'> <span style='color: #5b9bd1'>查看跟单</span></a>" +
            "<a href='javascript:;' onclick='accountAdd(1," + account + ")'> <span style='color: #5b9bd1'>修改</span></a>" +
            "<a href='javascript:;' onclick='deleteAccount("+accountInfo+")'> <span style='color: #5b9bd1'>删除</span></a>";
    }
}
];


$(function () {
    showByTableId(tb_account, method, url_account, unique_Id, sortOrder, columns);

})

function lo() {
    location.href = "../documentary/documentary.html"
    $.ajax({
        url: url_ + "/account/goToFollowOrderPage.Action",
        type: 'GET', //GET
        async: true,    //或false,是否异步
        data: {},
        timeout: 5000,    //超时时间
        dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend: function (xhr) {

        },
        success: function (data) {

        },
        error: function (xhr, textStatus) {

        },
        complete: function () {

        }
    })

}

function deleteAccount(accountInfo) {
    layer.confirm("确定要删除"+accountInfo.platformName+"平台的"+accountInfo.account.account+"账号吗？",function (index) {
        if(accountInfo.status == "跟单中"){
            layer.msg("该账号还在跟单中不能删除");
        }else {
            $.ajax({
                url: url_ + "/account/deleteAccount.Action",
                type: 'post', //GET
                async: true,    //或false,是否异步
                data: {
                    accountId: accountInfo.account.id
                },
                timeout: 5000,    //超时时间
                dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
                beforeSend: function (xhr) {

                },
                success: function (data) {
                    layer.msg(data.msg);
                    $("#accountTable").bootstrapTable('refresh', {
                        silent: true//静默跟新
                    });
                },
                error: function (xhr, textStatus) {

                },
                complete: function () {

                }
            })
        }
    })

}

function accountAdd(num, account) {
    var url = "account-add.html";
    var title = "账号管理";
    var w = 1000;
    var h = 600;
    if (title == null || title == '') {
        title = false;
    }
    ;
    if (url == null || url == '') {
        url = "404.html";
    }
    ;
    if (w == null || w == '') {
        w = ($(window).width() * 0.9);
    }
    ;
    if (h == null || h == '') {
        h = ($(window).height() - 50);
    }
    ;
    layer.open({
        type: 2,
        area: [w + 'px', h + 'px'],
        fix: false, //不固定
        maxmin: true,
        shadeClose: true,
        shade: 0.4,
        title: title,
        content: url,
        success: function (layero, index) {
            //找到子页面
            var iframeWin = window['layui-layer-iframe' + index]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();

            iframeWin.setParameter(num, account);


        }
    });
}
