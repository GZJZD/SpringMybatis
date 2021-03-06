var varietyNum = $("#variety_id option:selected").val();
var accountNum = $("#variety_id option:selected").val();
var tableOrderId = $("#followOrderTable");
var tableHistoryOrderId = $("#historyFollowOrderTable");
var method = "get";

var url_orderPage = url_ + "/followOrder/getListFollowOrder.Action?varietyId=" + varietyNum + "&accountId=" + accountNum;
var url_history = url_ + "/followOrder/getListFollowOrder.Action?varietyId=" + varietyNum + "&accountId=" + accountNum + "&status=0";
var unique_Id = "detailId";
var sortOrder = "desc";
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

        return value.platform.name + "-" + value.account;
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
    ortable: true,
    formatter: function (value, row, index) {
        return row.successTotal + "/" + row.allTotal;
    }

}, {
    field: 'positionGainAndLoss',
    title: '持仓盈亏',
    sortable: true
}, {
    field: 'offsetGainAndLoss',
    title: '平仓盈亏',
    sortable: true
}, {
    field: 'poundageTotal',
    title: '手续费'
}, {
    field: 'profitAndLossRate',
    title: '盈亏效率',
    sortable: true
}, {
    field: 'clientProfit',
    title: '客户盈亏',
    sortable: true
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

            var obj = JSON.stringify(row);
            var varietyName = row.followOrder.variety.varietyName;

            if (value == "1") {
                return "<a onclick='follow_order_stop(this," + followOrderId + ")' href='javascript:;' title='暂停'> <i class='layui-icon'>&#xe651;</i> </a>" +
                    "<a onclick='follow_order_stop(this," + +followOrderId + ")' href='javascript:;' title='停止'> <i class='layui-icon'>&#x1006;</i> </a>" +
                    "<a title=\"查看\" onclick='openDeatil(" + obj + ")' href=\"javascript:;\"><i class=\"layui-icon\"> &#xe615;</i></a>" +
                    "<a title=\"编辑\" onclick='updateOrderShow(" + obj + ")' href=\"javascript:;\">\n" +
                    "<i class=\"layui-icon\">&#xe642;</i>" +
                    "</a>";
            } else {
                return "<a onclick='follow_order_stop(this," + followOrderId + ")' href='javascript:;' title='启用'> <i class='layui-icon'>&#xe652;</i> </a>" +
                    "<a onclick='follow_order_stop(this," + followOrderId + ")' href='javascript:;' title='停止'> <i class='layui-icon'>&#x1006;</i> </a>" +
                    "<a title=\"查看\" onclick='openDeatil(" + obj + ")'  href=\"javascript:;\"><i class=\"layui-icon\"> &#xe615;</i></a>" +
                    "<a title=\"编辑\" onclick='updateOrderShow(" + obj + ")' href=\"javascript:;\">\n" +
                    "<i class=\"layui-icon\">&#xe642;</i>" +
                    "</a>";
            }
        }
    }
];


$(function () {

    /*
    * 品种查找
    * */
    findByVariety();

    /*
    * 账号查询
    * */
    findByAccount();
    /*
     *表格加载
     * */
    orderTableShow(url_orderPage,tableOrderId,true);
    /*
    * 跟单历史点击事件
    * */
    $("#historyShow").click(function () {

        $(tableHistoryOrderId).bootstrapTable('destroy');
        orderTableShow(url_history,tableHistoryOrderId,false);
        $(tableHistoryOrderId).bootstrapTable('refresh', {
            silent: true//静默跟新
        });
    })



});
function orderTableShow(url,tableId,historyOrNow){
    $.ajax({
        url: url,
        type: 'post', //GET
        async: true,    //或false,是否异步
        data: {},
        timeout: 5000,    //超时时间
        dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend: function (xhr) {

        },
        success: function (data) {

            if(data.followOrderVoList == null ){
                $(tableId).bootstrapTable({load:data.followOrderVoList,
                    columns:columns,
                    formatLoadingMessage: function(){
                        return "";
                    }
                });
            }else {

                showByOrderId(tableId, method, unique_Id, sortOrder, columns,data.followOrderVoList);
            }
            if(historyOrNow){

                $("#history_close_position").html(data.historyHandNumber + "/" + data.historyProfit);
                if (data.holdPositionHandNumber == null) {
                    $("#hold_position").html(0 + "/" + 0);
                } else {
                    $("#hold_position").html(data.holdPositionHandNumber + "/" + data.holdPositionProfit);

                }
                $("#profit_rate").html(data.profitAndLossRate);
                $("#win_rate").html(data.winRate + "%");
            }
        },
        error: function (xhr, textStatus) {

        },
        complete: function () {

        }
    })

}
//根据窗口调整表格高度
$(window).resize(function () {
    $('#detailTable').bootstrapTable('resetView', {
        height: tableHeight()
    })
})


