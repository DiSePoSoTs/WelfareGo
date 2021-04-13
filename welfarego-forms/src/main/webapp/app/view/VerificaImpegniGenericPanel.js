Ext.define('wf.view.VerificaImpegniGenericPanel', {
    extend: 'wf.view.GenericFormPanel',
    title: 'Verifica Impegni',
    initComponent: function() {
        
      var impegniStore= Ext.create('wf.store.ImpegniStore', {
          listeners: {
              datachanged: function(store) {
                  //                        store.load(function(){
                  updateBudgetResiduo();
                  //                        });
              }
          }
      });
      
     
      
     function weeks_between(date1, date2) {
      	// The number of milliseconds in one week
      	var ONE_WEEK = 1000 * 60 * 60 * 24 * 7;
      	// Convert both dates to milliseconds
      	var date1_ms = date1.getTime();
      	var date2_ms = date2.getTime();
      	// Calculate the difference in milliseconds
      	var difference_ms = Math.abs(date1_ms - date2_ms);
      	// Convert back to weeks and return hole weeks
      	return Math.floor(difference_ms / ONE_WEEK);
      }
     //quanti mesi fra una data e l'altra 
     function monthDiff(d1, d2) {
    	    var months;
    	    var year1=d1.getFullYear();
    	    var year2=d2.getFullYear();
    	    var month1=d1.getMonth();
    	    var month2=d2.getMonth();
    	    if(month1===0){ //Have to take into account
    	      month1++;
    	      month2++;
    	    }
    	    months = (year2 - year1) * 12 + (month2 - month1) + 1;
    	    return months;
    	}
      var impegniUotStore=Ext.create('wf.store.ImpegniUotStore');
        function setVisibleQuantitaFields(visible) {
            //log('VerificaImpegniGenericiPanel visibilita campi quantita : ', visible);
            try {
                Ext.getCmp('wf_verifica_impegni_acarico_sum_qta').setVisible(visible);
                Ext.getCmp('wf_verifica_impegni_acarico_rem_qta').setVisible(visible);
                Ext.getCmp('wf_unitaDiMisuraDescColumn').setVisible(visible);
                Ext.getCmp('wf_bdgDispQtaColumn').setVisible(visible);
                Ext.getCmp('wf_bdgPrevQtaColumn').setVisible(visible);
                Ext.getCmp('wf_bdgDispQtaDispColumn').setVisible(visible);
                Ext.getCmp('quantitaUot1').setVisible(visible);
                Ext.getCmp('quantitaUot2').setVisible(visible);
            } catch (e) {
                // if invoked before the grid is shown, will throw an error
                // simplest thing is, catch the error and ignore it. The function will
                // be invoked successfully later
                log('expected error : ', e);
            }
        }
        var onSelectTipoIntervento=function(component, record){
//            log('VerificaImpegniGenericiPanel selected tipInt = ',record);
            if(!record[0]){
                log('VerificaImpegniGenericiPanel null data, exit');
                return;
            }
            var tipoIntervento = record[0].data.value;
            log('VerificaImpegniGenericiPanel tipoIntervento:', tipoIntervento);
            var impStdCosto = record[0].data.imp_std_costo;
            log('VerificaImpegniGenericiPanel impStdCosto:', impStdCosto);
           
            Ext.getCmp('wf_verifica_dati_spesa_unitaria').setValue(impStdCosto);
            var interventoImpegnoList=Ext.getCmp('wf_verifica_dati_grid_impegni');
            interventoImpegnoList.store.proxy.extraParams = {
            	nuovoIntervento:"nuovoInterventoImpegni",
            	codTipInt: tipoIntervento,
                task_id:wf.config.taskId
               	
               
            };
            interventoImpegnoList.store.load();
            var interventoImpegnoUotList=Ext.getCmp('wcs_interventoImpegnoUotList');
            interventoImpegnoUotList.store.proxy.extraParams = {
                codTipInt: tipoIntervento,
                task_id:wf.config.taskId,
                nuovoIntervento:"nuovoInterventoImpegniUot"
               
            };
            interventoImpegnoUotList.store.load();
          /*  var store=Ext.getCmp('wf_verifica_dati_grid_impegni').getStore();
            store.getProxy().extraParams.codNuovoInt=tipoIntervento;
            store.reload();*/
          
            //store.load();
            Ext.getCmp('wf_verifica_dati_dur_mesi_proroga').setValue(null);
            Ext.getCmp('wf_verifica_dati_dur_mesi_proroga').disable();
            Ext.getCmp('wf_verifica_dati_dur_mesi_proroga').hide();
            Ext.getCmp('wf_verifica_dati_costo_proroga').setValue(null);
            Ext.getCmp('wf_verifica_dati_costo_proroga').hide();
            Ext.getCmp('wf_verifica_dati_costo_proroga').disable();
            
            updateCostoTotalePrevisto();
            
            setTimeout(function() { /// FIX ORRIBILE
			   	  var store = Ext.getCmp('wf_verifica_dati_grid_impegni').getStore();
			       	  if (store.count() > 0) {
			            var value = store.first().get('unitaDiMisuraDesc');
			            setVisibleQuantitaFields((value && value.match('euro')) ? false : true);
			            	  }
			              }, 300);
            
          
//            }();
        };
        
        var updateCostoProroga = function (){
        	  quantita = Ext.getCmp('wf_verifica_dati_imp_mens').getValue();
              quantitaTotProroga = quantita * Number(Ext.getCmp('wf_verifica_dati_dur_mesi_proroga').getValue());
              Ext.getCmp('wf_verifica_dati_costo_proroga').setValue(quantitaTotProroga);
              updateTotale();
        
        };
        var updateBudgetResiduo = function() {
            var
                    store = Ext.getCmp('wf_verifica_dati_grid_impegni').getStore(),
                    sum = store.sum('a_carico'),
                    sumQta = store.sum('bdgPrevQta'),
                    prev = Ext.getCmp('wf_verifica_dati_cost_tot').getValue(),
                    prevQta = Ext.getCmp('wf_verifica_dati_qta_tot').getValue(),
                    res = prev - sum,
                    resQta = prevQta - sumQta;

            Ext.getCmp('wf_verifica_impegni_acarico_sum').setValue(sum);
            Ext.getCmp('wf_verifica_dati_budg_rest').setValue(res);
            Ext.getCmp('wf_verifica_impegni_acarico_rem').setValue(res);
            Ext.getCmp('wf_verifica_impegni_acarico_sum_qta').setValue(sumQta);
            Ext.getCmp('wf_verifica_impegni_acarico_rem_qta').setValue(resQta);
            //            var bdgDispQta=store.sum('bdgDispQta');
            //            if(bdgDispQta==0&&sumQta==0){
            if (store.count() > 0) {
                var value = store.first().get('unitaDiMisuraDesc');
                setVisibleQuantitaFields((value && value.match('euro')) ? false : true);
            }
        }, updateCostoTotalePrevisto = function() {
            var costo, quantitaTot,
                    spesaUnitaria = Ext.getCmp('wf_verifica_dati_spesa_unitaria').getValue(),
                    quantita = Ext.getCmp('wf_verifica_dati_imp_mens').getValue();
            
            var unitaMis = Ext.getCmp('wf_cod_param_unita_misura').getValue();
            

            if (usaDataFine) {
            	 setTimeout(function() { /// FIX ORRIBILE
            		 var dataInizio = Ext.getCmp('wf_verifica_dati_data_avvio').getValue()   ; 
                     var dataFine = Ext.getCmp('wf_verifica_dati_data_fine').getValue();
                     var numeroGiorni;
                   //  dataInizio = (((dataInizio.getTime() / 1000 / 60) - dataInizio.getTimezoneOffset()) / 60);
                   //  dataFine = (((dataFine.getTime() / 1000 / 60) - dataFine.getTimezoneOffset()) / 60);

                     if (unitaMis === 'os' || unitaMis === 'ps') { //ore settimanali
                    	 var numeroSettimane = weeks_between(dataInizio, dataFine);
                    	 Ext.getCmp('wf_verifica_dati_durata_settimane').setValue(numeroSettimane);
                    	 quantitaTot = quantita * numeroSettimane;
                    	 }
                     else if(unitaMis === 'pa' || unitaMis ==='pm' ||  unitaMis ==='om'){
                    	 //presenze mensili 
                    	 var numeroMesi = monthDiff(dataInizio, dataFine);
                    	 quantitaTot = quantita * numeroMesi;
                     }
                     else {
                   	 dataInizio = (((dataInizio.getTime() / 1000 / 60) - dataInizio.getTimezoneOffset()) / 60);
                      dataFine = (((dataFine.getTime() / 1000 / 60) - dataFine.getTimezoneOffset()) / 60);	 
                     numeroGiorni = (dataFine - dataInizio) / 24;
                     numeroGiorni++;
                     //TODO check this
                     quantitaTot = numeroGiorni * quantita;
                     }
                     
                     costo = spesaUnitaria * quantitaTot;
                     //            if(costo!=0||quantitaTot!=0){
                     Ext.getCmp('wf_verifica_dati_cost_tot').setValue(costo);
                     Ext.getCmp('wf_verifica_dati_qta_tot').setValue(quantitaTot);
                     updateBudgetResiduo();
            	 }, 100);
               
            } else {
                quantitaTot = quantita * Number(Ext.getCmp('wf_verifica_dati_durata').getValue());
                if (unitaMis === 'os' || unitaMis === 'ps') { //ore settimanali
                    quantitaTot *= 4;
                    Ext.getCmp('wf_verifica_dati_durata_settimane').setValue(4*Number(Ext.getCmp('wf_verifica_dati_durata').getValue()));
                }
            }
            
           
            costo = spesaUnitaria * quantitaTot;
            //            if(costo!=0||quantitaTot!=0){
            Ext.getCmp('wf_verifica_dati_cost_tot').setValue(costo);
            
            Ext.getCmp('wf_verifica_dati_qta_tot').setValue(quantitaTot);
            updateBudgetResiduo();
            updateTotale();
            //            }
        },
        updateTotale=function(){
        	costoIntervento =  Ext.getCmp('wf_verifica_dati_cost_tot').getValue();
        	costoProroghe =    Ext.getCmp('wf_verifica_dati_costo_proroga').getValue();
        	totale = costoIntervento + costoProroghe;
        	 Ext.getCmp('wf_verifica_dati_costo_intervento').setValue(totale);
        };
        
       
        this.codForm = 'default';

      

        this.items = [
        {
                xtype: 'container',
                layout: 'column',
                items: [{
                        xtype: 'hidden',
                        name: 'cod_param_unita_misura',
                        id: 'wf_cod_param_unita_misura'
                    },{
                        xtype: 'hidden',
                        name: 'is_prorogabile',
                        id: 'wf_is_prorogabile'
                    },
                    {
                        xtype: 'hidden',
                        name: 'cod_tmpl_documento_di_autorizzazione',
                        id: 'cod_tmpl_documento_di_autorizzazione'
                    },

                        // 4 righe di sx
                    {
                        xtype: 'container',
                        columnWidth: .5,
                        layout: 'anchor',
                        defaults: {
                            xtype: 'textfield',
                            readOnly: true
                        },
                        items: [
                        {
                                id: 'wf_verifica_dati_nome_utente',
                                fieldLabel: 'Nome Utente',
                                name: 'nome_utente_ver_dati'
                            }, {
                                xtype: 'datefield',
                                id: 'wf_verifica_dati_data_apert_pai',
                                fieldLabel: 'Data apertura PAI',
                                name: 'data_apert_pai'
                            }, {
                                id: 'wf_verifica_dati_n_pai',
                                fieldLabel: 'PAI n°',
                                name: 'n_pai'
                            }, {
                                id: 'wf_verifica_dati_spesa_unitaria',
                                fieldLabel: 'Costo unitario',
                                name: 'spesa_unitaria',
                                xtype:'weuronumberfield'
//                                decimalSeparator:',',
//                                allowDecimals:true,
//                                decimalPrecision:2
                            }]
                    },
                        // 4 righe di dx
                    {
                        xtype: 'container',
                        columnWidth: .5,
                        layout: 'anchor',
                        defaults: {
                            xtype: 'textfield',
                            readOnly: true
                        },
                        items: [
                            {
                                id: 'wf_verifica_dati_cognome_ut',
                                fieldLabel: 'Cognome utente',
                                name: 'cognome_ut'
                            }, {
                                id: 'wf_verifica_dati_assist_soc',
                                fieldLabel: 'Assistente sociale',
                                name: 'assist_soc'
                            }, {
                                id: 'wf_verifica_dati_interv',
                                fieldLabel: 'Intervento',  // da dove arriva ??
                                name: 'interv'
                            }, {
                                id: 'wf_verifica_dati_descr',
                                fieldLabel: 'Descrizione',
                                name: 'descrizione'
                            }]
                    }]
            }, {
                xtype: 'container',
                layout: 'column',
                items: [
                        {
                        xtype: 'wdecimalnumberfield',
                        allowDecimals: true,
                        minValue: 1,
                        id: 'wf_verifica_dati_imp_mens',
                        fieldLabel: 'Quantità', /* TODO fix con mensile/giornaliera/settimanale automatica */
                        name: 'imp_mens',
                        columnWidth: .5,
//                        decimalSeparator: ',',
//                        decimalPrecision: 2,
                        listeners: {
                            change: updateCostoTotalePrevisto
                        }
                    }, {
                        xtype: 'numberfield',
                        allowDecimals: false,
                        minValue: 1,
                        id: 'wf_verifica_dati_durata',
                        fieldLabel: 'Durata mesi',
                        name: 'durata',
                        columnWidth: .5,
                        listeners: {
                            change: updateCostoTotalePrevisto
                        }
                    },{
                        xtype: 'numberfield',
                        allowDecimals: false,
                        minValue: 1,
                        id: 'wf_verifica_dati_durata_settimane',
                        fieldLabel: 'Durata settimane',
                        columnWidth: .5,
                        readOnly:true,
                        hidden:true
                    },  {
                        xtype: 'datefield',
                        id: 'wf_verifica_dati_data_avvio',
                        fieldLabel: 'Data avvio',
                        name: 'data_avvio',
                        format: 'd/m/Y',
                        columnWidth: .5,
                         listeners:{
                                                  change:updateCostoTotalePrevisto
                                              }
                    },{
                        xtype:'weuronumberfield',
                        id: 'wf_verifica_dati_cost_tot',
                        fieldLabel: 'Costo previsto (senza proroghe)',
                        columnWidth: .5,
                        name: 'cost_tot'
//                            format: '0,000.00 €', Won't work on numberfield :/
//                        allowDecimals: true,
//                        decimalSeparator: ',',
//                        decimalPrecision: 2
                    },
                    
                   {
                        xtype: 'datefield',
                        id: 'wf_verifica_dati_data_fine',
                        fieldLabel: 'Data fine',
                        name: 'data_fine',
                        format: 'd/m/Y',
                        columnWidth: .5,
                         listeners:{
                                                  change:updateCostoTotalePrevisto
                                              }
                    },  {
                        xtype: 'datefield',
                        id: 'wf_verifica_dati_data_avvio_proposta',
                        fieldLabel: 'Data avvio proposta dall A.S.',
                        name: 'data_avvio_proposta',
                        columnWidth: .5,
                       readOnly: true
                       
                                //                ,
                                //                listeners:{
                                //                    change:updateCostoTotalePrevisto
                                //                }
                    },
                    {
                        xtype: 'numberfield',
                        id: 'wf_verifica_dati_dur_mesi_proroga',
                        fieldLabel: 'Mesi da prorogare',
                        name: 'dur_mesi_proroga',
                        columnWidth: .5,
                   
                       hidden:true,
                       disabled:true,
                       listeners:{
                           change:updateCostoProroga
                       }

                       
                                //                ,
                                //                listeners:{
                                //                    change:updateCostoTotalePrevisto
                                //                }
                    }
                   
                    ]
            }, {
                xtype: 'container',
                layout: 'column',
                defaults: {
                    xtype: 'numberfield',
                    columnWidth: .5,
                    readOnly: true
                },
                items: [
                {
              			 xtype:'combo',
                         displayField: 'name',
                         valueField: 'value',
                         fieldLabel:'Trasforma in ',
                         store:'combo.TipologieInterventoStore',
                         name:'trasforma_in',
                         readOnly:false,
                         listeners:{
                        	  select: onSelectTipoIntervento
                         }
                         
            		},
            		{
                        xtype:'weuronumberfield',
                        id: 'wf_verifica_dati_costo_proroga',
                        fieldLabel: 'Costo totale mesi proroga \u20ac',
                       // name: 'cost_tot'
                        readOnly:true,
                        hidden:true,
                        disabled:true
//                            format: '0,000.00 €', Won't work on numberfield :/
//                        allowDecimals: true,
//                        decimalSeparator: ',',
//                        decimalPrecision: 2
                    },
                    {
                    xtype:'weuronumberfield',
                    id: 'wf_verifica_dati_costo_intervento',
                    fieldLabel: 'Costo totale intervento \u20ac',
                   // name: 'cost_tot'
                    readOnly:true
                  
                  
//                        format: '0,000.00 €', Won't work on numberfield :/
//                    allowDecimals: true,
//                    decimalSeparator: ',',
//                    decimalPrecision: 2
                },
            		{
                        id: 'wf_verifica_dati_qta_tot',
                        //                fieldLabel: 'Costo totale previsto \u20ac',
                        name: 'quantita_tot',
                        hidden: true
                    }, {
                        xtype:'weuronumberfield',
                        id: 'wf_verifica_dati_budg_rest',
                        fieldLabel: 'Budget restante \u20ac',
                        name: 'budg_rest',
                        listeners: {
                            change: function(field, value) {
                                field.setFieldStyle("color:" + (value < 0 ? "red" : "black"));
                            }
                        },
                        hidden: true
                    }/*, {
                        xtype: 'datefield',
                        id: 'wf_verifica_dati_data_fine',
                        fieldLabel: 'Data fine*',
                        name: 'data_fine',
                        format: 'd/m/Y',
                        allowBlank: false,
                        readOnly: false,
                        listeners: {
                            change: updateCostoTotalePrevisto
                        }
                    }*/]
            }, {
    			xtype: 'textarea',
    			labelWidth:50,
    			labelAlign:'top',
    			id: 'wf_verifica_dati_note',
    			anchor:'99%',
    			//                height: 90,
    			fieldLabel: 'Motivazione',
    			name: 'note'	
            }
            	
            ,
            // grid Impegni
            {
                xtype: 'gridpanel',
                title: 'Impegni',
                height: 250,
                columnWidth: .7,
                store: impegniStore/* = Ext.create('wf.store.ImpegniStore', {
                    listeners: {
                        datachanged: function(store) {
                            //                        store.load(function(){
                            updateBudgetResiduo();
                            //                        });
                        }
                    }
                })*/,
                stateful: true,
                id: 'wf_verifica_dati_grid_impegni',
                defaults: {
                    sortable: true
                },
                //			features: [{                //risultati raggruppabili
                //				ftype: 'grouping'
                //			}],
                columns: [
                {
                        id: 'wf_verifica_dati_anno',
                        header: "Anno",
                        dataIndex: 'anno',
                        flex: 1
                    }, {
                        id: 'wf_verifica_dati_capitolo',
                        header: "Capitolo",
                        dataIndex: 'capitolo',
                        flex: 1
                    }, {
                        id: 'wf_verifica_dati_impegno',
                        header: "Impegno",
                        dataIndex: 'impegno',
                        flex: 1
                    },
                    {
                        header: 'importo',
                        
                        columns: [  {
                            id: 'wf_verifica_dati_imp_disp_gda',
                            header: "Budget disponibile ",
                            dataIndex: 'imp_disp_netto',
                            flex: 1,
                            width:120,
                            xtype: 'numbercolumn',
                            format: '0,000.00 €'
                        },{
                                id: 'wf_verifica_dati_imp_disp',
                                header: "Budget disponibile con prenotazioni",
                                dataIndex: 'imp_disp',
                                flex: 1,
                                xtype: 'numbercolumn',
                                format: '0,000.00 €'
                            },
                           {
                                id: 'wf_verifica_dati_imp_disp_proroghe',
                                header: "Disponibile con proroghe",
                                dataIndex: 'imp_disp_proroghe',
                                flex: 1,
                                width:120,
                                xtype: 'numbercolumn',
                                format: '0,000.00 €'
                            },{
                                id: 'wf_verifica_dati_a_carico',
                                header: "A carico",
                                dataIndex: 'a_carico',
                                flex: 1,
                                xtype: 'numbercolumn',
                                format: '0,000.00 €',
                                editor: {
                                    xtype: 'weuronumberfield',
                                    allowBlank: false,
//                                    decimalSeparator: ',',
                                    listeners: {
                                        edit: function() {
                                            log('reloading impegni store');
                                            impegniStore.load();
                                        }
                                    }
                                }
                            }]
                    }, {
                        header: 'quantita',
                        columns: [{
                                //                id:'wf_verifica_dati_imp_disp', 
                                header: " ",
                                dataIndex: 'unitaDiMisuraDesc',
                                width: 90,
                                id: 'wf_unitaDiMisuraDescColumn'
                            }, {
                                //                id:'wf_verifica_dati_imp_disp', 
                                header: "disponibile",
                                dataIndex: 'bdgDispQta',
                                flex: 1,
                                id: 'wf_bdgDispQtaColumn',
                                xtype: 'numbercolumn'
                            },
                            {
                                //                id:'wf_verifica_dati_imp_disp', 
                                header: "disponibile senza prenotazioni",
                                dataIndex: 'bdgDispQtaCons',
                                flex: 1,
                                id: 'wf_bdgDispQtaDispColumn',
                                xtype: 'numbercolumn'
                            },{
                                //                id:'wf_verifica_dati_a_carico', 
                                header: "A carico",
                                dataIndex: 'bdgPrevQta',
                                xtype: 'numbercolumn',
                                flex: 1,
                                editor: {
                                    xtype: 'numberfield',
                                    allowBlank: false,
                                    decimalSeparator: ',',
                                    listeners: {
                                        edit: function() {
                                            log('reloading impegni store');
                                            impegniStore.load();
                                        }
                                    }
                                },
                                id: 'wf_bdgPrevQtaColumn'
                            }]
                    }],
                viewConfig: {
                    stripeRows: true
                },

                bbar: Ext.create('Ext.PagingToolbar', {
                    store: impegniStore,
                    displayInfo: true,
                    items: [
                    {
                            //	labelWidth:120,
                            labelWidth: 80,
                            xtype: 'weuronumberfield',
                            id: 'wf_verifica_impegni_acarico_sum',
                            name: 'wf_verifica_impegni_acarico_sum',
                            fieldLabel: 'importo assegnato €',
                            readOnly: true,
                            //                    hidden:true,
                            width: 180
//                            format: '0,000.00 €', Won't work on numberfield :/
//                            decimalSeparator: ','
                        }, {
                            //	labelWidth:120,
                            labelWidth: 80,
                            xtype: 'weuronumberfield',
                            id: 'wf_verifica_impegni_acarico_rem',
                            name: 'wf_verifica_impegni_acarico_rem',
                            fieldLabel: 'importo da assegnare €',
                            readOnly: true,
                            width: 180
//                            format: '0,000.00 €', Won't work on numberfield :/
//                            decimalSeparator: ','
                        }, {
                            //	labelWidth:120,
                            labelWidth: 80,
                            xtype: 'numberfield',
                            id: 'wf_verifica_impegni_acarico_sum_qta',
                            name: 'wf_verifica_impegni_acarico_sum_qta',
                            fieldLabel: 'quantita assegnata',
                            readOnly: true,
                            //                    hidden:true,
//                            decimalSeparator: ',',
                            width: 160
                        },{
                            //	labelWidth:120,
                            labelWidth: 80,
                            xtype: 'numberfield',
                            id: 'wf_verifica_impegni_acarico_rem_qta',
                            name: 'wf_verifica_impegni_acarico_rem_qta',
                            fieldLabel: 'quantita da assegnare',
                            readOnly: true,
//                            decimalSeparator: ',',
                            width: 160
                        }]
                }),
                plugins: [
                    Ext.create('Ext.grid.plugin.RowEditing', {
                        clicksToEdit: 1
                    })]
            },
            {
            	id: 'wcs_interventoImpegnoUotList',
                xtype:'grid',
                title: 'Impegni UOT',
                store: impegniUotStore,
                height: 150,
            
                columns:[{
                    header: 'Anno',
                    dataIndex: 'anno',
                    sortable: false,
                    width: 70
                },{
                    header: 'Capitolo',
                    dataIndex: 'capitolo',
                    sortable: false,
                    width: 80
                },{
                    header: 'Impegno',
                    dataIndex: 'impegno',
                    sortable: false,
                    width: 80
                },{
                    header: 'Budget iniziale',
                    dataIndex: 'budget_iniziale',
                    xtype: 'numbercolumn',
                    format: '0,000.00 €',
                    sortable: false,
                    flex: 1
                },{
                    header: 'Budget disponibile',
                    dataIndex: 'imp_disp_netto',
                    xtype: 'numbercolumn',
                    format: '0,000.00 €',
                    sortable: false,
                    flex: 1
                },{
                    header: 'Totale prenotazioni',
                    dataIndex: 'totale_prenotato',
                    xtype: 'numbercolumn',
                    format: '0,000.00 €',
                    sortable: false,
                    flex: 1
                },{
                    header: 'Budget disponibile con prenotazioni',
                    dataIndex: 'imp_disp',
                    xtype: 'numbercolumn',
                    format: '0,000.00 €',
                    sortable: false,
                    flex: 1
                },
                {
                  
                    header: "Disponibile con proroghe",
                    dataIndex: 'imp_disp_proroghe',
                    xtype: 'numbercolumn',
                    format: '0,000.00 €',
                    sortable: false,
                    flex : 1
                },
                {
                    header: 'Unita\' disponibili',
                    dataIndex: 'bdgDispQta',
                    //                        renderer: function(value){
                    //                            var cmp=Ext.getCmp('wcs_interventoQuantita');
                    //                            if(cmp){
                    //                                var costoUnitario=cmp.getValue();
                    //                            }
                    //                            if(costoUnitario&&costoUnitario!=''){
                    //                                var val=Math.floor(value/costoUnitario);
                    //                                return val;
                    //                            }else{
                    //                                return '';
                    //                            }
                    //                        },
                    sortable: false,
                    id:'quantitaUot1',
                    flex: 1
                },{
                    header: 'Unita\' disponibili senza prenotazione ( ex GDA ) ',
                    dataIndex: 'bdgDispQtaCons',
                    //                        renderer: function(value){
                    //                            var cmp=Ext.getCmp('wcs_interventoQuantita');
                    //                            if(cmp){
                    //                                var costoUnitario=cmp.getValue();
                    //                            }
                    //                            if(costoUnitario&&costoUnitario!=''){
                    //                                var val=Math.floor(value/costoUnitario);
                    //                                return val;
                    //                            }else{
                    //                                return '';
                    //                            }
                    //                        },
                    sortable: false,
                    id:'quantitaUot2',
                    flex: 1
                }
                ,{
                    header: 'UOT',
                    dataIndex: 'uot',
                    sortable: false,
                    width: 50,
                    flex: 1
                }]
              
        
            }           
            //Ext.create('wf.view.BreComponent')
            ];

        var usaDataFine = false;
        var isProrogabile = false ;
        
        var form = this;

        function initDataInizioDurata(values) {
            //            var values=form.getValues();
            //            log('form : ',form,' values : ',values);
            //            log('form : ',form,' values : ',values);
            if (values['data_fine']) {
                Ext.getCmp('wf_verifica_dati_durata').disable();
                Ext.getCmp('wf_verifica_dati_durata').hide();
                usaDataFine = true;
                //                if(Ext.getCmp('wf_verifica_dati_imp_mens').el){ TODO broken, must fix later
                //                    Ext.getCmp('wf_verifica_dati_imp_mens').el.dom.childNodes[0].innerHTML="Quantità giornaliera";
                //                }
            } else {
              //  Ext.getCmp('wf_verifica_dati_data_avvio').disable();
              //  Ext.getCmp('wf_verifica_dati_data_avvio').hide();
                Ext.getCmp('wf_verifica_dati_data_fine').disable();
                Ext.getCmp('wf_verifica_dati_data_fine').hide();
                if(values['is_prorogabile']=='S'){
                	  Ext.getCmp('wf_verifica_dati_dur_mesi_proroga').enable();
                      Ext.getCmp('wf_verifica_dati_dur_mesi_proroga').show();
                      Ext.getCmp('wf_verifica_dati_costo_proroga').show();
                      Ext.getCmp('wf_verifica_dati_costo_proroga').enable();
                      
                }
                
                usaDataFine = false;
            }
            if(values['cod_param_unita_misura']=='os' || values['cod_param_unita_misura']=='ps'){
            	
                 Ext.getCmp('wf_verifica_dati_durata_settimane').show();
            }
            else {
            	 Ext.getCmp('wf_verifica_dati_durata_settimane').hide();
            }
        };

        //        this.load=function(){
        //            this.callParent(arguments);
        ////            initDataInizioDurata();
        //        }
        //        this.on('actioncomplete', function (form,action) {
        //            initDataInizioDurata();
        //        });


        impegniStore = null;
        if (!this.buttons) {
            this.buttons = [this.submitButton];
        }

        this.callParent(arguments);

        var basicForm = this.getForm();
        var oldFunc = basicForm.setValues;

        basicForm.setValues = function(values) {
            initDataInizioDurata(values);
            oldFunc.apply(basicForm, arguments);
            updateCostoTotalePrevisto();
        }
     
    }
   
});
