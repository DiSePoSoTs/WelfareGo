Ext.define('wcs.model.FamigliaModel', {
    extend: 'Ext.data.Model',
    fields: [
    'nome',
    'statoCivile',
    'statoNascita',
    'statoResidenza',
    'viaResidenza',
    'viaResidenzaDes',
    'dataMorte',
    'cognome',
    'codiceFiscale',
    'comuneNascita',
    'comuneNascitaDes',
    'provinciaResidenza',
    'provinciaResidenzaDes',
    'civicoResidenza',
    'civicoResidenzaDes',
    'sesso',
    'dataNascita',
    'nazionalita',
    'cittadinanza',
    'attivitaLavoroStudio',
    'comuneResidenza',
    'comuneResidenzaDes',
    'codAnaFamigliare',
    'codAnaComunale',
    'posizioneAnagrafica',
    'codQual',
    'desQual',
    'codAnag',
    'redditoFamiliare',
    'dataAggiornamentoRedditoFamiliare',
    'statoNascitaDes',
    'statoResidenzaDes',
    'flgPai',
    'pai',
    {
      name    : 'cognomeNome', 
       convert : function (v, rec) {
       return rec.get('cognome') + ' ' + rec.get('nome');
        }
    },
	'costo_famigliare', //aggiunto per poter editare il costo (inserimento in struttura) direttamente nella tabella
	'selezionato', //aggiunto per poter gestire l'associazione dei famigliare in maniera pratica (inserimento in struttura - causa selezione grafica, selezione della checkbox, non affidabile)
	'tipo' //aggiunto per capire la tipologia del famigliare
    ]
});