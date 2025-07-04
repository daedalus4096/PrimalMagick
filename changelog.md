v6.0.7
------
* Add client-side config option to render affinities as text instead of with icons
* Fixed a bug where Mana Orbs could sometimes drastically reduce mana stored in a wand
* Made text formatting of mana values during wand charging less jerky
* Fixed a bug where placing warded armor into an Auto-Charger would appear to equip the armor instead

v6.0.6
------
* Added Pixie Houses, to give your flighty friends a home to defend
* Added Spelltomes, to expand the repertoire of spells you can use at once
* Added Mana Orbs, to supplement your supply of mana on the go
* Added a new enchantment for Mana Orbs, Pondering, to increase the rate of paired wands' passive regeneration
* Allowed magickal saplings to be placed in flower pots
* Allowed the Desalinator to fill itself with water if placed in an infinite water source
* Increased mana throughput for all tiers of mana relays and injectors
* Increased the required mana siphon amount for the Link Saturation advancement
* Made the Mana Efficiency enchantment applicable to magickal robes and offhand items as well
* Made the Spell Power enchantment applicable to spelltomes as well
* Fixed a bug where consecration fields would cast shadows
* Fixed a bug where the hitbox of the Celestial Harp didn't always line up with it visually
* Fixed an item duplication bug with the Runic Grindstone
* Fixed a bug where the Runic Grindstone wouldn't give hints in some circumstances
* Fixed a bug that could allow Enderman teleportation to ignore an Enderward in some circumstances
* Restored the Treasure enchantment to full functionality
* Made several blocks waterloggable (Thanks, eehunter!)
* Removed outdated references to mana cost penalties from some Grimoire entries
* Fixed a tooltip bug for wands that were showing incorrect mana costs for spells
* Fixed base mana cost modifiers for multiple spell components

v6.0.5
------
* Added Mana Injectors, to let Magitech devices pull mana directly from fonts
* Added Mana Relays, to extend the range of your mana distribution network
* Magitech devices compatible with Mana Injectors now also accept material inputs from hoppers attached to the sides of the block, in addition to the top
    * NOTE: Infernal Furnaces cannot currently accept Ignyx fuel via hopper; this will be fixed in a future version
* Greatly decreased the mana regeneration rate of Artificial Mana Fonts
* Greatly increased the mana regeneration rate of Heavenly Mana Fonts
* Rebalanced mana consumption and storage capacity of magitech devices to account for the introduction of mana networking
* Added the Desalinator, for when you need help boiling water
* Changed the prerequisite for Expert Magitech from the Honey Extractor to the Desalinator
* Added an affinity index to the Grimoire, to remind you of all the data you've analyzed
* Changed Scribe's Table vocabulary costs from levels to a flat number of experience points
* Previously studied ancient books in chiseled or carved bookshelves near the Scribe's Table now grant an experience discount when studying vocabulary
* Made item textures for energized gems more distinct from their parents' versions
* Added a warning message for when the player tries to transform the wrong type of bookshelf into a Grimoire
* Fixed various tooltips to refer to mana effectiveness instead of flat discounts
* Fixed Flying Carpet research to only require knowledge of Insight Runes instead of Power Runes
* Removed the Bloodletter from the research and prop requirements for Blood Roses and Emberflowers
* Standardized the breaking times of many devices, to make it less painful to have to move them
* Allowed mana fonts to stack to sixteen
* Fixed a bug allowing Magick Protection to be applied to armor alongside other Protection-style enchants
* Fixed a bug where the contents of Essence Casks would not be analyzed when scanned with the Arcanometer
* Fixed a bug where the Grimoire entries for ritual tool rune enchantments would be prematurely unlocked

v6.0.4
------
* Added a toast that appears when the player unlocks a new research discipline
* Added Grimoire buttons to certain entries to help guide players to where they need to go in the book
* Made the icons of newly-unlocked disciplines pulse until read
* Added highlight icons to unread Grimoire entries
* Moved datapack template generation to a background thread
* Fixed a bug that could crash a server when firing projectile spells through Nether portals in certain circumstances
* Fixed a bug allowing some attribute modifiers to ignore suppression by Attunement Shackles
* Fixed a server crash that would occur when retrieving a block entity's item handler with a null direction on Neoforge
* Moved tips data from client resource pack to server data pack
* Hardened the Treefolk fertilization process against errors in other mods' blocks
* Fixed a bug where ritual props not connected to salt would still count for stability purposes
* Fixed a mod conflict with Immersive Engineering during some affinity calculations

