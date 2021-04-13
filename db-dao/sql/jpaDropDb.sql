ALTER TABLE ANAGRAFE_FAM DROP CONSTRAINT FK_ANAGRAFE_FAM_COD_ANA_FAM
ALTER TABLE ANAGRAFE_FAM DROP CONSTRAINT FK_ANAGRAFE_FAM_COD_QUAL
ALTER TABLE ANAGRAFE_FAM DROP CONSTRAINT FK_ANAGRAFE_FAM_COD_ANA
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT FK_ANAGRAFE_SOC_COD_STATO_NAZ
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT FK_ANAGRAFE_SOC_COD_STATO_CITT
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT FK_ANAGRAFE_SOC_COD_LUOGO_RES
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT ANAGRAFE_SOC_ID_PARAM_TIP_ALL
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT FK_ANAGRAFE_SOC_COD_LUOGO_DOM
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT FK_ANAGRAFE_SOC_COD_SEGN_DA
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT FK_ANAGRAFE_SOC_ID_PARAM_REDD
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT NGRFESOCDPRMTIPOLOGIARESIDENZA
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT ANAGRAFE_SOCID_PARAM_COND_PROF
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT ANAGRAFE_SOCID_PARAM_STATO_FIS
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT FK_ANAGRAFE_SOC_COD_LUOGO_NASC
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT ANAGRAFE_SOCID_PARAM_MOD_PAGAM
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT FK_ANAGRAFE_SOC_ID_PARAM_TIT
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT FK_ANAGRAFE_SOC_COND_FAM
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT ANAGRAFE_SOC_ID_PARAM_POS_ANA
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT ANAGRAFE_SOCID_PARAM_STATO_CIV
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT UNQ_ANAGRAFE_SOC_0
ALTER TABLE ANAGRAFE_SOC DROP CONSTRAINT UNQ_ANAGRAFE_SOC_1
ALTER TABLE APPUNTAMENTO DROP CONSTRAINT FK_APPUNTAMENTO_COD_AS
ALTER TABLE APPUNTAMENTO DROP CONSTRAINT FK_APPUNTAMENTO_COD_UTE
ALTER TABLE APPUNTAMENTO DROP CONSTRAINT FK_APPUNTAMENTO_COD_PAI
ALTER TABLE BUDGET_TIP_INTERVENTO DROP CONSTRAINT BUDGETTIP_INTERVENTOCOD_TIPINT
ALTER TABLE BUDGET_TIP_INTERVENTO DROP CONSTRAINT UNQ_BUDGET_TIP_INTERVENTO_0
ALTER TABLE BUDGET_TIP_INTERVENTO_UOT DROP CONSTRAINT BDGETTIPINTERVENTOUOTCODTIPINT
ALTER TABLE BUDGET_TIP_INTERVENTO_UOT DROP CONSTRAINT BDGETTIPINTERVENTOUOTDPARAMUOT
ALTER TABLE CARTELLA_SOCIALE DROP CONSTRAINT CARTELLASOCIALEIDPARAM_TIP_ALL
ALTER TABLE CARTELLA_SOCIALE DROP CONSTRAINT FK_CARTELLA_SOCIALE_COD_ANA
ALTER TABLE COMUNE DROP CONSTRAINT FK_COMUNE_COD_PROV
ALTER TABLE CONTATTO DROP CONSTRAINT FK_CONTATTO_COD_ANA
ALTER TABLE CONTATTO DROP CONSTRAINT FK_CONTATTO_COD_UTE
ALTER TABLE CONTATTO DROP CONSTRAINT FK_CONTATTO_ID_PARAM_MOTIV
ALTER TABLE FATTURA DROP CONSTRAINT FK_FATTURA_ID_PARAM_STATO
ALTER TABLE FATTURA DROP CONSTRAINT FK_FATTURA_COD_TIPINT
ALTER TABLE FATTURA DROP CONSTRAINT FK_FATTURA_ID_PARAM_FASCIA
ALTER TABLE FATTURA DROP CONSTRAINT FK_FATTURA_COD_ANA
ALTER TABLE FATTURA DROP CONSTRAINT FK_FATTURA_ID_PARAM_PAGAM
ALTER TABLE FATTURA DROP CONSTRAINT FK_FATTURA_ID_PARAM_IVA
ALTER TABLE FATTURA_DETTAGLIO DROP CONSTRAINT FK_FATTURA_DETTAGLIO_ID_FATT
ALTER TABLE FATTURA_DETTAGLIO DROP CONSTRAINT FATTURA_DETTAGLIO_COD_TIPINT
ALTER TABLE FATTURA_DETTAGLIO DROP CONSTRAINT FTTRADETTAGLIODPRAMUNITAMISURA
ALTER TABLE INDISPONIBILITA DROP CONSTRAINT FK_INDISPONIBILITA_COD_AS
ALTER TABLE LOG_ANAGRAFE DROP CONSTRAINT FK_LOG_ANAGRAFE_COD_ANA
ALTER TABLE LOG_ANAGRAFE DROP CONSTRAINT FK_LOG_ANAGRAFE_COD_PAI
ALTER TABLE LOG_ANAGRAFE DROP CONSTRAINT FK_LOG_ANAGRAFE_COD_UTE
ALTER TABLE LOG_MESSAGGI DROP CONSTRAINT FK_LOG_MESSAGGI_COD_TIPINT
ALTER TABLE MANDATO DROP CONSTRAINT MANDATO_COD_ANA_BENEFICIARIO
ALTER TABLE MANDATO DROP CONSTRAINT FK_MANDATO_ID_PARAM_STATO
ALTER TABLE MANDATO DROP CONSTRAINT FK_MANDATO_COD_TIPINT
ALTER TABLE MANDATO DROP CONSTRAINT FK_MANDATO_COD_ANA_DELEGANTE
ALTER TABLE MANDATO DROP CONSTRAINT FK_MANDATO_ID_PARAM_FASCIA
ALTER TABLE MANDATO_DETTAGLIO DROP CONSTRAINT MANDATO_DETTAGLIO_COD_TIPINT
ALTER TABLE MANDATO_DETTAGLIO DROP CONSTRAINT FK_MANDATO_DETTAGLIO_ID_MAN
ALTER TABLE MANDATO_DETTAGLIO DROP CONSTRAINT MNDTODETTAGLIODPRAMUNITAMISURA
ALTER TABLE MAP_DATI_SPEC_TIPINT DROP CONSTRAINT MAP_DATI_SPEC_TIPINT_COD_CAMPO
ALTER TABLE MAP_DATI_SPEC_TIPINT DROP CONSTRAINT MAP_DATI_SPEC_TIPINTCOD_TIPINT
ALTER TABLE MAP_DATI_SPECIFICI_INTERVENTO DROP CONSTRAINT MPDTSPECIFICIINTERVENTOCDTPINT
ALTER TABLE MAP_DATI_SPECIFICI_INTERVENTO DROP CONSTRAINT MPDTISPECIFICIINTERVENTOCDCMPO
ALTER TABLE PAI DROP CONSTRAINT FK_PAI_COD_ANA
ALTER TABLE PAI DROP CONSTRAINT FK_PAI_ID_PARAM_FASCIA
ALTER TABLE PAI DROP CONSTRAINT FK_PAI_ID_PARAM_UOT
ALTER TABLE PAI DROP CONSTRAINT PAI_ID_PARAM_PROVV_GIUDIZIARIO
ALTER TABLE PAI DROP CONSTRAINT FK_PAI_COD_UTE_AS
ALTER TABLE PAI DROP CONSTRAINT PAI_ID_PARAM_CERTIFICATO_L104
ALTER TABLE PAI_CDG DROP CONSTRAINT FK_PAI_CDG_COD_TIPINT
ALTER TABLE PAI_CDG DROP CONSTRAINT FK_PAI_CDG_COD_ANA
ALTER TABLE PAI_DOCUMENTO DROP CONSTRAINT FK_PAI_DOCUMENTO_COD_PAI
ALTER TABLE PAI_DOCUMENTO DROP CONSTRAINT FK_PAI_DOCUMENTO_COD_TIPINT
ALTER TABLE PAI_DOCUMENTO DROP CONSTRAINT FK_PAI_DOCUMENTO_COD_UTE_AUT
ALTER TABLE PAI_DOCUMENTO DROP CONSTRAINT UNQ_PAI_DOCUMENTO_0
ALTER TABLE PAI_EVENTO DROP CONSTRAINT FK_PAI_EVENTO_COD_PAI
ALTER TABLE PAI_EVENTO DROP CONSTRAINT FK_PAI_EVENTO_COD_UTE
ALTER TABLE PAI_EVENTO DROP CONSTRAINT FK_PAI_EVENTO_COD_TMPL
ALTER TABLE PAI_EVENTO DROP CONSTRAINT FK_PAI_EVENTO_ID_DETERMINA
ALTER TABLE PAI_EVENTO DROP CONSTRAINT FK_PAI_EVENTO_COD_TIPINT
ALTER TABLE PAI_INTERVENTO DROP CONSTRAINT PINTERVENTODSIDPARAMFASCIAREDD
ALTER TABLE PAI_INTERVENTO DROP CONSTRAINT PAIINTERVENTODSID_PARAM_STRUTT
ALTER TABLE PAI_INTERVENTO DROP CONSTRAINT FK_PAI_INTERVENTO_COD_PAI
ALTER TABLE PAI_INTERVENTO DROP CONSTRAINT FK_PAI_INTERVENTO_COD_TIPINT
ALTER TABLE PAI_INTERVENTO DROP CONSTRAINT PAI_INTERVENTODS_COD_ANA_BENEF
ALTER TABLE PAI_INTERVENTO_ANAGRAFICA DROP CONSTRAINT PIINTERVENTOANAGRAFICACDTIPINT
ALTER TABLE PAI_INTERVENTO_ANAGRAFICA DROP CONSTRAINT PNTRVNTOANAGRAFICADPRMQLANASOC
ALTER TABLE PAI_INTERVENTO_ANAGRAFICA DROP CONSTRAINT PIINTERVENTOANAGRAFICACDANASOC
ALTER TABLE PAI_INTERVENTO_CIV_OBB DROP CONSTRAINT PAIINTERVENTOCIV_OBBCOD_ANA_CO
ALTER TABLE PAI_INTERVENTO_CIV_OBB DROP CONSTRAINT PAIINTERVENTOCIV_OBBCOD_TIPINT
ALTER TABLE PAI_INTERVENTO_MESE DROP CONSTRAINT PNTRVNTMESEMTVZNVRIAZIONESPESA
ALTER TABLE PAI_INTERVENTO_MESE DROP CONSTRAINT PAIINTERVENTOMESEDMANDETTAGLIO
ALTER TABLE PAI_INTERVENTO_MESE DROP CONSTRAINT PIINTERVENTOMESEDFATTDETTAGLIO
ALTER TABLE PAI_INTERVENTO_MESE DROP CONSTRAINT PAI_INTERVENTO_MESE_COD_TIPINT
ALTER TABLE PARAMETRI DROP CONSTRAINT FK_PARAMETRI_TIP_PARAM
ALTER TABLE PARAMETRI DROP CONSTRAINT UNQ_PARAMETRI_0
ALTER TABLE PARAMETRI_INDATA DROP CONSTRAINT FK_PARAMETRI_INDATA_ID_PARAM
ALTER TABLE PROVINCIA DROP CONSTRAINT FK_PROVINCIA_COD_STATO
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TPLOGIAINTERVENTOCDTMPLLETTPAG
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TIPOLOGIAINTERVENTOCODTMPL_VAR
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TPOLOGIAINTERVENTOCDTMPLESEMUL
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TPOLOGIAINTERVENTOCODTMPLCHIUS
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TIPOLOGIAINTERVENTOCODTMPL_ESE
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TPLGIAINTERVENTOCDTMPLRICEVUTA
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TPLGIAINTERVENTODPRAMSTRUTTURA
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TPLGIAINTERVENTOCDTMPLCHIUSMUL
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TPOLOGIAINTERVENTOCDTMPLCOMLIQ
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TPLGINTERVENTODPRMCLASSETIPINT
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TIPOLOGIAINTERVENTOCODLISTAATT
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TIPOLOGIAINTERVENTOIDPARAM_SRV
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TPOLOGIAINTERVENTODPARAMUNIMIS
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TPOLOGIAINTERVENTOCDTMPLVARMUL
ALTER TABLE TIPOLOGIA_INTERVENTO DROP CONSTRAINT TIPOLOGIAINTERVENTODPARAMGRUTE
ALTER TABLE TOPONOMASTICA DROP CONSTRAINT FK_TOPONOMASTICA_COD_COM
ALTER TABLE TOPONOMASTICA_CIVICI DROP CONSTRAINT TOPONOMASTICA_CIVICI_COD_VIA
ALTER TABLE TOPONOMASTICA_CIVICI DROP CONSTRAINT TPONOMASTICACIVICIDPARAMUOTRIF
ALTER TABLE UNIQUE_TASKLIST DROP CONSTRAINT FK_UNIQUE_TASKLIST_COD_TMPL
ALTER TABLE UNIQUE_TASKLIST DROP CONSTRAINT FK_UNIQUE_TASKLIST_COD_TIPINT
ALTER TABLE UNIQUE_TASKLIST DROP CONSTRAINT FK_UNIQUE_TASKLIST_FORM
ALTER TABLE UNIQUE_TASKLIST DROP CONSTRAINT FK_UNIQUE_TASKLIST_COD_PAI
ALTER TABLE UTENTI DROP CONSTRAINT FK_UTENTI_ID_PARAM_SER
ALTER TABLE UTENTI DROP CONSTRAINT FK_UTENTI_ID_PARAM_UOT
ALTER TABLE UTENTI DROP CONSTRAINT FK_UTENTI_ID_PARAM_LVL_ABIL
ALTER TABLE UTENTI DROP CONSTRAINT UNQ_UTENTI_0
ALTER TABLE PAI_MACRO_PROBLEMATICHE DROP CONSTRAINT PMCRPRBLMATICHEPBTTVPREVALENTE
ALTER TABLE PAI_MACRO_PROBLEMATICHE DROP CONSTRAINT PMCRPRBLEMATICHEPFRNTGGIAMENTO
ALTER TABLE PAI_MACRO_PROBLEMATICHE DROP CONSTRAINT PMCRPRBLEMATICHEPMCRPRBLMATICA
ALTER TABLE PAI_MACRO_PROBLEMATICHE DROP CONSTRAINT PIMACROPROBLEMATICHEPRILEVANZA
ALTER TABLE PAI_MACRO_PROBLEMATICHE DROP CONSTRAINT PAI_MACRO_PROBLEMATICHECOD_PAI
ALTER TABLE PAI_MICRO_PROBLEMATICHE DROP CONSTRAINT PAI_MICRO_PROBLEMATICHECOD_PAI
ALTER TABLE PAI_MICRO_PROBLEMATICHE DROP CONSTRAINT PMCRPRBLEMATICHEPMCRPRBLMATICA
ALTER TABLE LUOGO DROP CONSTRAINT FK_LUOGO_COD_CIV
ALTER TABLE LUOGO DROP CONSTRAINT FK_LUOGO_COD_COM
ALTER TABLE LUOGO DROP CONSTRAINT FK_LUOGO_COD_VIA
ALTER TABLE LUOGO DROP CONSTRAINT FK_LUOGO_COD_STATO
ALTER TABLE LUOGO DROP CONSTRAINT FK_LUOGO_COD_PROV
ALTER TABLE FATTURA_MESI_PRECEDENTI DROP CONSTRAINT FTTURAMESIPRECEDENTIDFATTPRINC
ALTER TABLE FATTURA_MESI_PRECEDENTI DROP CONSTRAINT FATTURAMESIPRECEDENTIDFATTCORR
DROP TABLE ANAGRAFE_FAM
DROP TABLE ANAGRAFE_SOC
DROP TABLE APPUNTAMENTO
DROP TABLE BUDGET_TIP_INTERVENTO
DROP TABLE BUDGET_TIP_INTERVENTO_UOT
DROP TABLE CARTELLA_SOCIALE
DROP TABLE COMUNE
DROP TABLE CONFIGURATION
DROP TABLE CONTATTO
DROP TABLE DATI_SPECIFICI
DROP TABLE DETERMINE
DROP TABLE FATTURA
DROP TABLE FATTURA_DETTAGLIO
DROP TABLE INDISPONIBILITA
DROP TABLE LIQUIDAZIONE
DROP TABLE LISTA_ATTESA
DROP TABLE LOG_ANAGRAFE
DROP TABLE LOG_MESSAGGI
DROP TABLE MANDATO
DROP TABLE MANDATO_DETTAGLIO
DROP TABLE MAP_DATI_SPEC_TIPINT
DROP TABLE MAP_DATI_SPECIFICI_INTERVENTO
DROP TABLE PAI
DROP TABLE PAI_CDG
DROP TABLE PAI_DOCUMENTO
DROP TABLE PAI_EVENTO
DROP TABLE PAI_INTERVENTO
DROP TABLE PAI_INTERVENTO_ANAGRAFICA
DROP TABLE PAI_INTERVENTO_CIV_OBB
DROP TABLE PAI_INTERVENTO_MESE
DROP TABLE PARAMETRI
DROP TABLE PARAMETRI_INDATA
DROP TABLE PROVINCIA
DROP TABLE STATO
DROP TABLE TEMPLATE
DROP TABLE TIPOLOGIA_INTERVENTO
DROP TABLE TIPOLOGIA_PARAMETRI
DROP TABLE TOPONOMASTICA
DROP TABLE TOPONOMASTICA_CIVICI
DROP TABLE UNIQUE_FORM
DROP TABLE UNIQUE_TASKLIST
DROP TABLE UTENTI
DROP TABLE D00.GDA@GDA
DROP TABLE PAI_MACRO_PROBLEMATICHE
DROP TABLE PAI_MICRO_PROBLEMATICHE
DROP TABLE LUOGO
DROP TABLE FATTURA_MESI_PRECEDENTI
DROP SEQUENCE WG_SEQ
