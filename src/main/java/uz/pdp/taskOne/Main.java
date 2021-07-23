package uz.pdp.taskOne;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.pdp.taskOne.model.BotUser;
import uz.pdp.taskOne.util.CbuApiUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends TelegramLongPollingBot {

    Map<Long, BotUser> userMap = new HashMap<>();

    public static void main(String[] args) {
        try {

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Main());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {

        return "anvar0898_bot";

    }

    @Override
    public String getBotToken() {

        return "1895704166:AAH4J8tB7FlLKJb7aOYP9LnQ2IFnEEK_e6g";

    }

    @Override
    public synchronized void onUpdateReceived(Update update) {

        String receivedMessage = update.getMessage().getText();
        String sendingMessage;
        SendMessage sendMessage = new SendMessage();

        BotUser botUser;

        if (receivedMessage.equals("/start")) {
            botUser = new BotUser();
            botUser.setStep(1);
            userMap.put(update.getMessage().getChatId(), botUser);

        } else {
            botUser=userMap.get(update.getMessage().getChatId());

        }

        if (receivedMessage.contains("UZS")) {
            botUser.setStep(2);
        }

        if (receivedMessage.equals("Cancel")) {
            botUser.setStep(botUser.getStep()-2);
        }

        if (receivedMessage.equals("Select currency")) {
            botUser.setStep(botUser.getStep()-3);

        }

        switch (botUser.getStep()) {

            case 1:
                sendingMessage = "Botga hush kelibsiz." + update.getMessage().getFrom().getFirstName() + " Convertatsiya turini tanlang";
                setButtons(sendMessage, botUser.getStep());
                break;

            case 2:
                sendingMessage = "Summani kiriting";
                CbuApiUtil.getCurrency(receivedMessage, botUser);
                setButtons(sendMessage, botUser.getStep());
                break;

            case 3:
                sendingMessage = CbuApiUtil.conversion(receivedMessage, botUser) + " bo'ldi";
                setButtons(sendMessage, botUser.getStep());
                break;

            default:
                sendingMessage = "Error";
                break;
        }

        botUser.setStep(botUser.getStep()+1);

        sendMessage.setText(sendingMessage);
        sendMessage.setChatId(update.getMessage().getChatId().toString());

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage, int step) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        if (step == 2) {

            KeyboardRow row1 = new KeyboardRow();
            row1.add("Cancel");
            keyboardRows.add(row1);

        } else if (step == 1) {

            KeyboardRow row1 = new KeyboardRow();
            row1.add("USD-UZS");
            row1.add("UZS-USD");
            keyboardRows.add(row1);

            KeyboardRow row2 = new KeyboardRow();
            row2.add("EUR-UZS");
            row2.add("UZS-EUR");
            keyboardRows.add(row2);

            KeyboardRow row3 = new KeyboardRow();
            row3.add("RUB-UZS");
            row3.add("UZS-RUB");
            keyboardRows.add(row3);
        } else {
            KeyboardRow row1 = new KeyboardRow();
            row1.add("Select currency");
            keyboardRows.add(row1);
        }

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
}