v6.0.3
------
* Fixed incorrectly specified mod dependency on Common Networking

v6.0.2
------
* Redesigned mana efficiency, now called mana effectiveness, to feature diminishing returns; increased mana effectiveness of all wand types to compensate
    * NOTE: This change seeks to eliminate the efficiency penalty on low-grade wands without resulting in overwhelming discounts at high levels. The end result should be a buff to players at low power levels and a nerf at high power levels, breaking even at roughly the Master tier of research.
* Added ability to wax copper blocks using beeswax
* Redesigned the Essence Thief enchantment: now provides a chance to drop a single essence from slain mobs, with higher enchantment levels yields higher grades of essence
* Made all glow fields sparkle for visibility
* Glow fields conjured by spells are now permanent
* Glow fields conjured by player Sun attunement now fade over time
* Updated all recipes to express mana cost in hundredths of mana points, rather than whole mana points
* Replaced ancient library accent blocks with stained skyglass
* Added support for specifying a name translation key for research entries in datapacks
* Fixed dispenser behavior for Mana-Tinged Arrows
* Fixed a world-destroying bug that could occur when players accumulated too much knowledge
* Updated to Forge 52.1.0
* Updated to NeoForge 21.1.132

v6.0.1-beta
-----------
* Added recipes to un-attune Humming Artifacts
* Improved mouse navigation options for the Grimoire
* Added a hint to the Upcoming section of discipline pages in the Grimoire for when available leads have been exhausted
* Disabled repairs for Earthshatter Hammer to fix a duplication bug
* Allowed all blocks in the common crafting tables block tag to be transformed into Arcane Workbenches
* Fixed a bug causing calcinators to output only Alchemical Waste when their owner is logged out
* Fixed a bug preventing staves and mundane wands from being enchanted at an Enchanting Table
* Fixed missing Alchemical Bomb localizations for new 1.21 potion effects
* Fixed a bug allowing some low-level Break spells to be cast for free
* Fixed a bug allowing Concocter recipes to sometimes overwrite existing output items
* Fixed a bug that in some heavy mod packs would prevent Primal Magick from loading

v6.0.0-beta
-----------
* Added NeoForge support to Primal Magick
* Updated to Forge 52.0.40

v5.0.2-beta
-----------
* Fixed a crash that would occur when pixies or the Inner Demon would attempt to cast a spell

v5.0.1-beta
-----------
* Updated to Forge 52.0.15
* Added affinity data for new items and creatures
* Added sanguine cores for new creature types
* Added rune enchantment definitions for new mace enchantments
* Runecarving Table recipes now properly grant expertise
* Fixed a crash that occurs when a thrown magickal metal trident strikes a creature
* Fixed a bug preventing the Silk Touch property for Break spells working in certain circumstances
* Fixed concocter recipes to properly recognize water flasks and water bombs as ingredients
* Fixed an issue where some Earthshatter Hammer and Dissolution Chamber recipes were not being recognized
* Made the Shear spell payload's research entry no longer require shearing things as a prerequisite
* Removed the shear use statistic from mod tracking
* Fixed Dissolution Chamber research requirements to use metal dust tags instead of specific items
* Updated the Conjure Animal spell to allow summoning armadillos
* Fixed a bug where conjuring light could crash a multiplayer server in certain circumstances
* Prevent Treefolk from fertilizing Alpha Grass blocks from the mod Regions Unexplored to prevent a crash

v5.0.0-beta
-----------
* Updated to Minecraft 1.21.1
* Added ancient libraries, for those seeking knowledge of the past
* Added story advancements, because they're a great teaching tool
* Replaced item craft count requirements for tier research with a new system: expertise
* Many sword/axe/trident enchantments can now also be applied to staves and maces
* The Aegis enchantment now also reduces fire burn time and explosion knockback suffered
* Decreased the rarity of the Spell Power enchantment at the Enchanting Table
* Some rune enchantments (Lifesteal, Enderlock, and Guillotine) are now available at the Enchanting Table
* The ward shield bar now renders on top of the health bar, instead of above the armor bar
* Essence Furnaces can now be transformed from any block in the forge:furnaces block tag, instead of just the vanilla Furnace

