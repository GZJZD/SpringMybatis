$(function(){
    setParameter();
});

function showTableBase(tableId,data_){
    var columns = [
        {
            field: 'id',
            title: 'ID'
        },
        {
            field: 'userCode',
            title: '客户编号'
        },
        {
            field: 'userCode',
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
            field:'orderType',
            title:'跟单类型',
            align: 'center',
            valign: 'middle',
        },
        {
            field:'handType',
            title:'手数类型',
            align: 'center',
            valign: 'middle',
        }
        ,{
            field:'handNumber',
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

//table数据赋值
function  setParameter() {

    var opts = parent.$("#table").bootstrapTable('getData');
    var newjsonObj = JSON.stringify(opts)
    var data_ = $.parseJSON(newjsonObj);
    var tableId =$("#mytable");
    var columns = [
        {
            field: 'id',
            title: 'ID'
        },
        {
            field: 'userCode',
            title: '客户编号'
        },
        {
            field: 'userCode',
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
            field:'orderType',
            title:'跟单类型',
            align: 'center',
            valign: 'middle',
            formatter: setType

        },
        {
            field:'handType',
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
            field: 'ID',
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
    if(status ==1){
        layui.use('element', function(){
            var element = layui.element;
            element.progress('demo', '66%');
        });
        $(".two-div").css('background-color','#44b7af');
        $(".one-div").css('background-color','#f0eff0');
        $('.first-div').css('display','none');
        $('.table-div').show();
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

        //设置展示数据
        var fayuan_data = new Array();
        $("#mytable tbody").find('tr').each(function(){
            var tdArr = $(this).children();
            var id_ = tdArr.eq(0).html();//id
            var userCode = tdArr.eq(1).html();//用户编号
            var profit = tdArr.eq(2).html();//平仓盈亏
            var profit_loss_than  =tdArr.eq(3).html();//盈亏率
            var handNumber = tdArr.eq(7).find('input').val();//手数
            var json_ = {"id":id_,"userCode":userCode,"profit":profit,"profit_loss_than":profit_loss_than,"handNumber":handNumber};
            fayuan_data.push(json_);
        });
        var newjsonObj = JSON.stringify(fayuan_data);
        var data_ = $.parseJSON(newjsonObj);
        var tableId =$("#datails-table");
        $(".detalis-div").show();
        showTableBase(tableId,data_);
    }

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
    }
    if(status == 2){
        layui.use('element', function(){
            var element = layui.element;
            element.progress('demo', '66%');
        });

        $(".one-div").css('background-color','#f0eff0');
        $(".two-div").css('background-color','#44b7af');
        $(".three-div").css('background-color','#f0eff0')
        $('.table-div').show();
        $('.first-div').hide();
        $('.detalis-div').hide();


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
        var date = $("#mytable").bootstrapTable('getData');
        console.log(date)
    });

}
//设置跟单类型下拉框
function setType(value,row,index){
    var result='';
    result +='<select class="form-control" name="" ><option value="1">正向</option><option value="2">反向</option></select>';

    return result;
}

function setHandType(value,row,index){
    var result='';
    result +='<select class="form-control" name="" ><option value="1">按比例</option><option value="2">按固定手术</option></select>';

    return result;
}
function handNumber(){
    var result='';
    result +='1:<input style="width: 50px;"></input>';

    return result;
}

/**
 * 设置勾选状态
 * id_
 */
function setStatus(status){
    //下单点位
    if(status == 'setPT'){
        var val = $('input[name="setPT"]:checked').val();
        $('#setPT').val(val);
    }
    //单笔最大止盈
    if(status == 'profitAndLoss'){
        var val = $('input[name="profitAndLoss"]:checked').val();
        $('#setPT').val(val);
    }
    //单笔最大止损
    if(status == 'lossLimit'){
        var val = $('input[name="lossLimit"]:checked').val();
        $('#lossLimit').val(val);
    }
    //账户止损
    if(status == 'ZHlossLimit'){
        var val = $('input[name="ZHlossLimit"]:checked').val();
        $('#ZHlossLimit').val(val);

    }
    //跟每单或净头寸
    if(status == 'singleOrLoose'){
        var val = $('input[name="singleOrLoose"]:checked').val();
        $('#singleOrLoose').val(val);
    }


}