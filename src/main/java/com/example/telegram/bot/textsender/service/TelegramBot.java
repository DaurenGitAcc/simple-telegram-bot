package com.example.telegram.bot.textsender.service;

import com.example.telegram.bot.textsender.config.BotConfig;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.ParseException;


@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            logger.info("Получено сообщение от пользователя: "+messageText);


            switch (messageText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    logger.info("Бот начал свою работу");
                    break;
                default:
                    sendMessage(chatId, messageText);
                    logger.info("Отправлено сообщение пользователю: "+messageText);
            }
        }

    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Здравствуйте, " + name + ", рад Вас встретить!" + "\n" +
                "Я готов отправить вам ваше же сообщение ;)";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
}