function showByOrderId(table_Id, method,  unique_Id, sortOrder, columns,data) {
    $(table_Id).bootstrapTable({
        method: method,
        contentType: "application/x-www-form-urlencoded", //必须要有！！！！
        async:true,//异步加载
        striped: false, //是否显示行间隔色
        data: data,
        clickToSelect: true,
        uniqueId: unique_Id, //每一行的唯一标识，一般为主键列
        pagination: true, //是否显示分页（*）
        sortable: true, //是否启用排序
        sortOrder: sortOrder, //排序方式
        clickToSelect: true, // 是否启用点击选中行
        pageSize: 10, //单页记录数
        pageList: [50, 100], //分页步进值
        //			search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: true,
        showColumns: false, //是否显示所有的列
        showRefresh: false, //是否显示刷新按钮
        showToggle: false, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        silent:true,
        //			height:tableHeight(),//高度调整
        queryParams: function(params) {
            return {
                offset: params.offset, //页码
                limit: params.limit, //页面大小
                search: params.search, //搜索
                order: params.order, //排序
                ordername: params.sort, //排序
            };
        },
        buttonsAlign: 'right', //按钮对齐方式
        columns: columns,
        onLoadSuccess: function(data) {
            // console.log(data);

        },
        onLoadError: function(status) {
            // console.log(status);
            //                  showTips("数据加载失败！");
        },
        onDblClickRow: function(row, $element) {
            // console.log(row);
            var id = row.id;
            EditViewById(id, 'view');
            // console.log(id);
        },
        formatLoadingMessage: function(){
            return "";
        }

    });

}





/*用户-停用*/
function follow_order_stop(obj, id) {

    if ($(obj).attr('title') == '启用') {
        layer.confirm('确认要启用吗？确定后系统将会继续跟进客户的做单数据', function (index) {
            update_status(id, obj, "暂停", "启用", 1, '&#xe651;');
        })

    } else if ($(obj).attr('title') == '暂停') {
        layer.confirm('确认要暂停吗？确定后系统将不会继续跟进客户的做单数据', function (index) {
            update_status(id, obj, "启用", "暂停", 2, '&#xe652;');

        })
    } else {
        layer.confirm('确认要停止吗？确定后系统将不会继续跟进客户的做单数据，并同时平仓所有未平的跟单', function (index) {
            update_status(id, obj, "停止", "停止", 0, '&#x1006;');
        })
    }

}

/*
* 条件查询
* */

function orderByParameter(num) {
    var varietyNum = $("#variety_id option:selected").val();
    var accountNum = $("#account_id option:selected").val();
    var startTime;
    var endTime;
    var status;
    if (num) {
        varietyNum = $("#variety_id option:selected").val();
        accountNum = $("#account_id option:selected").val();
        (($('#test1').val()) == 'undefined') ? (startTime = '') : (startTime = ($("#test1").val()));
        (($('#test2').val()) == 'undefined') ? (endTime = '') : (endTime = ($("#test2").val()));
    } else {
        varietyNum = $("#history_variety_id option:selected").val();
        accountNum = $("#history_account_id option:selected").val();
        (($('#history-start-time').val()) == 'undefined') ? (startTime = '') : (startTime = ($("#history-start-time").val()));
        (($('#history-end-time').val()) == 'undefined') ? (endTime = '') : (endTime = ($("#history-end-time").val()));
        status = 0;
    }

    // ?varietyId=" + varietyNum + "&accountId=" + accountNum + "&startTime=" + startTime + "&endTime=" + endTime
    var url = url_ + "/followOrder/getListFollowOrder.Action";

    $.ajax({
        url: url,
        type: 'post', //GET
        async: true,    //或false,是否异步
        data: {
            varietyId:varietyNum,
            accountId:accountNum,
            startTime:startTime,
            endTime:endTime,
            status:status
        },
        timeout: 5000,    //超时时间
        dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend: function (xhr) {

        },
        success: function (data) {

            var tableName;
            if(num){
                tableName = tableOrderId
            }else{
                tableName= tableHistoryOrderId;
            }
            $(tableName).bootstrapTable('destroy');
            if(data.followOrderVoList == null ){
                $(tableName).bootstrapTable({load:data.followOrderVoList,
                    columns:columns,
                    formatLoadingMessage: function(){
                        return "";
                    }
                });
            }else {

                showByOrderId(tableName, method, unique_Id, sortOrder, columns,data.followOrderVoList);
            }
            if(num) {
                $("#history_close_position").html(data.historyHandNumber + "/" + data.historyProfit);
                if (data.holdPositionHandNumber == null) {
                    $("#hold_position").html(0 + "/" + 0);
                } else {
                    $("#hold_position").html(data.holdPositionHandNumber + "/" + data.holdPositionProfit);

                }
                $("#profit_rate").html(data.profitAndLossRate);
                $("#win_rate").html(data.winRate + "%");
            }
        },
        error: function (xhr, textStatus) {

        },
        complete: function () {

        }
    })


}

