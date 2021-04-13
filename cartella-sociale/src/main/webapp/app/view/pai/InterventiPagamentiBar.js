Ext.define('wcs.view.pai.InterventiPagamentiBar',{
    extend: 'Ext.PagingToolbar',
    alias: 'widget.wcs_interventopagamentibar',
    store: 'InterventiPagamentiStore',
    displayMsg: 'Visualizzo i pagamenti da {0} a {1} di {2}',
    emptyMsg: 'Nessun pagamento'
});