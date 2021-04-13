Ext.define('wcs.view.appuntamenti.AppuntamentiBar',{
    extend: 'Ext.PagingToolbar',
    alias: 'widget.wcs_appuntamentibar',
    store: 'AppuntamentiStore',
    displayMsg: 'Visualizzo gli appuntamenti da {0} a {1} di {2}',
    emptyMsg: 'Nessun appuntamento'
});