package dev.apexstudios.apexcore.common.platform;

import java.util.Optional;

public interface Mod
{
    String id();

    String displayName();

    String issueTrackerURL();

    Optional<String> sourcesURL();

    Optional<String> displayURL();
}
