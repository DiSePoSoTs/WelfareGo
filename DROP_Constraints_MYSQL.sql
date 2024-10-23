-- Lanciare questa Select e quindi selezionare l''output e lanciare tutti gli alter table drop foreign key

SELECT concat('ALTER TABLE `', TABLE_NAME, '` DROP FOREIGN KEY `', CONSTRAINT_NAME, '`;') 
FROM information_schema.key_column_usage 
WHERE CONSTRAINT_SCHEMA = 'socialgo' 
AND referenced_table_name IS NOT NULL
group by table_name, constraint_name;


ALTER TABLE `anagrafe_fam` DROP FOREIGN KEY `FK_1`;
ALTER TABLE `anagrafe_fam` DROP FOREIGN KEY `FK_2`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK1`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK10`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK11`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK12`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK13`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK14`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK15`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK2`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK3`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK4`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK5`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK6`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK7`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK8`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_FK9`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_LFK_DOM`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_LFK_NASC`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_LFK_RES`;
ALTER TABLE `anagrafe_soc` DROP FOREIGN KEY `ANAGRAFE_SOC_UTENTI_FK`;
ALTER TABLE `appuntamento` DROP FOREIGN KEY `APPUNTAMENTO_FK3`;
ALTER TABLE `budget_tip_intervento` DROP FOREIGN KEY `TIP_INTERVENTO_IMPEGNO_FK1`;
ALTER TABLE `budget_tip_intervento_uot` DROP FOREIGN KEY `TIP_INT_UOT_IMPEGNO_FK1`;
ALTER TABLE `cartella_sociale` DROP FOREIGN KEY `CARTELLA_SOCIALE_FK1`;
ALTER TABLE `comune` DROP FOREIGN KEY `COMUNE_FK1`;
ALTER TABLE `contatto` DROP FOREIGN KEY `CONTATTO_FK2`;
ALTER TABLE `fattura` DROP FOREIGN KEY `FATTURA_FK1`;
ALTER TABLE `fattura` DROP FOREIGN KEY `FATTURA_FK2`;
ALTER TABLE `fattura_dettaglio` DROP FOREIGN KEY `FATTURA_DETTAGLIO_FK1`;
ALTER TABLE `fattura_dettaglio` DROP FOREIGN KEY `FATTURA_DETTAGLIO_FK3`;
ALTER TABLE `fattura_mesi_precedenti` DROP FOREIGN KEY `FATTURA_MESI_PREC_FK1`;
ALTER TABLE `fattura_mesi_precedenti` DROP FOREIGN KEY `FATTURA_MESI_PREC_FK2`;
ALTER TABLE `interventi_associati` DROP FOREIGN KEY `INTERVENTI_ASSOCIATI_PAI__FK1`;
ALTER TABLE `interventi_associati` DROP FOREIGN KEY `INTERVENTI_ASSOCIATI_PAI__FK2`;
ALTER TABLE `liberatoria` DROP FOREIGN KEY `LIBERATORIA_ANAGRAFE_SOC_FK1`;
ALTER TABLE `liberatoria` DROP FOREIGN KEY `LIBERATORIA_ASSOCIAZIONE_FK1`;
ALTER TABLE `log_anagrafe` DROP FOREIGN KEY `LOG_ANAGRAFE_FK2`;
ALTER TABLE `log_anagrafe` DROP FOREIGN KEY `LOG_ANAGRAFE_FK3`;
ALTER TABLE `log_messaggi` DROP FOREIGN KEY `LOG_MESSAGGI_FK1`;
ALTER TABLE `luogo` DROP FOREIGN KEY `LUOGO_FK1`;
ALTER TABLE `luogo` DROP FOREIGN KEY `LUOGO_FK2`;
ALTER TABLE `luogo` DROP FOREIGN KEY `LUOGO_FK3`;
ALTER TABLE `luogo` DROP FOREIGN KEY `LUOGO_FK4`;
ALTER TABLE `luogo` DROP FOREIGN KEY `LUOGO_FK5`;
ALTER TABLE `mandato` DROP FOREIGN KEY `MANDATO_FK1`;
ALTER TABLE `mandato` DROP FOREIGN KEY `MANDATO_FK2`;
ALTER TABLE `mandato` DROP FOREIGN KEY `MANDATO_FK3`;
ALTER TABLE `mandato_dettaglio` DROP FOREIGN KEY `MANDATO_DETTAGLIO_FK1`;
ALTER TABLE `mandato_dettaglio` DROP FOREIGN KEY `MANDATO_DETTAGLIO_FK3`;
ALTER TABLE `map_dati_spec_tipint` DROP FOREIGN KEY `map_dati_spec_tipint_ibfk_1`;
ALTER TABLE `map_dati_spec_tipint` DROP FOREIGN KEY `map_dati_spec_tipint_ibfk_2`;
ALTER TABLE `note_condivise` DROP FOREIGN KEY `NOTE_CONDIVISE_ANAGRAFE_S_FK1`;
ALTER TABLE `note_condivise` DROP FOREIGN KEY `NOTE_FK1`;
ALTER TABLE `pai` DROP FOREIGN KEY `PAI_FK1`;
ALTER TABLE `pai_anagrafica` DROP FOREIGN KEY `PAI_ANAGRAFICA_FK1`;
ALTER TABLE `pai_anagrafica` DROP FOREIGN KEY `PAI_ANAGRAFICA_FK2`;
ALTER TABLE `pai_cdg` DROP FOREIGN KEY `PAI_CDG_FK1`;
ALTER TABLE `pai_cdg` DROP FOREIGN KEY `PAI_CDG_FK2`;
ALTER TABLE `pai_documento` DROP FOREIGN KEY `PAI_DOCUMENTO_FK1`;
ALTER TABLE `pai_documento` DROP FOREIGN KEY `PAI_DOCUMENTO_FK2`;
ALTER TABLE `pai_evento` DROP FOREIGN KEY `PAI_EVENTO_FK1`;
ALTER TABLE `pai_evento` DROP FOREIGN KEY `PAI_EVENTO_FK3`;
ALTER TABLE `pai_evento` DROP FOREIGN KEY `PAI_EVENTO_FK6`;
ALTER TABLE `pai_intervento` DROP FOREIGN KEY `PAI_INTERVENTO_ANAGRAFE_S_FK1`;
ALTER TABLE `pai_intervento` DROP FOREIGN KEY `PAI_INTERVENTO_ASSOCIAZIO_FK1`;
ALTER TABLE `pai_intervento` DROP FOREIGN KEY `PAI_INTERVENTO_FK1`;
ALTER TABLE `pai_intervento` DROP FOREIGN KEY `PAI_INTERVENTO_FK2`;
ALTER TABLE `pai_intervento` DROP FOREIGN KEY `PAI_INTERVENTO_FK4`;
ALTER TABLE `pai_intervento_anagrafica` DROP FOREIGN KEY `PAI_INTERVENTO_ANAGRAFICA_FK1`;
ALTER TABLE `pai_intervento_anagrafica` DROP FOREIGN KEY `PAI_INTERVENTO_ANAGRAFICA_FK2`;
ALTER TABLE `pai_intervento_civ_obb` DROP FOREIGN KEY `PAI_INTERVENTO_CIV_OBB_FK1`;
ALTER TABLE `pai_intervento_civ_obb` DROP FOREIGN KEY `PAI_INTERVENTO_CIV_OBB_FK2`;
ALTER TABLE `pai_intervento_mese` DROP FOREIGN KEY `PAI_INTERVENTO_MESE_FK1`;
ALTER TABLE `pai_intervento_mese` DROP FOREIGN KEY `PAI_INTERVENTO_MESE_FK2`;
ALTER TABLE `pai_intervento_mese` DROP FOREIGN KEY `PAI_INTERVENTO_MESE_FK3`;
ALTER TABLE `pai_intervento_mese` DROP FOREIGN KEY `PAI_INTERVENTO_MESE_FK4`;
ALTER TABLE `pai_intervento_mese` DROP FOREIGN KEY `PAI_INTERVENTO_MESE_PARAM_FK1`;
ALTER TABLE `pai_macro_problematiche` DROP FOREIGN KEY `PAI_MP_FK_PAI`;
ALTER TABLE `pai_micro_problematiche` DROP FOREIGN KEY `PAI_MIP_FK_MAP`;
ALTER TABLE `pai_micro_problematiche` DROP FOREIGN KEY `PAI_MIP_FK_PAI`;
ALTER TABLE `parametri` DROP FOREIGN KEY `PARAMETRI_FK1`;
ALTER TABLE `provincia` DROP FOREIGN KEY `PROVINCIA_FK1`;
ALTER TABLE `ricevuta_cassa` DROP FOREIGN KEY `RICEVUTA_CASSA_PAI_FK1`;
ALTER TABLE `struttura` DROP FOREIGN KEY `FK_ENTE`;
ALTER TABLE `struttura` DROP FOREIGN KEY `STRUTTURA_TIPOLOGIA_INTER_FK1`;
ALTER TABLE `tariffa` DROP FOREIGN KEY `STRUTTURA_FK`;
ALTER TABLE `tipologia_intervento` DROP FOREIGN KEY `FK_IP_PI`;
ALTER TABLE `tipologia_intervento` DROP FOREIGN KEY `TIPOLOGIA_INTERVENTO_FK4`;
ALTER TABLE `toponomastica` DROP FOREIGN KEY `TOPONOMASTICA_FK1`;
ALTER TABLE `toponomastica_civici` DROP FOREIGN KEY `TOPONOMASTICA_CIVICI_FK1`;
ALTER TABLE `unique_tasklist` DROP FOREIGN KEY `UNIQUE_TASKLIST_FK1`;
ALTER TABLE `unique_tasklist` DROP FOREIGN KEY `UNIQUE_TASKLIST_FK2`;
ALTER TABLE `unique_tasklist` DROP FOREIGN KEY `UNIQUE_TASKLIST_FK3`;