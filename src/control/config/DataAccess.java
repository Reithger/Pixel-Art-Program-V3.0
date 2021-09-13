package control.config;

import java.util.ArrayList;

import filemeta.config.Config;

public class DataAccess {
	
	private static final String SEPARATOR = ";,;";
	private static final String NEW_LINE = "~_~_~";
	
	private static final String ROOT_FOLDER = "PixelArtProgram";
	private static final String FILE_PALLETTES = "saved_pallettes.txt";
	
	private static final String FILE_ENTRY_SIZE = "size";
	private static final String FILE_ENTRY_PALLETTES = "pallettes";
	
	private Config c;

	public DataAccess() {
		c = new Config("", new ValidateConfig());
		
		c.addFilePath(ROOT_FOLDER);
		c.addFile(ROOT_FOLDER, FILE_PALLETTES, "");
		c.addFileEntry(ROOT_FOLDER, FILE_PALLETTES, FILE_ENTRY_SIZE, "Number of pallettes", "?");
		c.addFileEntry(ROOT_FOLDER, FILE_PALLETTES, "pallettes", "Pallette color info", "?");
		
		c.softWriteConfig();
		
		//When we need to make sure our config settings are set up properly, we'll use this
		while(!c.verifyConfig()) {
			switch(c.getErrorCode()) {
				case 0:
					break;
				default:
					break;
			}
		}
	}
	
	public void saveColorPallettes(ArrayList<ArrayList<Integer>> pallettes) {
		StringBuilder sb = new StringBuilder();

		Config.setConfigFileEntry(ROOT_FOLDER + "/" + FILE_PALLETTES, FILE_ENTRY_SIZE, pallettes.size()+"");
		
		for(ArrayList<Integer> list : pallettes) {
			System.out.println(list);
			for(Integer s : list) {
				sb.append(s + SEPARATOR);
			}
			sb.append("\n" + NEW_LINE + "\n");
		}
		Config.setConfigFileEntry(ROOT_FOLDER + "/" + FILE_PALLETTES, FILE_ENTRY_PALLETTES, sb.toString());
	}
	
	public ArrayList<ArrayList<String>> getColorPallettes(){
		ArrayList<ArrayList<String>> out = new ArrayList<ArrayList<String>>();
		String use = Config.getConfigFileEntry(ROOT_FOLDER + "/" + FILE_PALLETTES, FILE_ENTRY_PALLETTES);
		
		System.out.println("Status:\n" + use.replaceAll(NEW_LINE, "\n"));
		
		if(!use.contains(NEW_LINE)) {
			return null;
		}
		String[] lines = use.split(NEW_LINE);
		System.out.println(lines.length);
		for(String s : lines) {
			ArrayList<String> pallet = new ArrayList<String>();
			String[] curr = s.split(SEPARATOR);
			for(String t : curr) {
				pallet.add(t);
			}
			out.add(pallet);
		}
		return out;
	}
	
}
