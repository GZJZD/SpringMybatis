$(function(){
    setParameter();
});

function findById(id){
$('#followOrder-id').val(id);

    $.ajax({
        url:url_+"/orderUser/getFollowOrder.Action",
        type:'POST', //GET
        async:true,    //或false,是否异步
        data:{
            id:id
        },
        // timeout:5000,    //超时时间
        dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend:function(xhr){

        },
        success:function(data,textStatus,jqXHR){

            setFollowOrderParemt(data);
        },
        error:function(xhr,textStatus){
        },
        complete:function(){
        }
    })


}
function setFollowOrderParemt(data){

    $("#followOrderName").val(data.followOrderName) //策略名称
    //下点单位 orderPoint
    if(data.orderPoint == 0){
        $("input[name='orderPoint']").get(1).checked=true;
        $(".clientPoint-class").val(data.clientPoint);
        $(".clientPoint-class option:selected").val(data.clientPoint);
        $('#clientPointNumber-id').val(data.clientPointNumber);
    }else{

        $("input[name='orderPoint']").get(0).checked=true;
        $('#clientPointNumber-id').hide();
        $('.if-ok').hide();
    }
    //最大止盈
    if(data.maxProfit == 0){
        $('#maxProfit-id').val(data.maxProfitNumber);
        $("input[name='maxProfit']").get(1).checked=true;
    }else{
        $("input[name='maxProfit']").get(0).checked=true;
        $('#maxProfit-id').hide();
    }
    //最大止损
    if(data.maxLoss == 0){
        $('#maxLoss-id').val(data.maxLossNumber);
        $("input[name='maxLoss']").get(1).checked=true;
    }else{
        $("input[name='maxLoss']").get(0).checked=true;
        $('#maxLoss-id').hide();
    }
    //账户止损
    if(data.accountLoss == 0){
        $('#accountLoss-id').val(data.accountLossNumber);
        $("input[name='accountLoss']").get(1).checked=true;

    }else{
        $("input[name='accountLoss']").get(0).checked=true;
        $('#accountLoss-id').hide();
    }
     setAccount(data)//跟单人
    //跟单方式
    if(data.followManner == 0){
        $("input[name='followManner']").get(1).checked=true;
    }else{
        $("input[name='followManner']").get(0).checked=true;
    }

}
function setAccount(data){
    var content = "";
        content += "<option value="+data.account.agent.id+" selected>"+data.account.agent.name+"</option>"
    $("#GD-id").append(content); //跟单账号

}