v4.2.0-beta
-----------
* Updated to Minecraft 1.20.4
* Added backgrounds and tooltips to most mod device slots
* Added Earthshatter Hammer and Dissolution Chamber recipes for common modded ores
* Added ability for datapacks to specify recipes that output a tag instead of an item
* Fixed a page navigation bug in the Grimoire
* Glow fields from sunlamps should no longer interfere with things like tree growing

v4.1.1
------
* Full release for Minecraft 1.20.2

v4.1.0-beta
-----------
* Updated to Minecraft 1.20.2

v4.0.9
------
* Fixed a client crash when picking up the Dream Journal in some modpacks
* Fixed a potential server crash in instances of malformed theorycrafting data packs
* Fixed a bug where research entries in the Grimoire would sometimes overrun available space
* Fixed a bug where shift-clicking device output would not record the item as having been crafted
* Added hints to some ambiguous research requirements
* Made the Arcane Workbench show final modified mana costs
* Added Bamboo Jungles to the list of biomes in which Sky shrines can spawn
* Removed redundant output when a player executes a debug command on themselves
* Datapacks can now specify additional non-theory rewards for theorycrafting projects, such as experience or items
* Added additional non-theory rewards to several theorycrafting projects
* Added text searching to the Recipes section of the Grimoire

v4.0.8
------
* Fixed a bug where Incense Braziers could be chosen for activation twice in a row during a ritual
* Added many new theorycrafting projects to the pool
* Increased rewards for some theorycrafting projects and bonus materials
* Rebalanced material requirements for some theorycrafting projects
* Made some low-value theorycrafting projects less likely to show up as you advance in research
* Added Synthetic Gem Buds, for a different kind of farming
* Made the Earthshatter Hammer and Dissolution Chamber able to refine and multiply Rock Salt
* Reduced break time of the Celestial Harp to a more reasonable level
* Made datapack template generation tool include sourceless entities as well as items
* Made devices which were previously compatible with hoppers also compatible with modded pipes, etc
* Made Essence Furnace compatible with both hoppers and pipes
* Fixed a bug with the Infernal Furnace which could delete existing output upon selecting a recipe book entry
* Fixed client crashes when an item's affinities cannot be calculated due to cyclic recipe chains
* Fixed a client crash when picking up the Dream Journal in some modpacks
* Fixed a bug causing upcoming research entries to be hidden in the Grimoire when they shouldn't be
* Allowed salted steak and potato variants to be used as materials in research projects
* Added Enderwards, to keep those pesky Endermen out

v4.0.7
------
* Added Earthshatter Hammer and Dissolution Chamber recipes for Rock Salt and Ancient Debris
* Gave Phantom Membranes some moon affinity
* Fixed tooltip Z-order issue with Runecarving Table screen
* Fixed tooltip Z-order issue with Research Table screen
* Changed research projects to use the Forge milk tag instead of just the milk bucket
* Added new theorycrafting project to the pool
* Made research projects refund container items, such as empty milk buckets
* Increased base processing speed of most magitech devices
* Increased total mana capacity of the Dissolution Chamber
* Fixed a multiplayer display bug in the menu of the Mana Nexus
* Fixed a bug preventing ops from granting research via debug command in multiplayer
* Fixed a display bug with long player names on the Dream Journal
* Made Flying Carpet drop in world upon taking sufficient damage instead of being destroyed
* Fixed a rare client crash that could occur when initializing the arcane recipe book
* Eliminated toast spam when granting research via debug command
* Added Creative Grimoire, for creative players who don't want to fiddle with debug commands
* Made Endermen take additional damage from Sea spells; they remain immune to ranged attacks
* Fixed a bug where the Bounty enchantment could recursively multiply bonus loot
* Improved the Bounty enchantment to increase yields of all members of the vanilla crops block tag
* Added a command to help debug from where items are sourcing their affinities
* Added Guillotine enchantment, to help you get a head
* Added a Cryotreatment recipe to turn magma cream into slime balls
* Fixed a bug where Consecration fields caused suffocation
* Added Shear spell payload, to help you keep things neatly trimmed
* Added theory gain preview to the Research Table

