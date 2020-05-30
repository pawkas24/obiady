package obiady.Utility;

public enum Unit {

		SZT("szt."), LYZ_S("łyżeczka"), LYZ_L("łyżka"), G("gram"), DAG("dag"), KG("kg"), L("litr"), ML("ml"), SZKL("szklanka"), ZB("ząbek"), PECZ("pęczek");
		
		private String label;
		private Unit (String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return label;
		}
}
