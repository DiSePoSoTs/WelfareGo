Ext.define('wcs.view.anagrafica.CivicoRemoteCombo', {
	extend: 'Ext.form.field.ComboBox',
	alias: 'widget.wcs_civicoremotecombo',
	typeAhead: true,
	hideTrigger: true,
	triggerAction: 'all',
	loadingText: 'Cerca...',
	minChars: 1,

	mode: 'local',
	initComponent: function () {
		this.store = Ext.create('wcs.store.CivicoStore'),
		this.callParent(arguments);
	},

	// da persona fisica lo chiamo dalla funzione popola
    loadValue: function( codCivico, codVia){
        // qui arrivo quando leggo i dati dal back end e valorizzo i campi del form.
        // arrivo qui da AnagraficaSocForm.reloadFields e search.popolaAnagrafica
        log('loadValue codCivico, codVia: ', codCivico, codVia );

			this.store.load({
				params: {
					codice: codCivico,
					codVia: codVia
				},
				scope: this,
				synchronous: true,
				callback: function (records, operation, success) {
					if (success) {
						Ext.form.field.ComboBox.prototype.setValue.call(this, records[0].data.codCivico);
					} else {
					    Ext.form.field.ComboBox.prototype.setValue.call(this, codCivico);
					}
					this.clearInvalid();
					wcs_isModified = '';
				}
			});
    },

	setValue: function (value) {

        // qui arrivo tramite la modifica dei dati sul form da parte dell'utente


        var residenza = this.name.indexOf('Residenza');
        var domicilio = this.name.indexOf('Domicilio');
        var legale = this.name.indexOf('Legale');
        var operativa = this.name.indexOf('Operativa');
        var cmpSedeLegaleVia;
        var sedeOperativaVia;

         if( Ext.ComponentQuery.query('[name=sedeLegaleVia]')[0]){
            cmpSedeLegaleVia= Ext.ComponentQuery.query('[name=sedeLegaleVia]')[0].getStore().data.items[0];
         }

         if(Ext.ComponentQuery.query('[name=sedeOperativaVia]')[0]) {
            sedeOperativaVia = Ext.ComponentQuery.query('[name=sedeOperativaVia]')[0].getStore().data.items[0]
         }

        if (residenza > 0) {
            codVia = Ext.getCmp('wcs_anagraficaViaResidenza').getValue();

        } else if (domicilio > 0) {
            codVia = Ext.getCmp('wcs_anagraficaViaDomicilio').getValue();

        } else if (legale > 0 && cmpSedeLegaleVia) {
            codVia = cmpSedeLegaleVia.data.codVia;

        } else if (sedeOperativaVia) {
            codVia = sedeOperativaVia.data.codVia;

        } else {
            Ext.form.field.ComboBox.prototype.setValue.call(this, value);
        }

		if (Number.parseInt(value) && !value[0].raw && !(this.getValue() === value)) {
			this.store.load({
				params: {
					codice: value,
					codVia: codVia
				},
				scope: this,
				synchronous: true,
				callback: function (records, operation, success) {
					if (success) {
						Ext.form.field.ComboBox.prototype.setValue.call(this, records[0].data.codCivico);
					} else {
					    Ext.form.field.ComboBox.prototype.setValue.call(this, value);
					}
					this.clearInvalid();
					wcs_isModified = '';
				}
			});
		}

		else if ( codVia && value && value[0] && value[0].data  && value[0].data.codCivico) {
            // qui dovrei arrivare quando l'utente modifica il campo a mano
            			this.store.load({
            				params: {
            					codice: value[0].data.codCivico,
            					codVia: codVia
            				},
            				scope: this,
            				synchronous: true,
            				callback: function (records, operation, success) {
            					if (success) {
            						Ext.form.field.ComboBox.prototype.setValue.call(this, records[0].data.codCivico);
                                    // così vedo il codice e non la descrizione
                                    // Ext.form.field.ComboBox.prototype.setRawValue.call(this, records[0].data.codVia);
            					} else {
            					    Ext.form.field.ComboBox.prototype.setValue.call(this, value);
            					}
            					this.clearInvalid();
            					wcs_isModified = '';
            				}
            			});
		}

		else {
			this.callParent(arguments);
		}

	},

	reset: function () {
		this.store.removeAll(false);
		//This is too dirty :)
		//wcs.view.anagrafica.CivicoRemoteCombo.superclass.setValue.call(this, '');
		this.superclass.setValue.call(this, '');
	}
});