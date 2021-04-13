function createJSONFromImpegniStore(store){
    var items = store.data.items;
    var json = [];
    for (var i=0; i<items.length; i++){
        var item = items[i];
        json.push({
           aCarico: String(item.data.aCarico),
            anno: item.data.anno,
            capitolo: item.data.capitolo,
            centroElementareDiCosto: item.data.centroElementareDiCosto,
            id: item.data.id,
            impegno: item.data.impegno,
            importoDisponibile: item.data.importoDisponibile,
            importoComplessivo: item.data.importoComplessivo,
            uot:item.data.uot
        });
    }
    return Ext.JSON.encode(json);
}

function changeQuantitaLabel(text){
    Ext.getCmp("wcs_interventoQuantita").el.dom.firstChild.lastChild.nodeValue = text;
}

function createJSONCivilmenteObbligato(store){
    if (store != null){
        var items = store.data.items;
        var json = [];
        for (var i=0; i<items.length; i++){
            var item = items[i];
            json.push({
                codTipInt: item.data.codTipInt,
                cntTipInt: item.data.cntTipInt,
                codAnag: item.data.codAnag,
                nome: item.data.nome,
                cognome: item.data.cognome,
                codicefiscale: item.data.codicefiscale,
                importoMensile: item.data.importoMensile,
                codPai: item.data.codPai
            });
        }
        return Ext.JSON.encode(json);
    } else {
        return null;
    }
}

function salvaUtenteInSessione(username){
    Ext.Ajax.request({
        url: '/CartellaSociale/StoreInSession',
        params: {
            action: 'connectedUser',
            username: username
        },
        success: function(response){
            var json = Ext.JSON.decode(response.responseText);
            if (!json.success) {
                Ext.MessageBox.show({
                    title: 'Esito operazione',
                    msg: json.data.message,
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.window.MessageBox.ERROR
                });
            } 
        }
    });
}
function ricaricaGridInterventiVuote(){
    Ext.getCmp('wcs_interventoImpegnoList').store.removeAll();
    Ext.getCmp('wcs_interventoCronologiaList').store.removeAll();
    Ext.getCmp('wcs_interventoPagamentiList').store.removeAll();
    Ext.getCmp('wcs_interventoContribuzioniList').store.removeAll();
}

function resetAll(){
    var anagraficaTab = Ext.getCmp('wcs_anagraficaTab');
    var condizioneTab = Ext.getCmp('wcs_condizioneTab');
    var diarioTab = Ext.getCmp('wcs_diarioTab');
    var paiTab = Ext.getCmp('wcs_paiTab');
    anagraficaTab.getForm().reset();
    condizioneTab.getForm().reset();
    diarioTab.getForm().reset();
    paiTab.items.get('wcs_paiForm').getForm().reset();
    disabilitaTabCartellaSociale();
    disabilitaPulsantiCartellaSociale();
    wcs_isModified = '';
}