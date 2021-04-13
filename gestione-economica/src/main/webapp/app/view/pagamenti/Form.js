
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questa view definisce la form di ricerca per la sezione Pagamenti
 */

(function(){

    /*
     * Funzione helper eseguita quando il valore di uno dei campi "Periodo
     * considerato" viene modificato
     */
    function changePeriodoConsiderato(field, newValue) {
        var form = field.up('wppagamentiform'),
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
        var form = field.up('wppagamentiform'),
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

/*    
     * Funzione helper eseguita quando viene selezionato o deselezionato uno dei
     * checkbox "Stato pagamenti"
     
    function changeCheckbox(field, checked) {
        var form = field.up('wppagamentiform'),
        daGenerare = form.down('checkboxfield[name=da_generare]'),
        daEmettere = form.down('checkboxfield[name=da_emettere]'),
        emesse = form.down('checkboxfield[name=emesse]'),
        da_inviare = form.down('checkboxfield[name=da_inviare]'),
        inviate = form.down('checkboxfield[name=inviate]'),
        pagate = form.down('checkboxfield[name=pagate]'),
        checkboxes = form.query('checkboxfield'),
        atLeastOneSelected = false;

        if (checked) {
            if (field.is('checkboxfield[name=da_generare]')) {
                // deseleziona gli altri checkbox
                daEmettere.setValue(false);
                emesse.setValue(false);
                da_inviare.setValue(false);
                inviate.setValue(false);
                pagate.setValue(false);
            }
            else {
                // deseleziona il checkbox "da_generare"
                daGenerare.setValue(false);
            }

            // rendi tutti i checkbox non obbligatori e validi
            Ext.each(checkboxes, function(item) {
                item.required = false;
                item.validate();
            });
        }
        // se togli la spunta
        else {
            // verifica se c'è almeno un checkbox selezionato
            Ext.each(checkboxes, function(item){
                if (item.getValue()) {
                    atLeastOneSelected = true;
                    // ferma il ciclo
                    return false;
                }
            });

            if (!atLeastOneSelected) {
                // rendi tutti i checkbox obbligatori e invalidi
                Ext.each(checkboxes, function(item) {
                    item.required = true;
                    item.validate();
                });
            }
        }
    }*/

    Ext.define('Wp.view.pagamenti.Form' ,{
        extend: 'Ext.form.Panel',
        alias : 'widget.wppagamentiform',

        border: 0,
        bodyPadding: 0,
        margin: '0 0 10 0',

        fieldDefaults: {
            margin: '0 15 15 0',
            labelWidth: 150,
            blankText: 'Questo campo è obbligatorio.'
        },
        listeners:{
            beforerender:function(panel){
                if(!panel.wfgeAlreadyShownOnce){
                    log('initializing anno/mese');
                    panel.getForm().setValues({
                        periodo_considerato_dal_mese:Ext.getStore('Mese').first().getId(),
                        periodo_considerato_dal_anno:Ext.getStore('Anno').first().getId(),
                        periodo_considerato_al_mese:Ext.getStore('Mese').last().getId(),
                        periodo_considerato_al_anno:Ext.getStore('Anno').last().getId(),
                        stato_pagamenti:Ext.getStore('StatiPagamenti').first().getId()
                        //da_generare:true
                    });
                    changePeriodoConsiderato(panel.down('combobox[name=mese_di_competenza_mese]'), 'x');
                    panel.wfgeAlreadyShownOnce=true;
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
                id: 'classe_intervento_pagamenti',
                store: 'ClasseTipoInterventoPagamenti',
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
                id: 'tipo_intervento_pagamenti',
                store: 'TipoInterventoPagamenti',
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
                width:505
            }
            ]
        },
        // seconda riga
        {
            xtype: 'panel',
            border: 0,
            layout: 'column',
            items: [
            // prima colonna
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
            // seconda colonna
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
            }, {
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
           /* items: [
            // prima colonna
            {
                xtype: 'fieldcontainer',
                fieldLabel: 'Stato liquidazioni',
                layout: 'column',
                defaultType: 'checkboxfield',
                width: 800,
                defaults: {
                    margin: '0 15 0 0',
                    listeners: {
                        change: changeCheckbox
                    }
                    ,
                    required: true,
                    validate: function() {
                        if (this.required && !this.getValue()) {
                            this.markInvalid('Selezionare almeno una opzione');
                            return false;
                        }
                        else {
                            this.clearInvalid();
                            return true;
                        }
                    }
                },
                items: [
                {
                    boxLabel: 'DA GENERARE',
                    name: 'da_generare',
                    tabIndex: 73
                },
                {
                    boxLabel: 'Da emettere',
                    name: 'da_emettere',
                    tabIndex: 74
                },
                {
                    boxLabel: 'Emessi',
                    name: 'emesse',
                    tabIndex: 75
                },
                {
                    boxLabel: 'Da inviare',
                    name: 'da_inviare',
                    tabIndex: 76
                },
                {
                    boxLabel: 'Inviati',
                    name: 'inviate',
                    tabIndex: 77
                },
                {
                    boxLabel: 'Liquidati',
                    name: 'pagate',
                    tabIndex: 78
                }
                ]
            }
            ]*/
            items: [
                    // prima colonna
                    {
                        xtype: 'combobox',
                        queryMode: 'local',
                        fieldLabel: 'Stato liquidazioni',
                        name: 'stato_pagamenti',
                        store: 'StatiPagamenti',
                        displayField: 'label',
                        valueField: 'id',
                        forceSelection: true,
                        allowBlank:false,
                        tabIndex: 73
                    }
                    ]
        },
        // sesta riga
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
        ],// items

        buttons: [
        {
            text: 'Cerca',
            action: 'search',
            tabIndex: 80
        },
        {
            text: 'Indietro',
            action: 'back',
            tabIndex: 90
        //            },
        //            {
        //                text: 'Nuovo pagamento',
        //                action: 'nuovoPagamentoVuoto',
        //                tabIndex: 100
        },
        {
            text: 'Scarica file generato',
            tabIndex: 110,
            id:'scaricaFileGeneratoPagamentiButton',
            disabled:true
        }
        ]// buttons
    });

})();