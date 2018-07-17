var tableId = $("#detailTable");
var tableClientId = $("#clientTable");
var method = "post";
var url_detail;
var followOrderId;
var unique_Id = "detailId";
var sortOrder = "asc";
var clientStatus = $("#clientFollowOrder option:selected").val();
var clientName = $("#clientName option:selected").val();
var varietyName;
var columns = [
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
        title: '方向',
        formatter: function (value, row, index) {
            if (value == 1) {
                return "空";
            } else {
                return "多";
            }
        }
    }, {
        field: 'marketPrice',
        title: '点位'
    }, {
        field: 'tradeTime',
        title: '下单时间',
        sortable: true

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
            if (value == "0" || value == null) {
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
function detailShow(varietyName, name, manager, id, orderNum, offsetGainAndLoss,positionGainAndLoss, poundageTotal) {
    $("#orderNum").html(orderNum);
    $("#offsetGainAndLoss").html(offsetGainAndLoss);
    $("#positionGainAndLoss").html(positionGainAndLoss);
    $("#poundageTotal").html(poundageTotal);
    url_detail = url_ + "/followOrder/getListDetails.Action?followOrderId=" + id;
    followOrderId = id;
    varietyName = varietyName;
    if (manager == "1") {

        showByTableId(tableId, method, url_detail, unique_Id, sortOrder, columns);

    } else {
        var clientDetailColumns = [
            {
                field: 'id',
                title: '跟单编号'
            }
            , {
                field: 'clientName',
                title: '客户名'
            }, {
                field: 'varietyName',
                title: '品种',
                formatter: function () {
                    return varietyName;
                }
            }, {
                field: 'tradeDirection',
                title: '方向',
                formatter: function (value, row, index) {
                    if (value == "1") {
                        return "空";
                    } else {
                        return "多";
                    }
                }
            }, {
                field: 'handNumber',
                title: '手数',
                formatter: function (value, row, index) {
                    if(row.originalHandNumber==value){
                        return value;
                    }else {
                        return value+"("+row.originalHandNumber+")";
                    }
                }
            }, {
                field: 'openPrice',
                title: '开仓价'
            }, {
                field: 'openTime',
                title: '开仓时间',
                sortable: true

            }, {
                field: 'closePrice',
                title: '平仓价'
            }, {
                field: 'closeTime',
                title: '平仓时间',
                sortable: true
            }, {
                field: 'poundage',
                title: '手续费'
            }, {
                field: 'profitLoss',
                title: '盈亏'
            }, {
                field: 'clientProfit',
                title: '客户盈亏'
            },
            {
                field: 'closeTime',
                title: '操作',

                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    var detailId = row.id;
                    if (value != null) {
                        return "-";
                    } else {
                        return "<a href='javascript:;' class='btn btn-xs green' onclick='manually_close_position(" + detailId + ")' >  <span style='color: #4cae4c'>手动平仓</span></a>"
                    }
                }
            }
        ];

        showByTableId(tableId, method, url_detail, unique_Id, sortOrder, clientDetailColumns);
        $("#detailExport").click(function () {
            tableExportFile(tableId, name + "的跟单明细");
        })
    }


}

/*
* 展示客户数据列表
* */

function clientTableShow(id, manager, name) {

    followOrderId = id;
    var url_client = url_ + "/followOrder/getListClientNetPosition.Action?followOrderId=" + id + "&status=" + clientStatus + "&clientName=" + clientName;
    var clientNetPositionColumns = [
        {
            title: '序号',

            formatter: function (value, row, index) {
                //获取每页显示的数量
                var pageSize = $('#clientTable').bootstrapTable('getOptions').pageSize;
                //获取当前是第几页
                var pageNumber = $('#clientTable').bootstrapTable('getOptions').pageNumber;
                //返回序号，注意index是从0开始的，所以要加上1
                return pageSize * (pageNumber - 1) + index + 1;
            }
        }, {
            field: 'netPositionSum',
            title: '净头寸'
        }, {
            field: 'userName',
            title: '客户名'
        }, {
            field: 'varietyName',
            title: '品种'
        }, {
            field: 'openCloseType',
            title: '类型',
            formatter: function (value, row, index) {
                if (value == "平仓") {
                    return "平仓";
                } else {
                    return "开仓";
                }
            }

        }, {
            field: 'tradeDirection',
            title: '方向',
            formatter: function (value, row, index) {
                if (value == "1") {
                    return "空";
                } else {
                    return "多";
                }
            }
        }, {
            field: 'handNumber',
            title: '手数'
        }, {
            field: 'marketPrice',
            title: '点位'
        }, {
            field: 'tradeTime',
            title: '下单时间',
            sortable: true

        }, {
            field: 'poundage',
            title: '手续费'
        }, {
            field: 'profit',
            title: '客户盈亏'
        }, {
            field: 'followOrderClientStatus',
            title: '是否跟单',
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                if (value == "1") {
                    return "<span style='color: #26a69a'>已跟单</span>";
                } else if (value == "0") {
                    return "<span style='color: #c49f47'>跟单失败</span>";
                }
            }
        }
    ];
    var clientColumns = [
        {
            field: 'detailId',
            title: '跟单编号'

        }, {
            field: 'clientName',
            title: '客户名'
        }, {
            field: 'orderUser.productCode',
            title: '品种'
        }, {
            field: 'orderUser.longShort',
            title: '方向',
            formatter: function (value, row, index) {
                if (value == "1") {
                    return "空";
                } else {
                    return "多";
                }
            }
        }, {
            field: 'orderUser.handNumber',
            title: '手数'
        }, {
            field: 'orderUser.openPrice',
            title: '开仓价'
        }, {
            field: 'orderUser.openTime',
            title: '开仓时间',
            sortable: true

        }, {
            field: 'orderUser.closePrice',
            title: '平仓价'
        }, {
            field: 'orderUser.closeTime',
            title: '平仓时间',
            sortable: true

        }, {
            field: 'orderUser.poundage',
            title: '手续费',
            formatter: function (value, row, index) {
                return 1;
            }
        }, {
            field: 'orderUser.profit',
            title: '客户盈亏'
        }, {
            field: 'status',
            title: '是否跟单',
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                if (value == "1") {
                    return "<span style='color: #26a69a'>已跟单</span>";
                } else if (value == "0") {
                    return "<span style='color: #c49f47'>未跟单</span>";
                }
            }
        }
    ];
    if (manager == "1") {
        showByTableId(tableClientId, method, url_client, unique_Id, sortOrder, clientNetPositionColumns);
    } else {
        showByTableId(tableClientId, method, url_client, unique_Id, sortOrder, clientColumns);

    }
    //下拉列表客户名字获取
    findByClientName(id);
    $("#clientExport").click(function () {
        tableExportFile(tableClientId, name + '的客户做单明细');
    })
}

