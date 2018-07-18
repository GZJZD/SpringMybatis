$(function(){

     styleClick();
    });

 function styleClick(){
     $('.super-a').click(function(){
         $(this).siblings().css('border-bottom','5px solid white');
         // $(this).prev().css('border-bottom','5px solid white');
         $(this).css('border-bottom','5px solid #ee5050');
         var id_ = $(this).attr("id");
        if(id_=='holdList'||id_=='profitList'){
            (id_=='holdList'?$('#holdOrderList').show():$('#holdOrderList').hide());
            (id_=='profitList'?$('#profitOrderList').show():$('#profitOrderList').hide());
        }
     });


 }
 function  showDetails(userCode,platFormCode){

     var productCode =  parent.$("#product-val-id option:selected").val(); //商品
       $.ajax({
         url:url_ +'/orderUser/getOrderUser.Action?',
         type:'POST',
         async:true,
         dataType:'json',
         data:{
             userCode:userCode,
             productCode:productCode,
             platFormCode:platFormCode
         },

         beforeSend:function(xhr){

         },
         success:function(data,textStatus,jqXHR){
             $('#inMoney-id').text(data.inMoney);//入金
             $('#createTime-id').text(data.loginTime);//注册时间
             $('#agencyName-id').text(data.agencyName);//代理人
             $('#profit-id').text(data.profit);//平仓盈亏
             $('#remainMoney-id').text(data.remainMoney);//余额
             $('#countNumber-id').text(data.countNumber);//做单数
             $('#doOrderDays-id').text(data.doOrderDays);//做单天数
             $('#offset_gain_and_loss-id').text(data.offset_gain_and_loss);//持仓盈亏
             $('#position_gain_and_loss-id').text(data.position_gain_and_loss);//平仓盈亏
             $('#platformName-id').text(data.platformName);//注册平台
             $('#outMoney-id').text(data.outMoney);//出金
             $('#cmmission-id').text(data.cmmission);//手续费
             $('#countNumberAndHandNumber-id').text(data.countNumber+'/'+data.handNumber);
             var winRate = (data.winRate*100)+'%'+'('+data.profitNumber+'/'+data.lossNumber+')';
             $('#winRate-id').text(winRate); //胜率（盈利单数/亏损单数）
             $('#profitAndLossRatio-id').text(data.profitAndLossRatio+'('+data.profitVal+'/'+data.lossVal+')')
             $('#profitAndLossEfficiency-id').text(data.profitAndLossEfficiency+'('+data.offset_gain_and_loss+'/'+data.handNumber+')'); //平仓盈亏除以手数

             setHoldOrderTableData(data.holdList,"#holdOrderList"); //持仓
             setprofitOrderTableData(data.profitList,"#profitOrderList");
         },
         error:function(xhr,textStatus){
         },
         complete:function(){
         }
     })
 }
 function setHoldOrderTableData(data,tableId){
     var columns = [
         {
             field: 'createDate',
             title: '时间'
         },
         {
             field: '类型',
             title: '类型',
             formatter: function (value, row, index) {

                 return '买入';
             }
         },

         {
             field: 'productCode',
             title: '品种'
         },
         {
             field: 'handNumber',
             title: '手数'
         },

         {
             field: 'openPrice',
             title: '开仓价'
         },

         {
             field: 'stopProfit',
             title: '止盈'
         },
         {
             field:'stopLoss',
             title:'止损'
         },
         {
             field:'commission',
             title:'手续费'

         }
         ,{
             field:'marketPrice',
             title:'市价'

         },{
             field:'gain_and_loss',
             title:'盈亏'

         }
     ];
     $(tableId).bootstrapTable({
         data :data,
         columns:columns
     });
 }
 function setprofitOrderTableData(data,tableId){
     var columns = [
         {
             field: 'createDate',
             title: '时间'
         },
         {
             field: '类型',
             title: '类型',
             formatter: function (value, row, index) {

                 return '平仓';
             }
         },

         {
             field: 'productCode',
             title: '品种'
         },
         {
             field: 'handNumber',
             title: '手数'
         },

         {
             field: 'openPrice',
             title: '开仓价'
         },

         {
             field: 'stopProfit',
             title: '止盈'
         },
         {
             field:'stopLoss',
             title:'止损'
         },
         {
             field:'commission',
             title:'手续费'

         }
         ,{
             field:'closePrice',
             title:'平仓价'

         },{
             field:'profit',
             title:'盈亏'

         }
     ];
     $(tableId).bootstrapTable({
         data :data,
         columns:columns
     });
 }