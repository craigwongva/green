<html>
    <head>
		<meta name="layout" content="header"/>
	</head>
    <body>
		<style>
@font-face {
font-family: 'Strait Kcab';
src: url('/cue/fonts/kcab.ttf') format('truetype');
font-weight: normal;
font-style: normal;
}

body{
	font-size: 30px;
    font-family: "Strait Kcab";
}
		</style>
		<script>
		
		/*****************************************************
		 * draw words on the screen
		 *
		 * http://localhost:28080/cue/prompt/font				 
		 ****************************************************/
	 
		var paper = Raphael(0, 0, 1200, 1200);
		
		//var scorerect = paper.rect(0,0,800,100); //all values are arbitrary
		//scorerect.attr({'fill':'blue'});
	
		//var scoretext = paper.text(0+10,80,'You told me you were leaving for Calif.');
		//scoretext.attr({'font-size': 12, 'text-anchor': 'start', 'fill':'white', 'font-family':'Strait Kcab'}); 

		var t = paper.text(500, 300, "lliw rehtaew riehT\n \nrelooc ylbaeciton nrut\n \ntnorf dloc a sa\n \n.adanaC morf sevirra");
		t.attr({'font-size': 40});
		t.attr({'font-weight': 900});
		t.attr({'font-family':'Strait Kcab'});

		//var dot = paper.circle(500, 500, 20).attr({ "fill": "red" });
		
		//var anim = Raphael.animation({ cx: 500, cy: 100 }, 10000);
		//dot.animate(anim);
		
		t.animate({x:500, y:-150}, 10000);
		
		</script>
<!--
lliw rehtaew ehT<br>
relooc ylbaeciton nrut<br>
tnorf dloc a sa<br>
.adanaC morf sevirra<br>
-->
    </body>
</html>
