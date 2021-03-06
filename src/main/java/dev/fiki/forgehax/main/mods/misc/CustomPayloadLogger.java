package dev.fiki.forgehax.main.mods.misc;

import dev.fiki.forgehax.asm.events.packet.PacketInboundEvent;
import dev.fiki.forgehax.asm.events.packet.PacketOutboundEvent;
import dev.fiki.forgehax.main.Common;
import dev.fiki.forgehax.main.util.mod.Category;
import dev.fiki.forgehax.main.util.mod.ToggleMod;
import dev.fiki.forgehax.main.util.modloader.RegisterMod;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CCustomPayloadPacket;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@RegisterMod(
    name = "PayloadLogger",
    description = "Logs custom payloads",
    category = Category.MISC
)
public class CustomPayloadLogger extends ToggleMod {
  private static final Path CLIENT_PAYLOAD_LOG =
      Common.getFileManager().getMkBaseResolve("logs/payload/client2server_payload.log");
  private static final Path SERVER_PAYLOAD_LOG =
      Common.getFileManager().getMkBaseResolve("logs/payload/server2client_payload.log");

  private void log(IPacket packet) {
    if (packet instanceof SCustomPayloadPlayPacket) {
      SCustomPayloadPlayPacket payloadPacket = (SCustomPayloadPlayPacket) packet;
      String input =
          String.format(
              "%s=%s\n",
              payloadPacket.getChannelName(), payloadPacket.getBufferData().toString());
      try {
        Files.write(
            SERVER_PAYLOAD_LOG,
            input.getBytes(),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
      } catch (Exception e) {
      }
    } else if (packet instanceof CCustomPayloadPacket) {
      CCustomPayloadPacket payloadPacket = (CCustomPayloadPacket) packet;
      String input = String.format("%s=%s\n",
          payloadPacket.getName(), payloadPacket.getInternalData());
      try {
        Files.write(
            CLIENT_PAYLOAD_LOG,
            input.getBytes(),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
      } catch (Exception e) {
      }
    }
  }

  @SubscribeEvent
  public void onOutgoingCustomPayload(PacketOutboundEvent event) {
    log(event.getPacket());
  }

  @SubscribeEvent
  public void onIncomingCustomPayload(PacketInboundEvent event) {
    log(event.getPacket());
  }
}
