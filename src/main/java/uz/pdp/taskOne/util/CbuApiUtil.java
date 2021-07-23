package uz.pdp.taskOne.util;

import com.google.gson.Gson;
import uz.pdp.taskOne.model.BotUser;
import uz.pdp.taskOne.model.Currency;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CbuApiUtil {

//    static boolean conversionType;
//    static Currency currentCurrency;

    public static Currency[] connectToCbuApi() {

        Currency[] currencies = null;

        try {

            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");
            URLConnection urlConnection = url.openConnection();

            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());

            currencies = gson.fromJson(reader, Currency[].class);


        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return currencies;
    }

    public static void getCurrency(String convertMessage, BotUser botUser) {

        botUser.setConversionType(convertMessage.startsWith("UZS"));
        Currency[] currencies = connectToCbuApi();

        for (Currency currency : currencies) {
            if (convertMessage.contains(currency.getCcy())) {
                botUser.setCurrency(currency);
                break;
            }
        }
    }

    public static Double conversion(String summa, BotUser botUser) {

        Double sum = Double.parseDouble(summa);

        double result;
        result = botUser.isConversionType() ? sum / Double.parseDouble(botUser.getCurrency().getRate()) : sum * Double.parseDouble(botUser.getCurrency().getRate());
        return result;
    }
}