/*
*
* 展示跟每单中跟单数据
* */
function orderClientTableShow(followOrderId, name) {
    var orderClientTable = $("#orderClientTable");
    var url_order_client = url_ + "/followOrder/getListClientFollowOrderTrade.Action?followOrderId=" + followOrderId;
    var orderClientColumns = [
        {
            field: 'clientName',
            title: '用户'

        }, {
            field: 'varietyCode',
            title: '品种'
        }, {
            field: 'followDirection',
            title: '跟单方向',
            formatter: function (value, row, index) {
                if (value == "1") {
                    return "正向";
                } else {
                    return "反向";
                }
            }
        }, {
            field: 'handNumberType',
            title: '手数类型',
            formatter: function (value, row, index) {
                if (value == "1") {
                    return "按比例";
                } else {
                    return "固定手数";
                }
            }
        }, {
            field: 'followHandNumber',
            title: '跟单手数'
        }, {
            field: 'winRate',
            title: '跟单成功率'
        }, {
            field: 'clientProfit',
            title: '客户盈亏'
        }, {
            field: 'offsetGainAndLoss',
            title: '平仓盈亏'

        }, {
            field: 'positionGainAndLoss',
            title: '持仓盈亏'
        }, {
            field: 'poundageTotal',
            title: '手续费'
        }, {
            field: 'status',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                return "<a href='javascript:;'><span style='color: #26a69a'>资料</span></a>"

            }
        }
    ];

    showByTableId(orderClientTable, method, url_order_client, unique_Id, sortOrder, orderClientColumns);

    $("#orderClientExport").click(function () {

        tableExportFile(orderClientTable, name + "的跟单数据");
    })
}

