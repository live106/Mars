package com.live106.mars.client.console;

import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.live106.mars.client.ClientGroupRunner;
import com.live106.mars.client.ClientRunner;
import com.live106.mars.util.LoggerHelper;

public class ConsoleHandler {
	
	private final static Logger logger = LoggerFactory.getLogger(ConsoleHandler.class);
	
	public volatile static boolean loopRequest = false;
	
	private static final String HELP_FOOTER = "footer";
	private static final String HELP_HEADER = "header";
	private static final String START = "start";
	private static final String STOP = "stop";
	private static final String CHKARCHIVE = "chkArchive";
	private static final String SAVE = "save";
	private static final String LOAD = "load";

	private CommandLineParser commandParser = new DefaultParser();
	private Map<String, Options> commandOption = new HashMap<>();
	
	public ConsoleHandler() {
		//启动模拟线程
		{
			Options options = new Options();
			Option optionNumber = Option.builder("n").argName("number").hasArg().desc("the thread number to start").build();
			options.addOption(optionNumber);
			Option optionLoop = Option.builder("l").argName("boolean").hasArg().desc("loop request or not when finishing.").build();
			options.addOption(optionLoop);
			options.addOption("help", "show usage");
			
			commandOption.put(START, options);
		}
		//停止模拟线程
		{
			Options options = new Options();
			commandOption.put(STOP, options);
		}
		//检查存档时间点
		{
			Options options = new Options();
			commandOption.put(CHKARCHIVE, options);
		}
		//保存存档
		{
			Options options = new Options();
			commandOption.put(SAVE, options);
		}
		//获取存档
		{
			Options options = new Options();
			commandOption.put(LOAD, options);
		}
	}

	public void handleCommand(String command, PrintWriter writer) throws ParseException {
		
		LoggerHelper.debug(logger, ()->String.format("input command %s", command));
		
		String cmd = command;
		CommandLine line = null;
		Options options = null;
		if (command.indexOf(" ") > 0) {
			cmd = command.substring(0, command.indexOf(" "));
			String[] arguments = command.substring(command.indexOf(" ") + 1).split(" ");
			if (arguments == null) {
				arguments = new String[0];
			}
			options = commandOption.get(cmd);
			if (options != null) {
				try {
					line = commandParser.parse(options, arguments);
					if (line != null) {
						if (!line.getArgList().isEmpty()) {
							showHelp(writer, options);
							return;
						} else if (line.hasOption("help")) {
							showHelp(writer, options);
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					showHelp(writer, options);
					return;
				}
			}
		}
		try {
			switch (cmd) {
			case START:
				{
					int threadNum = 1;
					if (line != null) {
						if (line.hasOption("n")) {
							String count = line.getOptionValue("n");
							try {
								threadNum = Integer.parseInt(count);
							} catch (NumberFormatException e) {
								e.printStackTrace();
								showHelp(writer, options);
								return;
							}
						}
						if (line.hasOption("l")) {
							loopRequest = Boolean.parseBoolean(line.getOptionValue("l"));
						}
					}
					start(threadNum);
					break;
				}
			case STOP:
			{
				break;
			}
			case CHKARCHIVE:
			{
				checkArchive(writer);
				break;
			}
			case SAVE:
			{
				saveArchive();
				break;
			}
			case LOAD:
			{
				loadArchive();
				break;
			}
			default:
				break;
			}
		} catch (TException | InterruptedException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

	private void checkArchive(PrintWriter writer) {
		synchronized (ClientRunner.runners) {
			for (ClientRunner runner : ClientRunner.runners) {
				try {
					runner.setConsoleWriter(writer);
					runner.checkArchive();
				} catch (TException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void loadArchive() {
		synchronized (ClientRunner.runners) {
			for (ClientRunner runner : ClientRunner.runners) {
				try {
					runner.loadArchive();
				} catch (TException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void saveArchive() {
		synchronized (ClientRunner.runners) {
			for (ClientRunner runner : ClientRunner.runners) {
				try {
					runner.saveArchive();
				} catch (TException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void showHelp(PrintWriter writer, Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(writer, formatter.getWidth(), formatter.getSyntaxPrefix(), HELP_HEADER, options, formatter.getLeftPadding(), formatter.getDescPadding(), HELP_FOOTER);
	}

	private void start(int threadNum) throws TException, InterruptedException, NoSuchAlgorithmException {
		for (int i = 0; i < threadNum; i++) {
			new StarterThread().start();
			Thread.sleep(10);//到获取场景信息这一步，模拟每秒100个客户端频率, 400次连接及断开，500次协议收发。
		}
	}
	
	static class StarterThread extends Thread {
		
		private final static AtomicInteger runnerNumber = new AtomicInteger();
		private final int number;
		
		public StarterThread() {
			super();
			number = runnerNumber.getAndIncrement();
			setName("starter-" + number);
		}
		
		@Override
		public void run() {
			ClientRunner runner = ClientGroupRunner.ctx.getBean(ClientRunner.class);
			runner.setThreadNumber(number);
			try {
				runner.start();
			} catch (NoSuchAlgorithmException | TException | InterruptedException | InvalidAlgorithmParameterException e) {
				e.printStackTrace();
			}
		}
	};

}
