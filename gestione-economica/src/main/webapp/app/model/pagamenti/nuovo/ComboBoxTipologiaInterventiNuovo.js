
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Agosto 2011
 *
 * Questo è il model per le tipologie d'intervento
 */

Ext.define('Wp.model.pagamenti.nuovo.ComboBoxTipologiaInterventiNuovo', {
    extend: 'Ext.data.Model',
    fields: ['id', 'label', 'unita_di_misura', 'importo_unitario']
});