function showTableBase(tableId,data_){
    var columns = [

        {
            field: 'userCode',
            title: '客户编号'
        },
        {
            field: 'userName',
            title: '客户姓名'
        },
        {
            field: 'profit',
            title: '平仓盈亏'
        },
        {
            field: 'profit_loss_than',
            title: '盈亏效率'
        },
        {
            field:'followDirection',
            title:'跟单类型',
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
            valign: 'middle',
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
//table数据赋值
function  setParameter() {
    $("#mytable").bootstrapTable('destroy');
    var opts = parent.$("#table").bootstrapTable('getSelections');
    var newjsonObj = JSON.stringify(opts)
    var data_ = $.parseJSON(newjsonObj);
    var tableId =$("#mytable");

    var columns = [

        {
            field: 'userCode',
            title: '客户编号'
        },
        {
            field: 'userName',
            title: '客户姓名'
        },

        {
            field: 'offset_gain_and_loss',
            title: '平仓盈亏'
        },

        {
            field: 'profit_loss_than',
            title: '盈亏效率'
        },
        {
            field:'followDirection',
            title:'跟单类型',
            align: 'center',
            valign: 'middle',
            formatter: setType

        },
        {
            field:'handNumberType',
            title:'手数类型',
            align: 'center',
            valign: 'middle',
            formatter:setHandType
        }
        ,{
            field:'handNumber',
            title:'手数',
            align: 'center',
            valign: 'middle',
            formatter:handNumber
        }
        ,
        {
            field: 'platFormCode',
            title: '平台',
            align: 'center',
            valign: 'middle',
            hidden:true
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
        data :data_,
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
        ($('input[name="followManner"]:checked').val() == 0? $('.table-div').show():$('.jtc').show());

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
            var userCode = tdArr.eq(0).html();//id
            var userName = tdArr.eq(1).html();//用户编号
            var profit = tdArr.eq(2).html();//平仓盈亏
            var profit_loss_than  =tdArr.eq(3).html();//盈亏率
            var followDirection = tdArr.eq(4.).find(".followDirection option:selected").val();//跟单方向
            var handNumberType= tdArr.eq(5).find(".handNumberType option:selected").val();//手数类型
            var followHandNumber = tdArr.eq(6).find(".followHandNumber").val();//手数
            var json_ = {"userCode":userCode,"userName":userName,"profit":profit,"profit_loss_than":profit_loss_than,"followDirection":followDirection,"handNumberType":handNumberType,"followHandNumber":followHandNumber};
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
/**
 * 设置详情页明细显示
 */
function setDetalsTitle() {
    $('.followOrderName').text($("#followOrderName").val())//策略名称
    $('.orderPoint').text($('input[name="orderPoint"]:checked').val() == 1 ? '市价':'限价:比客户点位   '+($('.clientPoint-class option:selected').val() == 1 ? '好':'差')+''+$('#clientPointNumber-id').val());
    $('.maxProfit').text($('input[name="maxProfit"]:checked').val() == 1 ? '市价':'点/手   '+$('input[name="maxProfit"]:checked').val());//单笔最大止盈
    $('.maxLoss').text( $('input[name="maxLoss"]:checked').val() == 1 ? '不设':'点/手   '+ $('input[name="maxLoss"]:checked').val());//单笔最大止损
    $('.accountLoss').text($('input[name="accountLoss"]:checked').val() == 1 ? '不设':'美金   '+$("#accountLoss-id").val());//账户止损
    $('.userCode').text($('#GD-id option:selected').val());//跟单账号
    $('.followManner').text($('input[name="followManner"]:checked').val()== 1 ? '跟净头寸':'跟每一单');//跟单方式

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
        ($('input[name="followManner"]:checked').val() == 0? $('.table-div').show():$('.jtc').show());

    }
}
//操作栏的格式化
function actionFormatter(value, row, index) {
    var id = row.id;
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
        layer.msg('已删除!', {
            icon: 1,
            time: 1000
        });
        // var date = $("#mytable").bootstrapTable('getData');
        // console.log(date)
    });

}

//设置跟单类型下拉框
function setType(value,row,index){
    var result='';
    result +='<select class="form-control followDirection"  ><option value="1">正向</option><option value="0">反向</option></select>';

    return result;
}

function setHandType(value,row,index){
    var result='';
    result +='<select class="form-control handNumberType"   ><option value="1">按比例</option><option value="0">按固定手术</option></select>';

    return result;
}
function handNumber(){
    var result='';
    result +='1:<input style="width: 50px;"  class ="followHandNumber" ></input>';

    return result;
}

/**
 * 设置勾选状态
 * id_
 */
function setStatus(status){
    //下单点位
    if(status == 'orderPoint'){
        var val = $('input[name="orderPoint"]:checked').val();
        $('#orderPoint').val(val);
        (val==1? $(".bili-class").hide() : $(".bili-class").show() );

    }
    //单笔最大止盈
    if(status == 'maxProfit'){
        var val = $('input[name="maxProfit"]:checked').val();
        (val==1 ? $("#maxProfit-id").hide() : $("#maxProfit-id").show() );
    }
    //单笔最大止损
    if(status == 'maxLoss'){
        var val = $('input[name="maxLoss"]:checked').val();
        $('#maxLoss').val(val);
        (val==1? $("#maxLoss-id").hide() : $("#maxLoss-id").show() );
    }
    //账户止损
    if(status == 'accountLoss'){
        var val = $('input[name="accountLoss"]:checked').val();
        $('#accountLoss').val(val);
        (val==1? $("#accountLoss-id").hide() : $("#accountLoss-id").show() );

    }
    //跟每单或净头寸
    if(status == 'followManner'){
        var val = $('input[name="followManner"]:checked').val();
        $('#followManner').val(val);
    }
}

/**
 *   跟单创建提交
 */
function commit() {


    //构造用户数组
    var fayuan_data = new Array();
    $("#mytable tbody").find('tr').each(function () {
        var tdArr = $(this).children();
        var userCode = tdArr.eq(0).html();//用户编号
        var userName = tdArr.eq(1).html();//用户姓名
        var followDirection = tdArr.eq(4.).find(".followDirection option:selected").val();//跟单方向
        var handNumberType = tdArr.eq(5).find(".handNumberType option:selected").val();//手数类型
        var followHandNumber = tdArr.eq(6).find(".followHandNumber").val();//手数
        var platFormCode = tdArr.eq(7).html();//平台
        var json_ = {
            "userCode": userCode,
            "userName": userName,
            "followDirection": followDirection,
            "handNumberType": handNumberType,
            "followHandNumber": followHandNumber,
            "platFormCode": platFormCode
        };
        fayuan_data.push(json_);
    });
    var str = JSON.stringify(fayuan_data);

    $.ajax({
        url: url_ + "/followOrderClient/addClientByFollowOrder.Action",
        type: 'POST', //GET
        async: true,    //或false,是否异步
        data: {
            followOrderId:$('#followOrder-id').val(),
            followOrderClients:str
        },
        // timeout:5000,    //超时时间
        dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        beforeSend: function (xhr) {

        },
        success: function (data, textStatus, jqXHR) {
            if (data.success) {
                parent.layer.closeAll();

            } else {
                layer.msg(data.msg);
            }
            parent.layer.msg(data.msg);
        },
        error: function (xhr, textStatus) {

        },
        complete: function () {

        }
    })
}
function getAccountList(){
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

                content += "<option value="+ele.account.id+" selected>"+ele.account.platform.name+"-"+ele.account.account+"</option>"

            });
            $("#GD-id").append(content);

        },
        error:function(xhr,textStatus){

        },
        complete:function(){

        }
    })
}
