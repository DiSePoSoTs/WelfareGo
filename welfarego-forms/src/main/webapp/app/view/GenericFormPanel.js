Ext.require('wf.view.SubmitButton');
Ext.define('wf.view.GenericFormPanel', {
    extend: 'Ext.form.Panel',
    frame: true,
    layout: 'anchor',
    bodyStyle: 'padding:5px 5px 0',
    setFormName: function(name) {
        log('setting form name : ' + name);
        this.wf_formName = name;
    },

    getFormName: function() {
        return this.wf_formName;
    },

    load: function(config) {

        var name = this.getFormName();
        var form = this.getForm();

        if (!name){
            name = this.self.getName().replace(/^.*[.]([^.]+)[.][^.]+$/, '$1');
        }

        var data = wf.data.forms[name];

        if (data) {
            delete wf.data.forms[name];

            log('GenericFormPanel loading static record into form : ' + name, form);
            form.loadRecord(data);

            if (config && config.success)
                config.success(form, {});
        } else {
            log('GenericFormPanel loading ajax into form : ' + name);
            form.load({
                url: wf.config.path.form,
                params: {
                    action: "LOAD",
                    task_id: wf.config.taskId
                },
                failure: function(form, submit) {
                    Ext.Msg.show({
                        title: 'Errore caricamento dati',
                        msg: 'Si &egrave verificato un errore durante il caricamento dei dati:<br/>' + submit.result.message,
                        buttons: Ext.Msg.OK
                    });
                }
            });
        }
    },

    config: {
        listeners: {
            afterrender: function(component) {
                var el = component.getEl();
                if (this.autoScroll && el.parent().id === "wf_azione_panel") {
                    log('scrolling window to azione component');
                    window.scrollTo(el.getX() - 75, el.getY() - 75);
                }
            }
        },
        autoScroll: true
    },
    constructor: function(config) {
        this.initConfig(config);
        this.fieldDefaults = {
            msgTarget: 'side',
            labelAlign: 'right',
            labelWidth: 100,
            anchor: '100%'
            //            labelStyle: 'padding-left:10px'
        };
        this.callParent(arguments);

    },
    saveButton: {
        xtype: 'button',
        text: 'Salva',
        id: 'wa_generic_save_button',
        formBind: true,
        handler: function() {
            formPanel = this.up('form');
            form = formPanel.getForm();
            if (form.isValid()) {
                form.submit({
                    url: wf.config.path.form,
                    params: {
                        task_id: wf.config.taskId,
                        action: "SAVE",
                        data: Ext.JSON.encode(form.getValues())
                    },
                    waitMsg: 'Completamento in corso...',
                    success: function(form, submit) {
                        Ext.Msg.show({
                            title: 'Info',
                            msg: submit.result.message,
                            buttons: Ext.Msg.OK
                        });
                    },
                    failure: function(form, submit) {
                        Ext.Msg.show({
                            title: 'Errore',
                            msg: 'Si &egrave verificato un errore durante il salvataggio dei dati:<br/>' + submit.result.message,
                            buttons: Ext.Msg.OK
                        });
                    }
                });
            } else {
                Ext.Msg.show({
                    title: "errore",
                    msg: "dati inseriti non validi",
                    buttons: Ext.Msg.OK
                });
            }
        }
    },
    resetButton: {
        xtype: 'button',
        text: 'Reset',
        id: 'wa_generic_reset_button',
        formBind: true,
        handler: function() {
            this.up('form').getForm().reset();
        }
    },

    submitButton: {
        xtype: 'wf_submit_button'
    }

});