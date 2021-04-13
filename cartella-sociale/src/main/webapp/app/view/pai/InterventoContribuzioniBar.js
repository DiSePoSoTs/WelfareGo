Ext.define('wcs.view.pai.InterventoContribuzioniBar',{
    extend: 'Ext.PagingToolbar',
    alias: 'widget.wcs_interventocontribuzionibar',
    store: 'InterventiContribuzioniStore',
    displayMsg: 'Visualizzo i contributi da {0} a {1} di {2}',
    emptyMsg: 'Nessun contributo'
});