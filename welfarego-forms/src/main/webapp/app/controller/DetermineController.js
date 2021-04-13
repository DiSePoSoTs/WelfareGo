Ext.define('wf.controller.DetermineController', {
	extend: 'Ext.app.Controller',
	views: ['determine.DetermineContainer','determine.DetermineGrid','determine.DetermineTopBar','determine.DetermineProrogheWindow','determine.DetermineBudgetWindow'],
	stores: ['combo.ClassiTipologieInterventoStore','combo.TipologieInterventoStore','combo.StatoInterventoStore','DetermineStore','ImpegniProrogheDetermineStore','ImpegniBudgetStore'],
	models: ['ComboModel','DetermineModel','ImpegnoProrogheDetermineModel','ImpegnoModel']
});