
    var method = "get";
    var tb_account = $("#accountTable");
    var url_account = url_+"/account/getListAccount.Action";
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
        field: 'platformName',
        title: '平台'
    }, {
        field: 'account',
        title: '账号'
    }, {
        field: 'agentName',
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
            return "<a href='javascript:;' onclick='lo()'> <span style='color: #5b9bd1'>查看跟单</span></a>"+
                "<a href='page/documentary/documentary.html'> <span style='color: #5b9bd1'>修改</span></a>"+
                "<a href='page/documentary/documentary.html'> <span style='color: #5b9bd1'>删除</span></a>";
        }
    }
    ];

$(function () {
    showByTableId(tb_account, method, url_account, unique_Id, sortOrder, columns);

})
    function lo() {
        url_+"/account/goToFollowOrderPage.Action"

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
            content: url,
            success: function (layero, index) {
                //找到子页面
                var iframeWin = window['layui-layer-iframe' + index]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                //调用子页面的方法
                iframeWin.findPlatform();
                iframeWin.findAgent();
            }
        });
}
