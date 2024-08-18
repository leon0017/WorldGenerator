package me.leonrobi.worldgenerator.lib;

import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NMSLib {

    public static void sendPacket(@NotNull Player player, @NotNull Packet<?> packet) {
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }

    public static void setValue(@NotNull Object instance, Class<?> c, @NotNull String fieldName, @Nullable Object value) {
        try {
            Field field = c.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static @NotNull Object getValue(Object instance, Class<?> c, @NotNull String fieldName) {
        try {
            Field field = c.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static @Nullable Object getValueOrDefault(Object instance, Class<?> c, @NotNull String fieldName, Object def) {
        try {
            Field field = c.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            return def;
        }
    }

    public static void callFunc(Object instance, Class<?> c, @NotNull String fieldName) {
        try {
            Method method = c.getDeclaredMethod(fieldName);
            method.setAccessible(true);
            method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object callFunc(Object instance, Class<?> c, @NotNull String fieldName, Object... arguments) {
        try {
            Method method = c.getDeclaredMethod(fieldName, Object.class);
            method.setAccessible(true);
            return method.invoke(instance, arguments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
