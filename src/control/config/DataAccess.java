package control.config;

import java.util.ArrayList;
import java.util.HashMap;

import filemeta.config.Config;

public class DataAccess {
	
//---  Constants   ----------------------------------------------------------------------------
	
	private static final String SEPARATOR = ";,;";
	private static final String NEW_LINE = "~_~_~";
	
	private static final String ROOT_FOLDER = "PixelArtProgram";
	private static final String FILE_PALLETTES = "saved_pallettes.txt";
	private static final String FILE_MAPPINGS = "key_mappings.txt";
	
	private static final String FILE_ENTRY_SIZE = "size";
	private static final String FILE_ENTRY_PALLETTES = "pallettes";
	private static final String FILE_ENTRY_MAPPINGS = "key_mapping";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Config c;
	
//---  Constructors   -------------------------------------------------------------------------

	public DataAccess() {
		c = new Config("", new ValidateConfig());
		
		c.addFilePath(ROOT_FOLDER);
		c.addFile(ROOT_FOLDER, FILE_PALLETTES, "");
		c.addFileEntry(ROOT_FOLDER, FILE_PALLETTES, FILE_ENTRY_SIZE, "Number of pallettes", "?");
		c.addFileEntry(ROOT_FOLDER, FILE_PALLETTES, FILE_ENTRY_PALLETTES, "Pallette color info", "?");
		c.addFile(ROOT_FOLDER, FILE_MAPPINGS, "");
		c.addFileEntry(ROOT_FOLDER, FILE_MAPPINGS, FILE_ENTRY_MAPPINGS, "Key Mappings", "?");
		
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
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void saveColorPallettes(ArrayList<ArrayList<Integer>> pallettes) {
		StringBuilder sb = new StringBuilder();

		Config.setConfigFileEntry(ROOT_FOLDER + "/" + FILE_PALLETTES, FILE_ENTRY_SIZE, pallettes.size()+"");
		
		for(ArrayList<Integer> list : pallettes) {
			for(Integer s : list) {
				sb.append(s + SEPARATOR);
			}
			sb.append("\n" + NEW_LINE + "\n");
		}
		Config.setConfigFileEntry(ROOT_FOLDER + "/" + FILE_PALLETTES, FILE_ENTRY_PALLETTES, sb.toString());
	}
	
	public void saveKeyMappings(HashMap<Character, Integer> mapping) {
		StringBuilder sb = new StringBuilder();
		
		for(Character c : mapping.keySet()) {
			int v = mapping.get(c);
			sb.append((int)c + SEPARATOR + v + "\n" + NEW_LINE + "\n");
		}
		
		Config.setConfigFileEntry(ROOT_FOLDER + "/" + FILE_MAPPINGS, FILE_ENTRY_MAPPINGS, sb.toString());
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public HashMap<Character, Integer> getKeyMappings(){
		HashMap<Character, Integer> out = new HashMap<Character, Integer>();
		String use = Config.getConfigFileEntry(ROOT_FOLDER + "/" + FILE_MAPPINGS, FILE_ENTRY_MAPPINGS);
		
		if(!use.contains(NEW_LINE)) {
			return null;
		}
		
		String[] maps = use.split(NEW_LINE);
		for(String s : maps) {
			String[] here = s.split(SEPARATOR);
			int c = Integer.parseInt(here[0]);
			int val = Integer.parseInt(here[1]);
			out.put((char)c, val);
		}
		
		return out;
	}
	
	public ArrayList<ArrayList<String>> getColorPallettes(){
		ArrayList<ArrayList<String>> out = new ArrayList<ArrayList<String>>();
		String use = Config.getConfigFileEntry(ROOT_FOLDER + "/" + FILE_PALLETTES, FILE_ENTRY_PALLETTES);
		
		if(!use.contains(NEW_LINE)) {
			return null;
		}
		String[] lines = use.split(NEW_LINE);
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
