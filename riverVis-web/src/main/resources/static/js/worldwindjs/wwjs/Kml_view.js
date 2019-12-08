define(function () {
    "use strict";
    var Kml_view = function (worldWindow) {
    	
    	
        var kmlPath = WorldWind.configuration.baseUrl + "js/worldwindjs/wwjs/data/rivercrosssection.kml";
        var kmlFilePromise = new WorldWind.KmlFile(kmlPath);
        kmlFilePromise.then(function (kmlFile) {
            var renderableLayer = new WorldWind.RenderableLayer("浏阳河横截面");
                     
            renderableLayer.addRenderable(kmlFile);
            
            worldWindow.addLayer(renderableLayer);
            worldWindow.redraw();
        });

        
       
        
        
        
        
    };
    return Kml_view;
});
