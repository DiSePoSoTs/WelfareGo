Ext.Loader.setConfig({
    enabled:true
});

Ext.Loader.setConfig({
    disableCaching:true
});

Ext.Loader.setPath('app', '/Parametri/app');

Ext.require([
    'Ext.toolbar.Paging'
    ]);

Ext.application({    
    name: 'wp',
    appFolder: '/Parametri/app',
    controllers: [
    'ParametriController'
    ],
    launch: function() {
        Ext.create('wp.view.parametri.ParametriPanel', {
            renderTo:'wp_panel'
        });
    }
});

function showSuccessMsg(msg) {
    Ext.MessageBox.show({
        title: 'Eseguito',
        msg: msg,
        buttons: Ext.MessageBox.OK
    });
}

function showFailureMsg(msg) {
    Ext.MessageBox.show({
        title: 'Errore',
        msg: msg,
        buttons: Ext.MessageBox.OK,
        icon: Ext.window.MessageBox.ERROR
    });
}

Ext.QuickTips.init();