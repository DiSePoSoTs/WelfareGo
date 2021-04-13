Ext.apply(Ext.form.field.VTypes, {
    CodiceFiscale:  function(v) {
        return /^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$/.test(v);
    },
    CodiceFiscaleText: 'Deve essere un codice fiscale corretto'
});

Ext.apply(Ext.form.field.VTypes, {
    Ora:  function(v) {
        //        return /^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?$/.test(v);
        return /^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])$/.test(v);
    },
    OraText: 'Deve avere il formato hh:mm'
});

Ext.apply(Ext.form.field.VTypes, {
    FiveNums:  function(v) {
        return /^[0-9]{5}$/.test(v);
    },
    FiveNumsText: 'Devono esserci 5 numeri'
});

Ext.apply(Ext.form.field.VTypes, {
    TwoNums:  function(v) {
        return /^[0-9]{2}$/.test(v);
    },
    TwoNumsText: 'Devono esserci 2 numeri'
});


//
//Euro format
//http://www.sencha.com/forum/showthread.php?5499-Other-currency-formats-other-than-usMoney&p=313850&viewfull=1#post313850
Euro = function(v) {
    v = Ext.util.Format.usMoney(v);
//    if (v=='$0.00')
//        return '-';
//    else
//        return v.replace(/\$/, '').replace(/\,/g, ' ').replace(/\./, ',')+' &euro;';
//        return v.replace(/\$/, '').replace(/\./g, ".").replace(/\,/g, ",")+' &euro;';
        return v.replace(/\$/, '')+' &euro;';
}