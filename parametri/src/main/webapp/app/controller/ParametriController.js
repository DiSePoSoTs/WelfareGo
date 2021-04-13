Ext.define('wp.controller.ParametriController', {
    extend: 'Ext.app.Controller' ,
    views: [
        
        
    // il main panel
    'parametri.ParametriPanel',
        
    // parametri
    'parametri.ParamsTab',
    'parametri.ParamsTree',
    'parametri.ParamsForm',
        
    // tipologie
    'parametri.TipologieTab',
    'parametri.TipologieTree',
    'parametri.TipologieGrid',
    'parametri.TipologieForm',
        
    // gruppi
    /*
    'parametri.GruppiTab',
    'parametri.GruppiGrid',
    'parametri.GruppiForm',
    */
        
    // dati specifici
    'parametri.DatiTab',
    'parametri.DatiGrid',
    'parametri.DatiForm',
    
    // associazione dati specifici <-> tipint
    'parametri.AssociaTab',
    'parametri.AssociaTree',
    'parametri.AssociaDispGrid',
    'parametri.AssociaRelGrid',
        
    // utenti
    'parametri.UtentiTab',
    'parametri.UtentiGrid',
    'parametri.UtentiForm',
        
    // templates
    'parametri.TemplatesTab',
    'parametri.TemplatesGrid',
    'parametri.TemplatesForm',
        
    // liste d'attesa
    'parametri.ListeTab',
    'parametri.ListeGrid',
    'parametri.ListeForm',
        
    // budget
    'parametri.BudgetTab',
    'parametri.BudgetTree',
    'parametri.BudgetGrid',
    'parametri.BudgetGridUot',
    'parametri.BudgetUotWidget',
    'parametri.BudgetForm',
    
    //strutture 
    'parametri.StruttureTab',
    'parametri.StruttureTree',
    'parametri.StruttureGrid',
    'parametri.StruttureForm'
    
        
    ],
    stores: ['UtentiStore', 'ParamsFormStore', 'ParamsTreeStore', 'TipologieTreeStore', 'TemplateStore', 'ListeStore', 'BudgetStore', 'BudgetTreeStore', 'BudgetUotStore','StruttureStore','StruttureTreeStore', 'DatiStore', 'AssociaDispStore', 'AssociaRelStore', 'AssociaTreeStore'],
    models: ['UtentiModel', 'ParamsModel', 'TemplateModel', 'ListeModel', 'BudgetModel', 'BudgetUotModel', 'StruttureModel','DatiModel'],

    init: function(){

    },

    onPanelRendered: function() {

    }
});