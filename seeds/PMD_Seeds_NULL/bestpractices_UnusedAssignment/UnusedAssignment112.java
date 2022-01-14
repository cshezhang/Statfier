
            public class Test {
                public void append(LogEvent event) {
                    String logMessage = wrapIn.wrap(new String(getLayout().toByteArray(event), getLayout().getCharset()));
                    DiscordManager discordManager = findDiscordManager();
                    if (discordManager == null) {
                        bufferMessage(logMessage, event.getTimeMillis());
                    } else {
                        bufferedMessagesByAppenderName.computeIfPresent(getName(), (name, bufferedMessages) -> {
                            bufferedMessages.forEach(bufferedMessage -> sendMessage(discordManager, bufferedMessage));
                            return null;
                        });
                        sendMessage(discordManager, logMessage);
                    }
                }
            }
        