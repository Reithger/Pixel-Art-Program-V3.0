package visual.settings.page.tile;

public class TileMetaInfo {

	private String reference;
	private Integer refreshCode;
	private Integer pushChangeCode;
	
	public TileMetaInfo(String in, Integer refr, Integer push) {
		reference = in;
		refreshCode = refr;
		pushChangeCode = push;
	}
	
	public String getReference() {
		return reference;
	}
	
	public Integer getRefreshCode() {
		return refreshCode;
	}
	
	public Integer getPushChangeCode() {
		return pushChangeCode;
	}
	
}
