Ext.define("wf.utils.WfUtils", {
    singleton: true,
    getLoadDavDocumentFunc: function(ajaxConfig) {
        var davFunc = null;
        function  handleError(message) {
            Ext.Msg.show({
                title: 'Errore',
                msg: 'Si &egrave verificato un errore durante l\'apertura del documento:<br/>' + message,
                buttons: Ext.Msg.OK
            });
        }
        function invokeDavFunc() {
            try {
//                log('invoking dav function ', davFunc);
                eval(davFunc);
//                log('invoked dav function ');
            } catch (e) {
                log('dav func error', e);
                handleError(e.toString());
            }
        }

        return function() {
            if (davFunc) {
                invokeDavFunc();
            } else {
                Ext.Ajax.request(Ext.merge({
                    
                }, ajaxConfig, {
                    success: function(response) {
                        var data = Ext.JSON.decode(response.responseText);
                        if (data && data.success) {
                            davFunc = data.data;
                            invokeDavFunc();
                        } else {
                            handleError(data.message);
                        }
                    }, failure: function(response) {
                        handleError(response);
                    }
                }));
            }
        };
    }
});