v4.0.6
------
* Fixed a client crash that could sometimes occur with the arcane recipe book
* Increased affinities for bamboo blocks and their byproducts
* Increased Void affinity for chorus fruit
* Allowed Earthshatter Hammer and Dissolution Chamber to work with deepslate
* Added more Stonemelding recipes
* Fixed a bug with Mana Nexus research prerequisites
* Made Treefolk attempt to find land instead of swimming all day
* Fixed typos in the scan entry for Humming Artifacts
* Reduced break time of Ritual Bells
* Reduced the requirements for Expert, Master, and Supreme Ritual Magick research
* Added the Arcane Workbench as a JEI catalyst for vanilla crafting
* Added the Infernal Furnace as a JEI catalyst for vanilla smelting
* Fixed a bug causing Sunlamps to break when attached to phasing blocks such as Moonwood Logs
* Made runescribed enchantment results consistent in the face of conflicting combinations
* Fixed an error preventing granting users all scan research via debug command
* Added Hydromelons, a refreshing crop rich in Sea essence
* Added Blood Roses, a gothic decoration rich in Blood essence
* Added Emberflowers, a bright decoration rich in Infernal essence
* Added ability to make torches in bulk using Ignyx
* Various internal refactoring

v4.0.5
------
* Fixed a progression-breaking bug with the Analysis Table and Arcanometer
* Fixed a bug causing empty requirements sections to be shown for research entries in the Grimoire
* Fixed some error spam when reloading resource packs

v4.0.4
------
* Moved affinity calculation to a background thread to improve performance in large modpacks
* Fixed a bug preventing players from receiving observations for scanning mobs
* Improved arcane recipe book performance in large modpacks
* Added a Tips section to the Grimoire
* Fixed a multiplayer client crash when using the Auto Charger
* Refactored how the Dream Journal's text data is stored
* Made the Dream Journal able to be cloned
* Made enchanted books readable, at least if you're fluent in Standard Galactic
* Made enchanted books placeable on vanilla lecterns
* Changed ritual lectern to now retrieve books by shift-right-clicking
* Added debug command to grant mod books
* Added affinities to some items that were missing them
* Added tools to help mod and datapack authors identify items needing affinities; thanks Issaferret!
* Updated to Forge 47.2.0

v4.0.3
------
* Fixed a bug causing some attunement buffs to fall off upon player death
* Converted localization strings to a code-generated asset
* Allowed ritual-enhancement and Hallowed enchantments to be placed on books
* Added Infernal Furnace, to really heat things up
* Fixed a crash that could occur when changing game language
* Fixed a bug causing Ancient Mana Fonts to refill on every server restart
* Updated wand charger texture
* Added Mana Nexus, to better keep your arcane power
* Added Sanguine Core types for camels and sniffers
* Made Grimoires placeable on chiseled bookshelves
* Made Grimoires placeable on vanilla lecterns
* Fixed a bug with several devices causing their first input to be processed instantly
* Added GUI labels to some devices missing them
* Fixed a connectivity bug with Hallowed Marble Walls
* Added Warding Modules, to provide some extra magickal protection
* Renamed Wand Charger to Mana Charger to reflect its expanded scope of purpose
* Fixed a localization bug in JEI integration

v4.0.2
------
* Added Attunement Shackles, for those who don't want to embrace magickal attunement
* Fixed compatibility issue with the mod Create
* Changed allow and ban lists for the Polymorph spell to use tags instead of inter-mod comms
* Made mod robes and armor compatible with vanilla armor trims
* Added Runic Armor Trim, to better target your robes' mana discount
* Added support for several common ore types from other mods to the Lucky Strike enchantment
* Allowed datapack customization of ores supported by Lucky Strike's loot modifier
* Upgraded the Dowsing Rod to mark expected symmetry location if no matching block is found
* Fixed symmetry calculation bug with Offering Pedestals
* Performed miscellaneous refactoring and code cleanup
* Converted most hand-written blockstate, model, sound, and structure files to code-generated assets
* Fixed alchemical bombs with dud potions showing up in creative tab
* Made sunwood and moonwood trees use a solid render type when fully phased in

