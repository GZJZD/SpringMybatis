$(function () {
    var tableId = $("#followOrderTable");
    var method = "get";
    //var url_  ="demo.json";
  //  var url_ = "/getListFollowOrder.Action";
    var url_ = "/sm-1.0-SNAPSHOT/getListFollowOrder.Action";
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
        title: '跟单编号'
    }, {
        field: 'followOrder.followOrderName',
        title: '跟单方案'
    }, {
        field: 'followOrder.account',
        title: '跟单账号',
        formatter: function (value, row, index) {

            return value.platform.name + "-" + value.username;
        }

    }, {
        field: 'followOrder.variety.varietyName',
        title: '品种'
    }, {
        field: 'followOrder.followManner',
        title: '跟单方式',
        formatter: function (value, row, index) {
            if (value == 0) {
                return "跟客户";
            } else {
                return "跟净头寸";

            }
        }
    }, {
        field: 'followOrder.startTime',
        title: '开始时间'
    }, {
        field: '',
        title: '跟单成功率',
        formatter: function (value, row, index) {
            return row.successTotal+"/"+row.allTotal;
        }

    }, {
        field: 'positionGainAndLoss',
        title: '持仓盈亏'
    }, {
        field: 'offsetGainAndLoss',
        title: '平仓盈亏'
    }, {
        field: 'poundageTotal',
        title: '手续费'
    }, {
        field: 'profitAndLossRate',
        title: '盈亏效率'
    }, {
        field: 'clientProfit',
        title: '客户盈亏'
    }, {
        field: 'followOrder.followOrderStatus',
        title: '跟单状态',
        formatter: function (value, row, index) {
            if (value == 1) {
                return "<span style='color: #26a69a'>跟进中</span>"
            } else if (value == 2) {
                return "<span style='color: #c49f47'>已暂停</span>"

            } else if (value == 0) {
                return "<span style='color: #8775a7'>已停止</span>"

            }
        }
    },
        {
            field: 'followOrder.followOrderStatus',
            title: '操作',

            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                var followOrderId = row.followOrder.id;
                var successTotal = row.successTotal;
                var orderNum= row.handNumberTotal;
                var offsetGainAndLoss = row.offsetGainAndLoss;
                var poundageTotal = row.poundageTotal;
                if (value == "1") {
                    return "<a onclick='documentary_stop(this," + followOrderId + ")' href='javascript:;' title='暂停'> <i class='layui-icon'>&#xe651;</i> </a>" +
                        "<a onclick='documentary_stop(this," + followOrderId + ")' href='javascript:;' title='停止'> <i class='layui-icon'>&#x1006;</i> </a>" +
                        "<a title=\"查看\" onclick=\"follow_details("+successTotal+","+orderNum+","+offsetGainAndLoss+","+poundageTotal+"," + followOrderId + ",'/跟单明细','/" +
                        "sm-1.0-SNAPSHOT/page/documentary/documentary-details.html')\" href=\"javascript:;\"><i class=\"layui-icon\"> &#xe615;</i></a>" +
                        "<a title=\"编辑\" onclick=\"common_show()('编辑','member-edit.html',600,400)\" href=\"javascript:;\">\n" +
                        "<i class=\"layui-icon\">&#xe642;</i>" +
                        "</a>";
                } else {
                    return "<a onclick='documentary_stop(this," + followOrderId + ")' href='javascript:;' title='启用'> <i class='layui-icon'>&#xe652;</i> </a>" +
                        "<a onclick='documentary_stop(this," + followOrderId + ")' href='javascript:;' title='停止'> <i class='layui-icon'>&#x1006;</i> </a>" +
                        "<a title=\"查看\" onclick=\"follow_details("+successTotal+","+orderNum+","+offsetGainAndLoss+","+poundageTotal+"," + followOrderId + ",'跟单明细','sm-1.0-SNAPSHOT/page/documentary/documentary-details.html')\" href=\"javascript:;\"><i class=\"layui-icon\"> &#xe615;</i></a>" +
                        "<a title=\"编辑\" onclick=\"common_show()('编辑','member-edit.html',600,400)\" href=\"javascript:;\">\n" +
                        "<i class=\"layui-icon\">&#xe642;</i>" +
                        "</a>";
                }
            }
        }
    ];
    /*弹出层*/
    /*
        参数解释：
        title   标题
        url     请求的url
        id      需要操作的数据id
        w       弹出层宽度（缺省调默认值）
        h       弹出层高度（缺省调默认值）
    */

    $(function () {
        showByTableId(tableId, method, url_, unique_Id, sortOrder, columns);
    })
    //根据窗口调整表格高度
    $(window).resize(function () {
        $('#detailTable').bootstrapTable('resetView', {
            height: tableHeight()
        })
    })





    /*用户-停用*/
    function documentary_stop(obj, id) {

        layer.confirm('确认要停用吗？确定后系统将不会继续跟进客户的做单数据', function (index) {

            if ($(obj).attr('title') == '启用') {

                //发异步把用户状态进行更改
                $(obj).attr('title', '暂停')
                $(obj).find('i').html('&#xe651;');

                $(obj).parents("tr").find(".td-status").find('span').addClass('layui-btn-disabled').html('已停用');
                layer.msg('已启用!', {icon: 6, time: 1000});

            } else {
                $(obj).attr('title', '启用')
                $(obj).find('i').html('&#xe652;');

                $(obj).parents("tr").find(".td-status").find('span').removeClass('layui-btn-disabled').html('已启用');
                layer.msg('已暂停!', {icon: 5, time: 1000});
            }

        });
    }
})
function follow_details(successTotal,orderNum,offsetGainAndLoss,poundageTotal,id, title, url, w, h) {

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
            //调用子页面的方法

            iframeWin.child(id,successTotal+"/"+orderNum,offsetGainAndLoss,poundageTotal);

        },
        btn: ['关闭']
    });
}
