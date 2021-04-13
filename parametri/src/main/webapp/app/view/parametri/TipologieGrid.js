Ext.define('Tipologie', {
    extend: 'Ext.data.Model',
    fields: [ 'Anno', 'Capitolo', 'Impegno' ]
});

var tipologieStore = Ext.create('Ext.data.Store', {
    model: 'Tipologie',
    data: [
        { anno: '2010',  capitolo: 'xxxxxxxxxx',  impegno: '500.000' },
        { anno: '2011',  capitolo: 'yyyyyyyyyy',  impegno: '800.000' },
        { anno: '2012',  capitolo: 'zzzzzzzzzz',  impegno: '1.000.000' }
    ]
});

Ext.define('wp.view.parametri.TipologieGrid',{

    extend: 'Ext.grid.Panel',
    alias: 'widget.wp_tipologiegrid',
    store: tipologieStore,
    
    columns: [
        {
            text: 'Anno',
            width: 40,
            sortable: false,
            hideable: false,
            dataIndex: 'anno'
        },
        {
            text: 'Capitolo',
            flex: 1,
            dataIndex: 'capitolo'
        },
        {
            text: 'Impegno',
            flex: 1,
            dataIndex: 'impegno'
        }
    ]
    
});