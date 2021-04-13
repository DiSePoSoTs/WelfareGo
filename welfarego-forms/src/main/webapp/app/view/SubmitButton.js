Ext.define('wf.view.SubmitButton', {
    extend: 'Ext.button.Button',
    alias: 'widget.wf_submit_button',
    config: {
        text: 'Procedi',
        id: 'wf_generic_submit_button',
        success: function(message) {

            debugger;
            log('2: ' + message);
            Ext.Msg.show({
                title: 'Info',
                msg: message,
                buttons: Ext.Msg.OK,
                fn: function() {
                    if (wfg.utils.scrollToTasklist)
                        wfg.utils.scrollToTasklist();
                }
            });
        },

        handler: function() {
            var formPanel = this.up('form'),
                form = formPanel.getForm();
            var successHandler = this.success;

            if (form.isValid()) {

                form.submit({
                    url: wf.config.path.form,
                    params: {
                        task_id: wf.config.taskId,
                        action: "PROCEED",
                        data: Ext.JSON.encode(form.getValues())
                    },
                    waitMsg: 'Completamento in corso...',
                    success: function(form, submit) {
                        debugger;
                        form.getFields().each(function(field) {
                            field.setReadOnly(true);
                        });
                        if (wfg.removeTaskFromList)
                            wfg.removeTaskFromList(wf.config.taskId);
                        Ext.each(['wa_generic_submit_button', 'wa_generic_save_button', 'wa_generic_reset_button', 'ns_generic_submit_button', 'ns_generic_save_button', 'ns_generic_reset_button', 'wf_generic_submit_button', 'wf_generic_save_button', 'wf_generic_reset_button'], function(id) {
                            var cmp = Ext.getCmp(id);
                            if (cmp)
                                cmp.disable();
                        });
                        if (successHandler) {
                            successHandler(submit.result.message, form);
                        }
                    },
                    failure: function(form, submit) {

                        debugger;
                        var msg = (submit && submit.result && submit.result.message) ? submit.result.message : 'errore generico';
                        msg = msg.replace(/ [^ ].*Exception: /, " ");
                        Ext.Msg.show({
                            title: 'Errore',
                            msg: 'Si &egrave verificato un errore durante il completamento dell\'operazione:<br/>' + msg,
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
    constructor: function(config) {
        this.initConfig(config);
        this.callParent(arguments);
    }
});