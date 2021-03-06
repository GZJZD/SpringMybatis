
var followOrderId;//跟单ID
function hideRadio(id){
    $(".bili-class").hide();
    $("#maxProfit-id").hide();
    $("#accountLoss-id").hide();
    $("#maxLoss-id").hide();
    setParameter(id);
}

function showTableBase(tableId,data_){
    var columns = [
        {
            field: 'userName',
            title: '客户姓名'
        },{
            field: 'everyHandNumber',
            title: '每单手数'
        },

        {
            field: 'offsetGainAndLoss',
            title: '平仓盈亏'
        },

        {
            field: 'profitAndLossEfficiency',
            title: '盈亏效率'
        },
        {
            field:'followDirection',
            title:'跟单方向',
            align: 'center',
            valign: 'middle',
            formatter: returnFollowDirection
        },
        {
            field:'handNumberType',
            title:'手数类型',
            align: 'center',
            valign: 'middle',
            formatter: returnHandNumberType
        }
        ,{
            field:'followHandNumber',
            title:'手数',
            align: 'center',
            valign: 'middle'
        }
    ];

    $(tableId).bootstrapTable({
        data :data_,
        columns:columns
    });

}
function returnHandNumberType(value, row, index){
    return (value==1? '按比例':'固定手数' );
}
function returnFollowDirection(value, row, index){
    return (value==1? '正向':'反向' );
}
/*
* 设置跟单参数
* */
function setFollowOrderParameter(followOrder) {
    followOrderId = followOrder.id;
    $("#followOrderName").val(followOrder.followOrderName);
    hideRadio(followOrder.id);
    setRadio('orderPoint',followOrder.orderPoint,followOrder.clientPointNumber,followOrder.clientPoint);
    setRadio('maxProfit',followOrder.maxProfit,followOrder.maxProfitNumber);
    setRadio('maxLoss',followOrder.maxLoss,followOrder.maxLossNumber);
    setRadio('accountLoss',followOrder.accountLoss,followOrder.accountLossNumber);
    setRadio('followManner',followOrder.followManner);

    findAccount(followOrder.account.id);
    if(followOrder.followManner){
        setRadio('netPositionDirection',followOrder.netPositionDirection);
        $("#netPositionChange-id").val(followOrder.netPositionChange);
        $("#netPositionFollowNumber-id").val(followOrder.netPositionFollowNumber);
    }

}

function findAccount(accountId) {
    $.ajax({
        url:url_+"/account/getListAccount.Action",
        type:'POST', //GET
        async:true,    //或false,是否异步
        data:{
        },
        // timeout:5000,    //超时时间
        dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend:function(xhr){

        },
        success:function(data,textStatus,jqXHR){
            var content = "";
            $.each(data,function (index,ele) {
                if(accountId == ele.account.id){
                    content += "<option value="+ele.account.id+" selected>"+ ele.account.platform.name + "-"+ele.account.account+"</option>"
                }else {

                    content += "<option value="+ele.account.id+">"+ ele.account.platform.name + "-"+ele.account.account+"</option>"
                }
            });
            $("#account-id").append(content);
        },
        error:function(xhr,textStatus){

        },
        complete:function(){

        }
    })

}
function setRadio(radioName,parameter,number,value) {

    $("input[type=radio][name='"+radioName+"']").each(function () {
            if($(this).val() == parameter){
                $(this).attr("checked", "checked");
                    setStatus(radioName,number,value)
            }
    })

}
//table数据赋值
function  setParameter(id) {
    $("#mytable").bootstrapTable('destroy');
    var tableId = $("#mytable");
    var url_client=url_+"/followOrderClient/getListFollowOrderClientParamVo.Action?followOrderId="+id;
    var columns;
    columns = [
        {
            field: 'followOrderClient.id',
            title: 'ID'

        },
        {
            field: 'userName',
            title: '客户姓名'
        },{
            field: 'everyHandNumber',
            title: '每单手数'
        },

        {
            field: 'offsetGainAndLoss',
            title: '平仓盈亏'
        },

        {
            field: 'profitAndLossEfficiency',
            title: '盈亏效率'
        },
        {
            field: 'followOrderClient.followDirection',
            title: '跟单类型',
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                return setType(value);
            }
        },
        {
            field: 'followOrderClient.handNumberType',
            title: '手数类型',
            align: 'center',
            valign: 'middle',
            formatter:function (value, row, index) {
                return setHandType(value, row, index);
            }
        }
        , {
            field: 'followOrderClient.followHandNumber',
            title: '手数',
            align: 'center',
            valign: 'middle',
            formatter: function (value,row,index) {
               return handNumber(value);
            }
        } ,
        {
            field: 'followOrderClient.platformCode',
            title: '平台',
            align: 'center',
            valign: 'middle'
        }
        ,
        {
            field: 'userCode',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: actionFormatter
        }
    ];

    $(tableId).bootstrapTable({
       url:url_client,
        columns:columns
    });

}

