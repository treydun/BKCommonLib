package com.bergerkiller.bukkit.common.protocol;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_4_R1.Packet;

import com.bergerkiller.bukkit.common.internal.CommonPlugin;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class ProtocolLib {
	
	public static void enable(CommonPlugin plugin_) {
		ProtocolManager pm = ProtocolLibrary.getProtocolManager();
		HashMap<Set<Integer>, ConnectionSide> packets = new HashMap<Set<Integer>, ConnectionSide>();
		packets.put(Packets.Client.getSupported(), ConnectionSide.CLIENT_SIDE);
		packets.put(Packets.Server.getSupported(), ConnectionSide.SERVER_SIDE);
		
		for(Set<Integer> set : packets.keySet()) {
			pm.addPacketListener(new PacketAdapter(plugin_, packets.get(set),
					ListenerPriority.NORMAL, set) {
				@Override
				public void onPacketReceiving(PacketEvent event) {
					PacketContainer packet = event.getPacket();
					Packet vanilla = (Packet)packet.getHandle();
					Player player = event.getPlayer();
					
					if(!PacketUtil.callPacketReceiveEvent(player, vanilla))
						event.setCancelled(true);
				}
				
				@Override
				public void onPacketSending(PacketEvent event) {
					PacketContainer packet = event.getPacket();
					Packet vanilla = (Packet)packet.getHandle();
					Player player = event.getPlayer();
					
					if(!PacketUtil.callPacketSendEvent(player, vanilla))
						event.setCancelled(true);
				}
			});
		}
	}
}
