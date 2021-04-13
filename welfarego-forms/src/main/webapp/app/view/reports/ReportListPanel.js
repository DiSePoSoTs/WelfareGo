Ext.define('wf.view.reports.ReportListPanel',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wr_list_panel',
    width:300,
    title:'Elenco reports',
    showGridHeader: false,
    store:'ReportsStore',
    initComponent: function() {
        this.columns = [{
            header: 'Report',
            dataIndex: 'report',
            sortable: false,
            flex: 1,
            componentCls:'hand_cursor',
            renderer: function(value,meta) {
                meta.tdAttr = 'data-qtip="' + value + '"';
                return value;
            }
        }];        
        this.tools=[{
            type:'help',
            qtip: 'Vedi elenco dei parametri supportati',
            handler: function(event, toolEl, panel){
                var reportsHelpMsg = '';
                reportsHelpMsg += '<b>par_elenco_uot</b> : Tendina con le Uot registrate nel sistema <br/>';
                reportsHelpMsg += '<b>par_elenco_tip_int</b> : Tendina con Tipologie intervento registrate nel sistema <br/>';
                reportsHelpMsg += '<b>par_cognome_assistito</b> : Campo di testo per cognome assistito <br/>';
                reportsHelpMsg += '<b>par_dal</b> : Campo data  <br/>';

                Ext.Msg.alert('Elenco parametri supportati', reportsHelpMsg);

            }
        }];
        this.getSelectionModel().on('selectionchange', function(sm, selectedRecord) {
            if (selectedRecord.length) {
                var report = selectedRecord[0].data.report;
                var tabPanel = Ext.getCmp('wr_tab_panel');
                if(!this.reportArray)
                    this.reportArray = new Array(); 
                //					  var reportArray=null;
                //                if(typeof reportArray === 'undefined'){           
                //                }
                var i,found=false;      
                if(this.reportArray[report]){          
                    for(i=0;i<tabPanel.items.length;i++){
                        if(tabPanel.items.items[i]==this.reportArray[report]){
                            found=true;
                            break;
                        }                    
                    }
                }
                if(!found){
                    this.reportArray[report] = Ext.create('wf.view.reports.ReportCenterPanel',{
                        title:report
                    });
                    tabPanel.add(this.reportArray[report]);
                }
                tabPanel.setActiveTab(this.reportArray[report]);
                
                if(!found){
                    this.reportArray[report].report=report;
                    this.reportArray[report].loadReport();
                }
            }
        });

        this.callParent(arguments);
    }
});

Ext.define('wf.view.reports.ParametroTipologiaInterventi',{
    extend: 'Ext.form.ComboBox',
    alias: 'widget.wr_par_elenco_tip_int',
    fieldLabel: 'Tipologia intervento',
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    store: 'combo.TipologieInterventoStore',
    name: 'par_elenco_tip_int',
    emptyText: 'Tipo intervento ...',
    enableKeyEvents: true,
    //editable: true,
   
  
    listeners: {
       	  beforequery: function(q) {
              if (q.query) {
                  var length = q.query.length;
                  q.query = new RegExp(Ext.escapeRe(q.query),'i');
                  q.query.length = length;
              }
          }
}
});


Ext.define('wf.view.reports.ParametroCognomeAssistito',{
    extend: 'Ext.form.TextField',
    alias: 'widget.wr_par_cognome_assistito',
    name:'par_cognome_assistito',
    fieldLabel: 'Cognome assistito'
});

Ext.define('wf.view.reports.ParametroDal',{
    extend: 'Ext.form.DateField',
    format: 'd/m/Y',
    alias: 'widget.wr_par_dal',
    name:'par_dal',
    fieldLabel: 'Da data:'
});

Ext.define('wf.view.reports.ParametroElencoUOT',{
    extend: 'Ext.form.ComboBox',
    alias: 'widget.wr_par_elenco_uot',    
    fieldLabel: 'Uot',
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    store: 'combo.UotStore',
    name: 'par_elenco_uot',
    emptyText: 'UOT...',
    selectOnFocus:true,
    editable: false
});

Ext.define('wf.view.reports.ParametroCondFamiliare',{
    extend: 'Ext.form.ComboBox',
    alias: 'widget.wr_par_cond_fam',    
    fieldLabel: 'Composizione nucleo familiare',
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    store: 'combo.CondFamiliareStore',
    name: 'par_cond_fam',
    selectOnFocus:true,
    editable: false
});