function goOn(status){
    if(status == 1){

        layui.use('element', function(){
            var element = layui.element;
            element.progress('demo', '66%');
        });
        $(".two-div").css('background-color','#44b7af');
        $(".one-div").css('background-color','#f0eff0');
        $('.first-div').css('display','none');
        ($('input[name="followManner"]:checked').val() == 1 ? $('.jtc').show(): $('.table-div').show());

    }
    if(status == 2){
        layui.use('element', function(){
            var element = layui.element;
            element.progress('demo', '100%');
        });
        $(".three-div").css('background-color','#44b7af');
        $(".one-div").css('background-color','#f0eff0');
        $(".two-div").css('background-color','#f0eff0');
        $(".table-div").hide();
        $(".title-fiel").hide();
        $('.jtc').hide();
        var leght = $("#datails-table tbody").find('tr').length;
        //设置展示数据
        var fayuan_data = new Array();

        $("#mytable tbody").find('tr').each(function(){
            var tdArr = $(this).children();
            var userName = tdArr.eq(1).html();//客户姓名
            var everyHandNumber = tdArr.eq(2).html();//每单手数
            var offsetGainAndLoss = tdArr.eq(3).html();//平仓盈亏
            var profitAndLossEfficiency  =tdArr.eq(4).html();//盈亏率
            var followDirection = tdArr.eq(5).find(".followDirection option:selected").val();//跟单方向
            var handNumberType= tdArr.eq(6).find(".handNumberType option:selected").val();//手数类型
            var followHandNumber = tdArr.eq(7).find(".followHandNumber").val();//手数
            var json_ = {
                "userName":userName,
                "everyHandNumber":everyHandNumber,
                "offsetGainAndLoss":offsetGainAndLoss,
                "profitAndLossEfficiency":profitAndLossEfficiency,
                "followDirection":followDirection,
                "handNumberType":handNumberType,
                "followHandNumber":followHandNumber
            };
            fayuan_data.push(json_);
        });
        setDetalsTitle();//设置明细动态数据

        var newjsonObj = JSON.stringify(fayuan_data);
        var data_ = $.parseJSON(newjsonObj);
        var tableId =$("#datails-table");
        if(leght > 0){
            $(tableId).bootstrapTable('destroy');

        }
        showTableBase(tableId,data_);
        //设置参数

        tableOrInputs();

    }

}
/**
 * 根据状态 显示 table表格 或 input 参数
 */
function tableOrInputs(){
    ($('input[name="followManner"]:checked').val() == 0 ? $('.user-set').show() : showInput());
    //设置参数
    $(".detalis-div").show();
}
function showInput(){

    var netPositionDirection = $('input[name="netPositionDirection"]:checked').val(); //正向反向
    if(netPositionDirection ==0){
        $("input[name='netPositionDirection-val']").get(1).checked=true;
    }else {
        $("input[name='netPositionDirection-val']").get(0).checked=true;
    }
    $('#netPositionChange-val').val($('#netPositionChange-id').val());
    $('#netPositionFollowNumber-val').val($('#netPositionFollowNumber-id').val());

    $('.user-input-set').show()
}

