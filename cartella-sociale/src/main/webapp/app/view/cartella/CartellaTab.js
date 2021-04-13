Ext.require('wcs.view.anagrafica.AnagraficaSocForm');

Ext.define('wcs.view.cartella.CartellaTab',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.wcs_cartellatab',
    layout: 'fit',
    id: 'wcs_cartellaTabPanel',

    initComponent: function() {
              
        var referentiTab = Ext.create('Ext.form.Panel', {
            title: 'Referenti',
            id: 'wcs_referentiTab',
            disabled: true,
            items: [
            {
                itemId: 'wcs_cartellaReferentiGrid',
                id: 'wcs_cartellaReferentiGrid',
                xtype: 'wcs_referentilist',
                height: 200,
                flex: 1,
                listeners: {
                    selectionchange: function(selModel, selected) {
                        referentiTab.child('#wcs_referentiForm').setActiveRecord(selected[0] || null);
                    }
                }
            },{
                xtype: 'wcs_referentiform',
                itemId: 'wcs_referentiForm'
            }]
        });

        var famigliaTab = Ext.create('Ext.form.Panel', {
            title: 'Famiglia',
            id: 'wcs_famigliaTab',
            disabled: true,
            items: [
            {
                itemId: 'wcs_famigliaSocialeGrid',
                id : 'wcs_famigliaSocialeGrid',
                xtype: 'wcs_famigliasocialelist',
                autoHeight:true,
                listeners: {
                    selectionchange: function(selModel, selected) {
                        famigliaTab.child('#wcs_famigliaSocialeForm').setActiveRecord(selected[0] || null);
                    },
                    itemclick: function(){
                        Ext.getCmp('wcs_famigliaSocialeRemoveButton').setDisabled(false);
                    }
                }
            },
            {
                itemId: 'wcs_famigliaSocialeForm',
                xtype: 'wcs_famigliasocialeform'
            },
            {
                itemId: 'wcs_famigliaAnagraficaGrid',
                xtype: 'wcs_famigliaanagraficalist',
                autoHeight:true,
                listeners: {
                    selectionchange: function(selModel, selected) {
                        famigliaTab.child('#wcs_famigliaAnagraficaForm').setActiveRecord(selected[0] || null);
                    }
                }
            },
            {
                itemId: 'wcs_famigliaAnagraficaForm',
                xtype: 'wcs_famigliaanagraficaform'
            }
            ]
        });

        var paiTab = Ext.create('Ext.form.Panel', {
            title: 'PAI',
            id: 'wcs_paiTab',
            name: 'wcs_paiTab',
            itemId: 'wcs_paiTab',
            disabled: true,
            autoScroll: false,
            items: [{
                itemId: 'wcs_paiList',
                xtype: 'wcs_pailist',
                height: 200,
                listeners: {
                    selectionchange: function(selModel, selected) {

                        log('pai selection changed, selected : ',selected);
                        var interventiFieldSet = Ext.getCmp('wcs_paiTab').items.get("wcs_paiForm").items.get('interventiFieldset');
                        var paiInterventiStore = interventiFieldSet.items.get('wcs_paiInterventiList').store;
						
                        var interventiSocialCrtFieldSet = Ext.getCmp('wcs_paiTab').items.get("wcs_paiForm").items.get('interventiSocialCrtFieldset');
                        var paiInterventiSocialCrtStore = interventiSocialCrtFieldSet.items.get('wcs_paiInterventiListSocialCrt').store;
						
						var liberatoriaSocialCrtFieldSet = Ext.getCmp('wcs_paiTab').items.get("wcs_paiForm").items.get('liberatoriaSocialCrtFieldset');
                        var liberatoriaSocialCrtStore = liberatoriaSocialCrtFieldSet.items.get('wcs_liberatoriaSocialCrt').store;
                        
						var paiCronologiaStore = interventiFieldSet.items.get('wcs_paiCronologiaList').store;
						
                        var paiDocumentiStore = interventiFieldSet.items.get('wcs_paiDocumentiList').store;
						
                        var record=selected[0];
                        if(!record){
							/*Se non ho nessun Pai, svuoto tutti gli store
							 * patch DOTCOM 2017/10/24
							 */
							paiInterventiStore.removeAll();
							paiInterventiSocialCrtStore.removeAll();
							liberatoriaSocialCrtStore.removeAll();
							paiCronologiaStore.removeAll();
							paiDocumentiStore.removeAll();

                            return;
                        }
                        paiTab.child('#wcs_paiForm').setActiveRecord(record);
                        
                        var dataChiusura = record.data.dtChiusPai;

                        var codPai = record.get('codPai');
                        paiInterventiStore.proxy.extraParams = {
                            action: 'read',
                            codPai: codPai
                        }
                        paiInterventiStore.load();
                        
                        paiInterventiSocialCrtStore.proxy.extraParams = {
                            action: 'readSocialCrt',
                            codPai: codPai
                        }
                        paiInterventiSocialCrtStore.load();

                        var codAnag = record.get('codAnag');
                        liberatoriaSocialCrtStore.proxy.extraParams = {
                            codAna: codAnag
                        }
                        liberatoriaSocialCrtStore.load();

                        paiCronologiaStore.proxy.extraParams = {
                            action: 'read',
                            type: 'pai',
                            codPai: codPai
                        }
                        paiCronologiaStore.load();

                        paiDocumentiStore.proxy.extraParams = {
                            action: 'read',
                            codPai: codPai
                        }
                        paiDocumentiStore.load();
                        Ext.getCmp('wcs_paiFormNuovoInterventoButton').setDisabled(false);
                        Ext.getCmp('wcs_paiFormSalvaButton').setDisabled(false);
                        Ext.getCmp('wcs_paiChiudiPaiButton').setDisabled(false);
                        if (dataChiusura != "" && dataChiusura != null){
                            Ext.getCmp('wcs_paiFormNuovoButton').setDisabled(false);
                            Ext.getCmp('wcs_nuovoPaiButton').setDisabled(false);
                            Ext.getCmp('wcs_paiFormSalvaButton').setDisabled(true);
                            Ext.getCmp('wcs_paiChiudiPaiButton').setDisabled(true);
                            Ext.getCmp('wcs_paiFormNuovoInterventoButton').setDisabled(true);
                        } else {
                            Ext.getCmp('wcs_paiFormNuovoButton').setDisabled(true);
                            Ext.getCmp('wcs_nuovoPaiButton').setDisabled(true);
                            Ext.getCmp('wcs_paiFormSalvaButton').setDisabled(false);
                            Ext.getCmp('wcs_paiChiudiPaiButton').setDisabled(false);
                            Ext.getCmp('wcs_paiFormNuovoInterventoButton').setDisabled(false);
                        }
                        wcs_isModified = '';
                    },
                    afterrender: function(){
                        Ext.getCmp('wcs_keyboardPaiForm').fireEvent('afterrender');
                        var form = paiTab.child('#wcs_paiForm').getForm();
                        form.isValid();
                        form.getFields().each(
                            function(field){
                                field.on('change',function(f,n,o){
                                    if (f.name != 'inputItem'){
                                        wcs_isModified = 'wcs_paiTab';
                                    }
                                });
                            });
                    }
                }
            },{
                itemId: 'wcs_paiForm',
                xtype: 'wcs_paiform'
            }]
        });

        this.items = [{
            title: 'Anagrafica',
            id: 'wcs_anagraficaTab',
            xtype: 'wcs_anagraficaform',
            listeners: {
                afterrender: function(){
                    Ext.getCmp('wcs_keyboardAnagraficaForm').fireEvent('afterrender');
                    var form = Ext.getCmp('wcs_anagraficaTab').getForm();
                    form.isValid();
                    form.getFields().each(function(field){
                            field.on('change',function(f,n,o){
                                wcs_isModified = 'wcs_anagraficaTab';
                            });
                        });
                }
            }
        },{
            id: 'wcs_anagraficaSocTab',
            title:'Anagrafica Persone Giuridiche',
            xtype: 'wcs_anagraficasocform'
        },{
            title: 'Condizione',
            disabled: true,
            id: 'wcs_condizioneTab',
            xtype : 'wcs_condizioneform',
            listeners: {
                afterrender: function(){
                    Ext.getCmp('wcs_keyboardCondizioneForm').fireEvent('afterrender');
                    var form = Ext.getCmp('wcs_condizioneTab').getForm();
                    form.isValid();
                    form.getFields().each(function(field){
                            field.on('change',function(f,n,o){
                                wcs_isModified = 'wcs_condizioneTab';
                            });
                        });
                }
            }
        },
        famigliaTab,
        referentiTab,
        paiTab,
        {
            title: 'Appuntamenti',
            disabled: true,
            height: 600,
            id: 'wcs_appuntamentiTab',
            xtype: 'wcs_appuntamentilist'
        },
        {
            title: 'Diario',
            disabled: true,
            id: 'wcs_diarioTab',
            xtype : 'wcs_diarioform',
            listeners: {
                afterrender: function(){
                     var form = Ext.getCmp('wcs_diarioTab').getForm();
                    form.isValid();
                    form.getFields().each(function(field){
                            field.on('change',function(f,n,o){
                                wcs_isModified = 'wcs_diarioTab';
                            });
                        });
                }
            }
        }];

    
        this.callParent(arguments);
        
        this.tabBar.add(Ext.create('Ext.form.Label',{
            text:'',
            id:'wcs_currentUserName',
            hidden:true
        }));
    }    
});