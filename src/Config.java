import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Config {
	public static String rotate = "up", left = "Left", right = "right", down = "Down", pause = "p";
	public static ArrayList<Choice> choices;

	public static void openConfig(JFrame frame) {
		choices = new ArrayList<Choice>();
		final JFrame Options = new JFrame("Options");
		Options.setSize(400, 300);
		Options.setResizable(false);
		Options.setLocationRelativeTo(frame);
		Options.setLayout(null);
		Choice left = addChoice("Left", Options, 30, 30);
		left.select(Config.left);
		Choice right = addChoice("Right", Options, 150, 30);
		right.select(Config.right);
		Choice down = addChoice("Down", Options, 30, 80);
		down.select(Config.down);
		Choice rotate = addChoice("Up", Options, 150, 80);
		rotate.select(Config.rotate);
		Choice pause = addChoice("Pause", Options, 30, 130);

		pause.select(Config.pause);
		JButton done = new JButton("Done");
		done.setBounds(150, 220, 100, 50);
		done.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Options.dispose();
				saveChanges();
				

			}
		});
		Options.add(done);
		Options.setVisible(true);
	}

	public static void saveChanges() {
		Choice left = choices.get(0);
		Choice right = choices.get(1);
		Choice down = choices.get(2);
		Choice rotate = choices.get(3);
		Choice pause = choices.get(4);

		Config.left = left.getSelectedItem();
		Config.right = right.getSelectedItem();
		Config.down = down.getSelectedItem();
		Config.rotate = rotate.getSelectedItem();
		Config.pause = pause.getSelectedItem();
		try {
			saveConfig();
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

	public static Choice addChoice(String name, JFrame Options, int x, int y) {
		JLabel label = new JLabel(name);
		label.setBounds(x, y - 20, 100, 20);
		Choice key = new Choice();
		for (String s : getKeyNames()) {
			key.add(s);
		}
		key.setBounds(x, y, 100, 20);
		Options.add(key);
		Options.add(label);
		choices.add(key);
		return key;
	}

	public static ArrayList<String> getKeyNames() {
		ArrayList<String> result = new ArrayList<String>();
		for (String s : KeyGetter.keyNames) {
			result.add(s);
			if (s.equalsIgnoreCase("24")) {
				break;
			}
		}
		return result;
	}

	public static void loadConfig() throws Exception {
		File directory = new File(getDefaultDirectory(), "/Tetris");
		if(!directory.exists()) {
			directory.mkdirs();
		}
		File config = new File(directory, "/config");
		if (!config.exists()) {
		
			config.createNewFile();
			saveConfig();
			return;
		}
		Scanner s = new Scanner(config);
		HashMap<String, String> values = new HashMap<String, String>();
		while (s.hasNextLine()) {
			String[] entry = s.nextLine().split(":");
			String key = entry[0];
			String value = entry[1];
			values.put(key, value);
		}
		if (values.size() != 5) {
			System.out.println("Config is unusable saving defaults");
			saveConfig();
			return;

		}
		if (!values.containsKey("left") || !values.containsKey("right") || !values.containsKey("rotate")
				|| !values.containsKey("down") || !values.containsKey("pause")) {
			System.out.println("Invalid names in config saving defaults");
			saveConfig();
			return;
		}
		String left = values.get("left");
		String right = values.get("right");
		String down = values.get("down");
		String rotate = values.get("rotate");
		String pause = values.get("pause");

		if (!(getKeyNames().contains(left) && getKeyNames().contains(right) && getKeyNames().contains(down)
				&& getKeyNames().contains(rotate) && getKeyNames().contains(pause))) {
			System.out.println("Invalid key in config, saving defaults");
			return;
		}
		Config.left = left;
		Config.right = right;
		Config.rotate = rotate;
		Config.down = down;
		Config.pause = pause;

	}

	public static void saveConfig() throws Exception {
		File config = new File(getDefaultDirectory(), "/tetris/config");
		if (!config.exists()) {
			config.createNewFile();
		}
		PrintWriter pw = new PrintWriter(config);
		pw.println("right : " + right);
		pw.println("left : " + left);
		pw.println("rotate : " + rotate);
		pw.println("down : " + down);
		pw.println("pause : " + pause);
		pw.close();
	}

	public static String getDefaultDirectory() {
		String os = System.getProperty("os.name").toUpperCase();
		if (os.contains("Win")) {
			return System.getenv("AppData");
		}
		if (os.contains("MAC")) {
			return System.getProperty("user.home") + "Library/Application support";
		}
		return System.getProperty("user.home");
	}

}
