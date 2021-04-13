
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Agosto 2011
 *
 * Questa classe implementa un campo di input testo (textfield) che memorizza
 * numeri in tipo primitivo float e li visualizza come stringhe nel formato
 * italiano (',' separatore delle decine, '.' separatore delle migliaia)
 */

Ext.define('Wp.library.form.field.FloatNumber', {
    extend: 'Ext.form.field.Text',
    alias: 'widget.wpfloatnumberfield',

    // numero positivo con al più 2 cifre decimali; si può utilizzare il punto '.'
    // come carattere separatore delle migliaia
    regex: /^((0|([1-9][0-9\.]*))(,[0-9]{1,2})?)$/,
    regexText: 'Il valore deve essere un numero positivo con al più due decimali',
    allowBlank: false,

    /*
     * Se settato a true in fase di creazione dell'oggetto è possibile inserire
     * anche numeri negativi
     */
    allowNegative: false,

    initComponent: function() {
        this.callParent();

        if (this.allowNegative) {
            // numero positivo o negativo, con al più 2 cifre decimali; si può
            // utilizzare il punto '.' come carattere separatore delle migliaia
            this.regex = /^(-?(0|([1-9][0-9\.]*))(,[0-9]{1,2})?)$/;
            this.regexText = 'Il valore deve essere un numero positivo o negativo con al più due decimali'
        }
    },

    /*
     * Trasforma il valore visualizzato (rappresentazione stringa di un numero
     * float con separatori delle decine e delle migliaia nel formato italiano)
     * in un tipo primitivo float che rappresenta lo stesso valore
     */
    rawToValue: function(value) {
        return Ext.isEmpty(value) ? undefined : parseFloat(value.replace(/\./g, '').replace(',', '.'));
    },
    /*
     * Effettua la trasformazione opposta rispetto al metodo precedente
     */
    valueToRaw: function(value) {
            return Ext.isEmpty(value) ? "" : Ext.util.Format.number(value, '0,000.00');
    }
});
