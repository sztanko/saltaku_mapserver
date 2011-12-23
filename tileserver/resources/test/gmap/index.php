<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<title>Tiletest</title>
<link href="default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript">
<? 
$mapName="lsoa";
if(isset($_GET['id'])) $mapName=$_GET['id'];
?>
var trafficOptions = {
  getTileUrl: function(coord, zoom) {
//    return "http://tileserver:8081/tile/?id=lsoa&map=test&x="+coord.x+"&y="+coord.y+"&z="+zoom
//return "http://www.saltaku.com:8081/tile/?id=lsoa&map=test&x="+coord.x+"&y="+coord.y+"&z="+zoom
return "http://192.168.1.107:8081/tile/?id=<?=$mapName;?>&map=test&x="+coord.x+"&y="+coord.y+"&z="+zoom
    
  },
  tileSize: new google.maps.Size(256, 256),
  isPng: true,
  opacity: 0.5
};

var trafficMapType = new google.maps.ImageMapType(trafficOptions);


function initialize() {
  var myLatlng = new google.maps.LatLng(51.4685174,-0.0773397);
  var myOptions = {
    zoom: 11,
    center: myLatlng,
    mapTypeId: google.maps.MapTypeId.HYBRID
  }

  var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
  map.overlayMapTypes.insertAt(0, trafficMapType);


}
</script>
</head>
<body onload="initialize()">
  <div id="map_canvas"></div>
  
</body>

</html>
