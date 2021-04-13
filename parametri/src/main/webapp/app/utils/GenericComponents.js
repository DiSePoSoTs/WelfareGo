Ext.define('wp.FilterField', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.filter_field',
    
    initComponent : function() {
        wp.FilterField.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e) {
            if(e.getKey() == e.ESC) {
                this.onTrigger1Click();
            }
        }, this);
        this.on('render', function (f) {
            f.el.on('keydown',
                function (e) {
                    if (e.keyCode != 116 &&  e.keyCode != 27 && e.keyCode != 17 && e.keyCode != 16 && e.keyCode != 9 && e.keyCode != 18 && e.keyCode != 20 && (e.keyCode > 40 || e.keyCode < 33)) {
                        this.fField.onTrigger2Click();
                    }
                },
                this,
                {
                    buffer: 350
                }
                );
        },
        {
            fField : this
        }
        );
    },

    validationEvent:false,
    validateOnBlur:false,
    trigger1Cls:'x-form-clear-trigger',
    trigger2Cls:'x-form-search-trigger',
    hideTrigger1:false,
    hideTrigger2:false,
    width:180,
    hasSearch : false,
    paramWhat : 'paramWhat',
    paramBy : 'paramBy',
    disableKeyFilter : true,
    searchByFieldId : '',
    
    onTrigger1Click : function() {
        //        this.triggerEl.elements[0].show();
        this.reset();
        this.filter('');
    },

    onTrigger2Click : function() {
        //        this.triggerEl.elements[0].hide();
        this.filter(this.getValue());
    },

    filter : function (text) {
        this.getStore().proxy.extraParams[this.paramWhat] = text;
        searchByField = null;
        if(this.searchByFieldId !=null){
            searchByField = Ext.getCmp(this.searchByFieldId);
        }
        
        if(this.searchByFieldId !=null && searchByField!=null){
            searchValue = searchByField.getValue();
            this.getStore().proxy.extraParams[this.paramBy] = searchValue;
        }
        this.getStore().load();
    },

    getGrid : function() {
        return this.up('gridpanel');
    },

    getStore : function() {
        return this.getGrid().getStore();
    }
});
