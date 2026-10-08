const std = @import("std");

export fn fsh_bridge_version() callconv(.C) [*:0]const u8 {
    return "0.1.0";
}

export fn fsh_bridge_init() callconv(.C) void {
    // Placeholder. Real init goes here.
}

test "bridge loads" {
    try std.testing.expect(true);
}
