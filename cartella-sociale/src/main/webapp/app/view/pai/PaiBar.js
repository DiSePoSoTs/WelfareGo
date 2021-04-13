Ext.define('wcs.view.pai.PaiBar',{
    extend: 'Ext.PagingToolbar',
    alias: 'widget.wcs_paibar',
    store: 'PaiStore',
    displayMsg: 'Visualizzo i PAI da {0} a {1} di {2}',
    emptyMsg: 'Nessun PAI'
});