Ext.define('wcs.view.common.DatiBancariFieldSet',{
    extend: 'Ext.form.FieldSet',
    alias: 'widget.wcs_datibancarifieldset',
    collapsible: true,
    defaultType: 'textfield',
    layout: 'anchor',
    //    allowBlank:false,
    //    collapsible: true,
    //    defaultType: 'textfield',
    defaults: {
        anchor: '100%'
    },
    //    layout: 'anchor',
    initComponent: function() {
        var fieldSet=this;
        var calculateIBAN=function(){
            var form=fieldSet.up('form'),values=form.getValues();
            var codNazione = values['ibanCodNazione'].toUpperCase();
            var cin = values['CIN'].toUpperCase();
            var cab = values['CAB'];
            var abi = values['ABI'];
            var contr = values['codiceControlloIban'];
            var cc = values['contoCorrente'];
            while (cc.length < 12) {
                cc = '0'+cc;
            }
            var ibanValue = codNazione + contr + cin + abi + cab + cc;
            form.getForm().setValues({
                'IBAN':ibanValue.toUpperCase()
            });
        };
        
        this.items=[{
            xtype: 'container',
            anchor: '98%',
            layout:'column',
            items: [{
                xtype: 'container',
                columnWidth:.5,
                layout: 'anchor',
                items: [{
                    xtype:'textfield',
                    fieldLabel: 'Cod. Nazione',
                    //                    readOnly: wcs_condizioneCodNazioneRO,
                    //                    id: 'wcs_condizioneCodNazione',
                    minLength: 2,
                    //                    tabIndex: 16,
                    minLengthText: 'La lunghezza del campo è 2',
                    maxLength: 2,
                    name: 'ibanCodNazione',
                    anchor:'97%'
                }, {
                    xtype:'textfield',
                    //                    id: 'wcs_condizioneCIN',
                    //                    tabIndex: 18,
                    //                    readOnly: wcs_condizioneCINRO,
                    fieldLabel: 'CIN',
                    minLength: 1,
                    minLengthText: 'La lunghezza del campo è 1',
                    maxLength: 1,
                    name: 'CIN',
                    anchor:'97%'
                }, {
                    xtype:'textfield',
                    //                    tabIndex: 20,
                    //                    id: 'wcs_condizioneCAB',
                    //                    readOnly: wcs_condizioneCABRO,
                    fieldLabel: 'CAB',
                    minLength: 5,
                    minLengthText: 'La lunghezza del campo è 5',
                    maxLength: 5,
                    name: 'CAB',
                    vtype: 'FiveNums',
                    anchor:'97%'
                }, {
                    xtype:'textfield',
                    //                    id: 'wcs_condizioneIBAN',
                    //                    tabIndex: 22,
                    fieldLabel: 'IBAN',
                    //minLength: 27,
                    //minLengthText: 'La lunghezza minima del campo è 27',
                    //maxLength: 27,
                    //                    readOnly: wcs_condizioneIBANRO,
                    name: 'IBAN',
                    id:'iban_delegato',
                    anchor:'97%'
                }]
            }, {
                xtype: 'container',
                columnWidth:.5,
                layout: 'anchor',
                items: [
                {
                    xtype: 'textfield',
                    //                    id: 'wcs_condizioneContr',
                    fieldLabel: 'Codice controllo',
                    //                    tabIndex: 17,
                    minLength: 2,
                    minLengthText: 'La lunghezza del campo è 2',
                    maxLength: 2,
                    //                    readOnly: wcs_condizioneContrRO,
                    name: 'codiceControlloIban',
                    vtype: 'TwoNums',
                    anchor:'97%'
                }, {
                    xtype:'textfield',
                    //                    id: 'wcs_condizioneABI',
                    //                    readOnly: wcs_condizioneABIRO,
                    fieldLabel: 'ABI',
                    //                    tabIndex: 19,
                    minLength: 5,
                    minLengthText: 'La lunghezza del campo è 5',
                    maxLength: 5,
                    name: 'ABI',
                    vtype: 'FiveNums',
                    anchor:'97%'
                }, {
                    xtype:'numberfield',
                    hideTrigger: true,
                    keyNavEnabled: false,
                    mouseWheelEnabled: false,
                    //                    tabIndex: 21,
                    //                    id: 'wcs_condizioneContoCorrente',
                    maxLength: 12,
                    maxLengthText: 'La lunghezza massima del campo è 12',
                    //                    readOnly: wcs_condizioneCCorrenteRO,
                    fieldLabel: 'Conto corrente',
                    name: 'contoCorrente',
                    anchor:'97%'
                }, {
                    xtype:'button',
                    text: 'Calcola IBAN',
                    //                    tabIndex: 22,
                    //                    disabled: wcs_condizioneIBANButtonHidden,
                    //                    hidden: wcs_condizioneIBANButtonHidden,
                    handler: calculateIBAN
                }
                ]
            }]
        }];
        this.callParent(arguments);
    }
});