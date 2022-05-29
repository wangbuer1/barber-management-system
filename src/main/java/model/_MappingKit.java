package model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("customer", "id", Customer.class);
		arp.addMapping("manager", "id", Manager.class);
		arp.addMapping("recharge", "id", Recharge.class);
		arp.addMapping("staff", "id", Staff.class);
		arp.addMapping("task", "id", Task.class);
		arp.addMapping("type", "id", Type.class);
	}
}

