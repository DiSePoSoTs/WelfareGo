
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Entry-point dell'applicazione ExtJs
 */

if(console && console.log){
    log=function(){
        console.log.apply(console,arguments);
    }
    log('logging to debug console');
}else{
    log=function(){};
}

wpStoreExceptionFunction=function(proxy,response){
    var message=response && response.responseText ? Ext.JSON.decode(response.responseText) : '';
    if(message){
        message=message.message;
    }else{
        message="errore generico";
    }
    var config={
        title:"Errore",
        msg:"Si è verificato un errore durante il caricamento dei dati:<br/>"+message,
        buttons:Ext.Msg.OK
    };
    Ext.Msg.show(config);
}

Ext.Loader.setConfig({
    enabled: true
});

Ext.Loader.setPath('app', '/GestioneEconomica/app');

/*
 * ExtJs BUGFIX: in alcuni casi capita che all'interno di una griglia che utilizza
 * il plugin CellEditing, questo venga istanziato PRIMA che la classe sia stata
 * caricata dal loader. Per evitare questo problema, importo la classe PRIMA di
 * avviare l'applicazione, in questo modo sono sicuro che venga importato
 */
Ext.require('Ext.grid.plugin.CellEditing');

Ext.application({
    name: 'Wp',

    controllers: [
    'Tabs',
    'Acquisizioni',
    'Fatturazioni',
    'Pagamenti'
    ],

    appFolder: '/GestioneEconomica/app',

    launch: function() {
        Ext.create('Ext.container.Container', {
            renderTo: 'wp_container_principale',
            layout: 'fit',
            items: {
                xtype: 'wptabs'
            }
        });
    }
});