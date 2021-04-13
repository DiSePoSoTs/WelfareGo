(function(){
    
    var fields=[
    'nome',
    'cognome',
    {name:'dtApePai',type:'date',dateFormat:'d/m/Y'},
    {name:'dtChiusPai',type:'date',dateFormat:'d/m/Y'},
    'protocollo',
    'dataProtocollo',
    'certificatoL104',
    'provvedimentoGiudiziario',
    'numeroFigli',
    'numeroNucleoFamigliare',
    'numeroFigliConviventi',
    'diagnosiInCaricoSA',
    'diagnosiInCaricoS',
    'diagnosiInCaricoV',
    'diagnosiInCaricoP',
    'diagnosiInCaricoA',
    'diagnosiInCaricoAltro',
    'dataDiagnosi',
    'motivo',
    'codPai',
    'codAnag',
    'codUot',
    'statoPai',
    'isee',
    'isee2',
    'isee3',
    'fasciaDiReddito',
    'dataScadenzaIsee',
    'dtCambioFascia',
    'numeroADL',
    'affettoDaDemenza',
    'habitat'
    ];
    
    Ext.getStore('macroProblematicheStore').each(function(record){
        var macroProbCode=record.data.code;
        fields.push('macroProblematiche_'+macroProbCode,
            'rilevanza_'+macroProbCode,
            'fronteggiamento_'+macroProbCode,
            'obiettivoPrevalente_'+macroProbCode,
            'dettaglioNote_'+macroProbCode)
    });
        
    Ext.getStore('microProblematicheStore').each(function(record){
        fields.push('microProblematiche_'+record.data.param+'_'+record.data.code)
    });

    Ext.define('wcs.model.PaiModel', {
        extend: 'Ext.data.Model',
        fields: fields
    });

})();