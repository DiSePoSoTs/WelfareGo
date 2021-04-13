
function populateDatiSpecifici(fieldset, tipoIntervento, cnt, codPai,tipOld,cntOld) {
    Ext.Ajax.request({
        url: '/CartellaSociale/componenti',
        params: {
            tipo: tipoIntervento,
            cnt: cnt,
            codPai: codPai,
            tipOld:tipOld,
            cntOld:cntOld
            
        },
        success: function(response) {
            var json = Ext.JSON.decode(response.responseText);
            if (json.success) {
                var components = json.components;
                cleanItems(fieldset);
                for (var i = 0; i < components.length; i++) {
                    var el = components[i];
                    addComponent(fieldset, el);
                }
                fieldset.doLayout();
            } else {
                Ext.MessageBox.show({
                    title: 'Esito operazione',
                    msg: json.message,
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.window.MessageBox.ERROR
                });
            }
            var civObbComponent = Ext.getCmp('wcs_ricercaCivilmenteObbligatoGrid');
            var interventiFieldSet = Ext.getCmp('wcs_impegniFieldset');
            if (civObbComponent != null) {
                interventiFieldSet.remove(civObbComponent, true);
            }
            civObbComponent = getCivilmenteObbligati(tipoIntervento);
            interventiFieldSet.add(civObbComponent);
        }
    });
}

function cleanItems(element) {
    if (element.items != undefined) {
        element.items.each(function(item) {
            element.remove(item, true);
        });
    }
}

function addComponent(container, element) {
    var components = getComponents(element);
    if (components != null) {
        for (var i = 0; i < components.length; i++) {
            container.add(components[i]);
        }
    }
}

function getComponents(el) {
    var component = null;
    if (el.tipoCampo == 'L') {
        component = getComboElement(el);
    }
    if (el.tipoCampo == 'D') {
        component = getDateElement(el);
    }
    if (el.tipoCampo == 'I') {
        component = getNumericElement(el);
    }
    if (el.tipoCampo == 'T') {
        component = getTextFieldElement(el);
    }
    if (el.tipoCampo == 'X') {
        component = getTextAreaElement(el);
    }
    return component;
}

function getComboElement(el) {
    var blank = allowBlank(el);
    var ro = isRO(el);
    var hidden = isHidden(el);
    var error = getErrorMessage(el);
    var elementArray = Ext.JSON.decode(el.valAmm);
    var componentStore = new Ext.data.Store({
        fields: ['value', 'name'],
        data: elementArray
    });
    var components = [];
    var comboComponent = new Ext.form.ComboBox({
        xtype: 'combo',
        queryMode: 'local',
        displayField: 'name',
        valueField: 'value',
        store: componentStore,
        fieldLabel: el.desCampo,
        readOnly: ro,
        forceSelection: true,
        allowBlank: blank,
        hidden: hidden,
        editable: false,
        value: el.valDef,
        name: el.codCampo,
        invalidText: error,
        blankText: error,
        listeners: {
            select: function(combo, record) {
                var name = record[0].get('name');
                hiddenComponent.setValue(name);
            }
        }
    });

    var hiddenComponent = new Ext.form.field.Hidden({
        name: el.codCampo + '_hidden',
        value: comboComponent.getRawValue()
    });

    components.push(comboComponent);
    components.push(hiddenComponent);
    return components;
}

function getDateElement(el) {
    var blank = allowBlank(el);
    var ro = isRO(el);
    var error = getErrorMessage(el);
    var hidden = isHidden(el);
    var component = {
        xtype: 'datefield',
        format: 'd/m/Y',
        readOnly: ro,
        allowBlank: blank,
        hidden: hidden,
        value: el.valDef,
        editable: false,
        fieldLabel: el.desCampo,
        name: el.codCampo,
        invalidText: error,
        blankText: error
    };
    var components = [];
    components.push(component);
    return components;
}

