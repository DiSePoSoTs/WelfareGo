package it.wego.welfarego.determine.servlet;

import it.wego.welfarego.determine.model.PreviewReportBean;
import it.wego.welfarego.determine.servlet.logica.applicativa.DeterminaBusinessLogic;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@PrepareForTest({DetermineForm.class})
public class DetermineFormTest {


//    @Test
    public void testDetermina_parziale() throws Exception {
//given
        String numero_mesi = "1";
        String al = "01/01/2018";
        boolean escludiVerificaEsistenzaBudget = false;

        DetermineForm determineFormMock = mock(DetermineForm.class);
        DeterminaBusinessLogic determinaBusinessLogicMock = mock(DeterminaBusinessLogic.class);
        PreviewReportBean previewReportBeanWithMock = getPreviewReportBeanWithMock();
        when(determineFormMock.getDataParameter(PreviewReportBean.class)).thenReturn(previewReportBeanWithMock);
        when(determineFormMock.getParameter("proroghe")).thenReturn("basdf");
        when(determineFormMock.getParameter("numero_mesi")).thenReturn(numero_mesi);
        when(determineFormMock.getParameter("al")).thenReturn(al);

        PowerMockito.whenNew(DeterminaBusinessLogic.class).withAnyArguments().thenReturn(determinaBusinessLogicMock);
        doNothing().when(determinaBusinessLogicMock).determina_parzialmente(previewReportBeanWithMock, numero_mesi, al, escludiVerificaEsistenzaBudget);


//when
        determineFormMock.proceedPerTest();

//then
//        Mockito.ver
//                zuccolin chiara
//                        5/9

    }

    private PreviewReportBean getPreviewReportBeanWithMock() {
        PreviewReportBean previewReportBeanMock = mock(PreviewReportBean.class);

        when(previewReportBeanMock.getEventi()).thenReturn(Arrays.asList("sss"));

        return previewReportBeanMock;
    }

    private PreviewReportBean getPreviewReportBean() {
        PreviewReportBean previewReportBean = new PreviewReportBean();
        previewReportBean.setEventi(Arrays.asList("sss"));
        return previewReportBean;
    }

}