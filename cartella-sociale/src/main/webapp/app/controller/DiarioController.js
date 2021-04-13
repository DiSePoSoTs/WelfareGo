Ext.define('wcs.controller.DiarioController', {
    extend: 'Ext.app.Controller' ,
    views: ['diario.DiarioForm',
            'diario.NoteList',
            'diario.notaForm'],
    stores: ['NoteListStore'],
    models: ['NoteModel', 'DiarioModel']
});