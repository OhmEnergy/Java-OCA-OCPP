package eu.chargetime.ocpp.model.ohme;

public class CellTower {

	private String cellId;

	private String lac;

	private String mcc;

	private String mnc;

	private String signalDbn;

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getLac() {
		return lac;
	}

	public void setLac(String lac) {
		this.lac = lac;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public String getSignalDbn() {
		return signalDbn;
	}

	public void setSignalDbn(String signalDbn) {
		this.signalDbn = signalDbn;
	}
}
