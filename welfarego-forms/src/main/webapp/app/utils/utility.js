function createJSONFromImpegniStore(store){
    var items = store.data.items;
    var json = [];
    for (var i=0; i<items.length; i++){
        var item = items[i];
        json.push({
            aCarico: item.data.aCarico,
            anno: item.data.anno,
            capitolo: item.data.capitolo,
            centroElementareDiCosto: item.data.centroElementareDiCosto,
            id: item.data.id,
            impegno: item.data.impegno,
            importoDisponibile: item.data.importoDisponibile,
            importoComplessivo: item.data.importoComplessivo
        });
    }
    return Ext.JSON.encode(json);
}
