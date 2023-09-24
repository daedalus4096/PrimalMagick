<p align="center"><img src="https://i.imgur.com/hA5Yl2j.png" alt="Primal Magick Logo" width="600"></p>
<h2 align="center">Harness the magick of the primal sources of the land</br>
    <a title="CurseForge" target="_blank" href="https://www.curseforge.com/minecraft/mc-mods/primal-magick"><img src="http://cf.way2muchnoise.eu/full_primal-magick_downloads.svg" alt="CurseForge"></a>
    <a title="Modrinth" target="_blank" href="https://modrinth.com/mod/primal-magick"><img alt="Modrinth Downloads" src="https://img.shields.io/modrinth/dt/VlgDBPBy?logo=modrinth&logoColor=1c1c1c&labelColor=5ca424&color=242629" alt="Modrinth"></a>
    <a title="License" target="_blank" href="https://github.com/daedalus4096/PrimalMagick/blob/master/LICENSE"><img alt="License" src="https://img.shields.io/github/license/daedalus4096/PrimalMagick?color=900c3f"></a>
    <a title="Crowdin" target="_blank" href="https://crowdin.com/project/primal-magick"><img src="https://badges.crowdin.net/primal-magick/localized.svg"></a>
    <a title="Discord" target="_blank" href="https://discord.gg/VYqn7wGKaS"><img alt="Discord" src="https://img.shields.io/discord/880654627270434896?logo=discord&logoColor=ffffff&label=Discord&color=5865f2"></a>
</h2>
<p>Primal Magick is a magick-themed mod for Minecraft.  Research new items, cast spells, and attune yourself to the wonders of magick!  To get started, simply go exploring and check out any ancient shrines you happen to find along the way.  Everything else you need to progress is taught to you in the course of play, no external wikis needed!</p>


## For Mod/Modpack Maintainers

### Adding affinities for other mods

PrimalMagick derives affinities for items from their recipes; for base items that have no recipe, you will need to provide datapack-format affinity definitions.

The below commands will make generating these affinities correctly easier when run inside a minecraft instance with PrimalMagic and your mod(s).

/pm affinities lint [all] - iterates over all items to identify which have no sources and logs a list of items that resolve to empty sourceLists in ResourceLocation (mod:itemname) form. Also sends a message to the triggering player with a count of items missing sources. By default, will skip over any entities in minecraft and primalmagick namespaces, on the theory that the mod owner owns the task of maintaining those.

/pm affinities generateDatapack [all] - Performs the same task as above, including skipping minecraft and primalmagick namespaces by default, then writes a datapack to disk in the configured java temp directory containing all the affected items. The path to the file is written to system logs.
