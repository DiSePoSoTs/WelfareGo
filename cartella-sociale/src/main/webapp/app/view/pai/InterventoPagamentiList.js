Ext.define('wcs.view.pai.InterventoPagamentiList',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_interventopagamentilist',
    title: 'Mandati',
    store: 'InterventiPagamentiStore',
    loadMask: true,
    bbar: {
        xtype: 'wcs_interventopagamentibar'
    },

    initComponent: function() {
        this.columns = [
        {
            header: 'Data',
            dataIndex: 'data',
            sortable: true,
            doSort: function(state) {
                //Sort using column sort
                var ds = this.up('tablepanel').store;
                var field = this.getSortParam();
                ds.sort({
                    property: field,
                    direction: state,
                    sorterFn: sorterFunction
                });
            },
            flex: 1
        },
        {
            header: 'Importo',
            dataIndex: 'importo',
            renderer: Euro,
            sortable: true,
            flex: 1
        },
        {
            header: 'ID Mandato',
            dataIndex: 'idMandato',
            sortable: true,
            flex: 1
        },
        {
            header: 'Mandato',
            dataIndex: 'mandato',
            sortable: true,
            flex: 1
        },
        {
            header: 'Mese/Anno',
            dataIndex: 'riscosso',
           // sortable: true,
            flex: 1
        },
        {
            header: 'Modalità erogazione',
            dataIndex: 'modalitaErogazione',
            sortable: false,
            flex: 1
        }
        ];

        this.callParent(arguments);
    }

});

function sorterFunction(o1, o2) {
	log('chiamata funzione di order');
    var userVal1 = toDate(o1.get('data')),
        userVal2 = toDate(o2.get('data'));

    if (userVal1 === userVal2) {
        return 0;
    }

    return userVal1 < userVal2 ? -1 : 1;
}

function toDate(dateStr) {
    var parts = dateStr.split("/");
    return new Date(parts[2], parts[1] - 1, parts[0]);
}