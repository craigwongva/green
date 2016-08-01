<html>
 <head>
  <meta name="layout" content="header"/>
 </head>
 <body>
  <script>
 var si1

 function getMessages1() {

  $.post('/green/timer/status', {}, function(r) {
   $('#controllerresults').html('This is urlBar.gsps #controllerresults plus status result: ' + r );
   for (row = 0; row < 1; row++) {
       var color
       var ROWS_PER_SQUARE = 4
       var THROWAWAY_BR_CHARS = 4
       var substring_start = THROWAWAY_BR_CHARS+row*ROWS_PER_SQUARE
       if (r.substring(substring_start, substring_start+1) == '4') {
           color = "green"
       }
       else {
           color = "blue"
       }
       var dot00 = paper.circle(500, 16*row+500, 20).attr({ "fill": color });
       var dot10 = paper.circle(540, 500, 20).attr({ "fill": color });
   }
  },'html'); 

 }

 $.post('/green/timer/work', {}, function(r) {
  $('#workresults').html('This is urlBar.gsps work plus work result: ' + r);
 },'html'); 

 var paper = Raphael(0, 0, 1200, 1200);
 console.log('xxx');
 si1 = setInterval(getMessages1, 200);

  </script>
This is body text in urlBar.gsp.
  <div id="workresults" >This is #workresults in urlBar.gsp...</div>	
  <div id="controllerresults" >This is #controllerresults in urlBar.gsp...</div>	
 </body>
</html>
