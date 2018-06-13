var tableId = $("#detailTable");
var method = "post";
var url_detail;
var followOrderId;
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
    title: '序号',

    formatter: function (value, row, index) {
        //获取每页显示的数量
        var pageSize = $('#detailTable').bootstrapTable('getOptions').pageSize;
        //获取当前是第几页
        var pageNumber = $('#detailTable').bootstrapTable('getOptions').pageNumber;
        //返回序号，注意index是从0开始的，所以要加上1
        return pageSize * (pageNumber - 1) + index + 1;
    }
}
    , {
        field: 'netPositionSum',
        title: '净头寸'
    }, {
        field: 'varietyName',
        title: '品种'
    }, {
        field: 'openCloseType',
        title: '类型'
    }, {
        field: 'handNumber',
        title: '手数'
    }, {
        field: 'tradeDirection',
        title: '方向'
    }, {
        field: 'marketPrice',
        title: '点位'
    }, {
        field: 'tradeTime',
        title: '下单时间'
    }, {
        field: 'poundage',
        title: '手续费'
    }, {
        field: 'profit',
        title: '盈亏'
    },
    {
        field: 'remainHandNumber',
        title: '操作',

        align: 'center',
        valign: 'middle',
        formatter: function (value, row, index) {
            var detailId = row.detailId;
            if (value == "0.0") {
                return "-";
            } else {
                return "<a href='javascript:;' class='btn btn-xs green' onclick='manually_close_position(" + detailId + ")' >  <span style='color: #4cae4c'>手动平仓</span></a>"
            }
        }
    }
];

/*
* 手动平仓
* */
function manually_close_position(id) {
    layer.confirm("确认要手动平仓吗？", function (index) {
        $.ajax({
            url: url_ + "/followOrder/manuallyClosePosition.Action?detailId=" + id,
            success: function (data) {
                var obj = eval('(' + data + ')');
                if (obj.success) {
                    alert(1311)
                    layer.msg('平仓成功', {icon: 6, time: 1000}, function () {
                        location.reload();
                        $(".layui-laypage-btn").click();
                    });
                } else {
                    layer.msg('系统出现故障，请联系管理员，进行下一步操作', {icon: 6, time: 1000});
                }

            }
        })
    })

}

/*
* 展示跟单明细列表
* */
function detailShow(id, orderNum, offsetGainAndLoss, poundageTotal) {
    $("#orderNum").html(orderNum);
    $("#offsetGainAndLoss").html(offsetGainAndLoss);
    $("#positionGainAndLoss").html(0);
    $("#poundageTotal").html(poundageTotal);
    url_detail = url_ + "/followOrder/getListDetails.Action?followOrderId=" + id;
    followOrderId = id;
    showByTableId(tableId, method, url_detail, unique_Id, sortOrder, columns);
}

/*
* 展示客户数据列表
* */
function clientTableShow(followOrderId,followManner) {
    var tableClientId = $("#clientTable");
    var url_client = url_ + "/followOrder/getListDetails.Action?followOrderId=" + followOrderId;
    var clientColumns = [
        {
            title: '序号',

            formatter: function (value, row, index) {
                //获取每页显示的数量
                var pageSize = $('#detailTable').bootstrapTable('getOptions').pageSize;
                //获取当前是第几页
                var pageNumber = $('#detailTable').bootstrapTable('getOptions').pageNumber;
                //返回序号，注意index是从0开始的，所以要加上1
                return pageSize * (pageNumber - 1) + index + 1;
            }
        },{
        field: 'netPositionSum',
        title: '跟单编号'
/*
        visible:followManner
*/

    }, {
        field: 'netPositionSum',
        title: '客户名'
    }, {
        field: 'varietyName',
        title: '品种'
    }, {
        field: 'openCloseType',
        title: '类型'
    }, {
        field: 'handNumber',
        title: '手数'
    }, {
        field: 'tradeDirection',
        title: '开仓价'
    }, {
        field: 'marketPrice',
        title: '开仓时间'
    }, {
        field: 'tradeTime',
        title: '平仓价'
    }, {
        field: 'poundage',
        title: '平仓时间'
    }, {
        field: 'profit',
        title: '手续费'
    }, {
        field: 'profit',
        title: '客户盈亏'
    }, {
            field: 'remainHandNumber',
            title: '是否跟单',
/*
            visible:followManner,
*/
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                var detailId = row.detailId;
                if (value == "0.0") {
                    return "-";
                } else {
                    return "<a href='javascript:;' class='btn btn-xs green' onclick='manually_close_position(" + detailId + ")' >  <span style='color: #4cae4c'>手动平仓</span></a>"
                }
            }
        }
    ];
    showByTableId(tableClientId, method, url_client, unique_Id, sortOrder, clientColumns);
}


/*
* 展示跟单参数
* */
function orderParameterShow(followOrder) {
    $("#accountName").html(followOrder.account.platform.name + "-" + followOrder.account.username);
    $("#varietyName").html(followOrder.variety.varietyName);
    var num = 0;//1:不设置，0代表设置
    if (followOrder.maxProfit == num) {
        $("#maxProfitNumber").html(followOrder.maxProfitNumber + "点");
    } else {
        $("#maxProfitNumber").html("不设");
    }
    if (followOrder.maxLoss == num) {
        $("#maxLossNumber").html(followOrder.maxLossNumber + "点");
    } else {
        $("#maxLossNumber").html("不设");
    }
    if (followOrder.accountLoss == num) {
        $("#accountLossNumber").html(followOrder.accountLossNumber + "美金");
    } else {
        $("#accountLossNumber").html("不设");
    }
    if (followOrder.orderPoint == num) {
        if (followOrder.clientPoint == num) {
            $("#orderPointNumber").html("-" + followOrder.clientPointNumber + "点");
        } else {
            $("#orderPointNumber").html("+" + followOrder.clientPointNumber + "点");
        }
    } else {
        $("#orderPointNumber").html("市价");
    }

}

//根据窗口调整表格高度
$(window).resize(function () {
    $('#detailTable').bootstrapTable('resetView', {
        height: tableHeight()
    })
})


//操作栏的格式化
function actionFormatter(value, row, index) {
    var id = row.id;
    var result = "";
    var editUrl = "page/clientUser/client-edit.html";
    var edit_w = 600;
    var edit_h = 400;
    var details = "查看明細";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"EditViewById('" + id + "', view='view','" + editUrl + "','" + edit_w + "','" + edit_h + "')\" title='查看明細'><span class='glyphicon glyphicon-search'></span></a>";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"EditViewById('" + id + "', view='view','" + editUrl + "','" + edit_w + "','" + edit_h + "')\" title='修改'><span class='glyphicon glyphicon-search'></span></a>";
    result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"DeleteByIds('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

    return result;
}