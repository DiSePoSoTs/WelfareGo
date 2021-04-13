<%--
    Document   : index
    Created on : 17-giu-2011, 9.37.33
    Author     : giuseppe
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="it.wego.persistence.ConditionBuilder"%>
<%@page import="it.wego.persistence.AssignmentBuilder"%>
<%@page import="it.wego.welfarego.persistence.entities.Template"%>
<%@page import="java.util.Date"%>
<%@page import="it.wego.welfarego.persistence.entities.UniqueForm"%>
<%@page import="it.wego.welfarego.persistence.entities.PaiInterventoPK"%>
<%@page import="it.wego.welfarego.persistence.entities.PaiIntervento"%>
<%@page import="it.wego.welfarego.persistence.entities.Pai"%>
<%@page import="it.wego.welfarego.persistence.entities.UniqueTasklist"%>
<%@page import="it.wego.persistence.PersistenceAdapterFactory"%>
<%@page import="it.wego.persistence.PersistenceAdapter"%>
<%@page import="java.util.Collections"%>
<%@page import="it.wego.welfarego.azione.forms.AzionePortletForm"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Azione</title>
		<script type="text/javascript" src="ext/ext-all-debug.js"></script>

		<link rel="stylesheet" type="text/css" href="ext/resources/css/ext-all.css">
		<link rel="stylesheet" type="text/css" href="ext/resources/css/ext-all-gray.css">
		<link rel="stylesheet" type="text/css" href="ext/virtualkeyboard/css/virtualkeyboard.css">
		<!--        <link rel="stylesheet" type="text/css" href="css/azione.css">-->
		<!--        <link rel="stylesheet" type="text/css" href="ext/virtualkeyboard/css/virtualkeyboard.css"> -->
	</head>
	<body>
		<%
			String taskId = request.getParameter("task_id"),
					  codForm = request.getParameter("cod_form"),
					  codPai = request.getParameter("cod_pai"),
					  codTipint = request.getParameter("cod_tipint"),
					  cntTipint = request.getParameter("cnt_tipint"),
					  codTmpl = request.getParameter("cod_tmpl");
			if (taskId != null) {
				AzionePortletForm azionePortletForm = new AzionePortletForm();
				azionePortletForm.setParameter("task_id", taskId);
				//	azionePortletForm.setParameter(AzionePortletForm.PARAM_NO_LIFERAY,Boolean.TRUE.toString());
				azionePortletForm.doView(out);

			} else if (codForm != null) {
				

				PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
 				persistenceAdapter.initTransaction();
				final String desTask = "TEMP TASK";
				/*persistenceAdapter.executeUpdate(
						  UniqueTasklist.class,
						  "UPDATE UniqueTasklist u",
						  ConditionBuilder.isEqual("u.desTask",desTask),
						  AssignmentBuilder.assign("u.flgEseguito","S"));*/
				//persistenceAdapter.getEntityManager().createQuery("UPDATE UniqueTasklist u SET u.flgEseguito='S' WHERE u.desTask = :desTask").setParameter("desTask", desTask).executeUpdate();

				UniqueTasklist task = new UniqueTasklist();
				task.setCodPai(persistenceAdapter.getEntityManager().getReference(Pai.class, Integer.valueOf(codPai)));
				if (codTipint != null && cntTipint != null && codTipint.length() > 0 && cntTipint.length() > 0) {
					task.setPaiIntervento(persistenceAdapter.getEntityManager().getReference(PaiIntervento.class, new PaiInterventoPK(Integer.valueOf(codPai), codTipint, Integer.valueOf(cntTipint))));
				}
				if (codForm != null && codForm.length() > 0) {
					task.setForm(persistenceAdapter.getEntityManager().getReference(UniqueForm.class, codForm));
				}
				task.setDesTask(desTask);
				task.setFlgEseguito("N");
				task.setFlgTasknot("N");
				task.setTsCreazione(new Date());
				if (codTmpl != null) {
					task.setCodTmpl(persistenceAdapter.getEntityManager().find(Template.class, Integer.valueOf(codTmpl)));
				}

				persistenceAdapter.getEntityManager().persist(task);
				persistenceAdapter.commitTransaction();
				//persistenceAdapter.getEntityManager().flush();

				AzionePortletForm azionePortletForm = new AzionePortletForm();
				azionePortletForm.setParameters(Collections.singletonMap("task_id", task.getId().toString()));
				azionePortletForm.doView(out);
				session.invalidate();

				//persistenceAdapter.initTransaction();
				//persistenceAdapter.getEntityManager().remove(task);
				//persistenceAdapter.commitTransaction();
				//	persistenceAdapter.rollbackTransaction();
			} else {
		%> 
		<br/> <h1>form azione:</h1><br/>
		<ul>
			<li><a href="index.jsp?task_id=60">Default</a></li>
			<li><a href="index.jsp?task_id=61">6.1) Appuntamento</a></li>
			<li><a href="index.jsp?task_id=62">6.2) Protocolla domanda</a></li>
			<li><a href="index.jsp?task_id=63">6.3) Valida interventi</a></li>
			<li><a href="index.jsp?task_id=64">6.4) Esecutivit&agrave; - verifica dati</a></li>
			<li><a href="index.jsp?task_id=65">6.5) Valida esecutivit&agrave;</a></li>
			<li><a href="index.jsp?task_id=66">6.6) Registra determina</a></li>
			<li><a href="index.jsp?task_id=67">6.7) Predisposizione documento</a></li>
			<li><a href="index.jsp?task_id=68">6.8) Validazione documento</a></li>
			<li><a href="index.jsp?task_id=69">6.9) Protocollazione documento</a></li>
			<li><a href="index.jsp?task_id=610">6.10) Registra determina multipla</a></li>
		</ul><br/>
		<h1>altri form:</h1><br/>
		<a href="index_determine.jsp">Determine</a><br/>
		<a href="index_liste_attesa.jsp">ListeAttesa</a><br/>
		<a href="index_tasklist.jsp">TaskList</a><br/>
		<a href="index_reports.jsp">Reports</a>
		<a href="index_cassa.jsp">Cassa</a>
     	<a href="index_lettere.jsp">Lettere</a>
		<br/><br/>
		<form>
			<%
				for (String field : new String[]{"cod_form", "cod_pai", "cod_tipint", "cnt_tipint", "cod_tmpl"}) {
			%>
			<label><%=field%></label><input name="<%=field%>"/>
			<% }%>
			<input type="submit" value="SUBMIT"/>
		</form>
		<%                    }
		%>
		<%--<div id="wa_panel"></div>--%>
	</body>
</html>
