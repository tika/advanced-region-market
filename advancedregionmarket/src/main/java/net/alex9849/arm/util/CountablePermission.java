package net.alex9849.arm.util;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;
import java.util.stream.Collectors;

public class CountablePermission {

    // Tika - 10/8/21
    // This class will be used for permissions that
    // use a number system which we need to get the highest/lowest
    // number. E.g. if a player has:
    //  - essentials.homes.1 -> 1 home
    //  - essentials.homes.2 -> 2 homes
    // We would want to return the second homes, depending on what
    // function is used. For this plugin, it will be used for:
    //  - arm.discount.<amount>
    //  - arm.limit.<amount>

    private final String baseNode; // e.g. arm.limit
    private final Player player;
    private final List<String> perms;
    private final List<Integer> counts;

    public CountablePermission(String baseNode, Player player) {
        this.baseNode = baseNode;
        this.player = player;
        this.perms = player.getEffectivePermissions().stream()
                .filter(PermissionAttachmentInfo::getValue)
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permString -> permString.startsWith(baseNode))
                .collect(Collectors.toList());

        this.counts = perms.stream()
                .map(perm -> {
                    String[] splitted = perm.split("\\.");
                    return Integer.parseInt(splitted[splitted.length - 1]);
                })
                .collect(Collectors.toList());
    }

    public boolean hasAny() {
        return perms.size() > 0;
    }

    public double getMax() {
        int max = Integer.MIN_VALUE;
        for (int x : counts)
            if (x > max) max = x;
        return max;
    }

    public double getMin() {
        int min = Integer.MAX_VALUE;
        for (int x : counts)
            if (x < min) min = x;
        return min;
    }

}
