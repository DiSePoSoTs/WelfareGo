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
    daterange : function(val, field) {
        var date = field.parseDate(val);

        if(!date){
            return;
        }
        if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
            var start = Ext.getCmp(field.startDateField);
            start.setMaxValue(date);
            start.validate();
            this.dateRangeMax = date;
        }
        else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
            var end = Ext.getCmp(field.endDateField);
            end.setMinValue(date);
            end.validate();
            this.dateRangeMin = date;
        }
        /*
         * Always return true since we're only using this vtype to set the
         * min/max allowed values (these are tested for after the vtype test)
         */
        return true;
    }
});



Ext.apply(Ext.form.VTypes, {
    timerange : function(val, field) {
        var time = field.parseDate(val);
        if(!time){
            return;
        }
        if (field.startTimeField && (!this.timeRangeMax || (time.getTime() != this.timeRangeMax.getTime()))) {
            var start = Ext.getCmp(field.startTimeField);
            start.maxValue = time;
            start.validate();
            this.timeRangeMax = time;
        }
        else if (field.endTimeField && (!this.timeRangeMin || (time.getTime() != this.timeRangeMin.getTime()))) {
            var end = Ext.getCmp(field.endTimeField);
            end.minValue = time;
            end.validate();
            this.timeRangeMin = time;
        }
        return true;
    }
});

// filtra file .odt
Ext.apply(Ext.form.VTypes, {
    odt: function(v){
        return /^.*\.(odt)$/.test(v);
    },
    odtText: 'Solo file OpenOffice (.odt)'
});

// Formato valuta europea
Ext.apply(Ext.util.Format, {
   
    euCurrency: function(v)
    {
        v = (Math.round((v-0)*100))/100;
        v = (v == Math.floor(v)) ? v + ".00" : ((v*10 == Math.floor(v*10)) ? v + "0" : v);
        return ('&euro;' + v).replace(/\./, ',');
    }
   
});