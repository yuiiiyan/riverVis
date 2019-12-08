

requirejs(['./WorldWindShim',
        './LayerManager','./Kml_view','./RiverPlacemark','./UnityPlacemark','./WMS'],
    function (WorldWind,
              LayerManager,Kml_view,RiverPlacemark,UnityPlacemark,WMS) {
        "use strict";

        WorldWind.Logger.setLoggingLevel(WorldWind.Logger.LEVEL_WARNING);
        
        var wwd = new WorldWind.WorldWindow("canvasOne");

        var layers = [
            {layer: new WorldWind.BMNGLandsatLayer(), enabled: false},
            {layer: new WorldWind.BingAerialLayer(null), enabled: false},
            {layer: new WorldWind.BingAerialWithLabelsLayer(null), enabled: true},
            {layer: new WorldWind.BingRoadsLayer(null), enabled: false},
            {layer: new WorldWind.CompassLayer(), enabled: true},
            {layer: new WorldWind.CoordinatesDisplayLayer(wwd), enabled: true},
            {layer: new WorldWind.ViewControlsLayer(wwd), enabled: true}
           
        ];

        for (var l = 0; l < layers.length; l++) {
            layers[l].layer.enabled = layers[l].enabled;
            wwd.addLayer(layers[l].layer);
        }
        
        var kml_view = new Kml_view(wwd);//河道横截面展示
        var riverPlacemark = new RiverPlacemark(wwd);//浏阳河全景图
        var unityPlacemark = new UnityPlacemark(wwd);//地标进入unity3d三维场景
        var wms =new WMS(wwd);
        wwd.goTo(new WorldWind.Position(28.20, 112.97, 300000));
        
       
        
     // Create a layer manager for controlling layer visibility.
        var layerManger = new LayerManager(wwd);
        // Now set up to handle highlighting.现在设置为处理突出显示。
        var highlightController = new WorldWind.HighlightController(wwd);
        
       
       
    });