Ext.define('wf.view.test.TestContainer',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.wf_test_container',
    layout: 'anchor',
    fullscreen: true,
    
    items:[
{
    xtype: 'combo',
    name: 'quanti',
    fieldLabel: 'Quanti ne portiamo avanti ? ',
    mode: 'local',
    id:'cambo',
    store: new Ext.data.SimpleStore({
        fields: ['id', 'value'],
        data : [['','tutti'],['1','1'],['10','10'],['100','100']]
    }),
    displayField:'value',
    valueField: 'id'
   
  } ],
    buttons:[{
        text:'MANDALI AVANTI!!!!!!!',
        handler: function (button, event) {
        	var quanti=Ext.getCmp('cambo').getValue();  
        	Ext.Ajax.request({
        		
        	   url: wf.config.path.base+'/TestServlet?action=save&quanti='+quanti,
        	   success: function (response, options) { alert('successo'); },
        	   failure: function (response, options) { alert('fallimento'); }
        	 }
        	); 

    }}]
 
});
