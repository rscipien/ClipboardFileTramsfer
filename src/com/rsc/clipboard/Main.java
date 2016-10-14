package com.rsc.clipboard;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import com.rsc.clipboard.strategy.MessageHandlerStrategy;
import com.rsc.clipboard.strategy.ReciverFileStrategy;
import com.rsc.clipboard.strategy.SendFileStrategy;


public class Main {

	public static void main(String[] args) throws Exception {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		ClipboradHelper helper = new ClipboradHelper(clipboard);
		ClipboardParser parser = new ClipboardParser();
		ClipboardSender sender =  new ClipboardSender(helper);
		HashGenerator generator = new HashGenerator();
		
		FileUtil fileUtil = new FileUtil();
		CommandLineParams cmd = new CommandLineParams(args, fileUtil);
		cmd.parse();
		
		if (cmd.isDump()) {
			return;
		}
//			} else if (arg.equals("-o")) {
//					//String outFile = args[++i];
					//fileReader.setFileLocation(outFile);
//				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("out"));
//				fileReader = new ClipboardFileReader(bos);
		
//		ClipboardMessageHandler messageHandler = new ClipboardMessageHandler(helper, cmd.getFileSender(), cmd.getFileReader(), sender, parser, generator);
		MessageHandlerStrategy messageHandler = cmd.isSender() 
				? new SendFileStrategy(parser, cmd.getFileSender(), helper) : 
				  new ReciverFileStrategy(parser, sender, cmd.getFileReader(), generator);
		ClipboardListener listener = new ClipboardListener(messageHandler, helper);
		Thread thread =  new Thread(listener);
		thread.run();
	}

}
