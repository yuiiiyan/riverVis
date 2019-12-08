
define(function () {
    "use strict";
    var RiverPlacemark = function (worldWindow) {
       
    	  var curWwwPath = window.document.location.href;
          //获取主机地址之后的目录，如： /ems/Pages/Basic/Person.jsp
          var pathName = window.document.location.pathname;
          var pos = curWwwPath.indexOf(pathName);
          var localhostPath = curWwwPath.substring(0, pos);
          var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
          var basePath=localhostPath+projectName+"/";  
          
          var images = [                      
                        "plain-blue.png",                      
                    ];
          var pinLibrary = WorldWind.configuration.baseUrl + "images/pushpins/", // location of the image files
          placemark,
          placemarkAttributes = new WorldWind.PlacemarkAttributes(null),
          highlightAttributes,
          placemarkLayer = new WorldWind.RenderableLayer("浏阳河全景图"),
          latitude = 28.18,
          longitude = 113.07;
          
          placemarkAttributes.imageScale = 1;
          placemarkAttributes.imageOffset = new WorldWind.Offset(
              WorldWind.OFFSET_FRACTION, 0.3,
              WorldWind.OFFSET_FRACTION, 0.0);
          placemarkAttributes.imageColor = WorldWind.Color.WHITE;
          placemarkAttributes.labelAttributes.offset = new WorldWind.Offset(
              WorldWind.OFFSET_FRACTION, 0.5,
              WorldWind.OFFSET_FRACTION, 1.0);
          placemarkAttributes.labelAttributes.color = WorldWind.Color.YELLOW;
          placemarkAttributes.drawLeaderLine = true;
          placemarkAttributes.leaderLineAttributes.outlineColor = WorldWind.Color.RED;

          // 每一个地标形象，创建一个带有标签的地标。
          for (var i = 0, len = images.length; i < len; i++) {
              // 打造地标和标签。
              placemark = new WorldWind.Placemark(new WorldWind.Position(latitude, longitude + i, 1e2), true, null);
              placemark.label = "浏阳河全景图 "  + "\n"
              + "双击地标进入全景图 " ;
              placemark.altitudeMode = WorldWind.RELATIVE_TO_GROUND;
              
              // 创建这个地标的地标属性。请注意，属性只与它们的属性不同。
              // image URL.
              placemarkAttributes = new WorldWind.PlacemarkAttributes(placemarkAttributes);
              placemarkAttributes.imageSource = pinLibrary + images[i];
              placemark.attributes = placemarkAttributes;
              
              // 创建突出了这一地标属性。注意，正常属性被指定为默认的突出显示属性，
              //因此所有属性都是相同的，除了图像级别。
              //您可以改变颜色、图像或其他属性来控制突出显示。
              highlightAttributes = new WorldWind.PlacemarkAttributes(placemarkAttributes);
              highlightAttributes.imageScale = 1.2;
              placemark.highlightAttributes = highlightAttributes;

              // 添加地标的层
              placemarkLayer.addRenderable(placemark);
          }
          
          // Add the placemarks layer to the WorldWindow's layer list.
          worldWindow.addLayer(placemarkLayer);


          // 现在着手处理采摘。

          var highlightedItems = [];

          // 常见的拾取处理函数。
          var handlePick = function (o) {
              //  输入参数是一个事件或一个taprecognizer。两者具有相同的属性，用于确定鼠标或抽头位置。
              var x = o.clientX,
                  y = o.clientY;

              var redrawRequired = highlightedItems.length > 0; // 如果我们必须重新强调以前选项目

              // De-highlight any previously highlighted placemarks.
              for (var h = 0; h < highlightedItems.length; h++) {
                  highlightedItems[h].highlighted = false;
              }
              highlightedItems = [];

              // 执行选择。必须首先从窗口坐标转换到画布坐标，这些坐标相对于画布左上角而不是页面左上角。
              var pickList = worldWindow.pick(worldWindow.canvasCoordinates(x, y));
              if (pickList.objects.length > 0) {
                  redrawRequired = true;
              }
             
              // 通过简单地将突出显示标记设置为true来突出显示所选项目。
              if (pickList.objects.length > 0) {
                  for (var p = 0; p < pickList.objects.length; p++) {
                      pickList.objects[p].userObject.highlighted = true;
                      
                      //跟踪突出显示的项，以便稍后将它们突出显示。
                      highlightedItems.push(pickList.objects[p].userObject);
                      window.location.href=basePath+"earth/quanjing";
                      
                      // Detect whether the placemark's label was picked. If so, the "labelPicked" property is true.
                      // If instead the user picked the placemark's image, the "labelPicked" property is false.
                      // Applications might use this information to determine whether the user wants to edit the label
                      // or is merely picking the placemark as a whole.
                      if (pickList.objects[p].labelPicked) {
                      	//window.location.href=basePath+"SanweiData/rivermap1";
                      }
                  }
              }

              // Update the window if we changed anything.
              if (redrawRequired) {
                  worldWindow.redraw(); //重绘使突出的变化需要在屏幕上的效果
              }
          };

          // Listen for mouse moves and highlight the placemarks that the cursor rolls over.
          worldWindow.addEventListener("dblclick", handlePick);

          // Listen for taps on mobile devices and highlight the placemarks that the user taps.
          var tapRecognizer = new WorldWind.TapRecognizer(worldWindow, handlePick);
    };
    return RiverPlacemark;
});