v4.0.1
------
* Changed the Essence Thief enchantment to work better with high damage weapons
* The Essence Thief enchantment can now also be applied to wands and staves
* Runic Grindstones will now grant hints to rune combinations when grinding off enchantments
* Reduced material requirements to make Power Runes
* Limited Power Runes to one per runescribe
* Added Insight Runes, to start empowering rune enchantments sooner in a limited fashion
* Added Grace Runes, to get the maximum power from your rune enchantments
* Rebalanced requirements in research and recipes that needed Power Runes
* Allowed rebinding of the View Item Affinities key, previously Shift, now by default Left Shift

v4.0.0-beta
-----------
* Updated to Minecraft 1.20.1
* Improved lag when reading affinities from items in large modpacks; thanks Issaferret!
* Fixed pixies' recognition of their owner upon spawn
* Breaking blocks bare-handed for the Break spell payload no longer requires an empty offhand
* Fixed race condition when syncing capability data
* Added missing affinities to Blood-Scrawled Ravings
* Allow datapack customization of biomes containing mod worldgen features
* Allow datapack customization of blocks on which mod trees can be placed
* Protect certain precious items from being consumed in the Analysis Table
* Fixed an issue preventing certain mod projectiles from breaking Chorus Flower blocks on impact

v3.2.2
------
* Added Sanguine Core types for allays and frogs
* Updated model for Wand Assembly Table
* Made mod saplings and leaves compostable
* Removed duplicate wheat seed reward from treefolk bartering loot table
* Exempted tall grass and ferns from treefolk fertilization
* Fixed bug preventing conversion of mod logs into charcoal
* Lowered output of wool in the Dissolution Chamber to four string

v3.2.1
------
* Fixed client crash with Enderport enchantments on multiplayer servers
* Fixed Runic Grindstone to remove runes even from unenchanted items
* Fixed multiplayer server crash on startup
* Removed default affinity definitions from chainmail armor
* Changed Runecarving Table slots to be filtered by tags instead of hard-coded items
* Added rune combinations for some previously missing enchantments
* Changed rune combination definitions to be modifiable by datapacks
* Treefolk will now barter with players in exchange for bone meal
* Treefolk will now occasionally throw spontaneous dance parties when idle
* Treefolk will now fertilize most nearby bonemealable plant life when idle
* Baby treefolk can now be grown from special seeds, which adults will sometimes offer in barter
* Added Chinese (Simplified) translations; thanks ArgonCrystal!

v3.2.0
------
* Updated to Minecraft 1.19.2
* Added Essence Casks, to keep your essence stocks tidier
* Added Wand Glamour Tables, to change the appearance of your wands
* Added missing recipe for Bomb Casings
* Added Dissolution Chamber recipe for Glowstone Dust
* Gave attunement-dropped glow fields a minor particle effect
* Replaced lesser Sea attunement bonus with flat swim speed buff
* Fixed a bug causing spells to cast when using a wand on an interactable object such as a mana font
* Added required research to JEI recipes
* Updated ores tag data for mod ores
* Fixed crash when using Essence Thief enchantment on mobs without affinities
* Incremented to Forge version 43.1.1
* Set minimum required Forge version to 43.1.1 for loading

v3.1.0
------
* Incremented to Forge version 41.1.0
* Set minimum required Forge version to 41.1.0 for loading

v3.0.1-beta
-----------
* Added Humming Artifacts, as some arcane dungeon loot
* Added Recall Stones, to whisk you away home
* Pixies are now drained instead of destroyed upon taking fatal damage, and can be revived with essence
* Made Infernal Pixies immune to fire
* Players must now travel lower to complete the research Source: The Earth
* Players must now travel higher to complete the research Source: The Sky
* Earthshatter Hammers may no longer be combined at a workbench for repairs
* Dissolution Chamber recipe changed to use an Earth Crystal instead of an Earthshatter Hammer
* Inscribing a spell onto a wand will now make that spell active on the wand as well
* Added more guidance for uncovering the secrets of the universe
* Added hints to some of the more oblique research requirements
* Added wand HUD
* Added radial menu for wand spell selection
* Made wand GUIs offhand friendly
* Added finale research, to let you know when you've finished
* Fixed crash when conjuring stone underwater
* Incremented to Forge version 41.0.109
* Set minimum required Forge version to 41.0.109 for loading

