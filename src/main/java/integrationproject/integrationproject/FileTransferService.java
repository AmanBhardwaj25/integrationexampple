package integrationproject.integrationproject;

import java.io.File;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;



@Configuration
@EnableIntegration
public class FileTransferService {
	
	public String inputDirectory;
	public String outputDirectory;
	
	
	
	@Bean
	public MessageChannel fileChannel()
	{
		return new DirectChannel();
	}
	
	
	@Bean
	@InboundChannelAdapter(value = "fileChannel", poller = @Poller(fixedDelay = "1000"))
	public MessageSource<File> fileReadingMessageSource(String fileName) {
	    FileReadingMessageSource sourceReader= new FileReadingMessageSource();
	    sourceReader.setDirectory(new File(inputDirectory));
        sourceReader.setFilter(new SimplePatternFileListFilter(fileName));
        return sourceReader;
	    }
	 
	 @Bean
	 @ServiceActivator(inputChannel= "fileChannel")
	 public MessageHandler fileWritingMessageHandler() {
       FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(outputDirectory));
       handler.setFileExistsMode(FileExistsMode.REPLACE);
	   handler.setExpectReply(false);
	   return handler;
	 }
	
	
	
}
