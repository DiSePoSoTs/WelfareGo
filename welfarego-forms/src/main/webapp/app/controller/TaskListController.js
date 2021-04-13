Ext.define('wf.controller.TaskListController', {
    extend: 'Ext.app.Controller' ,
    views: ['tasklist_container.TaskListContainer',
    'tasklist.TaskListGrid',
    'tasklist.TaskListBottomBar',
    'tasklist.TaskListTopBar',
//    'agenda.AgendaNew',
    'agenda.Container'
],
    stores: ['combo.AssistenteStore','combo.UotStore','combo.TaskSearchStore','TaskListStore', 'combo.POStore', 'combo.InterventiComboStore', 'combo.AttivitaComboStore'],
    models: ['ComboModel','TaskModel']
});