v3.0.0-beta
-----------
* Updated to Minecraft 1.19
* Removed vestigial worldgen config options
    * NOTE: Generation of marble, salt, and quartz is now controlled via datapacks
* Changed Sanguine Core recipe for Goats to use Goat Horns

v2.1.2
------
* Add Zephyr Engine and Void Turbine, to generate wind tunnels
* Fix issue with wand transform mod compatibility
* Fix bug allowing players to get extra companions through clever use of game mechanics
* Greatly increase spawn rate of quartz ore in new Overworld chunks
* Add Dissolution Chamber recipe for breaking down quartz blocks
* Decrease required Magitech crafts for tier progression
* Add theorycraft speed config to modify Research Table theory yields
* Increase drop chance of Four-Leaf Clovers
* Increase Sea and Sky affinities of several items

v2.1.1
------
* Add a secret means to befriend witches, RIP Corspilla
* Prevent hellish chain attacks from targeting team mates
* Prevent smaller healing spell absorbs from applying when a larger absorb is already active
* Update model for essence furnace

v2.1.0-beta
-----------
* Update to MC 1.18.2
    * NOTE: Due to technical changes by Mojang, compatibility with worlds from 1.18.1 and before is not guaranteed
* Make different types of shrine separately locateable with commands (e.g. /locate primalmagick:earth_shrine)
* Make sun shrines also spawn in badland type biomes
* Remove config options to control shrine spacing during worldgen
    * NOTE: Due to technical changes by Mojang, these values are now located in the shrine's structure_set JSON file

v2.0.7
------
* Fix multiplayer server crash when using spellcrafting altar

v2.0.6
------
* Fix texture references for wand and staff caps
* Update model for spellcrafting altar
    * NOTE: Players upgrading existing worlds must break and re-place the block for the new renderer to activate
* Update textures for basic marble
* Add new Marble Tiles decorative block
* Update model for Offering Pedestal
* Update textures for cloth armor
* Allow re-mapping of grimoire navigation keybinds
* Make right-clicking in the grimoire GUI go back a topic
* Add four-leaf clovers, to help you concoct some luck if you can find one in the tall grass
* Allow magical tree leaves to be harvested by any shears-like tool
* Increment to Forge version 39.1.0

v2.0.5
------
* Let the Enchanting Studies research project recognize Quark Matrix Enchanters as valid materials
* Add config options to disable ore worldgen features; use with caution
* Have the grimoire remember player browsing history between sessions
* Make the grimoire ribbon a shortcut to the main index
* Show icons on grimoire entries
* Change sort order of disciplines in the grimoire main index
* Update models for wands and staves
* Update texture for refined salt
* Add missing scan research entry for dragon heads

v2.0.4
------
* Add JEI support
* Add Enchantment Descriptions support
* Fix error when placing certain block entities
* Fix statistic recording for runescribing and spellcrafting
* Allow the spellcasting altar to face different directions
* Fix bug with hoppering fuel into empty calcinators
* Fix additional block entity saving bugs
* Increment to Forge version 39.0.59
* Update recipes to use new Forge tags

v2.0.3
------
* Update model for runecarving table
* Fix misaligned mana cost widgets on Arcane Workbench screen
* Fix broken Spirit Lantern recipe
* Fix block entity saving

v2.0.2
------
* Update model for ritual altar
* Update texture for rock salt ore
* Add config file entries to control shrine spacing in worldgen
* Fix client crash when fetching affinities for items with circular recipe chains
* Fix client crash when fetching affinities for items made in a non-standard crafting grid
* Increment to Forge version 39.0.17

v2.0.1
------
* Full release for MC 1.18.1!

v2.0.0-beta
-----------
* Update to MC 1.18.1

v1.0.0
------
* Full release for MC 1.17.1!

v0.1.2-beta
-----------
* Increment to Forge version 37.1.0

v0.1.1-beta
-----------
* Return unscanned items to player when closing Analysis Table GUI

v0.1.0-beta
-----------
* Rename mod to Primal Magick
* Add update check URL

