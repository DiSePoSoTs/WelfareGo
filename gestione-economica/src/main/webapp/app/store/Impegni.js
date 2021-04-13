Ext.define('Wp.store.Impegni',{
    extend: 'Ext.data.Store',
    requires: ['Wp.model.ComboBoxImpegno'],

    autoLoad: true,
    model: 'Wp.model.ComboBoxImpegno',
     //remoteSort: true,

    proxy: {
        type: 'ajax',
        url: wp_url_servizi.ListaImpegni,
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});