Ext.define('wf.view.reports.ParametroSesso',{
    extend: 'Ext.form.ComboBox',
    alias: 'widget.wr_par_sesso',    
    fieldLabel: 'Sesso',
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    store:new Ext.data.ArrayStore({
        fields: ['name', 'value'],
        data : [['Maschile', 'M'],['Femminile','F']]
      
     }),
    name: 'par_sesso',
    selectOnFocus:true,
    editable: false
});

Ext.define('wf.view.reports.ParametroAl',{
    extend: 'Ext.form.DateField',
    alias: 'widget.wr_par_al',
    format: 'd/m/Y',
    name:'par_al',
    fieldLabel: 'A data:'
});

Ext.define('wf.view.reports.ParametroAnno',{
    extend: 'Ext.form.TextField',
    alias: 'widget.wr_par_anno',
    name:'par_anno',
    fieldLabel: 'Anno'
});

Ext.define('wf.view.reports.ParametroEta1',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_eta_1',
    name:'par_eta_1',
    fieldLabel: 'Da anni:'
});
Ext.define('wf.view.reports.ParametroEta2',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_eta_2',
    name:'par_eta_2',
    fieldLabel: 'Ad anni:'
});
Ext.define('wf.view.reports.ParametroAnno',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_anno',
    name:'par_anno',
    fieldLabel: 'Anno:'
});

Ext.define('wf.view.reports.ParametroAnnoDal',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_anno_dal',
    name:'par_anno_dal',
    fieldLabel: 'Da anno:'
});

Ext.define('wf.view.reports.ParametroAnnoAl',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_anno_al',
    name:'par_anno_al',
    fieldLabel: 'Ad anno:'
});

Ext.define('wf.view.reports.ParametroNrMesi',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_nr_mesi',
    name:'par_nr_mesi',
    fieldLabel: 'Numero mesi:'
});
Ext.define('wf.view.reports.ParametroMese',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_mese',
    name:'par_mese',
    minValue:1,
    maxValue:12,
    fieldLabel: 'Mese:'
});
Ext.define('wf.view.reports.ParametroNumeroDetermina',{
    extend: 'Ext.form.TextField',
    alias: 'widget.wr_par_num_determina',
    name:'par_num_determina',
     fieldLabel: 'Numero determina o protocollo:'
});

Ext.define('wf.view.reports.ParametroCognomeAssistito',{
    extend: 'Ext.form.TextField',
    alias: 'widget.wr_par_cognome_assistito',
    name:'par_cognome_assistito',
    fieldLabel: 'Cognome assistito'
});

Ext.define('wf.view.reports.ParametroElencoGiuridiche',{
    extend: 'Ext.form.ComboBox',
    alias: 'widget.wr_par_elenco_giuridiche',    
    fieldLabel: 'Persona giuridica',
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    store: 'combo.GiuridicheStore',
    name: 'par_elenco_giuridiche',
    emptyText: 'Giuridiche...',
    selectOnFocus:true,
  //  editable: false
    listeners: {
     	  beforequery: function(q) {
            if (q.query) {
                var length = q.query.length;
                q.query = new RegExp(Ext.escapeRe(q.query),'i');
                q.query.length = length;
            }
        }
}
});

Ext.define('wf.view.reports.ParametroMeseAl',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_mese_dal',
    name:'par_mese_dal',
    minValue:1,
    maxValue:12,
    fieldLabel: 'Dal mese:'
});

Ext.define('wf.view.reports.ParametroMeseDal',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_mese_al',
    name:'par_mese_al',
    minValue:1,
    maxValue:12,
    fieldLabel: 'Al mese:'
});
Ext.define('wf.view.reports.ParametroStatoIntervento',{
    extend: 'Ext.form.ComboBox',
    alias: 'widget.wr_par_stato_int',    
    fieldLabel: 'Stato intervento',
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    store:new Ext.data.ArrayStore({
        fields: ['name', 'value'],
        data : [['Approvati', 'Approvata proposta intervento'],['Respinti','Respinta proposta intervento']]
      
     }),
    name: 'par_stato_int',
    selectOnFocus:true,
    editable: false
});