v0.0.22-alpha
-------------
* Add Dissolution Chamber, for ore tripling at the cost of Earth mana
* Fix multiplayer client crash when using Sanguine Crucible
* Increment to Forge version 37.0.104

v0.0.21-alpha
-------------
* Fix Z-ordering issues with Research Table tooltips
* Add Hoe of the Nurturing Sun, to stimulate rapid plant growth
* Add new research project using a Dragon Head as a research aid
* Increase reward for Draconic Energies research project
* Spell projectiles are no longer slowed by water
* Rune enchantments discovered from primal tools are now indexed upon discovery, rather than creation
* Add Conjure Light spell payload, to create temporary glow fields
* Update model for Spellcrafting Altar
* Allow glow and consecration fields to work underwater
* Add Conjure Stone spell payload, for when you just need a block of stone right now
* Changed default name of Earth Damage spells to "Earthen" from "Stone"
* Increment to Forge version 37.0.103

v0.0.20-alpha
-------------
* Add scan research identifying most research aids
* Add recipe book functionality to the concocter
* Add Essence Transmuter, to convert one source of essence into another
* Add Mystical Relics, fragments of which drop from certain mobs, to study for theories
* Increment to Forge version 37.0.97

v0.0.19-alpha
-------------
* Allow Heartwood to rarely drop from Sunwood or Moonwood trees
* Add Blood-Scrawled Ravings, a means to unlock the Blood source for the faint of heart
* Certain advanced research project materials are no longer selected until having been crafted a few times
* Certain research project materials now grant bonus theory progress
* Increase distance before companions will attempt to teleport to their owners
* Tighten target selection zone for companion teleportation
* Add Magic Protection enchantment, reducing damage taken from all magical sources
* Change rune enchantment recipe for Blast Protection
* Add Dowsing Rod, to provide feedback on ritual altar layouts
* Add scan research for the Inner Demon
* Increment to Forge version 37.0.84

v0.0.18-alpha
-------------
* Clarified research requirement text for Polymorph spell payload
* Allow players to eat Ambrosia even while full
* Fix Unbreaking enchant interaction with Earthshatter Hammer
* Made ritual warning messages more descriptive about missing offerings and props
* Fix bug reseting arcane recipe book data on player death or return from the End
* Added workaround for translucent textures not rendering under Fabulous graphics settings
* The Hellish Chain ability now only triggers from attacks that actually do damage
* Add extra audio-visual effects to active ritual altars
* The Ritual Altar now requires two empty blocks above it to start a ritual
* Fix clumping of mana cost widgets on arcane workbench screen
* Increased affinities of glowstone blocks
* Add storage block for Refined Salt
* Allow the Trade research project to accept any color of wool
* Grant affinities to music discs
* Increased drop rate of the Essence Thief enchantment
* Allow the Earthshatter Hammer to break down stone
* Attack spells now do half damage against other players
* Greatly increased penetration of Burst spells at all Power ranks
* Increment to Forge version 37.0.78

v0.0.17-alpha
-------------
* Allow Earthshatter Hammers to be repaired with iron ingots
* Make attunement gain and loss messages more descriptive
* Allow the Research Table to detect non-consumed material blocks within five blocks of itself
* Fix third-person appearance of several tools
* Add recipe book functionality to the arcane workbench
* Increment to Forge version 37.0.75

v0.0.16-alpha
-------------
* Fix bug preventing blocks broken by Break spells from dropping experience
* Wand transforms now require a two-second channel
* Fix statistic increment when quick crafting in the arcane workbench
* Made statistic names for discipline crafting more consistent with research requirements
* Allow librarians and wandering traders to sell items that grant arcane knowledge
* Fix behavior of owned tile entities while their owners are offline
* Reduce string requirements for enchanted cloth
* Add Ignyx, an unstable super-coal

v0.0.15-alpha
-------------
* Fix intermittent server crash

v0.0.14-alpha
-------------
* Fix caching bug in owned tile entities, ritual altars, and spell mines
* Require individual rune research to unlock empowered rune enchantments
* Replaced sugar cane's sky affinity with an equal amount of sea affinity
* Prevent empowered rune enchantment books from being offered for trade by villagers
* Allow runecarving tables to hold their inventory between uses
* Better describe the raw destructive power of the Disintegration enchantment
* Fix Treasure enchantment for mob loot when using spells
* Allow shift-clicking fuel items into calcinator input slot if fuel slot is full
* Fix rounding issue in wand mana calculations
* Increase bone block affinities
* Fix bug causing mod progress reset when returning from the End
* Fix bug in Arcanometer rendering of multi-part entities

