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
    selectOnFocus:true,
    editable: true,
    typeAhead: true,
    listeners: {
      /*  'keyup': function() {
        	 
        	        	  re = new RegExp(this.getRawValue(), 'i');
        	              this.store.filter('name', re, true, true);
        	        
           
        },*/
    	  beforequery: function (record) {
              record.query = new RegExp(record.query, 'i');
              record.forceAll = true;
          }
       
}
});

Ext.define('wf.view.reports.ParametroCognomeAssistito',{
    extend: 'Ext.form.TextField',
    alias: 'widget.wr_par_cognome_assistito',
    name:'par_cognome_assistito',
    fieldLabel: 'Cognome assistito'
});

Ext.define('wf.view.reports.ParametroNomeAssistito',{
    extend: 'Ext.form.TextField',
    alias: 'widget.wr_par_nome_assistito',
    name:'par_nome_assistito',
    fieldLabel: 'Nome assistito'
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
    fieldLabel: 'Ad anno:'
});

Ext.define('wf.view.reports.ParametroAnnoAl',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_anno_dal',
    name:'par_anno_al',
    fieldLabel: 'Da anno :'
});
Ext.define('wf.view.reports.ParametroMese',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_mese',
    name:'par_mese',
    fieldLabel: 'Mese:'
});

Ext.define('wf.view.reports.ParametroMeseDal',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_mese_dal',
    name:'par_mese_dal',
    fieldLabel: 'Dal mese:'
});

Ext.define('wf.view.reports.ParametroMeseAl',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_mese_al',
    name:'par_mese_al',
    fieldLabel: 'Al mese:'
});

Ext.define('wf.view.reports.ParametroDal',{
    extend: 'Ext.form.DateField',
    alias: 'widget.wr_par_dal',
    format: 'd/m/Y',
    name:'par_dal',
    fieldLabel: 'Da data:'
});

Ext.define('wf.view.reports.ParametroAl',{
    extend: 'Ext.form.DateField',
    alias: 'widget.wr_par_al',
    format: 'd/m/Y',
    name:'par_al',
    fieldLabel: 'A data:'
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


Ext.define('wf.view.reports.ParametroAnno',{
    extend: 'Ext.form.TextField',
    alias: 'widget.wr_par_anno',
    name:'par_anno',
    fieldLabel: 'Anno'
});

Ext.define('wf.view.reports.ParametroNrMesi',{
    extend: 'Ext.form.NumberField',
    alias: 'widget.wr_par_nr_mesi',
    name:'par_nr_mesi',
    fieldLabel: 'Numero mesi:'
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
Ext.define('wf.view.reports.ParametroNumeroDetermina',{
    extend: 'Ext.form.TextField',
    alias: 'widget.wr_par_num_determina',
    name:'par_num_determina',
   fieldLabel: 'Numero determina o protocollo:'
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

Ext.define('wf.view.reports.ParametroCondFamiliare',{
    extend: 'Ext.form.ComboBox',
    alias: 'widget.wr_par_cond_fam',    
    fieldLabel: 'Condizione familiare',
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    store: 'combo.CondFamiliareStore',
    name: 'par_cond_fam',
    selectOnFocus:true,
    editable: false
});
