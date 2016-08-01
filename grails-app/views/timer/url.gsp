<html>
 <head>
  <meta name="layout" content="header"/>
 </head>
 <body>
  <script>
 var si1

 function getMessages1() {

  $.post('/green/timer/status', {}, function(r) {
   var foox = JSON.parse(r)
   console.log(foox);
   r = foox.dotStatus
   t = foox.dotDuration
   $('#controllerresults').html('This is urlBar.gsps #controllerresults plus status result:<br> ' + r + '<br>' + t );
   var THROWAWAY_BR_CHARS = 0
   var ROWS_PER_SQUARE = 10
   var COLS_PER_SQUARE = ROWS_PER_SQUARE
   var ORIGINX = 40
   var ORIGINY = 100
   var STOPLIGHT_YELLOW = '#FAD201'
   var STOPLIGHT_GREEN = '#27E833'
   var MEDIUM_GREEN =    '#27A033'
   var DARK_GREEN =      '#276033'  
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
           stroke = STOPLIGHT_YELLOW
           fill = "white"
       }
       if ((r.substring(substring_start, substring_start+1) == '2') ||
           (r.substring(substring_start, substring_start+1) == '2')    ) {
           stroke = STOPLIGHT_YELLOW
           fill = STOPLIGHT_YELLOW
       }
       if ((r.substring(substring_start, substring_start+1) == '4') ||
           (r.substring(substring_start, substring_start+1) == '4')    ) {
           fill = 'pink'
           if (t.substring(substring_start, substring_start+1) == '0') fill = DARK_GREEN
           if (t.substring(substring_start, substring_start+1) == '1') fill = DARK_GREEN
           if (t.substring(substring_start, substring_start+1) == '2') fill = MEDIUM_GREEN
           if (t.substring(substring_start, substring_start+1) == '3') fill = STOPLIGHT_GREEN
           stroke = fill
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
  <div id="workresults" >This is #workresults in urlBar.gsp...</div>	
  <div id="controllerresults" >This is #controllerresults in urlBar.gsp...</div>	
 </body>
</html>
