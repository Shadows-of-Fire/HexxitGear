/*
 * HexxitGear
 * Copyright (C) 2013  Ryan Cohen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package sct.hexxitgear.control;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import sct.hexxitgear.core.ArmorSet;
import sct.hexxitgear.net.ActivateMessage;
import sct.hexxitgear.net.HexNetwork;

public class HexKeybinds {

	public static KeyBinding activateHexxitArmor = new KeyBinding("Activate Hexxit Gear Armor", Keyboard.KEY_X, "Hexxit Gear");
	public static KeyBinding[] keybindArray = new KeyBinding[] { activateHexxitArmor };
	public static boolean[] repeats = new boolean[keybindArray.length];

	public HexKeybinds() {
		ClientRegistry.registerKeyBinding(activateHexxitArmor);
	}

	@SubscribeEvent
	public void keyEvent(InputEvent.KeyInputEvent event) {
		if (!FMLClientHandler.instance().isGUIOpen(GuiChat.class) && activateHexxitArmor.isPressed() && ArmorSet.getCurrentArmorSet(Minecraft.getMinecraft().player) != null) HexNetwork.INSTANCE.sendToServer(new ActivateMessage());
	}

}