function getNumericElement(el) {
    var blank = allowBlank(el);
    var ro = isRO(el);
    var error = getErrorMessage(el);
    var hidden = isHidden(el);
    var isDec = el.decimali && !isNaN(el.decimali) && Number(el.decimali) > 0;
    var component = {
        xtype: isDec ? 'wdecimalnumberfield' : 'numberfield',
        hideTrigger: true,
        keyNavEnabled: false,
        mouseWheelEnabled: false,
        readOnly: ro,
        maxLength: el.lunghezza,
        allowDecimals: true,
        allowBlank: blank,
        hidden: hidden,
        value: el.valDef,
        fieldLabel: el.desCampo,
        name: el.codCampo,
        invalidText: error,
        blankText: error,
        decimalSeparator: ',',
        decimalPrecision: Number(el.decimali) || 0
    };
    var components = [];
    components.push(component);
    return components;
}

function getTextFieldElement(el) {
    var blank = allowBlank(el);
    var error = getErrorMessage(el);
    var ro = isRO(el);
    var hidden = isHidden(el);
    var component = {
        xtype: 'textfield',
        readOnly: ro,
        maxLength: el.lunghezza,
        allowBlank: blank,
        hidden: hidden,
        value: el.valDef,
        regex: el.regExpr,
        fieldLabel: el.desCampo,
        name: el.codCampo,
        invalidText: error,
        blankText: error
    };
    var components = [];
    components.push(component);
    return components;
}

function getTextAreaElement(el) {
    var blank = allowBlank(el);
    var error = getErrorMessage(el);
    var ro = isRO(el);
    var hidden = isHidden(el);
    var component = {
        xtype: 'textareafield',
        readOnly: ro,
        grow: true,
        value: el.valDef,
        maxLength: el.lunghezza,
        allowBlank: blank,
        hidden: hidden,
        fieldLabel: el.desCampo,
        name: el.codCampo,
        invalidText: error,
        blankText: error
    };
    var components = [];
    components.push(component);
    return components;
}

function getErrorMessage(el) {
    if (el.msgErrore != null) {
        return el.msgErrore;
    }
    else {
        return 'Questo campo è obbligatorio';
    }
}

function allowBlank(el) {
    if (el.flgObb == 'S') {
        return false;
    } else {
        return true;
    }
}

function isRO(el) {
    if (el.flgEdit == 'S') {
        return false;
    } else {
        return true;
    }
}

function isHidden(el) {
    if (el.flgVis == 'S') {
        return false;
    } else {
        return true;
    }
}

