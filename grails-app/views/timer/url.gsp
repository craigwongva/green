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
   var ROWS_PER_SQUARE = 4
   var COLS_PER_SQUARE = ROWS_PER_SQUARE
   var ORIGINX = 40
   var ORIGINY = 100
   for (row = 0; row < ROWS_PER_SQUARE; row++) {
    for (col = 0; col < COLS_PER_SQUARE; col++) {
       var color
       var THROWAWAY_BR_CHARS = 4
       var substring_start = THROWAWAY_BR_CHARS+row*ROWS_PER_SQUARE+col
       if (r.substring(substring_start, substring_start+1) == '4') {
           color = "green"
       }
       else {
           color = "blue"
       }
       var dot00 = paper.circle(ORIGINX+46*col, ORIGINY+46*row, 20).attr({ "stroke": "orange" }); //.attr({ "fill": color });
    }
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
