Ext.define('wcs.view.pai.InterventiBar',{
    extend: 'Ext.PagingToolbar',
    alias: 'widget.wcs_interventibar',
    store: 'InterventiStore',
    displayMsg: 'Visualizzo gli interventi da {0} a {1} di {2}',
    emptyMsg: 'Nessun intervento'
});