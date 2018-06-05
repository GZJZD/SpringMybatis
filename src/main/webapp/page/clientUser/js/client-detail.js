$(function(){

     styleClick()

});

 function styleClick(){
     $('.super-a').click(function(){
         $(this).siblings().css('border-bottom','5px solid white');
         // $(this).prev().css('border-bottom','5px solid white');
         $(this).css('border-bottom','5px solid #ee5050');
     });

 }