package sct.hexxitgear.core;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import sct.hexxitgear.HexxitGear;
import sct.hexxitgear.net.PacketWrapper;
import sct.hexxitgear.net.Packets;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapeHandler {

    public static Map<String, String> capes = new HashMap<String, String>();

    public static void registerCape(String player, String capeUrl) {
        capes.put(player, capeUrl);
        sendCapeUpdate(player, capeUrl);
    }

    public static void removeCape(String playerName) {
        if (playerName != null) {
            capes.remove(playerName);
            sendCapeUpdate(playerName, null);
        }
    }

    public static String getCapeUrl(String player) {
        return capes.get(player);
    }

    public static void sendCapeUpdate(String player, String capeUrl) {
        if (capeUrl == null) {
            capeUrl = "";
        }
        Object[] data = new Object[] { player, capeUrl };

        PacketDispatcher.sendPacketToAllPlayers(PacketWrapper.createPacket(HexxitGear.modNetworkChannel, Packets.CapeChange, data));
    }

    public static void sendJoinUpdate(String player) {
        List<Object> tempList = new ArrayList<Object>();

        tempList.add(0, (byte) capes.size());

        for (String playerName : capes.keySet()) {
            tempList.add(playerName);
            tempList.add(capes.get(playerName));
        }

        PacketDispatcher.sendPacketToAllPlayers(PacketWrapper.createPacket(HexxitGear.modNetworkChannel, Packets.CapeJoin, tempList.toArray()));
    }

    public static void readCapeUpdate(String playerName, String capeUrl) {
        EntityPlayer player = HexxitGear.proxy.findPlayer(playerName);
        if (player != null) {
            if (capeUrl != "") {
                capes.put(playerName, capeUrl);
                player.cloakUrl = capes.get(playerName);
                FMLClientHandler.instance().getClient().renderEngine.obtainImageData(player.cloakUrl, null);
            } else {
                capes.remove(playerName);
                player.cloakUrl = null;
            }
        }
    }

    public static void readJoinUpdate(DataInputStream data) {

        try {
            capes = new HashMap<String, String>();

            int count = data.readByte();

            String playerName, capeUrl;
            for (int i = 0; i < count; i++) {
                playerName = data.readUTF();
                capeUrl = data.readUTF();
                capes.put(playerName, capeUrl);
                EntityPlayer player = HexxitGear.proxy.findPlayer(playerName);
                if (player != null) {
                    player.cloakUrl = capes.get(playerName);
                    FMLClientHandler.instance().getClient().renderEngine.obtainImageData(player.cloakUrl, null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}