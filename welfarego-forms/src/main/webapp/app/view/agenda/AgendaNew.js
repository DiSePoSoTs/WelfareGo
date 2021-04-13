(function(){
    
    //    Ext.require([
    //        'Ext.Viewport',
    //        'Ext.layout.container.Border',
    //        'Extensible.calendar.CalendarPanel',
    //        'Extensible.calendar.gadget.CalendarListPanel',
    //        'Extensible.calendar.data.MemoryCalendarStore',
    //        'Extensible.calendar.data.MemoryEventStore'
    //        ]);
        
        
    Ext.define('wf.view.agenda.AgendaNew',{
        extend: 'Ext.Panel',
        alias: 'widget.wftl_agenda_new',
        //	title: 'Agenda',
        //	autoHeight:true,
        //	layout: 'border',
        initComponent: function() {
        
            var calendarStore = Ext.create('Extensible.calendar.data.MemoryCalendarStore', {
                data:[{
                    "id"    : 2,
                    "title" : "Indisponibilita'",
                    "color" : 2
                },{
                    "id"    : 1,
                    "title" : "Appuntamenti",
                    "color" : 26
                }]
            //                autoLoad: true,
            //                proxy: {
            //                    type: 'ajax',
            //                    //            url: '../data/calendars.json',
            //                    url: 'unexistingUrl',
            //                    noCache: false,            
            //                    reader: {
            //                        type: 'json',
            //                        root: 'calendars'
            //                    }
            //                }
            });
    
            var eventStore = Ext.create('Extensible.calendar.data.EventStore', {
                autoLoad: true,
                id:'extensibleAgendaEventStore',
                proxy: {
                    type: 'rest',
                    //            url: 'php/app.php/events',
                    url: '/WelfaregoForms/ExtensibleAgendaServlet',
                    noCache: false,
                    extraParams:{
                        codUte:wf.info.user ? wf.info.user.id  : 'nouser'
                    },
                    reader: {
                        type: 'json',
                        root: 'data'
                    },            
                    writer: {
                        type: 'json',
                        nameProperty: 'mapping'
                    },            
                    listeners: {
                        exception: function(proxy, response, operation, options){
                            var msg = response.message ? response.message : Ext.decode(response.responseText).message;
                            // ideally an app would provide a less intrusive message display
                            Ext.Msg.alert('Server Error', msg);
                        }
                    }
                },

                // It's easy to provide generic CRUD messaging without having to handle events on every individual view.
                // Note that while the store provides individual add, update and remove events, those fire BEFORE the
                // remote transaction returns from the server -- they only signify that records were added to the store,
                // NOT that your changes were actually persisted correctly in the back end. The 'write' event is the best
                // option for generically messaging after CRUD persistence has succeeded.
                listeners: {
                    write: function(store, operation){
                        var title = Ext.value(operation.records[0].data[Extensible.calendar.data.EventMappings.Title.name], '(No title)');
                        switch(operation.action){
                            case 'create':
                                Ext.Msg.alert('Add', 'Added "' + title + '"');
                                break;
                            case 'update':
                                Ext.Msg.alert('Update', 'Updated "' + title + '"');
                                break;
                            case 'destroy':
                                Ext.Msg.alert('Delete', 'Deleted "' + title + '"');
                                break;
                        }
                    }
                }
            });
    
            //    var eventStore = Ext.create('Extensible.calendar.data.MemoryEventStore', {
            //        //        // defined in ../data/Events.js
            //        //        data: Ext.create('Extensible.example.calendar.data.Events')
            //        data:{"evts":[{"id":1001,"cid":1,"title":"Vacation","start":"2012-10-17T10:00:00","end":"2012-10-27T15:00:00","notes":"Have fun"},{"id":1002,"cid":2,"title":"Lunch with Matt","start":"2012-11-06T11:30:00","end":"2012-11-06T13:00:00","loc":"Chuy's!","url":"http : //chuys.com","notes":"Order the queso","rem":"15"},{"id":1003,"cid":3,"title":"Project due","start":"2012-11-06T15:00:00","end":"2012-11-06T15:00:00"},{"id":1004,"cid":1,"title":"Sarah's birthday","start":"2012-11-06T00:00:00","end":"2012-11-06T00:00:00","notes":"Need to get a gift","ad":true},{"id":1005,"cid":2,"title":"A long one...","start":"2012-10-25T00:00:00","end":"2012-11-15T23:59:59","ad":true},{"id":1006,"cid":3,"title":"School holiday","start":"2012-11-11T00:00:00","end":"2012-11-12T23:59:59","ad":true,"rem":"2880"},{"id":1007,"cid":1,"title":"Haircut","start":"2012-11-06T09:00:00","end":"2012-11-06T09:30:00","notes":"Get cash on the way"},{"id":1008,"cid":3,"title":"An old event","start":"2012-10-07T00:00:00","end":"2012-10-09T00:00:00","ad":true},{"id":1009,"cid":2,"title":"Board meeting","start":"2012-11-04T13:00:00","end":"2012-11-04T18:00:00","loc":"ABC Inc.","rem":"60"},{"id":1010,"cid":3,"title":"Jenny's final exams","start":"2012-11-04T00:00:00","end":"2012-11-08T23:59:59","ad":true},{"id":1011,"cid":1,"title":"Movie night","start":"2012-11-08T19:00:00","end":"2012-11-08T23:00:00","notes":"Don't forget the tickets!","rem":"60"},{"id":1012,"cid":4,"title":"Gina's basketball tournament","start":"2012-11-14T08:00:00","end":"2012-11-16T17:00:00"},{"id":1013,"cid":4,"title":"Toby's soccer game","start":"2012-11-11T10:00:00","end":"2012-11-11T12:00:00"}]}
            //    });\
            var startHour=7,endHour=20;

            var agenda=Ext.create('Extensible.calendar.CalendarPanel', {
                id: 'extensibleCalendar',
                eventStore: eventStore,
                calendarStore: calendarStore,
                //        renderTo: 'simple',
                //                title: 'Agenda',
                height:565,
                viewConfig :{
                    viewStartHour : startHour,
                    viewEndHour : endHour
                }
                
            //        width: 700,
            //        height: 500
            });
    
            this.items=[agenda];
    
            this.callParent(arguments);
        }
    });
//    // This is an example calendar store that enables event color-coding
//    var calendarStore = Ext.create('Extensible.calendar.data.MemoryCalendarStore', {
//        // defined in ../data/Calendars.js
//        data: []//Ext.create('Extensible.example.calendar.data.Calendars')
//    });
//
//    // A sample event store that loads static JSON from a local file. Obviously a real
//    // implementation would likely be loading remote data via an HttpProxy, but the
//    // underlying store functionality is the same.
//    var eventStore = Ext.create('Extensible.calendar.data.MemoryEventStore', {
//        // defined in ../data/Events.js
//        data: [],//Ext.create('Extensible.example.calendar.data.Events'),
//        // This disables the automatic CRUD messaging built into the sample data store.
//        // This test application will provide its own custom messaging. See the source
//        // of MemoryEventStore to see how automatic store messaging is implemented.
//        autoMsg: false
//    });
//        
//    // This is the app UI layout code.  All of the calendar views are subcomponents of
//    // CalendarPanel, but the app title bar and sidebar/navigation calendar are separate
//    // pieces that are composed in app-specific layout code since they could be omitted
//    // or placed elsewhere within the application.
//    var agenda=Ext.create('Ext.Viewport', {
//        layout: 'border',
//        //            renderTo: 'calendar-ct',
//        items: [{
//            id: 'app-header',
//            region: 'north',
//            height: 35,
//            border: false,
//            contentEl: 'app-header-content'
//        },{
//            id: 'app-center',
//            title: '...', // will be updated to the current view's date range
//            region: 'center',
//            layout: 'border',
//            listeners: {
//                'afterrender': function(){
//                    Ext.getCmp('app-center').header.addCls('app-center-header');
//                }
//            },
//            items: [{
//                id:'app-west',
//                region: 'west',
//                width: 179,
//                border: false,
//                items: [{
//                    xtype: 'datepicker',
//                    id: 'app-nav-picker',
//                    cls: 'ext-cal-nav-picker',
//                    listeners: {
//                        'select': {
//                            fn: function(dp, dt){
//                                Ext.getCmp('app-calendar').setStartDate(dt);
//                            },
//                            scope: this
//                        }
//                    }
//                },{
//                    xtype: 'extensible.calendarlist',
//                    store: calendarStore,
//                    border: false,
//                    width: 178
//                }]
//            },{
//                xtype: 'extensible.calendarpanel',
//                eventStore: eventStore,
//                calendarStore: calendarStore,
//                border: false,
//                id:'app-calendar',
//                region: 'center',
//                activeItem: 3, // month view
//                    
//                // Any generic view options that should be applied to all sub views:
//                viewConfig: {
//                    //enableFx: false,
//                    //ddIncrement: 10, //only applies to DayView and subclasses, but convenient to put it here
//                    //viewStartHour: 6,
//                    //viewEndHour: 18,
//                    //minEventDisplayMinutes: 15
//                    showTime: false
//                },
//                    
//                // View options specific to a certain view (if the same options exist in viewConfig
//                // they will be overridden by the view-specific config):
//                monthViewCfg: {
//                    showHeader: true,
//                    showWeekLinks: true,
//                    showWeekNumbers: true
//                },
//                    
//                multiWeekViewCfg: {
//                //weekCount: 3
//                },
//                    
//                // Some optional CalendarPanel configs to experiment with:
//                //readOnly: true,
//                //showDayView: false,
//                //showMultiDayView: true,
//                //showWeekView: false,
//                //showMultiWeekView: false,
//                //showMonthView: false,
//                //showNavBar: false,
//                //showTodayText: false,
//                //showTime: false,
//                //editModal: true,
//                //enableEditDetails: false,
//                //title: 'My Calendar', // the header of the calendar, could be a subtitle for the app
//                    
//                listeners: {
//                    'eventclick': {
//                        fn: function(vw, rec, el){
//                            clearMsg();
//                        },
//                        scope: this
//                    },
//                    'eventover': function(vw, rec, el){
//                    //console.log('Entered evt rec='+rec.data[Extensible.calendar.data.EventMappings.Title.name]', view='+ vw.id +', el='+el.id);
//                    },
//                    'eventout': function(vw, rec, el){
//                    //console.log('Leaving evt rec='+rec.data[Extensible.calendar.data.EventMappings.Title.name]+', view='+ vw.id +', el='+el.id);
//                    },
//                    'eventadd': {
//                        fn: function(cp, rec){
//                            showMsg('Event '+ rec.data[Extensible.calendar.data.EventMappings.Title.name] +' was added');
//                        },
//                        scope: this
//                    },
//                    'eventupdate': {
//                        fn: function(cp, rec){
//                            showMsg('Event '+ rec.data[Extensible.calendar.data.EventMappings.Title.name] +' was updated');
//                        },
//                        scope: this
//                    },
//                    'eventdelete': {
//                        fn: function(cp, rec){
//                            showMsg('Event '+ rec.data[Extensible.calendar.data.EventMappings.Title.name] +' was deleted');
//                        },
//                        scope: this
//                    },
//                    'eventcancel': {
//                        fn: function(cp, rec){
//                        // edit canceled
//                        },
//                        scope: this
//                    },
//                    'viewchange': {
//                        fn: function(p, vw, dateInfo){
//                            if(dateInfo){
//                                updateTitle(dateInfo.viewStart, dateInfo.viewEnd);
//                            }
//                        },
//                        scope: this
//                    },
//                    'dayclick': {
//                        fn: function(vw, dt, ad, el){
//                            clearMsg();
//                        },
//                        scope: this
//                    },
//                    'rangeselect': {
//                        fn: function(vw, dates, onComplete){
//                            clearMsg();
//                        },
//                        scope: this
//                    },
//                    'eventmove': {
//                        fn: function(vw, rec){
//                            var mappings = Extensible.calendar.data.EventMappings,
//                            time = rec.data[mappings.IsAllDay.name] ? '' : ' \\a\\t g:i a';
//                                
//                            rec.commit();
//                                
//                            showMsg('Event '+ rec.data[mappings.Title.name] +' was moved to '+
//                                Ext.Date.format(rec.data[mappings.StartDate.name], ('F jS'+time)));
//                        },
//                        scope: this
//                    },
//                    'eventresize': {
//                        fn: function(vw, rec){
//                            rec.commit();
//                            showMsg('Event '+ rec.data[Extensible.calendar.data.EventMappings.Title.name] +' was updated');
//                        },
//                        scope: this
//                    },
//                    'eventdelete': {
//                        fn: function(win, rec){
//                            eventStore.remove(rec);
//                            showMsg('Event '+ rec.data[Extensible.calendar.data.EventMappings.Title.name] +' was deleted');
//                        },
//                        scope: this
//                    },
//                    'initdrag': {
//                        fn: function(vw){
//                        // do something when drag starts
//                        },
//                        scope: this
//                    }
//                }
//            }]
//        }]
//    });
//    
//    // The CalendarPanel itself supports the standard Panel title config, but that title
//    // only spans the calendar views.  For a title that spans the entire width of the app
//    // we added a title to the layout's outer center region that is app-specific. This code
//    // updates that outer title based on the currently-selected view range anytime the view changes.
//    var updateTitle= function(startDt, endDt){
//        var p = Ext.getCmp('app-center'),
//        fmt = Ext.Date.format;
//        
//        if(Ext.Date.clearTime(startDt).getTime() == Ext.Date.clearTime(endDt).getTime()){
//            p.setTitle(fmt(startDt, 'F j, Y'));
//        }
//        else if(startDt.getFullYear() == endDt.getFullYear()){
//            if(startDt.getMonth() == endDt.getMonth()){
//                p.setTitle(fmt(startDt, 'F j') + ' - ' + fmt(endDt, 'j, Y'));
//            }
//            else{
//                p.setTitle(fmt(startDt, 'F j') + ' - ' + fmt(endDt, 'F j, Y'));
//            }
//        }
//        else{
//            p.setTitle(fmt(startDt, 'F j, Y') + ' - ' + fmt(endDt, 'F j, Y'));
//        }
//    },
//    
//    // This is an application-specific way to communicate CalendarPanel event messages back to the user.
//    // This could be replaced with a function to do "toast" style messages, growl messages, etc. This will
//    // vary based on application requirements, which is why it's not baked into the CalendarPanel.
//    showMsg= function(msg){
//        Ext.fly('app-msg').update(msg).removeCls('x-hidden');
//    },
//    
//    clearMsg= function(){
//        Ext.fly('app-msg').update('').addCls('x-hidden');
//    };
//    
//    //    
//    Ext.define('wf.view.agenda.AgendaNew',{
//        extend: 'Ext.Panel',
//        alias: 'widget.wftl_agenda_new',
//        //	title: 'Agenda',
//        //	autoHeight:true,
//        //	layout: 'border',
//        items:[agenda]
//    });
})();