v0.0.13-alpha
-------------
* Endermen can now be harmed by damaging touch-range spells
* Witches now have substantial resistance to damaging spells
* Fix bug where certain blocks could forget their owners in certain multiplayer situations
* Fix bug where the Spellcrafting Altar could consume double mana
* Fix bug where the Arcane Workbench could consume double mana
* Overhealing with the Healing spell payload now grants an absorb shield
* Allow interacting with Wand and Auto-Chargers via their central gap
* Made cloth armor prevent players from freezing in powder snow

v0.0.12-alpha
-------------
* Allow clearing wand spells using the Wand Inscription Table
* Add Auto-Charger, to automatically charge wands from nearby fonts
* Philosophy research for Earth and Sky now requires the player to be in the Overworld
* Add Dream Vision Talisman, a means to convert experience into observations
* Increased spell cast cooldown to 1.5sec, from one second
* Increased effectiveness of Quicken spell mod to 0.25sec per level, from 0.15sec per level
* Added research/grant_parents and research/grant_all debug commands
* Condense recipe index entries with the same name
* Allow clicking through to recipe pages from ingredient widgets
* Attunement meters now have a helpful tooltip
* Disable hopper interaction with Essence Furnaces; they're too primitive!
* Fix hopper interaction with various blocks
* Rebalance calcinator cook times
* Fix shift-click behavior for Arcane Workbench and Concocter

v0.0.11-alpha
-------------
* Update research table project background
* Made Flint and Steel set Treefolk on fire
* Add requirement of Sea Dust to Cryotreatment research
* Occlude some research entries from the Upcoming section of the Grimoire
* Add active spell details to wand and scroll tooltips
* Fix source omissions in mana arrow recipes
* Research tables now drop their contents when broken
* Reduced spell cast cooldown to one second, from three seconds
* Reduced effectiveness of Quicken spell mod to 0.15sec per level, from 0.5sec per level
* Increase damage and healing of all spell types
* Double duration of all damage spell secondary effects
* Rebalanced mana costs of most spells

v0.0.10-alpha
-------------
* Increment to Forge version 37.0.59

v0.0.9-alpha
------------
* Add grimoire pages for smelting recipes
* Show widget on research table listing active research aids
* Add upcoming section to grimoire discipline pages
* Add index of unlocked recipes to grimoire

v0.0.8-alpha
------------
* Allow research tables to hold writing implements between uses
* Show spell cooldowns visually
* Record teleport distance statistic
* Properly trigger pixie aggro when attacking with spells
* Fix analysis table button tooltip
* Add more scan entries to the grimoire
* Expand range at which research aids are detected
* Fix bug in wand core research requirements
* Infused stone no longer drops cobblestone, only dust
* Fix silk touch behavior for infused stone
* Increase knockback of Earth-Tinged Arrows
* Only show mana cost grimoire widget when the recipe has a mana cost
* Add workaround for inventory syncing issue with block entities
* Allow items of any durability for theorycrafting materials

v0.0.7-alpha
------------
* Add Mana-tinged Arrows, an early-game improvement to ammunition
* Fix failure to clone player mod data upon death
* Rearrange research tree

v0.0.6-alpha
------------
* Potential fix for dupe bug in inventory block entities

v0.0.5-alpha
------------
* Rework Basic Alchemy requirements
* Reformat recipe pages to make result name more prominent
* Add Sea shrines to icy biomes
* Only show Advanced Wandmaking research after spending some mana
* Fix analysis table crash bug

v0.0.4-alpha
------------
* Added Shield of the Sacred Oath, a ritually empowered shield that reduces all damage you take when blocking
* Fixed bug in aligned wand core mana regen
* Made several tutorial changes
* Increase drop rate of magical saplings from leaves
* Eliminated mana cost for Spellcrafting Altar
* Improve raw marble scan entry
* Improve grimoire navigation
* Start ancient mana fonts at full mana capacity

v0.0.3-alpha
------------
* Initial alpha release
