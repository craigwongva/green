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
   var THROWAWAY_BR_CHARS = 4
   var ROWS_PER_SQUARE = 10
   var COLS_PER_SQUARE = ROWS_PER_SQUARE
   var ORIGINX = 40
   var ORIGINY = 100
   for (row = 0; row < ROWS_PER_SQUARE; row++) {
    for (col = 0; col < COLS_PER_SQUARE; col++) {
       var fill
       var stroke
       var substring_start = THROWAWAY_BR_CHARS+row*ROWS_PER_SQUARE+col
       if ((r.substring(substring_start, substring_start+1) == '0') ||
           (r.substring(substring_start, substring_start+1) == '0')    ) {
           stroke = "white"
           fill = "white"
       }
       if ((r.substring(substring_start, substring_start+1) == '1') ||
           (r.substring(substring_start, substring_start+1) == '1')    ) {
           stroke = "#fad201"
           fill = "white"
       }
       if ((r.substring(substring_start, substring_start+1) == '2') ||
           (r.substring(substring_start, substring_start+1) == '2')    ) {
           stroke = "#fad201"
           fill = "#fad201"
       }
       if ((r.substring(substring_start, substring_start+1) == '4') ||
           (r.substring(substring_start, substring_start+1) == '4')    ) {
           stroke = "#27e833"
           fill = "#27e833"
       }
       var dot00 = paper.circle(ORIGINX+46*col, ORIGINY+46*row, 20).attr({ "stroke": stroke, "fill": fill });
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
