    var contractInfoTable = $("#contractInfoTable");
    var varietyTable = $("#varietyTable");
    var method = "get";

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

                var obj=JSON.stringify(row);

                if (value == "1") {
                    return "<a onclick='follow_order_stop(this," + followOrderId + ")' href='javascript:;' title='暂停'> <i class='layui-icon'>&#xe651;</i> </a>" +
                        "<a onclick='follow_order_stop(this,"+ + followOrderId + ")' href='javascript:;' title='停止'> <i class='layui-icon'>&#x1006;</i> </a>" +
                        "<a title=\"查看\" onclick='openDeatil("+obj+")' href=\"javascript:;\"><i class=\"layui-icon\"> &#xe615;</i></a>" +
                        "<a title=\"编辑\" onclick='updateOrderShow("+obj+")' href=\"javascript:;\">\n" +
                        "<i class=\"layui-icon\">&#xe642;</i>" +
                        "</a>";
                } else {
                    return "<a onclick='follow_order_stop(this," + followOrderId + ")' href='javascript:;' title='启用'> <i class='layui-icon'>&#xe652;</i> </a>" +
                        "<a onclick='follow_order_stop(this," + followOrderId + ")' href='javascript:;' title='停止'> <i class='layui-icon'>&#x1006;</i> </a>" +
                        "<a title=\"查看\" onclick='openDeatil("+obj+")'  href=\"javascript:;\"><i class=\"layui-icon\"> &#xe615;</i></a>" +
                        "<a title=\"编辑\" onclick='updateOrderShow("+obj+")' href=\"javascript:;\">\n" +
                        "<i class=\"layui-icon\">&#xe642;</i>" +
                        "</a>";
                }
            }
        }
    ];



    $(function () {
        //发送请求获取跟单页面的统计数据表格加载
        showByTableId(tableOrderId, method, url_orderPage, unique_Id, sortOrder, columns);
        //品种信息点击事件
        $("#varietyInfo").click(function () {
            $(varietyTable).bootstrapTable('destroy');
            var url_history = url_+"/followOrder/getListFollowOrder.Action?varietyId="+varietyNum+"&accountId="+accountNum+"&status=0";
            showByTableId(tableHistoryOrderId, method, url_history, unique_Id, sortOrder, columns);
        })
    });
    
    //根据窗口调整表格高度
    $(window).resize(function () {
        $('#detailTable').bootstrapTable('resetView', {
            height: tableHeight()
        })
    })


/*
* 条件查询
* */

function orderByParameter(num) {
    var varietyNum= $("#variety_id option:selected").val();
    var accountNum= $("#account_id option:selected").val();
    var startTime;
    var endTime;
    if(num){
        varietyNum= $("#variety_id option:selected").val();
        accountNum= $("#account_id option:selected").val();
        (($('#test1').val()) == 'undefined')?(startTime=''):(startTime=($("#test1").val()));
        (($('#test2').val()) == 'undefined')?(endTime=''):(endTime=($("#test2").val()));
    }else{
        varietyNum= $("#history_variety_id option:selected").val();
        accountNum= $("#history_account_id option:selected").val();
        (($('#history-start-time').val()) == 'undefined')?(startTime=''):(startTime=($("#history-start-time").val()));
        (($('#history-end-time').val()) == 'undefined')?(endTime=''):(endTime=($("#history-end-time").val()));
    }

    var url = url_+"/followOrder/getListFollowOrder.Action?varietyId="+varietyNum+"&accountId="+accountNum+"&startTime="+startTime+"&endTime="+endTime;
    $(tableOrderId).bootstrapTable('destroy');
    showByTableId(tableOrderId, method, url, unique_Id, sortOrder, columns);
}
    
/*
* 品种筛选
* */
function findByVariety() {
    $.ajax({
        url:url_+"/variety/getListVariety.Action",
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
            var obj=eval('('+data+')');
            var content = "";
            $.each(obj,function (index,ele) {
                content += "<option value="+ele.id+">"+ele.varietyCode+"</option>"
            });
            $("#variety_id").append(content);
            $("#history_variety_id").append(content);
        },
        error:function(xhr,textStatus){

        },
        complete:function(){

        }
    })
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
    function follow_details(obj,title, url, w, h) {


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
            var orderNum= obj.handNumberTotal;
            var offsetGainAndLoss = obj.offsetGainAndLoss;
            var poundageTotal = obj.poundageTotal;
            var manager= obj.followOrder.followManner;
            var name = obj.followOrder.followOrderName;
            //找到子页面
            var iframeWin = window['layui-layer-iframe' + index]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
            //调用子页面的方法
            iframeWin.detailShow(name,manager,followOrderId,successTotal+"/"+orderNum,offsetGainAndLoss,poundageTotal);
            iframeWin.clientTableShow(followOrderId,manager,name);
            iframeWin.orderParameterShow(obj.followOrder);
            iframeWin.orderClientTableShow(followOrderId,name);

        },
        btn: ['关闭']
    });
}
    function tableHeight() {
        //可以根据自己页面情况进行调整
        return $(window).height() - 280;
    }
