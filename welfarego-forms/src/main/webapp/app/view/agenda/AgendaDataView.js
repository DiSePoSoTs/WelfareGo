/**
 * LEGACY, should be safe to delete
 */
Ext.define('wf.view.agenda.AgendaDataView',{
    extend: 'Ext.DataView',
    alias: 'widget.wtl_agenda_data_view',
	 id:'wf_agenda',
    autoScroll:true,
    itemSelector: 'div',
    tpl: new Ext.XTemplate(
        '<div class="agenda_view">',
        '    <tpl for=".">',
        '        <table class="wtl_agenda_template">',
        '            <tr>',
        '                <th class="wtl_template_ore">Ore</th>',
        '                <tpl for="giorni">',
        '                    <th class="wtl_template_giorno">{desc}</th>',
        '                </tpl>',
        '            </tr>',
        '            <tpl for="ore">',
        '                <tr class="impegno_cls">',
        '                    <tpl for=".">',
        '                        <tpl if="tipo==1">',
        '                            <td class="agenda_ora_cell">{desc}</td>',
        '                        </tpl>',
        '                        <tpl if="tipo==2">',
        '                            <td class="agenda_indisponibile_cell agenda_cell" onclick="Ext.getCmp(\'wtl_agenda_detail\').setActiveRecord(\'{cod_as}\',\'{id_impegno}\',\'{tipo}\');return false;">{desc}</td>',
        '                        </tpl>',
        '                        <tpl if="tipo==3">',
        '                            <td class="agenda_appuntamento_cell agenda_cell" onclick="Ext.getCmp(\'wtl_agenda_detail\').setActiveRecord(\'{cod_as}\',\'{id_impegno}\',\'{tipo}\');return false;">{desc}</td>',
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
            log('dataView.beforeclick(%o,%o,%o,%o)',view,index,node,event);
        }
    },
	 initComponent:function(){
		 this.store=Ext.create('wf.store.AgendaStore');
		 this.callParent(arguments);
	 }
});