function getCivilmenteObbligati(tipoIntervento) {

    var codPai = Ext.getCmp('wcs_paiCodPai').getValue();
    //    //var tipoIntervento = Ext.getCmp('wcs_interventoTipo').getValue();
    var cntTipInt = Ext.getCmp('wcs_interventiFormCntTipInt').getValue();

    Ext.define('wcs.model.CivilmenteObbligatoModel', {
        extend: 'Ext.data.Model',
        fields: [
            'codPai',
            'codTipInt',
            'cntTipInt',
            'codAnag',
            'nome',
            'cognome',
            'codicefiscale',
            {
                name: 'importoMensile',
                type: 'float'
            }
        ]
    });

    var store = Ext.create('Ext.data.Store', {
        autoLoad: true,
        pageSize: 20,
        model: 'wcs.model.CivilmenteObbligatoModel',
        proxy: {
            type: 'ajax',
            url: '/CartellaSociale/civilmenteObbligato',
            extraParams: {
                codTipInt: tipoIntervento,
                codPai: codPai,
                cntTipInt: cntTipInt,
                action: 'read'
            },
            reader: {
                type: 'json',
                rootProperty: 'civilmenteObbligati',
                successProperty: 'success'
            }
        },
        sorters: [{
                property: 'cognome',
                direction: 'ASC'
            }],
        listeners: {
            load: {
                fn: function() {
                    var items = this.data.items;
                    var totale = 0;
                    for (var i = 0; i < items.length; i++) {
                        var item = items[i];
                        totale = totale + item.data.importoMensile;
                    }
                    var cmp = grid.down('pagingtoolbar');
                    if (cmp) {
                        cmp.items.get('totaleCivObbField').setValue(totale.toFixed(2));
                    }
                }
            }
        }
    });

    var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
        clicksToMoveEditor: 1,
        clicksToEdit: 1,
        saveText: 'Salva',
        cancelText: 'Annulla',
        autoCancel: false,
        listeners: {
            afteredit: {
                fn: function() {
                    var items = grid.store.data.items;
                    var totale = 0;
                    for (var i = 0; i < items.length; i++) {
                        var item = items[i];
                        totale = totale + item.data.importoMensile;
                    }
                    grid.down('pagingtoolbar').items.get('totaleCivObbField').setValue(totale.toFixed(2));
                }
            }
        }
    });

    var grid = Ext.create('Ext.grid.Panel', {
        store: store,
        loadMask: false,
        id: 'wcs_ricercaCivilmenteObbligatoGrid',
        columns: [{
                header: 'codAnag',
                dataIndex: 'codAnag',
                hidden: true
            },{
                header: 'Cognome',
                dataIndex: 'cognome',
                flex: 1
            }, {
                header: 'Nome',
                dataIndex: 'nome',
                flex: 1
            },  {
                header: 'Codice fiscale',
                dataIndex: 'codicefiscale',
                flex: 1
            }, {
                header: 'Importo mensile',
                dataIndex: 'importoMensile',
                renderer: Euro,
                editor: {
                    xtype: 'weuronumberfield',
                    allowBlank: false,
                    minValue: 0,
                    maxValue: 100000,
                    hideTrigger: true,
                    keyNavEnabled: false,
                    mouseWheelEnabled: false,
                    decimalSeparator: ',',
                    decimalPrecision: 2
                }
            }],
        height: 250,
        title: 'Compartecipazione dei familiari',
        frame: true,
        tbar: [{
                text: 'Nuovo',
                itemId: 'wcs_addCivObb',
                handler: function() {
                    rowEditing.cancelEdit();
                    Ext.widget('wcs_interventoRicercaCivilmenteObbligatoPopup').show();
                }
            }, {
                itemId: 'wcs_removeCivObb',
                text: 'Elimina',
                handler: function() {
                    var record = grid.getSelectionModel().selected.items[0];
                    var cntTipIntRow = record.data.cntTipInt;
                    var codAnagRow = record.data.codAnag;
                    var codPaiRow = record.data.codPai;
                    var codTipIntRow = record.data.codTipInt;
                    var codAnagOrigine = Ext.getCmp('wcs_anagraficaCodAna').getValue();
                    Ext.Ajax.request({
                        url: '/CartellaSociale/civilmenteObbligato',
                        params: {
                            action: 'delete',
                            cntTipInt: cntTipIntRow,
                            codAnag: codAnagRow,
                            codAnagOrigine: codAnagOrigine,
                            codPai: codPaiRow,
                            codTipInt: codTipIntRow,
                            codUte: wcs_codOperatore
                        },
                        success: function(response) {
                            var json = Ext.JSON.decode(response.responseText);
                            if (json.success) {
                                store.load();
                            } else {
                                Ext.MessageBox.show({
                                    title: 'Esito operazione',
                                    msg: json.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.window.MessageBox.ERROR
                                });
                            }
                        }
                    });
                    rowEditing.cancelEdit();
                },
                disabled: true
            }],
        plugins: [rowEditing],
        listeners: {
            'selectionchange': function(view, records) {
                grid.down('#wcs_removeCivObb').setDisabled(!records.length);
            }
        },
        dockedItems: [{
                xtype: 'pagingtoolbar',
                store: store,
                dock: 'bottom',
                displayMsg: 'Visualizzo gli importi da {0} a {1} di {2}',
                emptyMsg: 'Nessun importo compartecipazione dei familiari',
                displayInfo: true,
                items: ['-', {
                        xtype: 'weuronumberfield',
                        readOnly: true,
                        fieldLabel: 'Totale &euro;',
                        itemId: 'totaleCivObbField',
                        name: 'totale',
                        value: 'Totale...'
//                        decimalSeparator: ',',
//                        decimalPrecision: 2
                    }]
            }]
    });
    return grid;
}