/*
* 打开明细
* */
function openDeatil(obj) {
    follow_details(obj, '跟单明细', url_ + "/page/documentary/documentary-details.html", 1500, 800);
}

/*
* 品种筛选
* */
function findByVariety() {
    $.ajax({
        url: url_ + "/variety/getListVariety.Action",
        type: 'GET', //GET
        async: true,    //或false,是否异步
        data: {},
        timeout: 5000,    //超时时间
        dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend: function (xhr) {

        },
        success: function (data) {
            //

            var content = "";
            $.each(data, function (index, ele) {
                content += "<option value=" + ele.id + ">" + ele.varietyCode + "</option>"
            });
            $("#variety_id").append(content);
            $("#history_variety_id").append(content);
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
function findByAccount() {
    $.ajax({
        url: url_ + "/account/getListAccount.Action",
        type: 'GET', //GET
        async: true,    //或false,是否异步
        data: {},
        timeout: 5000,    //超时时间
        dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend: function (xhr) {

        },
        success: function (data) {
            //

            var content = "";
            $.each(data, function (index, ele) {
                content += "<option value=" + ele.account.id + ">" + ele.account.platform.name + "-"+ele.account.account+"</option>"
            });
            $("#history_account_id").append(content);
            $("#account_id").append(content);
        },
        error: function (xhr, textStatus) {

        },
        complete: function () {

        }
    })
}

/*
* 修改跟单状态
* */
function update_status(id, obj, newTitle, oldTitle, status, i) {
    $.ajax({
        url: url_ + "/followOrder/updateFollowOrderStatus.Action?id=" + id + "&status=" + status,
        type: 'POST', //GET
        async: true,    //或false,是否异步
        data: {},
        timeout: 5000,    //超时时间
        dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend: function (xhr) {

        },
        success: function (data) {
            if (data.success) {
                alert(111)
                //发异步把用户状态进行更改,'&#xe651;'
                $(obj).attr('title', newTitle)
                $(obj).find('i').html(i);
                $(obj).parents("tr").find(".td-status").find('span').addClass('layui-btn-disabled').html('已' + oldTitle);
                layer.msg("状态修改成功", {icon: 6, time: 1000}, function () {
                    // $(tableHistoryOrderId).bootstrapTable('refresh', {
                    //     silent: true//静默跟新
                    // });
                    $(tableHistoryOrderId).bootstrapTable('destroy');
                    orderTableShow(url_history,tableHistoryOrderId);
                    $(tableOrderId).bootstrapTable('destroy');
                    orderTableShow(url_orderPage,tableOrderId);
                });
            } else {
                //layer.msg(data.msg, {icon: 6, time: 1000});
                layer.msg("系统繁忙，请稍后再试", {icon: 6, time: 1000});
            }
        },
        error: function (xhr, textStatus) {

        },
        complete: function () {

        }
    })
}

//修改跟单
function updateOrderShow(obj) {
    var title = "跟单配置";
    var url = url_ + "/page/documentary/documentary-edit.html";
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
        shadeClose: false,
        shade: 0.4,
        title: title,
        content: url,
        success: function (layero, index) {
            //找到子页面
            var iframeWin = window['layui-layer-iframe' + index]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
            //调用子页面的方法
            iframeWin.setFollowOrderParameter(obj.followOrder);

        }
    });


}

/*弹出层*/

/*
    参数解释：
    title   标题
    url     请求的url
    id      需要操作的数据id
    w       弹出层宽度（缺省调默认值）
    h       弹出层高度（缺省调默认值）
*/
function follow_details(obj, title, url, w, h) {


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
        shadeClose: false,
        shade: 0.4,
        title: title,
        content: url,
        success: function (layero, index) {
            var followOrderId = obj.followOrder.id;
            var successTotal = obj.successTotal;
            var orderNum = obj.handNumberTotal;
            var offsetGainAndLoss = obj.offsetGainAndLoss;
            var poundageTotal = obj.poundageTotal;
            var manager = obj.followOrder.followManner;
            var name = obj.followOrder.followOrderName;
            var  varietyName = obj.followOrder.variety.varietyName;
            //找到子页面
            var iframeWin = window['layui-layer-iframe' + index]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
            //调用子页面的方法
            iframeWin.detailShow(varietyName,name, manager, followOrderId, successTotal + "/" + orderNum, offsetGainAndLoss,obj.positionGainAndLoss, poundageTotal);
            iframeWin.clientTableShow(followOrderId, manager, name,varietyName);
            iframeWin.orderParameterShow(obj.followOrder);
            iframeWin.orderClientTableShow(followOrderId, name,varietyName);

        },
        btn: ['关闭']
    });
}

function tableHeight() {
    //可以根据自己页面情况进行调整
    return $(window).height() - 280;
}