//每个返回按钮事件
function returnBlack(status){
    if(status ==1 ){
        layui.use('element', function(){
            var element = layui.element;
            element.progress('demo', '33%');

        });
        $(".one-div").css('background-color','#44b7af');
        $(".two-div").css('background-color','#f0eff0');
        $('.table-div').hide();
        $('.first-div').show();
        $('.jtc').hide();
    }
    if(status == 2){
        layui.use('element', function(){
            var element = layui.element;
            element.progress('demo', '66%');
        });

        $(".one-div").css('background-color','#f0eff0');
        $(".two-div").css('background-color','#44b7af');
        $(".three-div").css('background-color','#f0eff0');
        $('.first-div').hide();
        $('.detalis-div').hide();
        $('.user-set').hide();//table
        $('.user-input-set').hide(); //input框
        ($('input[name="followManner"]:checked').val() == 1 ? $('.jtc').show(): $('.table-div').show());

    }
}
//操作栏的格式化
function actionFormatter(value, row, index) {
    var id = row.followOrderClient.id;
    var result = "";
    var editUrl ="page/clientUser/client-detail.html";
    var edit_w = 1500;
    var edit_h = 800;
    var details = "删除";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"member_del('" + id+ "',this)\" title='删除'><span class='layui-iconh'>删除</span></a>";
    return result;
}
/*用户-删除*/
function member_del(id, obj) {

    layer.confirm('确认要删除吗？', function(index) {
        //发异步删除数据
        $(obj).parents("tr").remove();
        deleteUser(id);
    });

}
/*
* 删除客户
* */
function deleteUser(followOrderClientId) {
    $.ajax({
        url:url_+"/followOrderClient/deleteClient.Action",
        type:'POST', //GET
        async:true,    //或false,是否异步
        data:{
            followOrderClientId:followOrderClientId//id
        },
        // timeout:5000,    //超时时间
        dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend:function(xhr){

        },
        success:function(data,textStatus,jqXHR){
            if (data.success) {
                layer.msg(data.msg, {
                    icon: 1,
                    time: 1000
                });
            } else {
                layer.msg(data.msg);
            }
        },
        error:function(xhr,textStatus){

        },
        complete:function(){

        }
    })

}

//设置跟单类型下拉框
function setType(value,row,index){
    var result='';
    result +='<select class="form-control followDirection"  ><option value="1"';
    if(value){
        result += 'selected >正向</option><option value="0">反向</option></select>'
    }else{
        result += '>正向</option><option value="0" selected>反向</option></select>'
    }
    return result;
}



function setHandType(value,row,index){
    var result='';
    result +='<select class="form-control handNumberType"  ><option value="1"';

    if(value){
        result += 'selected >按比例</option><option value="0">按固定手数</option></select>'
    }else{
        result += '>按比例</option><option value="0" selected>按固定手数</option></select>'
    }
    return result;
}
function handNumber(value){
    var result='';
    result +='1:<input style="width: 50px;"  class ="followHandNumber"';
    result +='value='+value+'></input>';
    return result;
}

/**
 * 设置勾选状态
 * id_
 */
function setStatus(status,number,value){
    //下单点位
    if(status == 'orderPoint'){
        var val = $('input[name="orderPoint"]:checked').val();

        //(val==1 ? $(".bili-class").hide() : $(".bili-class").show() );
        if(val ==1){
            $(".bili-class").hide()
        }else{
            $(".bili-class").show();
            $(".clientPoint-class option").each(function () {
                if($(this).val()==value){
                    $(this).attr("selected",true);
                }
            })
            $("#clientPointNumber-id").val(number)
        }
    }
    //单笔最大止盈
    if(status == 'maxProfit'){
        var val = $('input[name="maxProfit"]:checked').val();
        (val==1? $("#maxProfit-id").hide() : $("#maxProfit-id").show()&& $("#maxProfit-id").val(number) );
    }
    //单笔最大止损
    if(status == 'maxLoss'){
        var val = $('input[name="maxLoss"]:checked').val();

        (val==1? $("#maxLoss-id").hide() : $("#maxLoss-id").show()&& $("#maxLoss-id").val(number) );
    }
    //账户止损
    if(status == 'accountLoss'){
        var val = $('input[name="accountLoss"]:checked').val();

        (val==1? $("#accountLoss-id").hide() : $("#accountLoss-id").show()&& $("#accountLoss-id").val(number) );

    }
    //跟每单或净头寸
    if(status == 'followManner'){
        $('input[name="followManner"]').each(function () {
            if($(this).val() == number){
                $(this).attr("checked",true)
            }
        });

    }
}

/**
 * 设置详情页明细显示
 */
