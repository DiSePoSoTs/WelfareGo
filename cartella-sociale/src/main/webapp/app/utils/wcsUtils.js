Ext.define("wcs.utils.wcsUtils", {
    singleton : true,
    getLoadDavDocumentFunc : function(ajaxConfig){
        var isReady=false,func=null,
        handleError=function(message){
            Ext.Msg.show({
                title:'Errore',
                msg: 'Si &egrave; verificato un errore durante l\'apertura del documento:<br/>'+message,
                buttons: Ext.Msg.OK
            });
        };

        return function(){
            if(isReady){
                func();
            }else{
                ajaxConfig.success=function(response){
                    var data=Ext.JSON.decode(response.responseText);
                    if(data.appletTag!=undefined&&data.openFunc!=undefined){
                        if(Ext.get('sanelemon_applet')==null){
                            Ext.core.DomHelper.append(document.body,data.appletTag);
                        }
                        eval('func = '+data.openFunc);
                        isReady=true;
                        func();
                    }else{
                        handleError(data.message);
                    }
                };
                ajaxConfig.failure=function(response) {
                    handleError(response);
                };
                Ext.Ajax.request(ajaxConfig);
            }
        };
    }
});