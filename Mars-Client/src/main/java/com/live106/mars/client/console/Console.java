package com.live106.mars.client.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.live106.mars.concurrent.MarsDefaultThreadFactory;

public class Console {
	public static String PROMPT = "Mars-Client> ";

	private static Logger log = LoggerFactory.getLogger(Console.class);
	private static ExecutorService executor = Executors.newCachedThreadPool(new MarsDefaultThreadFactory("console"));
	private Socket l = null;
	private ConsoleHandler handler;

	public static void open() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Console console = new Console();
				console.listen(8888, new ConsoleHandler());
			}
		};
		new Thread(runnable, "ConsoleListener").start();
	}

	public void listen(int port, ConsoleHandler handler) {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port);
			log.info("Open Mars Client Console at port " + socket.getLocalPort());

			this.handler = handler;

			while (true) {
				l = socket.accept();
				Runnable r = new Runnable() {
					@Override
					public void run() {
						try {
							Console.this.consoleService(l);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				};
				executor.execute(r);
			}

		} catch (IOException e) {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void consoleService(Socket socket) throws ParseException {
		BufferedReader reader = null;
		PrintWriter writer = null;

		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GB2312"));
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "GB2312"));
			String str = null;
			writer.print(PROMPT);
			writer.flush();
			while ((str = reader.readLine()) != null) {
				handler.handleCommand(str, writer);
				writer.print(PROMPT);
				writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
