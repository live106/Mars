package com.live106.mars.client.console;

import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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
	
	private static final String HELP_FOOTER = "footer";
	private static final String HELP_HEADER = "header";
	private static final String START = "start";

	private CommandLineParser commandParser = new DefaultParser();
	private Map<String, Options> commandOption = new HashMap<>();
	
	public ConsoleHandler() {
		Options options = new Options();
		Option optionNumber = Option.builder("n").argName("number").hasArg().desc("the thread number to start").build();
		options.addOption(optionNumber);
		options.addOption("help", "show usage");
		
		commandOption.put(START, options);
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
				line = commandParser.parse(options, arguments);
			}
		}
		try {
			switch (cmd) {
			case START:
				{
					int threadNum = 1;
					if (line != null) {
						if (line.hasOption("help")) {
							HelpFormatter formatter = new HelpFormatter();
							formatter.printHelp(writer, formatter.getWidth(), formatter.getSyntaxPrefix(), HELP_HEADER, options, formatter.getLeftPadding(), formatter.getDescPadding(), HELP_FOOTER);
							break;
						} else if (line.hasOption("n")) {
							String count = line.getOptionValue("n");
							try {
								threadNum = Integer.parseInt(count);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}
					}
					start(threadNum);
					break;
				}
			default:
				break;
			}
		} catch (TException | InterruptedException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

	private void start(int threadNum) throws TException, InterruptedException, NoSuchAlgorithmException {
		for (int i = 0; i < threadNum; i++) {
			new Thread(starter).start();
		}
	}
	
	Runnable starter = new Runnable() {
		@Override
		public void run() {
			ClientRunner runner = ClientGroupRunner.ctx.getBean(ClientRunner.class);
			try {
				runner.start();
			} catch (NoSuchAlgorithmException | TException | InterruptedException | InvalidAlgorithmParameterException e) {
				e.printStackTrace();
			}
		}
	};

//	private void start(int threadNum) throws TException, InterruptedException, NoSuchAlgorithmException {
//		/*
//		Channel ch = ClientRunner.channelFulture.channel();
//		
//		//check whether ch is valid
//		
//		ProtocolPeer2Peer tp = new ProtocolPeer2Peer();
//		tp.getHeader().setTargetType(PeerType.PEER_TYPE_ACCOUNT);
//		tp.getHeader().setSourceType(PeerType.PEER_TYPE_CLIENT);
//		tp.getHeader().setSerializeType(SerializeType.SERIALIZE_TYPE_THRIFT);//thrift is default.
//		
//		RequestSendClientPublicKey request = new RequestSendClientPublicKey();
//		
//		cryptor = new Cryptor(Cryptor.AES);
//		cryptor.generateKey();
//		
//		request.setClientPubKey(cryptor.getSecretKey());
//		ProtocolSerializer.serialize(request, tp);
//		*/
//
//		/*
//		tp.getHeader().setProtocolHash(request.getClass().getSimpleName().hashCode());
//		RequestAuthServerPublicKey request = new RequestAuthServerPublicKey();
//		byte[] bytes = serializer.serialize(request);
////		tp.getHeader().setTargetId(targetId);
////		tp.getHeader().setSourceId(sourceId);
//		
////		tp.setTargetType(IProtocol.PEER_TYPE_ACCOUNT);
////		tp.setSourceType(IProtocol.PEER_TYPE_CLIENT);
////		tp.setTargetId(0x1010);
////		tp.setSourceId(sourceId);
////		tp.setSerializeType(IProtocol.SERIALIZE_TYPE_THRIFT);
////		tp.setProtocolHash(request.getClass().getSimpleName().hashCode());
//		
//		tp.setData(bytes);
//		
////		RequestUserLogin login = new RequestUserLogin();
////		login.setUsername("admin" + new Random().nextInt(100));
//////		login.setPassword("111111");
////		
////		byte[] bytes = serializer.serialize(login);
////		
////		ClientProtocolPojo tp = new ClientProtocolPojo();
////		
////		
////		
////		tp.setProtocolHash(login.getClass().getSimpleName().hashCode());
////		tp.setData(bytes);
//		
//		*/
//		
//		/*
//		ChannelFuture writeFulture = ch.writeAndFlush(tp);
//		
//		if (writeFulture != null) {
//			writeFulture.sync();
//		}
//		*/
//	}
}
