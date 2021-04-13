(function() {
    var ns = Ext.ns('it.wego.utils');

    var log = ns.log = function() {
        if (console && console.debug) {
            console.debug.apply(console, arguments);
        }
    }

    ns.selectNodeContent = function(element) {
        if (element.dom)
            element = element.dom;
        var range = document.createRange();
        //        range.setStartBefore(element);
        //        range.setEndAfter(element);
        range.selectNode(element);
        window.getSelection().addRange(range);
    }

    ns.executeRemoteMethod = function(config) {
        var func = function() {
            var progressWindow = Ext.Msg.wait('operazione in corso ...');
            Ext.Ajax.request({
                url: config.url,
                params: config.params,
                timeout: config.timeout ? config.timeout : 10000,
                success: function(response) {
                    progressWindow.hide();
                    var responseJson = Ext.JSON.decode(response.responseText)
                    if (responseJson.success) {
                        if (config.showOk === true || config.showOk === undefined) {
                            Ext.Msg.alert('OK', responseJson.message);
                        }
                        if (config.success) {
                            config.success(responseJson);
                        }
                    } else {
                        Ext.Msg.alert('Errore', responseJson.message);
                    }
                },
                failure: function(response) {
                    progressWindow.hide();
                    Ext.Msg.alert('Errore', 'Si e` verificato un errore');
                }
            });
        };
        if (config.confirmTitle) {
            Ext.Msg.confirm(config.confirmTitle, config.confirmMessage,
                    function(button) {
                        if (button == 'yes') {
                            func();
                        }
                    });
        } else {
            func();
        }
    };

    ns.followRemoteProgress = function(config) {
        var progressMsg = config.progressMsg ? config.progressMsg : "Operazione in corso ...";
        var progressWindow = Ext.Msg.wait(progressMsg);
        var task = Ext.TaskManager.start({
            interval: 5000,
            run: function() {
                Ext.Ajax.request({
                    url: config.url,
                    success: function(response) {
                        var responseJson = Ext.JSON.decode(response.responseText);
                        if (responseJson.success) {
                            if (responseJson.completed) {
                                Ext.TaskManager.stop(task);
                                progressWindow.close();
                                Ext.Msg.alert('OK', responseJson.message);
                                if (config.success) {
                                    config.success(responseJson);
                                }
                            }
                        } else {
                            Ext.TaskManager.stop(task);
                            progressWindow.close();
                            Ext.Msg.alert('ERROR', responseJson.message);
                            if (config.error) {
                                config.error(responseJson);
                            }
                        }
                    }
                });
            },
            scope: this
        });
    };

    ns.showWindow = function(config) {
        config = Ext.merge({
            collapsible: true,
            minimizable: true,
            maximizable: true,
            layout: 'fit',
            autoScroll: 'true',
            width: 1000,
            height: 700,
            title: 'data',
            buttons: [{
                    text: 'Chiudi',
                    handler: function() {
                        window.hide();
                    }
                }],
            listeners: {
                afterrender: function() {
                    prettyPrint();
                }
            },
            html: config.data
        }, config);
        if (config.showSelectAllButton) {
            config.buttons.reverse();
            config.buttons.push({
                text: 'Seleziona tutto',
                handler: function() {
                    ns.selectNodeContent(Ext.get(config.textPreId));
                }
            });
            config.buttons.reverse();
        }
        var window = Ext.create('Ext.window.Window', config);
        window.show();
    };

    ns.showIframeWindow = function(config) {
        config.data = '<iframe src="' + config.data + '" width="100%" height="100%" scrolling="auto"></iframe>';
        ns.showWindow(config);
    };

    ns.showTextWindow = function(config) {
        config = Ext.merge({
            textPreId: Ext.id(),
            showSelectAllButton: true
        }, config);
        config.data = '<pre class="prettyprint lang-xml" id="' + config.textPreId + '">' + Ext.String.htmlEncode(config.data) + '</pre>';
        ns.showWindow(config);
    };

    if (Ext.data.IdGenerator) {
        var uuidGenerator = Ext.data.IdGenerator.get('uuid');
        ns.generateId = function() {
            return uuidGenerator.generate();
        };
    }

    ns.proxyExceptionListener = function(proxy, response) {
        var message = Ext.JSON.decode(response.responseText).message;
        var config = {
            title: "Errore",
            msg: "Si è verificato un errore durante il caricamento dei dati:<br/>" + message,
            buttons: Ext.Msg.OK
        };
        //        if(message.match('devi essere loggato')&&wf.utils.doLiferayLogin){
        //            config.buttons+=Ext.Msg.CANCEL;
        //            config.msg+="<br/>ricaricare la pagina?";
        //            config.fn=function(buttonId){
        //                if(buttonId=="ok")
        //                    wf.utils.doLiferayLogin();
        //            }
        //        }
        Ext.Msg.show(config);
    }

    Ext.define('it.wego.utils.MessaggioBreModel', {//definizione modello Messaggio verifica
        extend: 'Ext.data.Model',
        fields: ['subject', 'level', 'message']
    });
    Ext.define('it.wego.utils.MessaggiBreStore', {
        extend: 'Ext.data.Store',
        model: 'it.wego.utils.MessaggioBreModel',
        pageSize: 20,
        autoLoad: false
    });
    Ext.define('it.wego.utils.BreComponent', {
        extend: 'Ext.grid.Panel',
        alias: 'widget.brepanel',
        height: 150,
        margin: '10 0 5 0',
        initComponent: function() {
            var me = this;
            var id = ns.generateId();
            this.fieldDefaults = {
                title: 'Verifica automatica',
                width: '100%'
            };
            var store = this.store = Ext.create('it.wego.utils.MessaggiBreStore', {
                proxy: me.storeProxy,
                listeners: {
                    beforeload: function(store) {
                        if (me.dataLoaderFunction) {
                            log('bre pstore: invoking pre-load function :', me.dataLoaderFunction);
                            store.proxy.extraParams.data = me.dataLoaderFunction();
                        }
                        return true;
                    },
                    load: function(store) {
                        var result = "OK", style = "color:green";
                        if (store.getTotalCount() == 0) {
                            result = "la verifica automatica non ha prodotto risultati";
                            style = "color:black";
                        } else if (store.find("level", "WARN") != -1) {
                            result = "WARN";
                            style = "color:orange";
                        } else if (store.find("level", "ERROR") != -1) {
                            result = "ERROR";
                            style = "color:red";
                        }

                        Ext.getCmp(id + '_verifica_dati_esito_ver').setValue(result).setFieldStyle(style);
                    }
                }
            });
            this.tbar = ['Esito verifica automatica:', {
                    xtype: 'textfield',
                    id: id + '_verifica_dati_esito_ver',
                    //                fieldLabel: 'Esito verifica automatica',
                    name: 'esito_ver',
                    //                value: 'loading...',
                    readOnly: true
                            //                columnWidth:.7
                }, {
                    xtype: 'button',
                    text: 'ripeti verifica',
                    handler: function() {
                        Ext.getCmp(id + '_verifica_dati_esito_ver').setValue('loading...').setFieldStyle('{color:black;}');
                        store.load();
                    }
                }];
            this.columns = [{
                    id: id + '_verifica_dati_int_messaggi',
                    header: "Oggetto",
                    dataIndex: 'subject'
                }, {
                    id: id + '_verifica_dati_livello',
                    header: "Livello",
                    dataIndex: 'level',
                    renderer: function(value, metadata, record) {
                        var style = "color:black";
                        if (value == "INFO") {
                            style = "color:blue";
                        } else if (value == "WARN") {
                            style = "color:orange";
                        } else if (value == "ERROR") {
                            style = "color:red";
                        }
                        metadata.tdAttr = 'style="' + style + '"';
                        return value;
                    }
                }, {
                    id: id + '_verifica_dati_messaggio',
                    header: "Messaggio",
                    dataIndex: 'message',
                    width: 500,
                    flex: 1,
                    renderer: function(value, metadata, record) {
                        value = Ext.String.htmlEncode(value);
                        metadata.tdAttr = 'title="' + value + '"';
                        return value;
                    }
                }];
            this.callParent(arguments);
        }
    });

    Ext.define('it.wego.utils.DecimalNumberField', {
        extend: 'Ext.form.field.Number',
        alias: 'widget.wdecimalnumberfield',
        allowNegative: false,
        decimalPrecision: 2,
        decimalSeparator: ',',
        thousandSeparator: '.',
        format: '0,000.00',
        rawToValue: function(rawValue) {
            var value = this.fixPrecision(this.parseValue(rawValue));
            if (value === null) {
                value = rawValue || '';
            }
            return  value;
        },
        valueToRaw: function(value) {
            var num = this.rawToValue(value);
            if ((typeof num) !== 'number' || isNaN(num)) {
                return '';
            } else {
                return Ext.util.Format.number(value, this.format);
            }
        },
        parseValue: function(value) {
            var list = String(value).replace(/[^0-9.,]*$/, '').split(/[^0-9]/), tail = list.splice(-1).join(''), head = list.join(''), str = head === '' ? tail : (head + '.' + (tail || '0')), val = parseFloat(str);
            return isNaN(val) ? null : val;
        },
        getErrors: function(value) {
            return this.callParent([String(Ext.isDefined(value) ? this.rawToValue(value) : this.getValue())]);
        },
        onFocus: function() {
            this.setRawValue(this.getRawValue().replace(RegExp('[^0-9' + this.decimalSeparator + ']', 'g'), ''));
        },
        onBlur: function() {
            this.beforeBlur();
        },
        getSubmitValue: function() {
            var val = this.getValue();
            return String(val === undefined || val === null || isNaN(val) ? '' : val);
        }
    });

    Ext.define('it.wego.utils.EuroDecimalNumberField', {
        extend: 'it.wego.utils.DecimalNumberField',
        alias: 'widget.weuronumberfield',
        format: '0,000.00 €'
    });

})();