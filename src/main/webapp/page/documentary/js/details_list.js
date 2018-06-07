
var tableId = $("#detailTable");
var method = "post";

//var url_  ="demo.json";
// var url_ = "/getPositionDetails.Action?followOrderId="+followOrderId;
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
},{
    title : '序号',

    formatter: function (value, row, index) {
        //获取每页显示的数量
        var pageSize=$('#detailTable').bootstrapTable('getOptions').pageSize;
        //获取当前是第几页
        var pageNumber=$('#detailTable').bootstrapTable('getOptions').pageNumber;
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
    },{
        field: 'marketPrice',
        title: '点位'
    },{
        field: 'tradeTime',
        title: '下单时间'
    },{
        field: 'poundage',
        title: '手续费'
    },{
        field: 'profit',
        title: '盈亏'
    },
    {
        field: 'remainHandNumber',
        title: '操作',

        align: 'center',
        valign: 'middle',
        formatter: function (value, row, index) {
            var detailId= row.detailId;
            if(value == "0.0"){
                return "-";
            }else{
                return "<a href='javascript:;' class='btn btn-xs green' onclick='manually_close_position("+detailId+")' >  <span style='color: #4cae4c'>手动平仓</span></a>"
            }
        }
    }
];
function manually_close_position(id) {
    layer.confirm("确认要手动平仓吗？",function (index) {
        $.ajax({
            url:"/sm/followOrder/manuallyClosePosition.Action?detailId="+id,
            success:function (data) {
                var obj=eval('('+data+')');
                if(obj.success){
                    alert(1311)
                    layer.msg('平仓成功', {icon: 6,time:1000},function () {
                        location.reload();
                        $(".layui-laypage-btn").click();
                    });


                }else{
                    layer.msg('系统出现故障，请联系管理员，进行下一步操作', {icon: 6, time: 1000});

                }


            }
        })
    })

}
function child(id,orderNum,offsetGainAndLoss,poundageTotal){
    $("#orderNum").html(orderNum);
    $("#offsetGainAndLoss").html(offsetGainAndLoss);
    $("#positionGainAndLoss").html(0);
    $("#poundageTotal").html(poundageTotal);
    url_ = "/sm/followOrder/getPositionDetails.Action?followOrderId="+id;
   // url_ = "/getPositionDetails.Action?followOrderId="+id;
    followOrderId = id;
    showByTableId(tableId,method,url_,unique_Id,sortOrder,columns);

}
$(function(){

 //   showByTableId(tableId,method,url_,unique_Id,sortOrder,columns);
})

//根据窗口调整表格高度
$(window).resize(function() {
    $('#detailTable').bootstrapTable('resetView', {
        height: tableHeight()
    })
})


//操作栏的格式化
function actionFormatter(value, row, index) {
    var id = row.id;
    var result = "";
    var editUrl ="page/clientUser/client-edit.html";
    var edit_w = 600;
    var edit_h = 400;
    var details = "查看明細";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"EditViewById('" + id + "', view='view','"+editUrl+"','"+edit_w+"','"+edit_h+"')\" title='查看明細'><span class='glyphicon glyphicon-search'></span></a>";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"EditViewById('" + id + "', view='view','"+editUrl+"','"+edit_w+"','"+edit_h+"')\" title='修改'><span class='glyphicon glyphicon-search'></span></a>";
    result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"DeleteByIds('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

    return result;
}