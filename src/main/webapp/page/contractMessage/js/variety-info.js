$(function () {
    var contractInfoTable = $("#contractInfoTable");
    var varietyTable = $("#varietyTable");
    var method = "post";
    var url_contractInfoPage = url_+"/contractInfo/getContractInfoList.Action";
    var unique_Id = "id";
    var sortOrder = "asc";
	var contractInfoColumns = [{
    	field: 'id',
        title: '序号'
    }, {
        field: 'variety.varietyCode',
        title: '品种代码'
    }, {
        field: 'contractName',
        title: '合约名称'
    }, {
        field: 'contractCode',
        title: '合约代码',
    }, {
        field: '',
        title: '操作',
        align: 'center',
        valign: 'middle',
        formatter: function (value, row, index) {
        	var contractInfoId = row.id;
            var obj=JSON.stringify(row);
            return "<a onclick='alert(11)' href='javascript:;'>修改</a>   " +
                       "<a onclick='alert(11)' href='javascript:;'>删除</a>";
        }
	}];
	
	var varietyColumns = [{
    	field: 'id',
        title: '序号'
    }, {
        field: 'varietyCode',
        title: '品种代码'
    }, {
        field: 'varietyName',
        title: '品种名称'
    }, {
        field: 'tradePlaceName',
        title: '交易所名称',
	}];

    //发送请求获取跟单页面的统计数据表格加载
    showByTableId(contractInfoTable, method, url_contractInfoPage, unique_Id, sortOrder, contractInfoColumns);
    //合约信息点击事件
    $("#contractInfo").click(function () {
        $(contractInfoTable).bootstrapTable('destroy');
        var url_contractInfoPage = url_+"/contractInfo/getContractInfoList.Action";
        showByTableId(contractInfoTable, method, url_contractInfoPage, unique_Id, sortOrder, contractInfoColumns);
    })
    //品种信息点击事件
	$("#varietyInfo").click(function () {
        $(varietyTable).bootstrapTable('destroy');
        var url_variety = url_+"/variety/getListVariety.Action";
    	showByTableId(varietyTable, method, url_variety, unique_Id, sortOrder, varietyColumns);
	})
});
    

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


/*弹出层添加合约信息*/
/*
    参数解释：
    title   标题
    url     请求的url
    id      需要操作的数据id
    w       弹出层宽度（缺省调默认值）
    h       弹出层高度（缺省调默认值）
*/
function addContractInfo() {
    var url = "contractInfo-add.html";
    var title = "合约管理";
    var w = 1500;
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
            //iframeWin.setParameter(num, account);
        }
    });
}
    
//看情况使用该方法    
function tableHeight() {
    //可以根据自己页面情况进行调整
    return $(window).height() - 280;
}
