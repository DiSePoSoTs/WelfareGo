Ext.define('wcs.view.pai.InterventoCronologiaList',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wcs_interventocronologialist',
    store: 'InterventiCronologiaStore',
    loadMask: true,
    bbar: {
        xtype: 'wcs_interventibar',
        store: 'InterventiCronologiaStore'
    },
    listeners:{
    	scrollershow: function(scroller) {
    		  if (scroller && scroller.scrollEl) {
    		    scroller.clearManagedListeners(); 
    		    scroller.mon(scroller.scrollEl, 'scroll', scroller.onElScroll, scroller); 
    		  }
    		}
    },

    initComponent: function() {
        this.columns = [
        {
            header: 'Data',
            dataIndex: 'data',
            sortable: true,
            flex: 1
        },
        {
            header: 'Evento',
            dataIndex: 'evento',
            sortable: true,
            flex: 1
        },
        {
            header: 'Cognome operatore',
            dataIndex: 'cognomeOperatore',
            sortable: true,
            flex: 1
        },
        {
            header: 'Nome operatore',
            dataIndex: 'nomeOperatore',
            sortable: true,
            flex: 1
        },
        {
            header: 'Note',
            dataIndex: 'note',
            sortable: true,
            flex: 1
        }
        ];
        

        this.callParent(arguments);
    }
});