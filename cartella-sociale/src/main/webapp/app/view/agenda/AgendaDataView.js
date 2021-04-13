Ext.define('wcs.view.agenda.AgendaDataView',{
    extend: 'Ext.DataView',
    alias: 'widget.wcs_agenda_data_view',
    autoScroll:true,
    itemSelector: 'div',
    tpl: new Ext.XTemplate(
        '<div class="agenda_view">',
        '    <tpl for=".">',
        '        <table class="wcs_agenda_template">',
        '            <tr>',
        '                <th class="wcs_template_ore">Ore</th>',
        '                <tpl for="giorni">',
        '                    <th class="wcs_template_giorno">{desc}</th>',
        '                </tpl>',
        '            </tr>',
        '            <tpl for="ore">',
        '                <tr class="impegno_cls">',
        '                    <tpl for=".">',
        '                        <tpl if="tipo==1">',
        '                            <td class="agenda_ora_cell">{desc}</td>',
        '                        </tpl>',
        '                        <tpl if="tipo==2">',
        '                            <td class="agenda_indisponibile_cell agenda_cell" onclick="Ext.getCmp(\'wcs_agenda_detail\').setActiveRecord(\'{idImpegno}\',\'{tipo}\');return false;">{desc}</td>',
        '                        </tpl>',
        '                        <tpl if="tipo==3">',
        '                            <td class="agenda_appuntamento_cell agenda_cell" onclick="Ext.getCmp(\'wcs_agenda_detail\').setActiveRecord(\'{idImpegno}\',\'{tipo}\');return false;">{desc}</td>',
        '                        </tpl>',
        '                        <tpl if="tipo==4">',
        '                            <td class="agenda_libero_cell agenda_cell">{desc}</td>',
        '                        </tpl>',
        '                    </tpl>',
        '                </tr>',
        '            </tpl>',
        '        </table>',
        '    </tpl>',
        '</div>'
        ),
       
    getStore:function(){
        return this.store;
    },
    listeners: {
        click: function(view,index,node,event){
            alert("click");
        //            if( window.console ) console.log('dataView.click(%o,%o,%o,%o)',view,index,node,event);
        },
        beforeclick: function(view,index,node,event){
            if( window.log ) log('dataView.beforeclick(%o,%o,%o,%o)',view,index,node,event);
        }
    },
    initComponent: function() {
    	
        this.store = Ext.create('wcs.store.AgendaStore');
        var codAnag = Ext.getCmp('wcs_anagraficaCodAna').getValue();
        var asCombo = Ext.getCmp('wcs_appuntamentoAssistenteSociale');
        
        var codAs= null;
        if(asCombo!=null && asCombo instanceof Ext.form.field.ComboBox){
        codAs = asCombo.getValue();
        }
        else {
        	codAs=Ext.getCmp('wcs_anagraficaAssistenteSocialeValue').getValue();
        }
        this.store.proxy.extraParams = {
            codAnag: codAnag,
            codAs:codAs,
            action: 'load'
        };
        this.callParent(arguments);
    }
});