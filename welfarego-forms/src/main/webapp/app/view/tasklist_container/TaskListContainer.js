Ext.define('wf.view.tasklist_container.TaskListContainer',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.wtl_tasklist_container',
    layout: 'fit',
    activeTab:0,
    initComponent: function() {
        this.items = [{
            itemId: 'wtl_task_list_grid',
            id: 'wtl_task_list_grid',
            xtype: 'wtl_task_list_grid',
            height:750
        },{
            itemId: 'wtl_agenda_container',
            xtype: 'wftl_agenda_container',
            height:605
        }]
        this.callParent(arguments);
    }
});