/*
* 展示跟单参数
* */
function orderParameterShow(followOrder) {
    $("#accountName").html(followOrder.account.platform.name + "-" + followOrder.account.username);
    $("#varietyName").html(followOrder.variety.varietyName);
    var num = 0;//1:不设置/净头寸/正向，0代表设置/客户/反向

    followOrder.maxProfit == num ? $("#maxProfitNumber").html(followOrder.maxProfitNumber + "点") : $("#maxProfitNumber").html("不设");
    followOrder.maxLoss == num ? $("#maxLossNumber").html(followOrder.maxLossNumber + "点") : $("#maxLossNumber").html("不设");
    followOrder.accountLoss == num ? $("#accountLossNumber").html(followOrder.accountLossNumber + "点") : $("#accountLossNumber").html("不设");


    if (followOrder.orderPoint == num) {

        followOrder.clientPoint == num ? $("#orderPointNumber").html("-" + followOrder.clientPointNumber + "点") :
            $("#orderPointNumber").html("+" + followOrder.clientPointNumber + "点");
    } else {
        $("#orderPointNumber").html("市价");
    }
    if (followOrder.followManner == num) {
        $(".chooseNetPosition").hide();
        $(".clientAllTotal").show();
    } else {
        $(".clientAllTotal").hide();

        $(".chooseNetPosition").show();
        followOrder.netPositionDirection == num ? $("#netPositionDirection").html("反向") : $("#netPositionDirection").html("正向");
        $("#netPositionFollowNumber").html("净头寸每变化" + followOrder.netPositionChange + "手，跟单" + followOrder.netPositionFollowNumber + "手");
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

/*
* 表格导出
* */
function tableExportFile(tableId, fileName) {
    $(tableId).tableExport({
        type: 'excel',
        escape: 'false',
        fileName: fileName
    });
}

/*
*
* 客户查询条件
* */
function findByClient() {
    var clientTable = $("#clientTable");
    var status = $("#clientFollowOrder option:selected").val();
    var clientName = $("#clientName option:selected").val();
    var openOrCloseStatus = $("#clientOpenOrClose option:selected").val();
    $.ajax({
        url: url_ + "/followOrder/getListClientNetPosition.Action",
        type: 'post', //GET
        async: true,    //或false,是否异步
        data: {
            followOrderId: followOrderId,
            status: status,
            clientName: clientName,
            openOrCloseStatus: openOrCloseStatus
        },
        timeout: 5000,    //超时时间
        dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend: function (xhr) {

        },
        success: function (data) {
            // $(clientTable).bootstrapTable('destroy');
            $(clientTable).bootstrapTable('load', data);

        },
        error: function (xhr, textStatus) {

        },
        complete: function () {

        }
    })
}

/*
* 跟单明细查询条件
* */
function findByDetail() {

    var status = $("#detailOpenOrClose option:selected").val();
    var startTime = $("#test1").val();
    var endTime = $("#test2").val();

    $.ajax({
        url: url_ + "/followOrder/getListDetails.Action",
        type: 'post', //GET
        async: true,    //或false,是否异步
        data: {
            followOrderId: followOrderId,
            status: status,
            startTime: startTime,
            endTime: endTime

        },
        timeout: 5000,    //超时时间
        dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend: function (xhr) {

        },
        success: function (data) {
            $(tableId).bootstrapTable('load', data);

        },
        error: function (xhr, textStatus) {

        },
        complete: function () {

        }
    })
}

/*
* 跟单明细中跟单数据查询
* */
function findByOrderUserClient() {
    var tableId = $("#orderClientTable");
    var startTime = $("#tradeTimeStart").val();
    var endTime = $("#tradeTimeEnd").val();

    $.ajax({
        url: url_ + "/followOrder/getListClientFollowOrderTrade.Action",
        type: 'post', //GET
        async: true,    //或false,是否异步
        data: {
            followOrderId: followOrderId,
            startTime: startTime,
            endTime: endTime
        },
        timeout: 5000,    //超时时间
        dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend: function (xhr) {

        },
        success: function (data) {
            $(tableId).bootstrapTable('load', data);

        },
        error: function (xhr, textStatus) {

        },
        complete: function () {

        }
    })
}

/*
* 品种筛选
* */
function findByClientName(followOrderId) {
    $.ajax({
        url: url_ + "/followOrderClient/findListUserName.Action",
        type: 'post', //GET
        async: true,    //或false,是否异步
        data: {
            followOrderId:followOrderId
        },
        timeout: 5000,    //超时时间
        dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend: function (xhr) {

        },
        success: function (data) {
            var content = "";
            $.each(data, function (index, ele) {
                content += "<option value=" + ele + ">" + ele + "</option>"
            });
            $("#clientName").append(content);
        },
        error: function (xhr, textStatus) {

        },
        complete: function () {

        }
    })
}


