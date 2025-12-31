package x.withlithum.neoware.server.commands.condition;

import x.withlithum.neoware.server.commands.PermissionRank;

public final class CommandConditions {
    private CommandConditions() {}

    public static final NeoCommandCondition IS_MEMBER = createRank(PermissionRank.MEMBER);

    public static final NeoCommandCondition IS_MODERATOR = createRank(PermissionRank.MODERATOR);

    public static final NeoCommandCondition IS_SYS_OP = createRank(PermissionRank.SYS_OP);

    public static final NeoCommandCondition IS_WHEEL = createRank(PermissionRank.WHEEL);

    private static NeoCommandCondition createRank(PermissionRank rank) {
        return new PermissionConditionImpl(rank);
    }
}
