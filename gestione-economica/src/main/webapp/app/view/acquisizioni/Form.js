/*
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce la form di ricerca per la sezione Acquisizioni
 */


(function() {

    /*
     * Funzione helper eseguita quando il valore di uno dei campi "Periodo
     * considerato" viene modificato
     */
    function changePeriodoConsiderato(field, newValue) {
        var form = field.up('wpacquisizioniform'),
                campoCompetenzaMese = form.down('combobox[name=mese_di_competenza_mese]'),
                campoCompetenzaAnno = form.down('combobox[name=mese_di_competenza_anno]'),
                campoDalMese = form.down('combobox[name=periodo_considerato_dal_mese]'),
                campoDalAnno = form.down('combobox[name=periodo_considerato_dal_anno]'),
                campoAlMese = form.down('combobox[name=periodo_considerato_al_mese]'),
                campoAlAnno = form.down('combobox[name=periodo_considerato_al_anno]');

        if (newValue !== null) {
            // svuota i campi del "mese di competenza", rendili non più obbligatori
            campoCompetenzaMese.setValue(null).clearInvalid();
            campoCompetenzaMese.allowBlank = true;
            campoCompetenzaAnno.setValue(null).clearInvalid();
            campoCompetenzaAnno.allowBlank = true;

            // rendi obbligatori i campi del "periodo considerato"
            campoDalMese.allowBlank = false;
            campoDalAnno.allowBlank = false;
            campoAlMese.allowBlank = false;
            campoAlAnno.allowBlank = false;
        }
    }

    /*
     * Funzione helper eseguita quando il valore di uno dei campi "Mese di
     * competenza" viene modificato
     */
    function changeMeseDiCompetenza(field, newValue) {
        var form = field.up('wpacquisizioniform'),
                campoCompetenzaMese = form.down('combobox[name=mese_di_competenza_mese]'),
                campoCompetenzaAnno = form.down('combobox[name=mese_di_competenza_anno]'),
                campoDalMese = form.down('combobox[name=periodo_considerato_dal_mese]'),
                campoDalAnno = form.down('combobox[name=periodo_considerato_dal_anno]'),
                campoAlMese = form.down('combobox[name=periodo_considerato_al_mese]'),
                campoAlAnno = form.down('combobox[name=periodo_considerato_al_anno]');

        if (newValue !== null) {
            // svuota i campi del "periodo considerato", rendili non più obbligatori
            campoDalMese.setValue(null).clearInvalid();
            campoDalMese.allowBlank = true;
            campoDalAnno.setValue(null).clearInvalid();
            campoDalAnno.allowBlank = true;
            campoAlMese.setValue(null).clearInvalid();
            campoAlMese.allowBlank = true;
            campoAlAnno.setValue(null).clearInvalid();
            campoAlAnno.allowBlank = true;

            // rendi obbligatori i campi del "mese di competenza"
            campoCompetenzaMese.allowBlank = false;
            campoCompetenzaAnno.allowBlank = false;
        }
    }




    Ext.define('Wp.view.acquisizioni.Form', {

        extend: 'Ext.form.Panel',
        alias: 'widget.wpacquisizioniform',
        border: 0,
        bodyPadding: 0,
        margin: '0 0 10 0',
        fieldDefaults: {
            margin: '0 15 15 0',
            labelWidth: 150,
            blankText: 'Questo campo è obbligatorio.'
        },
        listeners: {
            beforerender: function(panel) {
                if (!panel.wfgeAlreadyShownOnce) {
                    log('initializing anno/mese');
                    panel.getForm().setValues({
                        periodo_considerato_dal_mese: Ext.getStore('Mese').first().getId(),
                        periodo_considerato_dal_anno: Ext.getStore('Anno').first().getId(),
                        periodo_considerato_al_mese: Ext.getStore('Mese').last().getId(),
                        periodo_considerato_al_anno: Ext.getStore('Anno').last().getId()
                    });
                    changePeriodoConsiderato(panel.down('combobox[name=mese_di_competenza_mese]'), 'x');
                    panel.wfgeAlreadyShownOnce = true;
                }
            }
        },
        items: [
            // prima riga
            {
                xtype: 'panel',
                border: 0,
                layout: 'column',
                items: [
                    // classe
                    {
                        xtype: 'combobox',
                        fieldLabel: 'Tipo intervento &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;classe',
                        name: 'classe_intervento',
                        id: 'classe_intervento_acquisizioni',
                        store: 'ClasseTipoInterventoAcquisizioni',
                        displayField: 'label',
                        valueField: 'id',
                        forceSelection: true,
//                allowBlank: false,
                        tabIndex: 1
                    },
                    // tipo
                    {
                        xtype: 'combobox',
                        fieldLabel: 'tipo',
                        name: 'tipo_intervento',
                        id: 'tipo_intervento_acquisizioni',
                        store: 'TipoInterventoAcquisizioni',
                        displayField: 'label',
                        valueField: 'id',
                        labelWidth: 25,
                        forceSelection: true,
                        queryMode: 'local',
                        listeners: {
                         	  beforequery: function(q) {
                                if (q.query) {
                                    var length = q.query.length;
                                    q.query = new RegExp(Ext.escapeRe(q.query),'i');
                                    q.query.length = length;
                                }
                            }
                  },
//                allowBlank: false,
                        tabIndex: 1,
                        width: 505
                    }
                ]
            },
            // seconda riga
            {
                xtype: 'panel',
                border: 0,
                layout: 'column',
                items: [
                    // mese colonna
                    {
                        xtype: 'combobox',
                        queryMode: 'local',
                        fieldLabel: 'Mese di competenza',
                        name: 'mese_di_competenza_mese',
                        store: 'Mese',
                        displayField: 'label',
                        valueField: 'id',
                        forceSelection: true,
                        allowBlank: false,
                        tabIndex: 10,
                        listeners: {
                            // quando cambia il valore del campo
                            change: changeMeseDiCompetenza
                        }
                    },
                    // anno colonna
                    {
                        xtype: 'combobox',
                        queryMode: 'local',
                        //fieldLabel: '',
                        name: 'mese_di_competenza_anno',
                        store: 'Anno',
                        displayField: 'label',
                        valueField: 'id',
                        forceSelection: true,
                        allowBlank: false,
                        tabIndex: 20,
                        listeners: {
                            // quando cambia il valore del campo
                            change: changeMeseDiCompetenza
                        }
                    }
                ]
            },
            // terza riga
            {
                xtype: 'panel',
                border: 0,
                layout: 'column',
                items: [
                    // prima colonna
                    {
                        xtype: 'combobox',
                        queryMode: 'local',
                        fieldLabel: 'Periodo considerato &nbsp;&nbsp;&nbsp; dal',
                        name: 'periodo_considerato_dal_mese',
                        store: 'Mese',
                        displayField: 'label',
                        valueField: 'id',
                        forceSelection: true,
                        allowBlank: false,
                        tabIndex: 30,
                        listeners: {
                            // quando cambia il valore del campo
                            change: changePeriodoConsiderato
                        }
                    },
                    // seconda colonna
                    {
                        xtype: 'combobox',
                        queryMode: 'local',
                        //fieldLabel: '',
                        name: 'periodo_considerato_dal_anno',
                        store: 'Anno',
                        displayField: 'label',
                        valueField: 'id',
                        forceSelection: true,
                        allowBlank: false,
                        tabIndex: 40,
                        listeners: {
                            // quando cambia il valore del campo
                            change: changePeriodoConsiderato,
                            afterRender: function () {
                                var anno_corrente = Ext.Date.format(new Date(), 'Y');
                                // Ext.ComponentQuery.query('[name=periodo_considerato_dal_anno]')[0]
                                var store_periodo_considerato_dal_anno =this.getStore();
                                var valore_di_default =store_periodo_considerato_dal_anno.findRecord('id',anno_corrente).get('label');
                                this.setValue(valore_di_default);
                            }

                        }
                    },
                    // terza colonna
                    {
                        xtype: 'combobox',
                        queryMode: 'local',
                        fieldLabel: 'al',
                        labelWidth: 20,
                        name: 'periodo_considerato_al_mese',
                        store: 'Mese',
                        displayField: 'label',
                        valueField: 'id',
                        forceSelection: true,
                        allowBlank: false,
                        tabIndex: 50,
                        listeners: {
                            // quando cambia il valore del campo
                            change: changePeriodoConsiderato
                        }
                    },
                    // quarta colonna
                    {
                        xtype: 'combobox',
                        queryMode: 'local',
                        //fieldLabel: '',
                        name: 'periodo_considerato_al_anno',
                        store: 'Anno',
                        displayField: 'label',
                        valueField: 'id',
                        forceSelection: true,
                        allowBlank: false,
                        tabIndex: 60,
                        listeners: {
                            // quando cambia il valore del campo
                            change: changePeriodoConsiderato,
                            afterRender: function () {
                                var anno_corrente = Ext.Date.format(new Date(), 'Y');
                                // Ext.ComponentQuery.query('[name=periodo_considerato_al_anno]')[0]
                                var store_periodo_considerato_dal_anno =this.getStore();
                                var valore_di_default =store_periodo_considerato_dal_anno.findRecord('id',anno_corrente).get('label');
                                this.setValue(valore_di_default);
                            }
                        }
                    }
                ]
            },
            // quarta riga
            {
                xtype: 'panel',
                border: 0,
                layout: 'column',
                items: [
                    // prima colonna
                    {
                        xtype: 'combobox',
                        fieldLabel: 'UOT/Struttura',
                        name: 'uot_struttura',
                        store: 'UotStruttura',
                        displayField: 'label',
                        valueField: 'id',
                        forceSelection: true,
                        tabIndex: 70
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: 'Cognome',
                        name: 'cognome',
                        tabIndex: 70
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: 'Nome',
                        name: 'nome',
                        tabIndex: 70
                    }
                    
                ]
            },
            // quinta riga
            {
                xtype: 'panel',
                border: 0,
                layout: 'column',
                items: [
                    // prima colonna
                    {
                        xtype: 'textfield',
                        fieldLabel: 'Delegato',
                        name: 'filtro_delegato',
                        tabIndex: 80
                    }
                ]
            }
        ], // items

        buttons: [
        {
                text: 'Cerca',
                action: 'search',
                tabIndex: 80,
                id:'searchAcquisizioni'
            }, {
                text: 'Indietro',
                action: 'back',
                tabIndex: 90
            }, {
                text: 'Importa file',
                tabIndex: 100,
                handler: function() {
                    Ext.create('Ext.window.Window', {
                        title: 'Importa file',
                        height: 120,
                        width: 500,
                        layout: 'fit',
                        modal: true,
                        items: {
                            xtype: 'form',
                            layout: 'fit',
                            bodyPadding:15,
                            items: [{
                                    xtype: 'filefield',
                                    name: 'file',
                                    label: 'File',
                                    buttonText: 'Scegli file...',
                                    allowBlank: false
                                }],
                            buttons: [{
                                    text: 'Carica',
                                    handler: function(button) {
                                        this.up('form').submit({
                                            url: '/GestioneEconomica/ImportaFileServlet',
                                            success: function(form, action) {
                                                button.up('window').close();
                                                Ext.Msg.alert('OK', action.result.message);                                                
                                            },
                                            failure: function(form, action) {     
                                                button.up('window').close();
                                                Ext.Msg.alert('ERROR', action.result.message);     
                                            }
                                        });
                                    }
                                }, {
                                    text: 'Annulla',
                                    handler: function() {
                                        this.up('window').close();
                                    }
                                }]
                        }
                    }).show();
                }
            }]// buttons
    });


})();


