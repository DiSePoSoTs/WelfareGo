Ext.define('wcs.store.CivicoStore', {
			extend : 'Ext.data.Store',
			model : 'wcs.model.CivicoModel',
			pageSize : 10,

			proxy : {
				type : 'ajax',
				url : '/CartellaSociale/civico',
				reader : {
					type : 'json',
					rootProperty : 'civico',
					totalProperty : 'total',
					successProperty : 'success'
				}
			}
		});