package snmp.tools.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class SnmpTable {

	private ArrayList<String> indexs = new ArrayList<String>();
	private HashMap<String,ArrayList<SnmpValue>> table = new HashMap<String,ArrayList<SnmpValue>>();
	
	public SnmpTable(String[] baseOIDs){
		
		for(String oid:baseOIDs){
			table.put(oid, new ArrayList<SnmpValue>());
		}
	}

	public String[] getIndexs() {
		return indexs.toArray(new String[indexs.size()]);
	}

	public String getValue(String index,String baseOid) {
		ArrayList<SnmpValue> column = table.get(baseOid);
		if(column!=null){
			String oid = baseOid+"."+index;
			for(SnmpValue v:column){
				if(v.getOid().equals(oid))
					return v.getValue();
			}
		}
		return null;
	}
	
	public void setCell(SnmpValue value){
		String oid = value.getOid();
		for(String baseOid:table.keySet()){
			if(oid.startsWith(baseOid)){
				ArrayList<SnmpValue> column = table.get(baseOid);
				
				int i=0;
				for(;i<column.size();i++){
					String v = column.get(i).getOid();
					if(v.equals(oid)){
						break;
					}
				}
				if(i>=column.size()){
					column.add(value);
					
					String index = oid.substring(baseOid.length()+1);
					if(!indexs.contains(index)){
						indexs.add(index);
					}
				}
			}
		}
	}
}
