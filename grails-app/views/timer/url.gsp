<html>
 <head>
  <meta name="layout" content="header"/>
 </head>
 <body>
  <script>
 var si1

 function getMessages1() {

  $.post('/green/timer/status', {}, function(r) {
   $('#controllerresults').html('This is urlBar.gsps #controllerresults plus status result: ' + r);
  },'html'); 

 }

 $.post('/green/timer/work', {}, function(r) {
  $('#workresults').html('This is urlBar.gsps work plus work result: ' + r);
 },'html'); 

 console.log('xxx');
 si1 = setInterval(getMessages1, 200);

  </script>
This is body text in urlBar.gsp.
  <div id="workresults" >This is #workresults in urlBar.gsp...</div>	
  <div id="controllerresults" >This is #controllerresults in urlBar.gsp...</div>	
 </body>
</html>
