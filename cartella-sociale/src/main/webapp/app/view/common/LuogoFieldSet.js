Ext.define('wcs.view.common.luogofieldset.LuogoFieldCombo', {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.wcs_luogofieldset_luogofieldcombo',
    typeAhead: true,
    minChars: 3,
    hideTrigger: true,
    queryMode: 'local',
    valueField: 'value'
});

Ext.define('wcs.view.common.luogofieldset.BaseComboModel', {
    extend: 'Ext.data.Model',
    fields: ['text', 'value', 'cod_com', 'cod_prov', 'cod_stato', 'cod_via', 'cod_civ']
});

Ext.define('wcs.view.common.luogofieldset.LuogoFieldStore', {
    extend: 'Ext.data.Store',
    model: 'wcs.view.common.luogofieldset.BaseComboModel'
});

Ext.define('wcs.view.common.LuogoFieldSet', {
    extend: 'Ext.form.FieldSet',
    alias: 'widget.wcs_luogofieldset',
    collapsible: true,
    layout: 'column',
    title: 'Luogo',
    defaults: {
        columnWidth: 0.5,
        layout: 'anchor',
        border: false
    },
    initComponent: function() {
        var log = function() {
            if (window.console && console.debug) {
                console.debug.apply(console, arguments)
            }
        };
        var tabIndex = this.tabIndex || 100;
        var prefix = this.namePrefix || 'luogo';
        var extraLabel = this.extraLabel ? (' ' + this.extraLabel) : '';
        var baseId = this.id || ('wcs_' + prefix + '_fieldset');
        var fieldDefaults = this.fieldDefaults || {};
        function getStore(name, emptyData) {
            return Ext.create('wcs.view.common.luogofieldset.LuogoFieldStore', {data: emptyData ? [] : wcs.data['combo_' + name]});
        }
        function getComboIdByBaseName(name) {
            return baseId + '_' + name + '_combo'
        }
        function getComboByBaseName(name) {
            return Ext.getCmp(getComboIdByBaseName(name));
        }
        function buildComboConfig(config) {
            return Ext.merge({}, fieldDefaults, {
                xtype: 'wcs_luogofieldset_luogofieldcombo',
                id: getComboIdByBaseName(config.name),
                fieldLabel: config.label + extraLabel,
                name: prefix + '_' + config.name,
//                store: getStore(config.name, config.preloadCombo ? false : true),
                store: getStore(config.name, false),
                anchor: '100%',
                tabIndex: ++tabIndex,
                listeners: {
                    change: function(combo) {
                        var value = combo.getValue(), raw = combo.getRawValue();
                        if (value) {
//                            if (value == raw) {
                            var record = (value == raw) ? null : this.store.findRecord('value', value);
//                            }
                            if (!record) {
                                log('raw value in ', config.name, ' : ', value);
                                emptyStores(config.nextFields || []);
//                                if (config.onrawvalue) {
//                                    config.onrawvalue(combo, raw);
//                                }
                            } else {
                                log('combo value in ', config.name, ' : ', value);
//                                if (config.onselectvalue) {
//                                    config.onselectvalue(combo, value);
//                                }
                                filterStores(fieldKeysByName[config.name], value, config.nextFields || []);
                                fillStores(record.data, config.previousFields || []);
                            }
                        }
                    }
                }
            }, config.extra || {});
        }
        function emptyStores(stores) {
            Ext.each(stores, function(name) {
                log('clearing store ', name);
                var combo = getComboByBaseName(name), store = combo.store;
                if (store.count()) {
                    store.removeAll();
                }
            });
        }
        function fillStores(data, stores) {
            log('filling stores for data ', data);
            Ext.each(stores, function(name) {
                log('filling store ', name);
                var combo = getComboByBaseName(name), store = combo.store, record = store.findRecord('value', data[fieldKeysByName[name]]);
                log('record ', record);
                combo.setValue(record.data.value);
            });
        }
        function filterStores(field, value, stores) {
            filterStoresWithFun(function(record) {
                return record[field] == value;
            }, stores);
        }
        function filterStoresWithFun(fun, stores) {
            Ext.each(stores, function(name) {
                log('filtering store ', name);
                var combo = getComboByBaseName(name), store = combo.store;
                store.removeAll();
                var data = [];
                Ext.each(wcs.data['combo_' + name], function(record) {
                    if (fun(record)) {
                        data.push(record);
                    }
                });
                store.loadData(data);
                log('new data : ', data);
                var value = combo.getValue(), raw = combo.getRawValue();
                if (data.length == 0) {
                    combo.forceSelection = false;
                    if (value != raw) {
                        combo.setRawValue('');
                        combo.getValue();
                        combo.setRawValue(raw);
                    }
                } else {
                    combo.forceSelection = true;
                    var value = combo.getValue(), record = value ? store.findExact('value', value) : -1;//, value = combo.getValue();
                    if (record === -1) {
                        log('reset field ', name, ' with value ', value);
                        combo.reset();
                    }
                }
//                combo.reset();
            });
        }

        this.resetLuogoFieldSet = function() {
//            filterStoresWithFun(function() {
//                return false;
//            }, ['provincia', 'comune', 'via', 'civico']);
//            filterStoresWithFun(function() {
//                return true;
//            }, ['stato']);
            filterStoresWithFun(function() {
                return true;
            }, ['stato', 'provincia', 'comune', 'via', 'civico']);
        };
//        this.initLuogoFieldSet = function() {
//            Ext.each(['stato', 'provincia', 'comune', 'via', 'civico'], function(name) {
//                var combo = getComboByBaseName(name);
//                combo.fireEvent('change', combo);
//            });
//        };
        var fieldKeysByName = {
            'stato': 'cod_stato',
            'provincia': 'cod_prov',
            'comune': 'cod_com',
            'via': 'cod_via',
            'civico': 'cod_civ'
        };

        var statoCombo = buildComboConfig({
            name: 'stato',
            label: 'Stato',
            nextFields: ['provincia', 'comune', 'via', 'civico'],
            preloadCombo: true,
            extra: {
                forceSelection: true
            }
        }),
        provinciaCombo = buildComboConfig({
            name: 'provincia',
            label: 'Provincia',
            previousFields: ['stato'],
            nextFields: ['comune', 'via', 'civico']
        }),
        comuneCombo = buildComboConfig({
            name: 'comune',
            label: 'Comune',
            previousFields: ['stato', 'provincia'],
            nextFields: ['via', 'civico']
        }),
        viaCombo = buildComboConfig({
            name: 'via',
            label: 'Via',
            previousFields: ['stato', 'provincia', 'comune'],
            nextFields: ['civico']
        }),
        capField = {
            xtype: 'textfield',
            fieldLabel: 'CAP' + extraLabel,
            name: prefix + '_cap',
            tabIndex: ++tabIndex
        },
        civicoCombo = buildComboConfig({
            name: 'civico',
            label: 'Civico',
            previousFields: ['stato', 'provincia', 'comune', 'via'],
            extra: {
                minChars: 1
            }});

        this.items = [{
                xtype: 'panel',
                padding: '5 10 5 5',
                items: [
                    statoCombo,
                    comuneCombo,
                    capField
                ]
            }, {
                xtype: 'panel',
                padding: '5 5 10 5',
                items: [provinciaCombo,
                    viaCombo,
                    civicoCombo]
            }];
        this.callParent(arguments);
    }
});