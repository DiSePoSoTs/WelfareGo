package it.wego.welfarego.scheduler;


import com.google.gson.Gson;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

public class FakeInterventoFactory {

    public static PaiIntervento getFakeIntervento(int i) throws IOException {
        Gson gson = new Gson();
        return getPaiInterventoFromJson(i, gson);
    }

    public static PaiIntervento getPaiInterventoFromJson(int i, Gson gson) throws IOException {
        return gson.fromJson(getJson(i), PaiIntervento.class);
    }


    public static String getJson(int s) throws IOException {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("fake_PaiIntervento_" + s + ".json");
        return IOUtils.toString(resourceAsStream, "UTF-8");
    }

}
