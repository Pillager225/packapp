function menuitem(sel) {
	$('.menu-sel').removeClass("menu-sel")
	$('.menu div').eq(sel).addClass("menu-sel");
}

function openMenu() {
	animate_time = 1000;
	$('#main').animate({
			left: "+=200"
		},animate_time,null);
	$('#menu').animate({
			left: "+=200"
		},animate_time,null);
	$('#back-button').animate({
			left: "+=200"
		},animate_time,null);
	$('#next-button').animate({
			right: "-=200"
		},animate_time,null);
	$('#mask').animate({height:"100%"},0,null)
//	$('#mask').animate({
//			opacity:0.7
//		},animate_time,null);
}
function closeMenu() {
	animate_time = 1000;
	$('#mask').height('0');
	$('#main').animate({
			left: "-=200"
		},animate_time,null);
	$('#menu').animate({
			left: "-=200"
		},animate_time,null);
	$('#back-button').animate({
			left: "-=200"
		},animate_time,null);
	$('#next-button').animate({
			right: "+=200"
		},animate_time,null);

//	$('#mask').animate({
//			opacity:0
//		},animate_time,function() {
//			$('#mask').height("0");
//	});
}

function package_selection(menu) {
	var lft = 20;
	var rght = 20;
	if (menu) {
		lft = 220;
		rght = -180;
	}
	$('.menu-sel').removeClass("menu-sel")
	$('.menu div').eq(3).addClass("menu-sel");

	$("#main_input").html('');
	$("#main_input").append("<p>Select package type</p>");
	$("#main_input").append("<form action='' id='package_type'></form>");
	$("#package_type").append("<input type='radio' name='package_type' value='std'>STANDARD <img src='img/info.png' onclick='package_std()' height='12' width='12' /><br />");
	$("#package_type").append("<input type='radio' name='package_type' value='small'>SMALL <img src='img/info.png' onclick='package_sm()' height='12' width='12' /><br />");
	$("#package_type").append("<input type='radio' name='package_type' value='flat'>FLAT <img src='img/info.png' onclick='package_fl()' height='12' width='12' /><br />");
	$("#package_type").append("<input type='radio' name='package_type' value='elon'>ELONGATED <img src='img/info.png' onclick='package_el()' height='12' width='12' /><br />");
	$("#main_input").append("<img id='back-button' src='img/Back.png' style='left: "+lft+"px;'>");
	$("#main_input").append("<img id='next-button' src='img/Next.png' style='right: "+rght+"px;'>");
}
function package_std() {
	$("#main_input").html('');
	$("#main_input").append("<div class='intro'><strong>STANDARD</strong> package-products shall be defined as any packaged-product that does not meet any of the definitions for a small, flat, or elongated pckaged-product</div>");
	$("#main_input").append("<div class='intro'>A Standard packaged-product may be packages such as traditionalfiberboard cartons, as well as plastic, wooden, or sylindrical containers.</div>");
	$("#main_input").append("<div class='example'>&nbsp; Examples shown below:</div>");
	$("#main_input").append("<div class='imgs'></div>");
	$(".imgs").append("<img id='stdbox1' class='boxes' src='img/box_std_1.png' height='80' width='100' />");
	$(".imgs").append("<img id='stdbox2' class='boxes' src='img/box_std_2.png' height='100' width='80' />");
	$(".imgs").append("<img id='stdbox3' class='boxes' src='img/box_std_3.png' height='70' width='100' />");
	$("#main_input").append("<img id='back-button' src='img/Back.png' onclick='package_selection(0)'/>");
}
function package_sm() {
	$("#main_input").html('');
	$("#main_input").append("<div class='intro'><strong>SMALL</strong> packaged-products shall be defined as any packaged-product where the:</div>");
	$("#main_input").append("<div>&nbsp; &#8226; volume is less than 13,000 cm<sup>3</sup> (800 in<sup>3</sup>), and</div>");
	$("#main_input").append("<div>&nbsp; &#8226; longest dimension is 350 mm (14 in) or less, and</div>");
	$("#main_input").append("<div>&nbsp; &#8226; weight is 4.5 kg (10 lb) or less.");
	$("#main_input").append("<div class='example'>&nbsp; Example shown below:</div><br />");
	$("#main_input").append("<div class='imgs'></div>");
	$(".imgs").append("<img class='boxes' src='img/box_small.png' height='100' width='150' />");
	$("#main_input").append("<img id='back-button' src='img/Back.png' onclick='package_selection(0)'/>");
}
function package_fl() {
	$("#main_input").html('');
	$("#main_input").append("<div class='intro'><strong>FLAT</strong> packaged-products shall be defined as any packaged-product where the:</div>");
	$("#main_input").append("<div>&nbsp; &#8226; shortest dimension is 200 mm (8 in) or less, and</div>");
	$("#main_input").append("<div>&nbsp; &#8226; next longest dimension is four (4) or more times larger than the shortest dimension, and</div>");
	$("#main_input").append("<div>&nbsp; &#8226; volume is 13,000 cm<sup>3</sup> (800 in<sup>3</sup>) or greater.</div>");
	$("#main_input").append("<div class='example'>Example shown below:</div>");
	$("#main_input").append("<div class='imgs'></div>");
	$(".imgs").append("<img class='boxes' src='img/box_flat.png' height='50' width='100' />");
	$("#main_input").append("<img id='back-button' src='img/Back.png' onclick='package_selection(0)'/>");
}
function package_el() {
	$("#main_input").html('');
	$("#main_input").append("<div class='intro'><strong>ELONGATED</strong> packaged-products shall be defined as any packaged-product where the:</div>");
	$("#main_input").append("<div>&nbsp; &#8226; longest dimension is 900 mm (36 in) or greater, and</div>");
	$("#main_input").append("<div>&nbsp; &#8226; both of the package's other dimensions are each 20 percent or less of that of the longest dimension</div>");
	$("#main_input").append("<div class='example'>Examples shown below:</div>");
	$("#main_input").append("<div class='imgs'></div>");
	$(".imgs").append("<img class='boxes' src='img/box_elon_1.png' height='70' width='100' />");
	$(".imgs").append("<img class='boxes' src='img/box_elon_2.png' height='90' width='80' />");
	$("#main_input").append("<img id='back-button' src='img/Back.png' onclick='package_selection(0)'/>");
}