function setDetalsTitle() {
    $('.followOrderName').text($("#followOrderName").val())//策略名称
    $('.orderPoint').text($('input[name="orderPoint"]:checked').val() == 1 ? '市价':'限价:比客户点位   '+($('.clientPoint-class option:selected').val() == 1 ? '好':'差')+''+$('#clientPointNumber-id').val());
    $('.maxProfit').text($('input[name="maxProfit"]:checked').val() == 1 ? '不设':'点/手   '+$('input[name="maxProfit"]:checked').val());//单笔最大止盈
    $('.maxLoss').text( $('input[name="maxLoss"]:checked').val() == 1 ? '不设':'点/手   '+ $('input[name="maxLoss"]:checked').val());//单笔最大止损
    $('.accountLoss').text($('input[name="accountLoss"]:checked').val() == 1 ? '不设':'美金   '+$("#accountLoss-id").val());//账户止损
    $('.userCode').text($('#account-id option:selected').text());//跟单账号
    $('.followManner').text($('input[name="followManner"]:checked').val()== 0 ? '跟每一单':'跟进头寸');//跟单方式

}

/**
 *   提交
 */
function commit(){
    var leght = $("#datails-table tbody").find('tr').length;
    var followOrderName = $("#followOrderName").val();//策略名称
    var id  = $("#account-id option:selected").val();//跟单人id
    var varietyCode =  parent.$("#product-val-id option:selected").val(); //商品
    var maxProfit = $('input[name="maxProfit"]:checked').val(); //最大止盈
    var maxProfitNumber = $("#maxProfit-id").val(); //止盈点数
    var maxLoss = $('input[name="maxLoss"]:checked').val();//止损
    var maxLossNumber = $("#maxLoss-id").val();//止损点数
    var accountLoss = $('input[name="accountLoss"]:checked').val(); //账户止损
    var accountLossNumber = $("#accountLoss-id").val();//账户止损金额
    var orderPoint = $("#orderPoint").val();//下单点位
    var clientPoint = $(".clientPoint-class option:selected").val(); //比客户点位
    var clientPointNumber = $('#clientPointNumber-id').val(); //点位
   var followManner = $('input[name="followManner"]:checked').val(); //跟单正反向
    var netPositionDirection = $('input[name="netPositionDirection"]:checked').val(); //正向反向
    var netPositionChange = $('#netPositionChange-id').val(); //变化基数
    var netPositionFollowNumber = $('#netPositionFollowNumber-id').val();//跟单、手数
    if(followOrderName == ''|| followOrderName == 'undefined'){
        return layer.msg('策略名称不能为空');
    }

    //构造用户数组
    var fayuan_data = new Array();
    $("#mytable tbody").find('tr').each(function(){
        var tdArr = $(this).children();
        var id = tdArr.eq(0).html();//id
        var followDirection = tdArr.eq(5.).find(".followDirection option:selected").val();//跟单方向
        var handNumberType= tdArr.eq(6).find(".handNumberType option:selected").val();//手数类型
        var followHandNumber = tdArr.eq(7).find(".followHandNumber").val();//手数
        var platformCode = tdArr.eq(8).html();//平台
        var json_ = {
             "id":id,
             "followDirection":followDirection,
             "handNumberType":handNumberType,
             "followHandNumber":followHandNumber,
             "platformCode": platformCode
        };
        fayuan_data.push(json_);
    });
    var str = JSON.stringify(fayuan_data);
    $.ajax({
        url:url_+"/followOrder/updateFollowOrder.Action",
        type:'POST', //GET
        async:true,    //或false,是否异步
        data:{
            id:followOrderId,//跟单id
            followOrderClients:str, // 用户数组字符串
            followOrderName:followOrderName, //策略名称
            accountId:id,//跟单人id
            varietyCode:varietyCode, //商品id
            maxProfit:maxProfit,//最大止盈
            maxProfitNumber : maxProfitNumber,//止盈点数
            maxLoss:maxLoss, //止损
            maxLossNumber : maxLossNumber,//止损点数
            accountLoss:accountLoss,//账户止损
            accountLossNumber:accountLossNumber, //账户止损金额
            orderPoint:orderPoint,//下单点位
            clientPoint :clientPoint, //比客户点位
            clientPointNumber:clientPointNumber, //点位
            netPositionDirection:netPositionDirection,//跟单正反向
            netPositionChange:netPositionChange,//变化基数
            netPositionFollowNumber:netPositionFollowNumber,//手数
            followManner :followManner //跟单方式:用户or净头寸
        },
        // timeout:5000,    //超时时间
        dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend:function(xhr){

        },
        success:function(data,textStatus,jqXHR){
            if (data.success) {
                parent.layer.closeAll();

            } else {
                layer.msg(data.msg);
            }
            parent.layer.msg(data.msg);
        },
        error:function(xhr,textStatus){

        },
        complete:function(){

        }
    })
}
/**
 * 参数设置 区块js动态操作
 * */

function disPaly(){

}