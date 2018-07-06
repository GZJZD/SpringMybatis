
    var method = "get";
    var tb_account = $("#accountTable");
    var url_orderPage = url_+"/followOrder/getListFollowOrder.Action?varietyId="+varietyNum+"&accountId="+accountNum;
    var unique_Id = "detailId";
    var sortOrder = "asc";
    var columns = [{
        title: '全选',
        field: 'select',
        //复选框
        checkbox: true,

        align: 'center',
        valign: 'middle'
    }, {
        field: 'followOrder.id',
        title: '平台'
    }, {
        field: 'followOrder.followOrderName',
        title: '账号'
    }, {
        field: 'followOrder.account',
        title: '对应代理人',
        formatter: function (value, row, index) {

            return value.platform.name + "-" + value.username;
        }

    }, {
        field: 'followOrder.variety.varietyName',
        title: '跟单状态',
        formatter: function (value, row, index) {
            if (value == 0) {
                return "未跟单";
            } else {
                return "跟单中";

            }
        }
    }, {
        field: 'followOrder.followManner',
        title: '跟单次数'

    }, {
        field: 'followOrder.startTime',
        title: '跟单收益'
    }, {
        field: '',
        title: '操作',
        formatter: function (value, row, index) {
            return "<a href='page/documentary/documentary.html'> <span style='color: #5b9bd1'>查看跟单</span></a>"+
                "<a href='page/documentary/documentary.html'> <span style='color: #5b9bd1'>修改</span></a>"+
                "<a href='page/documentary/documentary.html'> <span style='color: #5b9bd1'>删除</span></a>";
        }
    }
    ];


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
