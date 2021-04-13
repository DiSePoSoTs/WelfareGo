<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    //La prendo dalla sessione
    String paiId = "1000";
    String codOperatore = "A123";
    String codAnagraficaUtente = "012345";
    String tipoUtente = "AS_UOT";
    // ConnectedUserInformations user = UserType.valueOf(tipoUtente).getUtente();
    
%>
<script type="text/javascript">

    var wp_token = '<%= ""%>';
    
    var wp_window_widget;
    
</script>