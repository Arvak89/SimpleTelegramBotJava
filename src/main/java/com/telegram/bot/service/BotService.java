package com.telegram.bot.service;

import com.telegram.bot.config.BotConfig;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotService extends TelegramLongPollingBot {

    final BotConfig botConfig;

    public BotService(BotConfig botConfig) {

        this.botConfig = botConfig;

        List<BotCommand> listOfCommand = new ArrayList<>();
        listOfCommand.add(new BotCommand("/start", "start"));
        listOfCommand.add(new BotCommand("/help", "help info"));
        listOfCommand.add(new BotCommand("/new reminder", "new reminder"));

        try {
            this.execute(new SetMyCommands(listOfCommand, new BotCommandScopeDefault(), null));

        } catch (TelegramApiException e) {

        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start": {
                    startCommandReceive(chatId, update.getMessage().getChat().getFirstName());
                    break;
                }
                case "/newreminder": {
                    switch (update.getMessage().getText()){
                        case "/newreminder":{
                            sendMessage(chatId, update.getMessage().getChatId().toString());
                            break;
                        }
                    }
                    break;
                }
                case "/help":{
                    sendMessage(chatId, "help");
                    break;
                }
                default: {
                    sendMessage(chatId, "Простите, функционала ещё нет(");

                }
            }
        }
    }

    @Async
    protected void newReminder(long chatId, String time, String text) {
        try {
            Thread.sleep(1000 * 10);
            sendMessage(chatId, text);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    private void startCommandReceive(long chatId, String name) {
        String answer = "Привет, " + "Попа" + "!";

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
